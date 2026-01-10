import { useEffect, useState } from 'react';
import { apiService } from '../services/api';
import { Card, Button, Loading, Input, Select } from '../components';
import type { Account } from '../types';

export const Accounts = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {
    loadAccounts();
  }, []);

  const loadAccounts = async () => {
    try {
      setIsLoading(true);
      const data = await apiService.getAccounts();
      setAccounts(data);
      setError(null);
    } catch (err: any) {
      setError('Error al cargar las cuentas. Intenta nuevamente.');
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

  const getAccountTypeLabel = (type: string) => {
    const types: Record<string, string> = {
      SAVINGS: 'Ahorros',
      CHECKING: 'Corriente',
      CASH: 'Efectivo',
    };
    return types[type] || type;
  };

  const handleDelete = async (id: string) => {
    if (!confirm('¿Estás seguro de que deseas eliminar esta cuenta?')) {
      return;
    }

    try {
      await apiService.deleteAccount(id);
      await loadAccounts();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Error al eliminar la cuenta');
    }
  };

  if (isLoading) {
    return <Loading fullScreen />;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-spartan-900">Cuentas</h1>
          <p className="text-spartan-600 mt-1">Gestiona tus cuentas bancarias y financieras</p>
        </div>
        <Button variant="primary" onClick={() => setShowCreateModal(true)}>
          + Nueva Cuenta
        </Button>
      </div>

      {error && (
        <div className="bg-red-50 border-2 border-red-200 text-red-700 px-4 py-3 rounded-lg">
          {error}
        </div>
      )}

      {accounts.length === 0 ? (
        <Card>
          <div className="text-center py-12">
            <div className="bg-spartan-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg className="w-8 h-8 text-spartan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 className="text-lg font-bold text-spartan-900 mb-2">No tienes cuentas registradas</h3>
            <p className="text-spartan-600 mb-6">Comienza creando tu primera cuenta para gestionar tus finanzas</p>
            <Button variant="primary" onClick={() => setShowCreateModal(true)}>
              Crear Primera Cuenta
            </Button>
          </div>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {accounts.map((account) => (
            <Card key={account.id}>
              <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                  <h3 className="text-lg font-bold text-spartan-900 mb-1">{account.name}</h3>
                  <span className="inline-block px-2 py-1 rounded-full text-xs font-medium bg-bronze-100 text-bronze-700 border border-bronze-300">
                    {getAccountTypeLabel(account.accountType)}
                  </span>
                </div>
                {account.active ? (
                  <span className="px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700 border border-green-300">
                    Activa
                  </span>
                ) : (
                  <span className="px-2 py-1 rounded-full text-xs font-medium bg-spartan-100 text-spartan-600 border border-spartan-300">
                    Inactiva
                  </span>
                )}
              </div>

              <div className="space-y-3 mb-4">
                <div>
                  <p className="text-xs text-spartan-500 mb-1">Saldo Actual</p>
                  <p className="text-2xl font-bold text-spartan-900">
                    {formatCurrency(account.currentBalance, account.currency)}
                  </p>
                </div>
                <div className="pt-3 border-t border-spartan-200">
                  <p className="text-xs text-spartan-500 mb-1">Saldo Inicial</p>
                  <p className="text-sm font-medium text-spartan-700">
                    {formatCurrency(account.initialBalance, account.currency)}
                  </p>
                </div>
              </div>

              <div className="flex gap-2 pt-4 border-t border-spartan-200">
                <Button
                  variant="outline"
                  size="sm"
                  className="flex-1"
                  onClick={() => {/* TODO: Editar */}}
                >
                  Editar
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  className="flex-1"
                  onClick={() => handleDelete(account.id)}
                >
                  Eliminar
                </Button>
              </div>
            </Card>
          ))}
        </div>
      )}

      {showCreateModal && (
        <CreateAccountModal
          onClose={() => setShowCreateModal(false)}
          onSuccess={() => {
            setShowCreateModal(false);
            loadAccounts();
          }}
        />
      )}
    </div>
  );
};

interface CreateAccountModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

const CreateAccountModal = ({ onClose, onSuccess }: CreateAccountModalProps) => {
  const [formData, setFormData] = useState({
    name: '',
    accountType: 'SAVINGS',
    initialBalance: '',
    currency: 'COP',
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);

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
    setIsLoading(true);

    try {
      await apiService.createAccount({
        name: formData.name,
        accountType: formData.accountType,
        initialBalance: parseFloat(formData.initialBalance) || 0,
        currency: formData.currency,
      });
      onSuccess();
    } catch (err: any) {
      if (err.response?.data?.details) {
        const errorDetails: Record<string, string> = {};
        err.response.data.details.forEach((detail: string) => {
          if (detail.includes('nombre')) errorDetails.name = detail;
          if (detail.includes('tipo')) errorDetails.accountType = detail;
          if (detail.includes('saldo')) errorDetails.initialBalance = detail;
        });
        setErrors(errorDetails);
      } else {
        setErrors({ general: err.response?.data?.message || 'Error al crear la cuenta' });
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" onClick={onClose}>
      <div className="bg-white rounded-xl shadow-spartan-lg max-w-md w-full" onClick={(e) => e.stopPropagation()}>
        <div className="card-header">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold text-spartan-900">Nueva Cuenta</h2>
            <button
              onClick={onClose}
              className="text-spartan-400 hover:text-spartan-600 text-2xl leading-none"
            >
              ×
            </button>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          {errors.general && (
            <div className="bg-red-50 border-2 border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
              {errors.general}
            </div>
          )}

          <Input
            label="Nombre de la cuenta"
            name="name"
            value={formData.name}
            onChange={handleChange}
            error={errors.name}
            required
            placeholder="Ej: Bancolombia Ahorros"
          />

          <Select
            label="Tipo de cuenta"
            name="accountType"
            value={formData.accountType}
            onChange={handleChange}
            error={errors.accountType}
            required
            options={[
              { value: 'SAVINGS', label: 'Ahorros' },
              { value: 'CHECKING', label: 'Corriente' },
              { value: 'CASH', label: 'Efectivo' },
            ]}
          />

          <Input
            label="Saldo inicial"
            name="initialBalance"
            type="number"
            step="0.01"
            value={formData.initialBalance}
            onChange={handleChange}
            error={errors.initialBalance}
            required
            placeholder="0.00"
          />

          <Select
            label="Moneda"
            name="currency"
            value={formData.currency}
            onChange={handleChange}
            options={[
              { value: 'COP', label: 'Peso Colombiano (COP)' },
              { value: 'USD', label: 'Dólar (USD)' },
              { value: 'EUR', label: 'Euro (EUR)' },
            ]}
          />

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              fullWidth
              onClick={onClose}
              disabled={isLoading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="primary"
              fullWidth
              isLoading={isLoading}
              disabled={isLoading}
            >
              Crear Cuenta
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};
