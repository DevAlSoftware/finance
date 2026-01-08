# DevalFinance - Sistema de Contabilidad Personal

## Descripción del Proyecto

DevalFinance es una aplicación web moderna de contabilidad personal diseñada para ayudar a las personas a gestionar sus finanzas de manera eficiente y preparar su declaración de renta de forma simplificada. La aplicación permite a los usuarios llevar un control completo de sus ingresos, gastos, cuentas bancarias y generar reportes fiscales pre-formateados.

### Objetivo Principal

Proporcionar una plataforma accesible, intuitiva y potente que permita a cualquier persona gestionar su contabilidad personal, con planes de membresía que desbloquean funcionalidades avanzadas según las necesidades del usuario.

### Público Objetivo

- Personas naturales que necesitan llevar control de sus finanzas personales
- Profesionales independientes que requieren organización financiera
- Pequeños empresarios que necesitan contabilidad básica
- Usuarios que requieren preparar su declaración de renta de forma organizada

---

## Requisitos Funcionales

### RF01 - Gestión de Usuarios
- RF01.1: El sistema debe permitir el registro de nuevos usuarios con email y contraseña
- RF01.2: El sistema debe permitir autenticación de usuarios existentes
- RF01.3: El sistema debe permitir recuperación de contraseña
- RF01.4: El sistema debe gestionar perfiles de usuario (datos personales, preferencias)
- RF01.5: El sistema debe asignar automáticamente plan gratuito a usuarios nuevos

### RF02 - Gestión de Membresías
- RF02.1: El sistema debe gestionar tres tipos de planes: Gratuito, Premium y Business
- RF02.2: El sistema debe permitir actualización de plan de membresía
- RF02.3: El sistema debe aplicar límites según el plan del usuario
- RF02.4: El sistema debe validar permisos antes de permitir acciones
- RF02.5: El sistema debe gestionar fechas de inicio y fin de suscripción

### RF03 - Gestión de Cuentas
- RF03.1: El sistema debe permitir crear cuentas bancarias (Ahorros, Corriente, Efectivo)
- RF03.2: El sistema debe calcular saldo automático por cuenta
- RF03.3: El sistema debe permitir editar y eliminar cuentas
- RF03.4: El sistema debe limitar número de cuentas según plan (Gratuito: 1, Premium/Business: Ilimitado)
- RF03.5: El sistema debe mostrar resumen de todas las cuentas del usuario

### RF04 - Gestión de Transacciones
- RF04.1: El sistema debe permitir registrar transacciones (Ingresos y Gastos)
- RF04.2: El sistema debe permitir categorizar transacciones según códigos DIAN
- RF04.3: El sistema debe permitir editar y eliminar transacciones
- RF04.4: El sistema debe asociar transacciones a cuentas específicas
- RF04.5: El sistema debe permitir búsqueda y filtrado de transacciones
- RF04.6: El sistema debe limitar transacciones mensuales según plan (Gratuito: 50, Premium/Business: Ilimitado)

### RF05 - Categorización
- RF05.1: El sistema debe mantener catálogo de categorías predefinidas con códigos DIAN
- RF05.2: El sistema debe permitir categorización automática sugerida (Premium/Business)
- RF05.3: El sistema debe permitir categorías personalizadas (Premium/Business)
- RF05.4: El sistema debe validar códigos DIAN según normativa colombiana

### RF06 - Reportes y Análisis
- RF06.1: El sistema debe generar reporte de ingresos y gastos por período
- RF06.2: El sistema debe generar reporte pre-formateado para declaración de renta
- RF06.3: El sistema debe mostrar gráficos de distribución de gastos
- RF06.4: El sistema debe permitir exportar reportes en PDF (Premium/Business)
- RF06.5: El sistema debe permitir exportar reportes en Excel (Premium/Business)
- RF06.6: El sistema debe generar reportes anuales para declaración de renta

