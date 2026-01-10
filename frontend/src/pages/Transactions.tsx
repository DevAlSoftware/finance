import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { apiService } from '../services/api';
import { Card, Button, Loading, Input, Select } from '../components';
import { useLanguage } from '../hooks/useLanguage';
import type { Transaction, Account, Category } from '../types';

export const Transactions = () => {
  const { t } = useLanguage();
  const [searchParams, setSearchParams] = useSearchParams();
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showTransferModal, setShowTransferModal] = useState(false);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  // Detectar si debe abrirse el modal automáticamente desde query params
  useEffect(() => {
    const action = searchParams.get('action');
    if (action === 'create') {
      setShowCreateModal(true);
      // Limpiar query params después de abrir el modal
      setSearchParams({});
    }
  }, [searchParams, setSearchParams]);

  useEffect(() => {
    loadAccounts();
    loadTransactions();
  }, []);

  const loadAccounts = async () => {
    try {
      const data = await apiService.getAccounts();
      setAccounts(data.filter(acc => acc.active));
    } catch (err) {
      console.error('Error loading accounts:', err);
    }
  };

  const loadTransactions = async () => {
    try {
      setIsLoading(true);
      const data = await apiService.getTransactions(
        startDate || undefined,
        endDate || undefined
      );
      setTransactions(data);
      setError(null);
    } catch (err: any) {
      setError(t('transactions.errorLoading'));
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (startDate || endDate) {
      loadTransactions();
    }
  }, [startDate, endDate]);

  const formatCurrency = (amount: number, currency: string = 'COP') => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  const handleDelete = async (id: string) => {
    if (!confirm(t('transactions.deleteConfirm'))) {
      return;
    }

    try {
      await apiService.deleteTransaction(id);
      await loadTransactions();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Error al eliminar la transacción');
    }
  };

  const getAccountName = (accountId: string) => {
    const account = accounts.find(acc => acc.id === accountId);
    return account?.name || t('transactions.deletedAccount');
  };

  if (isLoading && transactions.length === 0) {
    return <Loading fullScreen />;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-4">
        <div>
          <h1 className="text-2xl font-bold text-spartan-900">{t('transactions.title')}</h1>
          <p className="text-spartan-600 mt-1">{t('transactions.subtitle')}</p>
        </div>
        <div className="flex gap-3">
          <Button variant="outline" onClick={() => setShowTransferModal(true)}>
            {t('transactions.transfer')}
          </Button>
          <Button variant="primary" onClick={() => setShowCreateModal(true)}>
            + {t('transactions.newTransaction')}
          </Button>
        </div>
      </div>

      <Card>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Input
            label={t('transactions.fromDate')}
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
          <Input
            label={t('transactions.toDate')}
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
          <div className="flex items-end">
            <Button
              variant="outline"
              fullWidth
              onClick={() => {
                setStartDate('');
                setEndDate('');
                loadTransactions();
              }}
            >
              {t('transactions.clearFilters')}
            </Button>
          </div>
        </div>
      </Card>

      {error && (
        <div className="bg-red-50 border-2 border-red-200 text-red-700 px-4 py-3 rounded-lg">
          {error}
        </div>
      )}

      {transactions.length === 0 ? (
        <Card>
          <div className="text-center py-12">
            <div className="bg-spartan-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg className="w-8 h-8 text-spartan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01" />
              </svg>
            </div>
            <h3 className="text-lg font-bold text-spartan-900 mb-2">{t('transactions.noTransactions')}</h3>
            <p className="text-spartan-600 mb-6">{t('transactions.noTransactionsDesc')}</p>
            <Button variant="primary" onClick={() => setShowCreateModal(true)}>
              {t('transactions.createFirst')}
            </Button>
          </div>
        </Card>
      ) : (
        <div className="space-y-3">
          {transactions.map((transaction) => (
            <Card key={transaction.id}>
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <div className={`w-3 h-3 rounded-full ${
                      transaction.transactionType === 'INCOME' ? 'bg-green-500' : 'bg-crimson-500'
                    }`}></div>
                    <div>
                      <h3 className="font-bold text-spartan-900">
                        {transaction.description || t('transactions.noDescription')}
                      </h3>
                      <div className="flex items-center gap-3 text-sm text-spartan-600 mt-1">
                        <span>{getAccountName(transaction.accountId)}</span>
                        <span>•</span>
                        <span>{new Date(transaction.transactionDate).toLocaleDateString('es-CO')}</span>
                      </div>
                    </div>
                  </div>
                  {transaction.tags && transaction.tags.length > 0 && (
                    <div className="flex flex-wrap gap-2 mt-2">
                      {transaction.tags.map((tag, index) => (
                        <span
                          key={index}
                          className="px-2 py-0.5 rounded text-xs font-medium bg-spartan-100 text-spartan-700 border border-spartan-300"
                        >
                          {tag}
                        </span>
                      ))}
                    </div>
                  )}
                </div>
                <div className="text-right ml-4">
                  <p className={`text-xl font-bold ${
                    transaction.transactionType === 'INCOME' ? 'text-green-600' : 'text-crimson-600'
                  }`}>
                    {transaction.transactionType === 'INCOME' ? '+' : '-'}
                    {formatCurrency(transaction.amount, 'COP')}
                  </p>
                  <div className="flex gap-2 mt-3">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {/* TODO: Editar */}}
                    >
                      {t('common.edit')}
                    </Button>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => handleDelete(transaction.id)}
                    >
                      {t('common.delete')}
                    </Button>
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}

      {showCreateModal && (
        <CreateTransactionModal
          accounts={accounts}
          initialType={searchParams.get('type') as 'INCOME' | 'EXPENSE' | null}
          onClose={() => {
            setShowCreateModal(false);
            setSearchParams({});
          }}
          onSuccess={() => {
            setShowCreateModal(false);
            setSearchParams({});
            loadTransactions();
            loadAccounts();
          }}
        />
      )}

      {showTransferModal && (
        <TransferModal
          accounts={accounts}
          onClose={() => setShowTransferModal(false)}
          onSuccess={() => {
            setShowTransferModal(false);
            loadTransactions();
            loadAccounts();
          }}
        />
      )}
    </div>
  );
};

interface CreateTransactionModalProps {
  accounts: Account[];
  initialType?: 'INCOME' | 'EXPENSE' | null;
  onClose: () => void;
  onSuccess: () => void;
}

const CreateTransactionModal = ({ accounts, initialType, onClose, onSuccess }: CreateTransactionModalProps) => {
  const { t } = useLanguage();
  const [selectedType, setSelectedType] = useState<'INCOME' | 'EXPENSE' | null>(initialType || null);
  const [formData, setFormData] = useState({
    accountId: '',
    amount: '',
    transactionType: initialType || 'EXPENSE',
    description: '',
    transactionDate: new Date().toISOString().split('T')[0],
    categoryId: '',
  });
  const [categories, setCategories] = useState<Category[]>([]);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    loadCategories();
  }, []);

  useEffect(() => {
    // Resetear categoría cuando cambia el tipo de transacción
    if (selectedType) {
      setFormData(prev => ({ ...prev, transactionType: selectedType, categoryId: '' }));
    }
  }, [selectedType]);

  const loadCategories = async () => {
    try {
      const data = await apiService.getCategories();
      setCategories(data);
    } catch (err) {
      console.error('Error loading categories:', err);
    }
  };

  const filteredCategories = selectedType 
    ? categories.filter(cat => cat.type === selectedType)
    : [];

  const formatCurrency = (amount: number, currency: string = 'COP') => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedType) return;
    
    setErrors({});
    setIsLoading(true);

    try {
      await apiService.createTransaction({
        accountId: formData.accountId,
        amount: parseFloat(formData.amount),
        transactionType: selectedType,
        description: formData.description || undefined,
        transactionDate: formData.transactionDate,
        categoryId: formData.categoryId || undefined,
      });
      onSuccess();
    } catch (err: any) {
      if (err.response?.data?.details) {
        const errorDetails: Record<string, string> = {};
        err.response.data.details.forEach((detail: string) => {
          if (detail.includes('cuenta')) errorDetails.accountId = detail;
          if (detail.includes('monto')) errorDetails.amount = detail;
          if (detail.includes('tipo')) errorDetails.transactionType = detail;
        });
        setErrors(errorDetails);
      } else {
        setErrors({ general: err.response?.data?.message || t('transactions.errorCreating') });
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" onClick={onClose}>
      <div className="bg-white rounded-xl shadow-spartan-lg max-w-md w-full max-h-[90vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
        <div className="card-header sticky top-0 bg-white z-10">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold text-spartan-900">{t('transactions.newTransaction')}</h2>
            <button
              onClick={onClose}
              className="text-spartan-400 hover:text-spartan-600 text-2xl leading-none"
            >
              ×
            </button>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4 p-6">
          {errors.general && (
            <div className="bg-red-50 border-2 border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
              {errors.general}
            </div>
          )}

          {!selectedType ? (
            <div className="space-y-4">
              <div className="text-center mb-4">
                <p className="text-spartan-700 font-medium text-lg mb-1">{t('transactions.selectType')}</p>
                <p className="text-spartan-600 text-sm">{t('transactions.selectTypeHelp')}</p>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <button
                  type="button"
                  onClick={() => setSelectedType('INCOME')}
                  className="group relative overflow-hidden bg-gradient-to-br from-green-500 to-green-600 hover:from-green-600 hover:to-green-700 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 p-6 text-left border-2 border-green-700"
                >
                  <div className="text-center">
                    <div className="text-4xl mb-2">💰</div>
                    <h3 className="text-xl font-bold text-white mb-1">{t('dashboard.income')}</h3>
                    <p className="text-green-100 text-sm">{t('transactions.incomeDescription')}</p>
                  </div>
                </button>

                <button
                  type="button"
                  onClick={() => setSelectedType('EXPENSE')}
                  className="group relative overflow-hidden bg-gradient-to-br from-crimson-600 to-crimson-700 hover:from-crimson-700 hover:to-crimson-800 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 p-6 text-left border-2 border-crimson-800"
                >
                  <div className="text-center">
                    <div className="text-4xl mb-2">💸</div>
                    <h3 className="text-xl font-bold text-white mb-1">{t('dashboard.expense')}</h3>
                    <p className="text-crimson-100 text-sm">{t('transactions.expenseDescription')}</p>
                  </div>
                </button>
              </div>
            </div>
          ) : (
            <>
              <div className="mb-4">
                <div className={`inline-flex items-center gap-2 px-4 py-2 rounded-lg ${
                  selectedType === 'INCOME' 
                    ? 'bg-green-100 text-green-800 border-2 border-green-300' 
                    : 'bg-crimson-100 text-crimson-800 border-2 border-crimson-300'
                }`}>
                  <span className="font-bold">{selectedType === 'INCOME' ? `💰 ${t('dashboard.income')}` : `💸 ${t('dashboard.expense')}`}</span>
                  <button
                    type="button"
                    onClick={() => setSelectedType(null)}
                    className="text-sm hover:underline ml-2"
                  >
                    {t('common.change', 'Cambiar')}
                  </button>
                </div>
              </div>

              <Select
                label={t('transactions.account')}
                name="accountId"
                value={formData.accountId}
                onChange={handleChange}
                error={errors.accountId}
                required
                options={accounts.map(acc => {
                  const balance = new Intl.NumberFormat('es-CO', {
                    style: 'currency',
                    currency: acc.currency,
                  }).format(acc.currentBalance);
                  return { value: acc.id, label: `${acc.name} (${balance})` };
                })}
              />
            </>
          )}

          {selectedType && (
            <>
              <div className="relative">
                <Select
                  label={`${t('transactions.category')} (${t('common.optional')})`}
                  name="categoryId"
                  value={formData.categoryId}
                  onChange={handleChange}
                  error={errors.categoryId}
                  options={[
                    { value: '', label: t('transactions.noCategory') },
                    ...filteredCategories.map(cat => ({
                      value: cat.id,
                      label: cat.name
                    }))
                  ]}
                />
                <p className="text-xs text-spartan-600 mt-1">
                  {t('transactions.categoryHelp')}
                </p>
              </div>

              <Input
                label={t('transactions.amount')}
                name="amount"
                type="number"
                step="0.01"
                value={formData.amount}
                onChange={handleChange}
                error={errors.amount}
                required
                placeholder="0.00"
              />

              <Input
                label={t('transactions.description')}
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder={t('transactions.descriptionPlaceholder')}
                required
              />

              <Input
                label={t('transactions.date')}
                name="transactionDate"
                type="date"
                value={formData.transactionDate}
                onChange={handleChange}
                required
              />

              <div className="flex gap-3 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  fullWidth
                  onClick={onClose}
                  disabled={isLoading}
                >
                  {t('common.cancel')}
                </Button>
                <Button
                  type="submit"
                  variant="primary"
                  fullWidth
                  isLoading={isLoading}
                  disabled={isLoading}
                >
                  {selectedType === 'INCOME' ? t('transactions.createIncome') : t('transactions.createExpense')}
                </Button>
              </div>
            </>
          )}
        </form>
      </div>
    </div>
  );
};

