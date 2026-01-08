# Documentación de Arquitectura - DevalFinance

## Diagrama de Arquitectura Hexagonal Completo

### Vista General del Sistema

```mermaid
graph TB
    subgraph "Frontend - React Application"
        UI[React UI Components]
        Router[React Router]
        State[State Management]
        API_Client[API Client/Axios]
    end
    
    subgraph "Backend - Spring Boot Application"
        subgraph "Presentation Layer"
            UC[UserController]
            TC[TransactionController]
            AC[AccountController]
            RC[ReportController]
        end
        
        subgraph "Application Layer"
            UU[User Use Cases]
            TU[Transaction Use Cases]
            AU[Account Use Cases]
            RU[Report Use Cases]
        end
        
        subgraph "Domain Layer"
            UE[User Entity]
            TE[Transaction Entity]
            AE[Account Entity]
            ME[Membership Entity]
            
            US[User Service]
            TS[Transaction Service]
            MS[Membership Service]
        end
        
        subgraph "Infrastructure Layer"
            UR[JPA User Repository]
            TR[JPA Transaction Repository]
            AR[JPA Account Repository]
            
            SEC[Security Adapter]
            DB[(PostgreSQL Database)]
        end
    end
    
    UI --> Router
    Router --> State
    State --> API_Client
    API_Client -->|HTTP/REST| UC
    API_Client -->|HTTP/REST| TC
    API_Client -->|HTTP/REST| AC
    API_Client -->|HTTP/REST| RC
    
    UC --> UU
    TC --> TU
    AC --> AU
    RC --> RU
    
    UU --> US
    UU --> UE
    TU --> TS
    TU --> TE
    AU --> AE
    RU --> ME
    
    US --> UR
    TS --> TR
    TS --> AR
    US --> SEC
    
    UR --> DB
    TR --> DB
    AR --> DB
```

## Diagrama de Flujo de Autenticación

```mermaid
sequenceDiagram
    participant U as Usuario
    participant UI as Frontend
    participant AC as AuthController
    participant AS as AuthService
    participant US as UserService
    participant JWT as JWT Provider
    participant DB as Database
    
    U->>UI: Ingresa credenciales
    UI->>AC: POST /api/auth/login
    AC->>AS: authenticate(email, password)
    AS->>US: findByEmail(email)
    US->>DB: Query user
    DB-->>US: User entity
    US-->>AS: User domain
    AS->>AS: validatePassword()
    AS->>JWT: generateToken(user)
    JWT-->>AS: JWT Token
    AS-->>AC: AuthResponse(token, user)
    AC-->>UI: 200 OK + Token
    UI->>UI: Store token
    UI-->>U: Redirect to Dashboard
```

## Diagrama de Flujo de Creación de Transacción

```mermaid
sequenceDiagram
    participant U as Usuario
    participant UI as Frontend
    participant TC as TransactionController
    participant TU as TransactionUseCase
    participant TS as TransactionService
    participant MS as MembershipService
    participant AR as AccountRepository
    participant TR as TransactionRepository
    participant DB as Database
    
    U->>UI: Crea transacción
    UI->>TC: POST /api/transactions
    TC->>TC: Validate DTO
    TC->>TU: createTransaction(dto)
    TU->>MS: checkTransactionLimit(userId)
    MS->>DB: Query limits
    DB-->>MS: Current count
    MS-->>TU: Limit status
    alt Limit exceeded (Free plan)
        TU-->>TC: LimitExceededException
        TC-->>UI: 403 Forbidden
    else Within limit
        TU->>AR: findById(accountId)
        AR->>DB: Query account
        DB-->>AR: Account entity
        AR-->>TU: Account domain
        TU->>TS: createTransaction(transaction, account)
        TS->>TR: save(transaction)
        TR->>DB: Insert transaction
        TS->>TS: updateAccountBalance(account, amount)
        TS->>AR: update(account)
        AR->>DB: Update account
        DB-->>TR: Transaction saved
        TR-->>TS: Transaction domain
        TS-->>TU: Transaction created
        TU-->>TC: TransactionDTO
        TC-->>UI: 201 Created
        UI-->>U: Show success message
    end
```

## Diagrama de Modelo de Datos (ERD)