### RF07 - Historial y Archivo
- RF07.1: El sistema debe mantener historial completo de transacciones (Premium/Business)
- RF07.2: El sistema debe limitar historial a 1 año para plan Gratuito
- RF07.3: El sistema debe permitir consulta histórica por año
- RF07.4: El sistema debe mantener integridad de datos históricos

### RF08 - Dashboard y Visualización
- RF08.1: El sistema debe mostrar dashboard con resumen financiero
- RF08.2: El sistema debe mostrar gráficos de tendencias de ingresos/gastos
- RF08.3: El sistema debe mostrar top categorías de gastos
- RF08.4: El sistema debe mostrar saldo total consolidado

### RF09 - Notificaciones y Alertas
- RF09.1: El sistema debe alertar cuando se aproxima límite de transacciones (plan Gratuito)
- RF09.2: El sistema debe notificar fechas importantes para declaración de renta
- RF09.3: El sistema debe alertar sobre categorías sin asignar

---

## Requisitos No Funcionales

### RNF01 - Rendimiento
- RNF01.1: El sistema debe responder a peticiones HTTP en menos de 500ms (p95)
- RNF01.2: El sistema debe soportar al menos 1000 usuarios concurrentes
- RNF01.3: Las consultas de base de datos deben optimizarse con índices apropiados
- RNF01.4: El frontend debe cargar en menos de 3 segundos

### RNF02 - Escalabilidad
- RNF02.1: La arquitectura debe permitir escalamiento horizontal
- RNF02.2: El sistema debe estar preparado para migración a microservicios
- RNF02.3: La base de datos debe soportar particionamiento si es necesario

### RNF03 - Seguridad
- RNF03.1: Las contraseñas deben almacenarse con hash bcrypt
- RNF03.2: El sistema debe usar autenticación JWT con tokens de acceso y refresh
- RNF03.3: Todas las comunicaciones deben usar HTTPS
- RNF03.4: El sistema debe validar y sanitizar todas las entradas del usuario
- RNF03.5: El sistema debe implementar rate limiting para prevenir abusos
- RNF03.6: Los datos financieros deben estar cifrados en reposo

### RNF04 - Disponibilidad
- RNF04.1: El sistema debe tener disponibilidad de 99.5% (uptime)
- RNF04.2: El sistema debe implementar manejo de errores robusto
- RNF04.3: El sistema debe tener backups automáticos diarios

### RNF05 - Usabilidad
- RNF05.1: La interfaz debe ser intuitiva y fácil de usar
- RNF05.2: El sistema debe ser responsive (móvil, tablet, desktop)
- RNF05.3: El sistema debe tener feedback visual para todas las acciones
- RNF05.4: El sistema debe tener mensajes de error claros y descriptivos

### RNF06 - Mantenibilidad
- RNF06.1: El código debe seguir principios SOLID y Clean Code
- RNF06.2: El código debe tener cobertura de tests superior al 70%
- RNF06.3: El sistema debe usar arquitectura hexagonal para facilitar mantenimiento
- RNF06.4: El código debe estar documentado con JavaDoc

### RNF07 - Portabilidad
- RNF07.1: El sistema debe estar containerizado con Docker
- RNF07.2: El sistema debe funcionar en diferentes sistemas operativos
- RNF07.3: El sistema debe usar variables de entorno para configuración

### RNF08 - Integración
- RNF08.1: El sistema debe exponer API REST bien documentada
- RNF08.2: El sistema debe usar OpenAPI/Swagger para documentación
- RNF08.3: El sistema debe estar preparado para integraciones futuras (bancos, DIAN)

---

## Alcance del Proyecto

### Dentro del Alcance (Versión 1.0)

**Funcionalidades Core:**
- Sistema de autenticación y autorización completo
- Gestión de usuarios y perfiles
- Sistema de membresías con tres planes (Gratuito, Premium, Business)
- Gestión completa de cuentas bancarias
- Registro y gestión de transacciones (Ingresos/Gastos)
- Categorización de transacciones con códigos DIAN
- Dashboard con resumen financiero
- Generación de reportes básicos y avanzados
- Exportación de reportes (PDF/Excel para planes pagos)
- Sistema de permisos y límites por plan
- Historial de transacciones con límites por plan

