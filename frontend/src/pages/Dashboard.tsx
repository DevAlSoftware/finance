import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiService } from '../services/api';
import { Card, Loading, Button } from '../components';
import { useLanguage } from '../hooks/useLanguage';
import type { Dashboard as DashboardType } from '../types';

export const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const { t } = useLanguage();
  const [dashboard, setDashboard] = useState<DashboardType | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setIsLoading(true);
      const data = await apiService.getDashboard();
      setDashboard(data);
      setError(null);
    } catch (err: any) {
      setError(t('dashboard.errorLoading'));
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const formatCurrency = (amount: number, currency: string = 'COP') => {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  if (isLoading) {
    return <Loading fullScreen />;
  }

  if (error || !dashboard) {
    return (
      <div className="text-center py-12">
        <p className="text-red-600">{error || t('dashboard.couldNotLoad')}</p>
      </div>
    );
  }

  const handleQuickAction = (type: 'INCOME' | 'EXPENSE') => {
    navigate(`/transactions?action=create&type=${type}`);
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-spartan-900">{t('dashboard.title')}</h1>
          <p className="text-spartan-600 mt-1">{t('dashboard.subtitle')}</p>
        </div>
      </div>

      {/* Botones grandes de Ingreso y Egreso */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <button
          onClick={() => handleQuickAction('INCOME')}
          className="group relative overflow-hidden bg-gradient-to-br from-green-500 to-green-600 hover:from-green-600 hover:to-green-700 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 p-8 text-left border-2 border-green-700"
        >
          <div className="relative z-10">
            <div className="flex items-center justify-between mb-4">
              <div className="bg-white/20 rounded-full p-3">
                <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                </svg>
              </div>
            </div>
            <h3 className="text-2xl font-bold text-white mb-2">{t('dashboard.income')}</h3>
            <p className="text-green-100 text-sm">{t('dashboard.registerIncome')}</p>
          </div>
          <div className="absolute inset-0 bg-white/10 transform scale-x-0 group-hover:scale-x-100 transition-transform duration-300 origin-left"></div>
        </button>

        <button
          onClick={() => handleQuickAction('EXPENSE')}
          className="group relative overflow-hidden bg-gradient-to-br from-crimson-600 to-crimson-700 hover:from-crimson-700 hover:to-crimson-800 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 p-8 text-left border-2 border-crimson-800"
        >
          <div className="relative z-10">
            <div className="flex items-center justify-between mb-4">
              <div className="bg-white/20 rounded-full p-3">
                <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 17h8m0 0V9m0 8l-8-8-4 4-6-6" />
                </svg>
              </div>
            </div>
            <h3 className="text-2xl font-bold text-white mb-2">{t('dashboard.expense')}</h3>
            <p className="text-crimson-100 text-sm">{t('dashboard.registerExpense')}</p>
          </div>
          <div className="absolute inset-0 bg-white/10 transform scale-x-0 group-hover:scale-x-100 transition-transform duration-300 origin-left"></div>
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-spartan-600">{t('dashboard.totalBalance')}</p>
              <p className="text-2xl font-bold text-spartan-900 mt-1">
                {formatCurrency(dashboard.totalBalance, 'COP')}
              </p>
            </div>
            <div className="bg-spartan-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-spartan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-spartan-600">{t('dashboard.monthlyIncome')}</p>
              <p className="text-2xl font-bold text-green-600 mt-1">
                {formatCurrency(dashboard.monthlyIncome, 'COP')}
              </p>
            </div>
            <div className="bg-green-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
              </svg>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-spartan-600">{t('dashboard.monthlyExpenses')}</p>
              <p className="text-2xl font-bold text-crimson-600 mt-1">
                {formatCurrency(dashboard.monthlyExpenses, 'COP')}
              </p>
            </div>
            <div className="bg-crimson-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-crimson-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 17h8m0 0V9m0 8l-8-8-4 4-6-6" />
              </svg>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-spartan-600">{t('dashboard.netBalance')}</p>
              <p className={`text-2xl font-bold mt-1 ${
                dashboard.monthlyBalance >= 0 ? 'text-green-600' : 'text-crimson-600'
              }`}>
                {formatCurrency(dashboard.monthlyBalance, 'COP')}
              </p>
            </div>
            <div className="bg-spartan-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-spartan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card title={t('dashboard.accounts')}>
          <div className="space-y-3">
            {dashboard.accounts.length === 0 ? (
              <p className="text-spartan-600 text-center py-4">{t('dashboard.noAccounts')}</p>
            ) : (
              dashboard.accounts.map((account) => (
                <div key={account.id} className="flex items-center justify-between p-3 bg-spartan-50 rounded-lg">
                  <div>
                    <p className="font-medium text-spartan-900">{account.name}</p>
                    <p className="text-sm text-spartan-600">{account.accountType}</p>
                  </div>
                  <p className="font-semibold text-spartan-900">
                    {formatCurrency(account.currentBalance, account.currency)}
                  </p>
                </div>
              ))
            )}
          </div>
        </Card>

        <Card title={t('dashboard.recentTransactions')}>
          <div className="space-y-3">
            {dashboard.recentTransactions.length === 0 ? (
              <p className="text-spartan-600 text-center py-4">{t('dashboard.noTransactions')}</p>
            ) : (
              dashboard.recentTransactions.map((transaction) => (
                <div key={transaction.id} className="flex items-center justify-between p-3 bg-spartan-50 rounded-lg">
                  <div>
                    <p className="font-medium text-spartan-900">{transaction.description || 'Sin descripción'}</p>
                    <p className="text-sm text-spartan-600">
                      {new Date(transaction.transactionDate).toLocaleDateString('es-CO')}
                    </p>
                  </div>
                  <p className={`font-semibold ${
                    transaction.transactionType === 'INCOME' ? 'text-green-600' : 'text-crimson-600'
                  }`}>
                    {transaction.transactionType === 'INCOME' ? '+' : '-'}
                    {formatCurrency(transaction.amount, 'COP')}
                  </p>
                </div>
              ))
            )}
          </div>
        </Card>
      </div>
    </div>
  );
};

