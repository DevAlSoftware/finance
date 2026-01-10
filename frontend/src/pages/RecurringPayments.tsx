import { Card } from '../components';
import { useLanguage } from '../hooks/useLanguage';

export const RecurringPayments: React.FC = () => {
  const { t } = useLanguage();
  
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-spartan-900">{t('recurringPayments.title')}</h1>
        <p className="text-spartan-600 mt-1">{t('recurringPayments.subtitle')}</p>
      </div>

      <Card>
        <div className="text-center py-12">
          <div className="bg-spartan-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8 text-spartan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
          </div>
          <h3 className="text-lg font-bold text-spartan-900 mb-2">{t('recurringPayments.comingSoon')}</h3>
          <p className="text-spartan-600">{t('recurringPayments.comingSoonDesc')}</p>
        </div>
      </Card>
    </div>
  );
};