**Tecnologías:**
- Backend: Java 21 + Spring Boot 4.0.1
- Frontend: React 18+ con TypeScript
- Base de Datos: PostgreSQL
- Autenticación: JWT con Spring Security
- Arquitectura: Hexagonal (Ports & Adapters)
- Containerización: Docker y Docker Compose
- Documentación: OpenAPI/Swagger

**Características Técnicas:**
- API REST completa
- Arquitectura preparada para escalamiento
- Código limpio siguiendo Clean Code y SOLID
- Tests unitarios y de integración
- Migraciones de base de datos con Flyway/Liquibase

### Fuera del Alcance (Versión 1.0)

**Funcionalidades No Incluidas:**
- Integración directa con bancos (API bancarias)
- Integración directa con DIAN para envío de declaración
- Aplicación móvil nativa (iOS/Android)
- Sistema de facturación electrónica completo
- Múltiples monedas (solo peso colombiano COP)
- Presupuestos y planeación financiera avanzada
- Inversiones y portafolio
- Reconciliación bancaria automática
- Notificaciones push en tiempo real
- Chat o soporte en vivo integrado
- Sistema de pagos integrado para suscripciones

**Nota:** Estas funcionalidades pueden ser consideradas para versiones futuras.

---

## Modelo de Membresías

### Plan Gratuito
**Límites y Características:**
- 1 cuenta bancaria
- 50 transacciones por mes
- Reportes básicos (visualización en pantalla)
- Historial limitado a 1 año
- Categorización manual básica
- Dashboard básico

**Precio:** Gratis

### Plan Premium
**Límites y Características:**
- Cuentas ilimitadas
- Transacciones ilimitadas
- Reportes avanzados con gráficos
- Historial completo sin límites
- Categorización automática inteligente
- Categorías personalizadas
- Exportación a PDF y Excel
- Reportes pre-formateados para declaración de renta
- Soporte prioritario por email
- Sin publicidad

**Precio:** Por definir

### Plan Business
**Todas las características de Premium, más:**
- Múltiples usuarios por cuenta
- Facturación y contabilidad empresarial
- Reportes consolidados multi-usuario
- API access para integraciones
- Soporte telefónico prioritario
- Personalización de reportes
- White-label (futuro)

**Precio:** Por definir

---

## Arquitectura del Sistema

### Arquitectura Hexagonal (Ports & Adapters)

La aplicación sigue una arquitectura hexagonal que separa la lógica de negocio de los detalles de implementación, facilitando el testing, mantenimiento y futura migración a microservicios.

#### Capas de la Arquitectura

**1. Domain (Núcleo)**
- Contiene las entidades de negocio y reglas de dominio
- No tiene dependencias externas
- Interfaces (Ports) para repositorios y servicios externos
- Lógica de negocio pura

**2. Application (Casos de Uso)**
- Implementa los casos de uso de la aplicación
- Orquesta las llamadas entre dominio e infraestructura
- Define DTOs para comunicación entre capas
- Contiene la lógica de aplicación

**3. Infrastructure (Adapters)**
- Implementa las interfaces definidas en Domain
- Adaptadores para persistencia (JPA)
- Adaptadores para seguridad (Spring Security)
- Adaptadores para servicios externos
- Configuraciones técnicas

**4. Presentation (Interfaz)**
- Controladores REST
- Mappers DTO <-> Domain
- Manejo de excepciones HTTP
- Validaciones de entrada

#### Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │UserController│  │Transaction   │  │Report        │      │
│  │              │  │Controller    │  │Controller    │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                  │                  │              │
└─────────┼──────────────────┼──────────────────┼──────────────┘
          │                  │                  │
┌─────────┼──────────────────┼──────────────────┼──────────────┐
│         │    APPLICATION LAYER (Use Cases)     │              │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐          │
│  │CreateUser   │  │CreateTrans  │  │GenerateTax  │          │
│  │UseCase      │  │UseCase      │  │ReportUseCase│          │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘          │
│         │                  │                  │              │
└─────────┼──────────────────┼──────────────────┼──────────────┘
          │                  │                  │
