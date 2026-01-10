import { Card } from '../components';
import { useLanguage } from '../hooks/useLanguage';

export const Charts: React.FC = () => {
  const { t } = useLanguage();
  
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-spartan-900">{t('charts.title')}</h1>
        <p className="text-spartan-600 mt-1">{t('charts.subtitle')}</p>
      </div>

      <Card>
        <div className="text-center py-12">
          <div className="bg-spartan-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8 text-spartan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          </div>
          <h3 className="text-lg font-bold text-spartan-900 mb-2">{t('charts.comingSoon')}</h3>
          <p className="text-spartan-600">{t('charts.comingSoonDesc')}</p>
        </div>
      </Card>
    </div>
  );
};