```mermaid
erDiagram
    USER ||--o{ SUBSCRIPTION : has
    USER ||--o{ ACCOUNT : owns
    USER ||--o{ TRANSACTION : creates
    USER ||--o{ CATEGORY : creates
    USER ||--o{ TRANSACTION_LIMIT : has
    
    MEMBERSHIP ||--o{ SUBSCRIPTION : defines
    ACCOUNT ||--o{ TRANSACTION : contains
    CATEGORY ||--o{ TRANSACTION : categorizes
    
    USER {
        uuid id PK
        string email UK
        string password
        string firstName
        string lastName
        timestamp createdAt
        timestamp updatedAt
        boolean active
    }
    
    MEMBERSHIP {
        uuid id PK
        enum planType
        integer maxAccounts
        integer maxTransactionsPerMonth
        boolean hasAdvancedReports
        boolean hasExportCapabilities
        boolean hasHistoricalData
        integer historicalDataYears
        boolean hasAutoCategorization
        boolean hasCustomCategories
        boolean hasMultipleUsers
        boolean hasApiAccess
    }
    
    SUBSCRIPTION {
        uuid id PK
        uuid userId FK
        uuid membershipId FK
        timestamp startDate
        timestamp endDate
        enum status
        timestamp createdAt
    }
    
    ACCOUNT {
        uuid id PK
        uuid userId FK
        string name
        enum accountType
        decimal initialBalance
        decimal currentBalance
        string currency
        timestamp createdAt
        boolean active
    }
    
    TRANSACTION {
        uuid id PK
        uuid accountId FK
        uuid userId FK
        uuid categoryId FK
        decimal amount
        enum transactionType
        string description
        date transactionDate
        string[] tags
        timestamp createdAt
    }
    
    CATEGORY {
        uuid id PK
        uuid userId FK "nullable"
        string name
        string code "DIAN code"
        enum type
        boolean isSystem
        uuid parentCategoryId FK "nullable"
        timestamp createdAt
    }
    
    TRANSACTION_LIMIT {
        uuid id PK
        uuid userId FK
        integer year
        integer month
        integer transactionCount
        integer maxAllowed
        timestamp resetDate
    }
```

## Diagrama de Permisos y Límites por Plan

```mermaid
graph LR
    subgraph "Plan Gratuito"
        F1[1 Cuenta]
        F2[50 Transacciones/mes]
        F3[1 Año Historial]
        F4[Reportes Básicos]
        F5[Categorización Manual]
    end
    
    subgraph "Plan Premium"
        P1[Cuentas Ilimitadas]
        P2[Transacciones Ilimitadas]
        P3[Historial Completo]
        P4[Reportes Avanzados]
        P5[Exportación PDF/Excel]
        P6[Categorización Automática]
        P7[Categorías Personalizadas]
        P8[Reportes Declaración Renta]
    end
    
    subgraph "Plan Business"
        B1[Todas Premium]
        B2[Múltiples Usuarios]
        B3[Facturación Empresarial]
        B4[API Access]
        B5[Soporte Prioritario]
    end
    
    F1 -.->|Upgrade| P1
    F2 -.->|Upgrade| P2
    F3 -.->|Upgrade| P3
    P1 -.->|Upgrade| B1
```

## Flujo de Validación de Permisos

```mermaid
graph TD
    Start[Request Entrante] --> Auth{Usuario Autenticado?}
    Auth -->|No| Reject1[401 Unauthorized]
    Auth -->|Sí| GetUser[Obtener Usuario]
    GetUser --> GetSub[Obtener Suscripción Activa]
    GetSub --> GetMembership[Obtener Detalles de Membresía]
    GetMembership --> CheckLimit{Requiere Validar Límite?}
    
    CheckLimit -->|Sí| QueryCount[Consultar Contador Mensual]
    QueryCount --> Compare{Count < MaxAllowed?}
    Compare -->|No| Reject2[403 Forbidden - Límite Excedido]
    Compare -->|Sí| CheckFeature{Requiere Feature Específica?}
    
    CheckLimit -->|No| CheckFeature
    CheckFeature -->|Sí| HasFeature{Usuario tiene Feature?}
    HasFeature -->|No| Reject3[403 Forbidden - Plan Insuficiente]
    HasFeature -->|Sí| Allow[200 OK - Procesar Request]
    CheckFeature -->|No| Allow
    
    Allow --> Process[Ejecutar Caso de Uso]
    Process --> Response[Retornar Respuesta]
```

## Diagrama de Componentes del Frontend

```mermaid
graph TB
    subgraph "React Application"
        subgraph "Pages"
            Login[Login Page]
            Register[Register Page]
            Dashboard[Dashboard Page]
            Transactions[Transactions Page]
            Accounts[Accounts Page]
            Reports[Reports Page]
            Settings[Settings Page]
        end
        
        subgraph "Components"
            Nav[Navigation]
            Sidebar[Sidebar]
            TransactionForm[Transaction Form]
            AccountCard[Account Card]
            Chart[Chart Components]
            Table[Data Table]
        end
        
        subgraph "State Management"
            AuthStore[Auth Store]
            UserStore[User Store]
            TransactionStore[Transaction Store]
        end
        
        subgraph "Services"
            AuthAPI[Auth API Service]
            TransactionAPI[Transaction API Service]
            AccountAPI[Account API Service]
            ReportAPI[Report API Service]
        end
        
        subgraph "Utils"
            Http[HTTP Client]
            Token[Token Manager]
            Validator[Form Validators]
        end
    end
    
    Login --> AuthStore
    Register --> AuthStore
    Dashboard --> TransactionStore
    Dashboard --> UserStore
    Transactions --> TransactionForm
    Transactions --> Table
    Accounts --> AccountCard
    Reports --> Chart
    
    AuthStore --> AuthAPI
    TransactionStore --> TransactionAPI
    UserStore --> AccountAPI
    
    AuthAPI --> Http
    TransactionAPI --> Http
    AccountAPI --> Http
    ReportAPI --> Http
    
    Http --> Token
    TransactionForm --> Validator
```