┌─────────┼──────────────────┼──────────────────┼──────────────┐
│         │      DOMAIN LAYER (Core Business)    │              │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐          │
│  │User         │  │Transaction  │  │Membership   │          │
│  │(Entity)     │  │(Entity)     │  │Service      │          │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘          │
│         │                  │                  │              │
│  ┌──────▼──────────────────▼──────────────────▼──────┐      │
│  │        Repository Interfaces (Ports)              │      │
│  │  UserRepository │ TransactionRepository │ ...     │      │
│  └───────────────────────────────────────────────────┘      │
└──────────────────────────────────────────────────────────────┘
          │                  │                  │
┌─────────┼──────────────────┼──────────────────┼──────────────┐
│         │    INFRASTRUCTURE LAYER (Adapters)   │              │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐          │
│  │JpaUser      │  │JpaTrans     │  │Security     │          │
│  │Repository   │  │Repository   │  │Adapter      │          │
│  │(Adapter)    │  │(Adapter)    │  │             │          │
│  └─────────────┘  └─────────────┘  └─────────────┘          │
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │PostgreSQL    │  │Spring        │  │External APIs │      │
│  │Database      │  │Security      │  │(Future)      │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└──────────────────────────────────────────────────────────────┘
```

### Flujo de Datos

1. **Request HTTP** → Controller (Presentation)
2. **Controller** → Valida entrada, convierte DTO a Domain
3. **Use Case** (Application) → Orquesta la lógica de negocio
4. **Domain Service** → Ejecuta reglas de negocio
5. **Repository Interface** (Port) → Definido en Domain
6. **Repository Adapter** (Infrastructure) → Implementa persistencia
7. **Database** → Almacena/recupera datos
8. **Response** → Flujo inverso hasta retornar DTO al cliente

---

## Modelo de Datos

### Entidades Principales

#### User (Usuario)
```
- id: UUID (PK)
- email: String (unique, not null)
- password: String (hashed, not null)
- firstName: String
- lastName: String
- membershipId: UUID (FK -> Membership)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- active: Boolean
```

#### Membership (Membresía)
```
- id: UUID (PK)
- planType: Enum (FREE, PREMIUM, BUSINESS)
- maxAccounts: Integer (null = unlimited)
- maxTransactionsPerMonth: Integer (null = unlimited)
- hasAdvancedReports: Boolean
- hasExportCapabilities: Boolean
- hasHistoricalData: Boolean
- historicalDataYears: Integer (null = unlimited)
- hasAutoCategorization: Boolean
- hasCustomCategories: Boolean
- hasMultipleUsers: Boolean
- hasApiAccess: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### Subscription (Suscripción del Usuario)
```
- id: UUID (PK)
- userId: UUID (FK -> User)
- membershipId: UUID (FK -> Membership)
- startDate: LocalDateTime
- endDate: LocalDateTime (null = active)
- status: Enum (ACTIVE, EXPIRED, CANCELLED)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### Account (Cuenta Bancaria)
```
- id: UUID (PK)
- userId: UUID (FK -> User)
- name: String (not null)
- accountType: Enum (SAVINGS, CHECKING, CASH)
- initialBalance: BigDecimal
- currentBalance: BigDecimal (calculated)
- currency: String (default: COP)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- active: Boolean
```

#### Transaction (Transacción)
```
- id: UUID (PK)
- accountId: UUID (FK -> Account)
- userId: UUID (FK -> User)
- amount: BigDecimal (not null)
- transactionType: Enum (INCOME, EXPENSE)
- categoryId: UUID (FK -> Category)
- description: String
- transactionDate: LocalDate (not null)
- tags: String[] (optional)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### Category (Categoría)
```
- id: UUID (PK)
- name: String (not null)
- code: String (DIAN code, optional)
- type: Enum (INCOME, EXPENSE)
- isSystem: Boolean (system categories vs custom)
- userId: UUID (FK -> User, null if system category)
- parentCategoryId: UUID (FK -> Category, optional for subcategories)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### TransactionLimit (Límite de Transacciones - para control)
```
- id: UUID (PK)
- userId: UUID (FK -> User)
- year: Integer
- month: Integer
- transactionCount: Integer (default: 0)
- maxAllowed: Integer
- resetDate: LocalDateTime
```

### Diagrama de Relaciones (ER)

```
User
├── 1:N Subscription
│   └── N:1 Membership
├── 1:N Account
│   └── 1:N Transaction
│       └── N:1 Category
├── 1:N Transaction (direct relationship)
├── 1:N Category (custom categories)
└── 1:12 TransactionLimit (one per month)
```

---

## Casos de Uso Principales

### UC01 - Registro de Usuario
**Actor:** Usuario no autenticado  
**Precondición:** El usuario no existe en el sistema  
**Flujo Principal:**
1. Usuario accede a página de registro
2. Usuario ingresa email y contraseña
3. Sistema valida formato de email y fortaleza de contraseña
4. Sistema crea usuario con plan Gratuito
5. Sistema envía email de confirmación
6. Sistema autentica al usuario automáticamente
7. Sistema redirige al dashboard

**Flujo Alternativo:**
- 3a. Email inválido: Sistema muestra error
- 3b. Contraseña débil: Sistema muestra error
- 4a. Email ya existe: Sistema muestra error

### UC02 - Autenticación
**Actor:** Usuario  
**Precondición:** El usuario está registrado  
**Flujo Principal:**
1. Usuario ingresa email y contraseña
2. Sistema valida credenciales
3. Sistema genera JWT token
4. Sistema retorna token al cliente
5. Cliente almacena token para futuras peticiones

### UC03 - Crear Transacción
**Actor:** Usuario autenticado  
**Precondición:** Usuario tiene al menos una cuenta creada  
**Flujo Principal:**
1. Usuario selecciona crear transacción
2. Usuario ingresa monto, tipo (Ingreso/Gasto), categoría, fecha
3. Sistema valida que no exceda límite de transacciones mensuales (si plan Gratuito)
4. Sistema crea transacción
5. Sistema actualiza saldo de la cuenta
6. Sistema muestra confirmación

**Flujo Alternativo:**
- 3a. Límite excedido: Sistema muestra alerta y opción de actualizar plan

### UC04 - Actualizar Membresía
**Actor:** Usuario autenticado  
**Precondición:** Usuario tiene plan Gratuito o Premium  
**Flujo Principal:**
1. Usuario accede a configuración de membresía
2. Usuario selecciona nuevo plan
3. Sistema muestra características y precio del plan
4. Usuario confirma actualización
5. Sistema actualiza suscripción del usuario
6. Sistema aplica nuevos límites inmediatamente
7. Sistema redirige a dashboard

### UC05 - Generar Reporte para Declaración de Renta
**Actor:** Usuario Premium o Business  
**Precondición:** Usuario tiene plan Premium o Business  
**Flujo Principal:**
1. Usuario accede a sección de reportes
2. Usuario selecciona "Reporte para Declaración de Renta"
3. Usuario selecciona año fiscal
4. Sistema genera reporte con transacciones categorizadas según DIAN
5. Sistema presenta reporte en pantalla
6. Usuario puede exportar a PDF o Excel

**Flujo Alternativo:**
- 2a. Usuario con plan Gratuito: Sistema muestra opción de actualizar plan

### UC06 - Categorización Automática
**Actor:** Usuario Premium o Business  
**Precondición:** Usuario tiene plan Premium o Business  
**Flujo Principal:**
1. Usuario crea transacción con descripción
2. Sistema analiza descripción usando algoritmo de ML/similitud
3. Sistema sugiere categoría más probable
4. Usuario confirma o selecciona otra categoría
5. Sistema guarda transacción con categoría asignada
6. Sistema aprende del patrón para futuras sugerencias

---

## Stack Tecnológico

### Backend
- **Lenguaje:** Java 21
- **Framework:** Spring Boot 4.0.1
- **Persistencia:** Spring Data JPA
- **Base de Datos:** PostgreSQL 15+
- **Seguridad:** Spring Security + JWT
- **Validación:** Jakarta Validation API
- **Mapeo:** MapStruct
- **Migraciones:** Flyway o Liquibase
- **Documentación API:** SpringDoc OpenAPI
- **Testing:** JUnit 5, Mockito, TestContainers

### Frontend
- **Lenguaje:** TypeScript 5+
- **Framework:** React 18+
- **Routing:** React Router v6
- **Estado Servidor:** TanStack Query (React Query)
- **Estado Global:** Zustand o Context API
- **HTTP Client:** Axios
- **UI Framework:** Tailwind CSS o Material-UI
- **Gráficos:** Recharts
- **Formularios:** Formik + Yup
- **Build Tool:** Vite

### Infraestructura
- **Containerización:** Docker + Docker Compose
- **Control de Versiones:** Git
- **CI/CD:** GitHub Actions (futuro)
- **Monitoreo:** (Por definir)
- **Logging:** Logback (Spring Boot default)

---

## Estructura de Directorios

### Backend
```
src/main/java/com/devalFinance/
├── DevalFinanceApplication.java
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── service/
│
├── application/
│   ├── usecase/
│   │   ├── user/
│   │   ├── transaction/
│   │   ├── account/
│   │   ├── membership/
│   │   └── report/
│   └── dto/
│       ├── request/
│       └── response/
│
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── adapter/
│   ├── security/
│   ├── external/
│   └── config/
│
└── presentation/
    ├── controller/
    ├── mapper/
    └── exception/
