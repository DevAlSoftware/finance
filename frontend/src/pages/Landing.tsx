import { Link } from 'react-router-dom';
import { Button } from '../components';

export const Landing = () => {
  return (
    <div className="min-h-screen bg-white">
      <div className="bg-gradient-to-r from-crimson-700 to-crimson-800 text-white py-2">
        <div className="container-custom">
          <div className="flex items-center justify-between text-sm">
            <div className="flex items-center gap-2">
              <span className="bg-white/20 px-2 py-0.5 rounded text-xs font-bold">NUEVO</span>
              <span>Webinar en línea: Descubre cómo gestionar tus finanzas de forma profesional</span>
            </div>
            <div className="flex items-center gap-4">
              <Link to="/dashboard" className="hover:underline font-medium">
                Ver aquí →
              </Link>
              <button className="hover:opacity-80">✕</button>
            </div>
          </div>
        </div>
      </div>

      <nav className="bg-white border-b border-spartan-200 sticky top-0 z-50 shadow-sm">
        <div className="container-custom">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-8">
              <div className="text-2xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-crimson-700 to-bronze-600">
                DevalFinance
              </div>
              <div className="hidden md:flex items-center gap-6">
                <Link to="/dashboard" className="text-spartan-700 hover:text-crimson-700 font-medium text-sm">
                  Plataforma
                </Link>
                <Link to="/dashboard" className="text-spartan-700 hover:text-crimson-700 font-medium text-sm flex items-center gap-1">
                  Soluciones <span className="text-xs">▼</span>
                </Link>
                <Link to="/dashboard" className="text-spartan-700 hover:text-crimson-700 font-medium text-sm">
                  Recursos
                </Link>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <Link to="/login" className="text-spartan-700 hover:text-crimson-700 font-medium text-sm">
                Login
              </Link>
              <Link to="/register">
                <Button variant="primary" size="sm">
                  Solicita una demo →
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </nav>

      <main>
        <section className="container-custom py-16 md:py-24">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div className="space-y-6">
              <div>
                <span className="inline-block px-3 py-1 rounded-full bg-gradient-to-r from-crimson-100 to-bronze-100 text-crimson-700 font-bold text-sm mb-4">
                  Plataforma
                </span>
                <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-spartan-900 leading-tight mb-6">
                  Transforma la gestión de tus finanzas con tecnología espartana
                </h1>
                <p className="text-lg text-spartan-600 mb-8 leading-relaxed">
                  Integraciones simples, soluciones modulares y eficiencia basada en principios espartanos. 
                  Gestiona ingresos, gastos y transferencias con total control y simplicidad.
                </p>
              </div>
              
              <div className="flex flex-col sm:flex-row gap-4">
                <Link to="/register">
                  <Button variant="primary" size="lg">
                    Comenzar Gratis →
                  </Button>
                </Link>
                <Link to="/login">
                  <Button variant="outline" size="lg">
                    Iniciar Sesión
                  </Button>
                </Link>
              </div>

              <div className="pt-6 border-t border-spartan-200">
                <div className="grid grid-cols-3 gap-6">
                  <div>
                    <div className="text-2xl font-bold text-crimson-700 mb-1">100%</div>
                    <div className="text-sm text-spartan-600">Automático</div>
                  </div>
                  <div>
                    <div className="text-2xl font-bold text-bronze-600 mb-1">24/7</div>
                    <div className="text-sm text-spartan-600">Disponible</div>
                  </div>
                  <div>
                    <div className="text-2xl font-bold text-spartan-700 mb-1">∞</div>
                    <div className="text-sm text-spartan-600">Escalable</div>
                  </div>
                </div>
              </div>
            </div>

            <div className="relative">
              <div className="relative bg-gradient-to-br from-spartan-100 to-bronze-50 rounded-2xl p-8 shadow-spartan-lg">
                <div className="aspect-square bg-white rounded-xl shadow-lg p-6 flex flex-col justify-between">
                  <div className="flex items-start justify-between">
                    <div className="bg-gradient-to-br from-crimson-500 to-crimson-600 w-12 h-12 rounded-full flex items-center justify-center">
                      <svg className="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                    </div>
                    <div className="text-right">
                      <div className="text-xs text-spartan-500 mb-1">Balance Total</div>
                      <div className="text-2xl font-bold text-spartan-900">$45,230</div>
                    </div>
                  </div>
                  
                  <div className="space-y-4">
                    <div className="bg-gradient-to-r from-green-500 to-green-400 rounded-lg p-4 text-white">
                      <div className="text-xs opacity-90 mb-1">Ingresos del Mes</div>
                      <div className="text-xl font-bold">+ $12,450</div>
                    </div>
                    <div className="bg-gradient-to-r from-crimson-500 to-crimson-600 rounded-lg p-4 text-white">
                      <div className="text-xs opacity-90 mb-1">Gastos del Mes</div>
                      <div className="text-xl font-bold">- $8,220</div>
                    </div>
                  </div>

                  <div className="pt-4 border-t border-spartan-200">
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-xs font-medium text-spartan-600">Reconciliado Automáticamente</span>
                      <span className="text-sm font-bold text-bronze-600">98%</span>
                    </div>
                    <div className="w-full bg-spartan-200 rounded-full h-2">
                      <div className="bg-gradient-to-r from-bronze-500 to-bronze-600 h-2 rounded-full" style={{ width: '98%' }}></div>
                    </div>
                  </div>
                </div>
                
                <div className="absolute -bottom-4 -right-4 w-24 h-24 bg-gradient-to-br from-bronze-400 to-bronze-500 rounded-full opacity-20 blur-2xl"></div>
                <div className="absolute -top-4 -left-4 w-32 h-32 bg-gradient-to-br from-crimson-400 to-crimson-500 rounded-full opacity-20 blur-3xl"></div>
              </div>
            </div>
          </div>
        </section>

        <section className="bg-spartan-50 py-20 border-t border-spartan-200">
          <div className="container-custom px-4">
            <div className="text-center mb-16">
              <h2 className="text-3xl md:text-4xl font-bold text-spartan-900 mb-4">
                Características Principales
              </h2>
              <p className="text-lg text-spartan-600 max-w-2xl mx-auto">
                Todo lo que necesitas para gestionar tus finanzas de forma profesional
              </p>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl mx-auto">
              <div className="text-center p-6">
                <div className="bg-gradient-to-br from-bronze-200 to-bronze-300 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 border-2 border-bronze-400 shadow-lg">
                  <svg className="w-8 h-8 text-bronze-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <h3 className="text-xl font-bold text-spartan-900 mb-2">Gestiona Cuentas</h3>
                <p className="text-spartan-700">
                  Organiza tus cuentas bancarias, de ahorro y efectivo en un solo lugar. 
                  Cada cuenta mantiene su saldo actualizado automáticamente.
                </p>
              </div>

              <div className="text-center p-6">
                <div className="bg-gradient-to-br from-crimson-200 to-crimson-300 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 border-2 border-crimson-400 shadow-lg">
                  <svg className="w-8 h-8 text-crimson-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01" />
                  </svg>
                </div>
                <h3 className="text-xl font-bold text-spartan-900 mb-2">Registra Transacciones</h3>
                <p className="text-spartan-700">
                  Registra tus ingresos y gastos fácilmente. Las transacciones se categorizan 
                  automáticamente y actualizan los saldos en tiempo real.
                </p>
              </div>

              <div className="text-center p-6">
                <div className="bg-gradient-to-br from-crimson-200 to-crimson-300 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4 border-2 border-crimson-400 shadow-lg">
                  <svg className="w-8 h-8 text-crimson-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
                  </svg>
                </div>
                <h3 className="text-xl font-bold text-spartan-900 mb-2">Transferencias</h3>
                <p className="text-spartan-700">
                  Transfiere dinero entre tus cuentas de forma sencilla. 
                  Las transferencias se registran automáticamente como transacciones.
                </p>
              </div>
            </div>
          </div>
        </section>

        <section className="container-custom py-16 px-4">
          <div className="max-w-3xl mx-auto">
            <h2 className="text-3xl font-bold text-center text-spartan-900 mb-12">
              Cómo Funciona
            </h2>
            <div className="space-y-8">
              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <div className="bg-gradient-to-br from-crimson-700 to-crimson-800 text-white w-12 h-12 rounded-full flex items-center justify-center font-bold border-2 border-crimson-900 shadow-lg">
                    1
                  </div>
                </div>
                <div>
                  <h3 className="text-xl font-bold text-spartan-900 mb-2">
                    Crea tu Cuenta
                  </h3>
                  <p className="text-spartan-700">
                    Regístrate con tu email y contraseña. El proceso es rápido y sencillo. 
                    No necesitas datos bancarios ni información sensible.
                  </p>
                </div>
              </div>

              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <div className="bg-gradient-to-br from-bronze-600 to-bronze-700 text-white w-12 h-12 rounded-full flex items-center justify-center font-bold border-2 border-bronze-800 shadow-lg">
                    2
                  </div>
                </div>
                <div>
                  <h3 className="text-xl font-bold text-spartan-900 mb-2">
                    Agrega tus Cuentas
                  </h3>
                  <p className="text-spartan-700">
                    Configura tus cuentas bancarias, de ahorro o efectivo. 
                    Puedes agregar múltiples cuentas y definir un saldo inicial para cada una.
                  </p>
                </div>
              </div>

              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <div className="bg-gradient-to-br from-crimson-700 to-crimson-800 text-white w-12 h-12 rounded-full flex items-center justify-center font-bold border-2 border-crimson-900 shadow-lg">
                    3
                  </div>
                </div>
                <div>
                  <h3 className="text-xl font-bold text-spartan-900 mb-2">
                    Registra Movimientos
                  </h3>
                  <p className="text-spartan-700">
                    Empieza a registrar tus ingresos y gastos. Cada transacción actualiza 
                    automáticamente el saldo de la cuenta correspondiente.
                  </p>
                </div>
              </div>

              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <div className="bg-gradient-to-br from-bronze-600 to-bronze-700 text-white w-12 h-12 rounded-full flex items-center justify-center font-bold border-2 border-bronze-800 shadow-lg">
                    4
                  </div>
                </div>
                <div>
                  <h3 className="text-xl font-bold text-spartan-900 mb-2">
                    Visualiza tu Dashboard
                  </h3>
                  <p className="text-spartan-700">
                    Revisa tu resumen financiero en tiempo real. Ve tus ingresos, gastos, 
                    balance neto y las transacciones más recientes de un vistazo.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section className="bg-gradient-to-br from-crimson-800 via-crimson-900 to-spartan-900 text-white py-16 border-t-4 border-bronze-500">
          <div className="container-custom px-4 text-center">
            <h2 className="text-3xl font-bold mb-4 text-bronze-100">
              ¿Listo para tomar control de tus finanzas?
            </h2>
            <p className="text-bronze-200 text-lg mb-8 max-w-2xl mx-auto">
              Únete a DevalFinance y comienza a gestionar tu dinero de forma espartana y profesional.
            </p>
            <Link to="/register">
              <Button variant="secondary" size="lg">
                Crear Cuenta Gratis
              </Button>
            </Link>
          </div>
        </section>
      </main>

      <footer className="bg-gradient-to-b from-spartan-900 to-spartan-800 text-bronze-200 py-8 border-t-2 border-bronze-700">
        <div className="container-custom px-4">
          <div className="text-center">
            <p className="text-sm">
              © 2024 DevalFinance. Todos los derechos reservados.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