## Arquitectura de Deployment (Futuro)

```mermaid
graph TB
    subgraph "Production Environment"
        subgraph "Load Balancer"
            LB[NGINX / AWS ALB]
        end
        
        subgraph "Application Servers"
            App1[Backend Instance 1]
            App2[Backend Instance 2]
            App3[Backend Instance N]
        end
        
        subgraph "Database"
            Primary[(PostgreSQL Primary)]
            Replica[(PostgreSQL Replica)]
        end
        
        subgraph "File Storage"
            S3[S3 / Object Storage]
        end
        
        subgraph "Cache"
            Redis[Redis Cache]
        end
        
        subgraph "Frontend"
            CDN[CDN / S3 + CloudFront]
        end
    end
    
    Users[Usuarios] --> LB
    LB --> App1
    LB --> App2
    LB --> App3
    
    App1 --> Primary
    App2 --> Primary
    App3 --> Primary
    App1 --> Redis
    App2 --> Redis
    App3 --> Redis
    App1 --> S3
    App2 --> S3
    App3 --> S3
    
    Primary --> Replica
    
    Users --> CDN
    CDN --> App1
    CDN --> App2
    CDN --> App3
```

## Flujo de Generación de Reporte de Declaración de Renta

```mermaid
sequenceDiagram
    participant U as Usuario Premium/Business
    participant UI as Frontend
    participant RC as ReportController
    participant RU as ReportUseCase
    participant TS as TransactionService
    participant CS as CategoryService
    participant DB as Database
    participant PDF as PDF Generator
    
    U->>UI: Solicita Reporte Declaración Renta
    UI->>RC: GET /api/reports/tax-report?year=2024
    RC->>RC: Validate user membership
    RC->>RU: generateTaxReport(userId, year)
    RU->>TS: getTransactionsByYear(userId, year)
    TS->>DB: Query transactions
    DB-->>TS: Transactions list
    TS-->>RU: Transactions domain
    RU->>CS: categorizeByDIAN(transactions)
    CS->>DB: Query DIAN categories
    DB-->>CS: Categories
    CS->>CS: Group by DIAN code
    CS-->>RU: Categorized data
    RU->>RU: formatForTaxDeclaration(data)
    RU->>PDF: generatePDF(reportData)
    PDF-->>RU: PDF file
    RU-->>RC: ReportDTO + PDF bytes
    RC-->>UI: 200 OK + PDF file
    UI-->>U: Download PDF
```

---

## Notas sobre la Arquitectura

### Principios Aplicados

1. **Separación de Responsabilidades:** Cada capa tiene una responsabilidad clara y única
2. **Inversión de Dependencias:** El dominio no depende de infraestructura
3. **Testabilidad:** Cada componente puede ser testeado de forma independiente
4. **Escalabilidad:** Preparado para migrar a microservicios sin cambiar el dominio
5. **Mantenibilidad:** Código organizado y fácil de entender

### Patrones de Diseño Utilizados

- **Repository Pattern:** Abstracción de acceso a datos
- **Use Case Pattern:** Encapsulación de lógica de negocio
- **DTO Pattern:** Transferencia de datos entre capas
- **Adapter Pattern:** Adaptación de interfaces externas
- **Strategy Pattern:** Para diferentes algoritmos de reportes
- **Factory Pattern:** Para creación de entidades complejas

### Decisiones Arquitectónicas

1. **Arquitectura Hexagonal:** Elegida por su flexibilidad y testabilidad
2. **Monolítico Modular:** Empezamos monolítico pero con módulos bien separados
3. **API REST:** Estándar de la industria para comunicación frontend-backend
4. **JWT para Autenticación:** Stateless y escalable
5. **PostgreSQL:** Base de datos relacional robusta y open-source

### Estructura de Paquetes

```
src/main/java/com/devalFinance/
├── domain/                    # CORE - Sin dependencias externas
│   ├── model/                # Entidades de dominio
│   └── repository/           # Interfaces (Ports)
│
├── application/              # Casos de Uso
│   ├── usecase/
│   └── dto/
│
├── infrastructure/           # Adapters - Implementaciones
│   ├── persistence/
│   │   ├── entity/          # JPA Entities
│   │   ├── repository/      # Spring Data Repositories
│   │   └── adapter/         # Repository Adapters
│   ├── security/
│   └── config/
│
└── presentation/            # REST Controllers
    ├── controller/
    ├── mapper/
    └── exception/
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

## Ventajas de esta Arquitectura

1. **Testabilidad:** Fácil de testear cada capa independientemente
2. **Mantenibilidad:** Código organizado y fácil de entender
3. **Escalabilidad:** Preparado para crecer y migrar a microservicios
4. **Flexibilidad:** Fácil cambiar implementaciones sin afectar el dominio
5. **Clean Code:** Separación clara de responsabilidades
