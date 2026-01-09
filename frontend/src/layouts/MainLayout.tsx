import type { ReactNode } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Button } from '../components';

interface MainLayoutProps {
  children: ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const navigation = [
    { name: 'Dashboard', path: '/dashboard' },
    { name: 'Cuentas', path: '/accounts' },
    { name: 'Transacciones', path: '/transactions' },
  ];


  return (
    <div className="min-h-screen bg-spartan-50">
      <nav className="bg-white border-b-2 border-spartan-300 shadow-spartan">
        <div className="container-custom">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-8">
              <Link to="/dashboard" className="text-xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-crimson-700 to-bronze-600">
                DevalFinance
              </Link>
              <div className="hidden md:flex gap-6">
                {navigation.map((item) => (
                  <Link
                    key={item.path}
                    to={item.path}
                    className={`px-4 py-2 rounded-lg text-sm font-bold transition-all ${
                      location.pathname === item.path
                        ? 'bg-gradient-to-br from-crimson-700 to-crimson-800 text-white shadow-lg border-2 border-crimson-900'
                        : 'text-spartan-700 hover:bg-spartan-100 border-2 border-transparent hover:border-spartan-300'
                    }`}
                  >
                    {item.name}
                  </Link>
                ))}
              </div>
            </div>
            <div className="flex items-center gap-4">
              <span className="text-sm text-spartan-600 hidden sm:block font-medium">{user?.email}</span>
              <Button variant="outline" size="sm" onClick={handleLogout}>
                Cerrar Sesión
              </Button>
            </div>
          </div>
        </div>
      </nav>
      
      <main className="container-custom py-8">
        {children}
      </main>
    </div>
  );
};