interface TransferModalProps {
  accounts: Account[];
  onClose: () => void;
  onSuccess: () => void;
}

const TransferModal = ({ accounts, onClose, onSuccess }: TransferModalProps) => {
  const { t } = useLanguage();
  const [formData, setFormData] = useState({
    fromAccountId: '',
    toAccountId: '',
    amount: '',
    description: '',
    transactionDate: new Date().toISOString().split('T')[0],
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);

  const formatCurrency = (amount: number, currency: string = 'COP') => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors({});

    if (formData.fromAccountId === formData.toAccountId) {
      setErrors({ general: t('transfer.sameAccountError') });
      return;
    }

    setIsLoading(true);

    try {
      await apiService.createTransfer({
        fromAccountId: formData.fromAccountId,
        toAccountId: formData.toAccountId,
        amount: parseFloat(formData.amount),
        description: formData.description || undefined,
        transactionDate: formData.transactionDate,
      });
      onSuccess();
    } catch (err: any) {
      if (err.response?.data?.details) {
        const errorDetails: Record<string, string> = {};
        err.response.data.details.forEach((detail: string) => {
          if (detail.includes('origen')) errorDetails.fromAccountId = detail;
          if (detail.includes('destino')) errorDetails.toAccountId = detail;
          if (detail.includes('monto') || detail.includes('saldo')) errorDetails.amount = detail;
        });
        setErrors(errorDetails);
      } else {
        setErrors({ general: err.response?.data?.message || t('transfer.error', 'Error al realizar la transferencia') });
      }
    } finally {
      setIsLoading(false);
    }
  };

  const getFromAccount = accounts.find(acc => acc.id === formData.fromAccountId);
  const canTransfer = getFromAccount && parseFloat(formData.amount) > 0 && 
                      parseFloat(formData.amount) <= getFromAccount.currentBalance;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" onClick={onClose}>
      <div className="bg-white rounded-xl shadow-spartan-lg max-w-md w-full max-h-[90vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
        <div className="card-header sticky top-0 bg-white z-10">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold text-spartan-900">{t('transfer.title')}</h2>
            <button
              onClick={onClose}
              className="text-spartan-400 hover:text-spartan-600 text-2xl leading-none"
            >
              ×
            </button>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4 p-6">
          {errors.general && (
            <div className="bg-red-50 border-2 border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
              {errors.general}
            </div>
          )}

          <Select
            label={t('transfer.fromAccount')}
            name="fromAccountId"
            value={formData.fromAccountId}
            onChange={handleChange}
            error={errors.fromAccountId}
            required
            options={accounts.map(acc => ({
              value: acc.id,
              label: `${acc.name} - ${t('transfer.availableBalance')}: ${formatCurrency(acc.currentBalance, acc.currency)}`
            }))}
          />

          {getFromAccount && (
            <div className="bg-bronze-50 border-2 border-bronze-200 rounded-lg p-3">
              <p className="text-xs text-bronze-700 mb-1">{t('transfer.availableBalance')}</p>
              <p className="font-bold text-bronze-800">
                {formatCurrency(getFromAccount.currentBalance, getFromAccount.currency)}
              </p>
            </div>
          )}

          <Select
            label={t('transfer.toAccount')}
            name="toAccountId"
            value={formData.toAccountId}
            onChange={handleChange}
            error={errors.toAccountId}
            required
            options={accounts
              .filter(acc => acc.id !== formData.fromAccountId)
              .map(acc => ({
                value: acc.id,
                label: `${acc.name}`
              }))}
          />

          <Input
            label={t('transfer.amount')}
            name="amount"
            type="number"
            step="0.01"
            value={formData.amount}
            onChange={handleChange}
            error={errors.amount}
            required
            placeholder="0.00"
          />

          {getFromAccount && parseFloat(formData.amount) > 0 && (
            <div className={`rounded-lg p-3 border-2 ${
              canTransfer
                ? 'bg-green-50 border-green-200'
                : 'bg-red-50 border-red-200'
            }`}>
              <p className={`text-sm font-medium ${
                canTransfer ? 'text-green-700' : 'text-red-700'
              }`}>
                {canTransfer
                  ? `${t('transfer.validTransfer')}: ${formatCurrency(
                      getFromAccount.currentBalance - parseFloat(formData.amount),
                      getFromAccount.currency
                    )}`
                  : `${t('transfer.insufficientBalance')}: ${formatCurrency(
                      getFromAccount.currentBalance,
                      getFromAccount.currency
                    )}`
                }
              </p>
            </div>
          )}

          <Input
            label={t('transactions.description')}
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder={t('transactions.descriptionPlaceholder')}
          />

          <Input
            label={t('transactions.date')}
            name="transactionDate"
            type="date"
            value={formData.transactionDate}
            onChange={handleChange}
            required
          />

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              fullWidth
              onClick={onClose}
              disabled={isLoading}
            >
              {t('common.cancel')}
            </Button>
            <Button
              type="submit"
              variant="primary"
              fullWidth
              isLoading={isLoading}
              disabled={isLoading || !canTransfer}
            >
              {t('transfer.executeTransfer')}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};
