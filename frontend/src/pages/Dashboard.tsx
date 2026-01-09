import { useEffect, useState } from 'react';
import { apiService } from '../services/api';
import { Card, Loading } from '../components';
import type { Dashboard as DashboardType } from '../types';

export const Dashboard: React.FC = () => {
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
      setError('Error al cargar el dashboard. Intenta nuevamente.');
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
        <p className="text-red-600">{error || 'No se pudo cargar el dashboard'}</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-primary-900">Dashboard</h1>
        <p className="text-primary-600 mt-1">Resumen financiero</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-primary-600">Saldo Total</p>
              <p className="text-2xl font-bold text-primary-900 mt-1">
                {formatCurrency(dashboard.totalBalance, 'COP')}
              </p>
            </div>
            <div className="bg-primary-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-primary-600">Ingresos del Mes</p>
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
              <p className="text-sm text-primary-600">Gastos del Mes</p>
              <p className="text-2xl font-bold text-red-600 mt-1">
                {formatCurrency(dashboard.monthlyExpenses, 'COP')}
              </p>
            </div>
            <div className="bg-red-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 17h8m0 0V9m0 8l-8-8-4 4-6-6" />
              </svg>
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-primary-600">Balance Neto</p>
              <p className={`text-2xl font-bold mt-1 ${
                dashboard.monthlyBalance >= 0 ? 'text-green-600' : 'text-red-600'
              }`}>
                {formatCurrency(dashboard.monthlyBalance, 'COP')}
              </p>
            </div>
            <div className="bg-primary-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card title="Cuentas">
          <div className="space-y-3">
            {dashboard.accounts.length === 0 ? (
              <p className="text-primary-500 text-center py-4">No hay cuentas registradas</p>
            ) : (
              dashboard.accounts.map((account) => (
                <div key={account.id} className="flex items-center justify-between p-3 bg-primary-50 rounded-lg">
                  <div>
                    <p className="font-medium text-primary-900">{account.name}</p>
                    <p className="text-sm text-primary-600">{account.accountType}</p>
                  </div>
                  <p className="font-semibold text-primary-900">
                    {formatCurrency(account.currentBalance, account.currency)}
                  </p>
                </div>
              ))
            )}
          </div>
        </Card>

        <Card title="Transacciones Recientes">
          <div className="space-y-3">
            {dashboard.recentTransactions.length === 0 ? (
              <p className="text-primary-500 text-center py-4">No hay transacciones recientes</p>
            ) : (
              dashboard.recentTransactions.map((transaction) => (
                <div key={transaction.id} className="flex items-center justify-between p-3 bg-primary-50 rounded-lg">
                  <div>
                    <p className="font-medium text-primary-900">{transaction.description || 'Sin descripción'}</p>
                    <p className="text-sm text-primary-600">
                      {new Date(transaction.transactionDate).toLocaleDateString('es-CO')}
                    </p>
                  </div>
                  <p className={`font-semibold ${
                    transaction.transactionType === 'INCOME' ? 'text-green-600' : 'text-red-600'
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

