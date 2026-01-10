import { Card, Select } from '../components';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../hooks/useLanguage';

export const Settings: React.FC = () => {
  const { user } = useAuth();
  const { t, changeLanguage, currentLanguage } = useLanguage();

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-spartan-900">{t('settings.title')}</h1>
        <p className="text-spartan-600 mt-1">{t('settings.subtitle')}</p>
      </div>

      <Card title={t('settings.accountInfo')}>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-spartan-700 mb-1">{t('settings.email')}</label>
            <p className="text-spartan-900">{user?.email}</p>
          </div>
          {user?.fullName && (
            <div>
              <label className="block text-sm font-medium text-spartan-700 mb-1">{t('settings.fullName')}</label>
              <p className="text-spartan-900">{user.fullName}</p>
            </div>
          )}
        </div>
      </Card>

      <Card title={t('settings.preferences')}>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-spartan-700 mb-2">{t('settings.language')}</label>
            <Select
              value={currentLanguage}
              onChange={(e) => changeLanguage(e.target.value)}
              options={[
                { value: 'es', label: t('settings.spanish') },
                { value: 'en', label: t('settings.english') },
              ]}
            />
          </div>
        </div>
      </Card>
    </div>
  );
};