```

### Frontend
```
frontend/
├── public/
├── src/
│   ├── components/
│   │   ├── common/
│   │   ├── transactions/
│   │   ├── accounts/
│   │   ├── dashboard/
│   │   └── reports/
│   ├── pages/
│   ├── services/
│   │   └── api/
│   ├── store/
│   ├── hooks/
│   ├── utils/
│   ├── types/
│   └── App.tsx
├── package.json
└── tsconfig.json
```

---

## Plan de Implementación (Fases)

### Fase 1: Fundamentos (Semanas 1-2)
- Configurar estructura de proyecto con arquitectura hexagonal
- Configurar base de datos PostgreSQL
- Implementar autenticación JWT básica
- Crear entidades de dominio y JPA entities
- Configurar migraciones de base de datos

### Fase 2: Gestión de Usuarios y Membresías (Semanas 3-4)
- Implementar registro y login
- Implementar sistema de membresías
- Implementar validación de permisos
- Configurar Spring Security completo

### Fase 3: Core de Contabilidad (Semanas 5-7)
- Implementar gestión de cuentas
- Implementar gestión de transacciones
- Implementar categorización
- Implementar cálculo de saldos

### Fase 4: Reportes y Dashboard (Semanas 8-9)
- Implementar dashboard con métricas
- Implementar generación de reportes básicos
- Implementar exportación PDF/Excel
- Implementar reportes para declaración de renta

### Fase 5: Frontend Básico (Semanas 10-12)
- Configurar proyecto React
- Implementar autenticación en frontend
- Implementar páginas principales
- Integrar con backend

### Fase 6: Frontend Avanzado (Semanas 13-14)
- Implementar dashboard interactivo
- Implementar formularios de transacciones
- Implementar visualizaciones con gráficos
- Optimizar UX/UI

### Fase 7: Dockerización y Deploy (Semana 15)
- Crear Dockerfiles
- Configurar Docker Compose para desarrollo
- Documentar proceso de despliegue
- Preparar para producción

---

## Convenciones de Código

### Naming Conventions
- **Clases:** PascalCase (ej: `UserService`, `TransactionRepository`)
- **Métodos:** camelCase (ej: `createUser`, `calculateBalance`)
- **Variables:** camelCase (ej: `userAccount`, `transactionAmount`)
- **Constantes:** UPPER_SNAKE_CASE (ej: `MAX_TRANSACTIONS_FREE`)
- **Paquetes:** lowercase (ej: `com.devalFinance.domain.model`)

### Estructura de Commits
- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `refactor:` Refactorización de código
- `docs:` Cambios en documentación
- `test:` Añadir o modificar tests
- `chore:` Tareas de mantenimiento

### Principios a Seguir
- **SOLID:** Aplicar todos los principios
- **DRY:** Don't Repeat Yourself
- **KISS:** Keep It Simple, Stupid
- **YAGNI:** You Aren't Gonna Need It
- **Clean Code:** Código legible y autodocumentado

---

## Estado del Proyecto

**Versión Actual:** 0.0.1-SNAPSHOT  
**Estado:** Fase 1 y 2 Completadas - Backend Core Implementado  
**Última Actualización:** Enero 2025

### Progreso de Implementación

**✅ Fase 1: Fundamentos - COMPLETADA**
- Estructura de arquitectura hexagonal implementada
- Entidades de dominio completas (11 entidades)
- Repositorios e interfaces (7 repositorios)
- Entidades JPA y adapters (7 adapters)
- Migraciones Flyway configuradas
- Base de datos PostgreSQL configurada

**✅ Fase 2: Gestión de Usuarios y Membresías - COMPLETADA**
- Casos de uso de autenticación implementados
- Sistema de membresías con planes (FREE, PREMIUM, BUSINESS)
- Validación de permisos y límites
- Spring Security con JWT configurado
- Controllers REST básicos

**✅ Casos de Uso Implementados:**
- Registro y autenticación de usuarios
- Gestión completa de cuentas (CRUD)
- Creación de transacciones con validación de límites
- Consulta de transacciones por período

**⏳ En Progreso:**
- Más casos de uso (actualización/eliminación de transacciones)
- Reportes y dashboard
- Frontend React

---

## Estructura Actual del Proyecto

### Código Implementado

**74 archivos Java compilados exitosamente**

#### Domain Layer (11 entidades + 7 repositorios)
- Modelos de dominio puros sin dependencias externas
- Interfaces de repositorio (Ports) bien definidas

#### Application Layer (13 casos de uso + 9 DTOs)
- Casos de uso organizados por dominio
- DTOs para request/response separados
- Validaciones de aplicación implementadas

#### Infrastructure Layer (7 entities + 7 adapters + security)
- Adapters que implementan los ports del dominio
- JWT authentication filter
- Flyway para migraciones SQL

#### Presentation Layer (3 controllers + exception handling)
- REST controllers con validación
- Manejo centralizado de excepciones
- Argument resolvers para obtener usuario actual

#### Tests
- 17 tests unitarios implementados y pasando
- Tests de modelos de dominio
- Tests de casos de uso

---

## Flujo de Trabajo Git

Este proyecto usa **Git Flow** con las siguientes ramas:

- **main:** Rama de producción (estable, solo releases)
- **develop:** Rama de desarrollo (integración continua)

Para más detalles sobre el flujo de trabajo, convenciones de commits y branching strategy, consulta:
- [docs/GIT_WORKFLOW.md](docs/GIT_WORKFLOW.md) - Estrategia completa de Git
- [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md) - Guía de contribución

### Convenciones de Commits

Usamos convenciones estándar:
- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `docs:` Documentación
- `refactor:` Refactorización
- `test:` Tests
- `chore:` Mantenimiento

---

## Próximos Pasos

1. Completar más casos de uso de transacciones
2. Implementar casos de uso de reportes
3. Crear frontend React básico
4. Mejorar documentación API con Swagger
5. Agregar más tests de integración

---

## Contacto y Contribución

Este es un proyecto en desarrollo activo. La documentación se actualizará conforme avance el desarrollo.

**Recuerda:** Este README es un documento vivo que se actualizará constantemente con cada fase del proyecto.

