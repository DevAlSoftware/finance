# Arquitectura de la Capa de Aplicación - DevalFinance

## Propósito de la Capa de Aplicación

La capa de aplicación actúa como orquestador entre la presentación (controllers) y el dominio (entidades y repositorios). Contiene la lógica de aplicación, coordina los casos de uso y define los DTOs para la comunicación entre capas.

---

## Estructura de Directorios

```
application/
├── dto/
│   ├── request/
│   │   ├── CreateAccountRequest.java
│   │   ├── CreateTransactionRequest.java
│   │   ├── LoginRequest.java
│   │   ├── RegisterUserRequest.java
│   │   └── UpdateAccountRequest.java
│   └── response/
│       ├── AccountResponse.java
│       ├── AuthResponse.java
│       ├── TransactionResponse.java
│       └── UserResponse.java
└── usecase/
    ├── account/
    │   ├── CreateAccountUseCase.java
    │   ├── DeleteAccountUseCase.java
    │   ├── GetAccountByIdUseCase.java
    │   ├── GetAccountsUseCase.java
    │   └── UpdateAccountUseCase.java
    ├── transaction/
    │   ├── CreateTransactionUseCase.java
    │   ├── GetTransactionsUseCase.java
    │   └── ValidateTransactionLimitUseCase.java
    └── user/
        ├── AuthenticateUserUseCase.java
        ├── GenerateTokenUseCase.java
        ├── RegisterUserUseCase.java
        └── ValidatePasswordStrengthUseCase.java
```

---

## DTOs (Data Transfer Objects)

### Request DTOs

Los Request DTOs representan los datos que vienen del cliente (HTTP requests). Incluyen validaciones con Jakarta Validation.

#### RegisterUserRequest
- email: String (validación de formato email)
- password: String (validación de fortaleza)
- confirmPassword: String
- firstName: String
- lastName: String

#### LoginRequest
- email: String
- password: String

#### CreateAccountRequest
- name: String
- accountType: AccountType (SAVINGS, CHECKING, CASH)
- initialBalance: BigDecimal (validación >= 0)
- currency: String

#### UpdateAccountRequest
- name: String (opcional)
- accountType: AccountType (opcional)
- currency: String (opcional)

#### CreateTransactionRequest
- accountId: UUID
- amount: BigDecimal (validación > 0)
- transactionType: TransactionType (INCOME, EXPENSE)
- description: String
- transactionDate: LocalDate
- tags: List<String> (opcional)

### Response DTOs

Los Response DTOs representan los datos que se retornan al cliente. No contienen información sensible y están optimizados para la serialización JSON.

#### AuthResponse
- accessToken: String
- refreshToken: String
- tokenType: String
- expiresIn: Long
- user: UserResponse

#### UserResponse
- id: UUID
- email: String
- firstName: String
- lastName: String
- fullName: String
- membershipPlanId: UUID
- createdAt: LocalDateTime
- active: Boolean

#### AccountResponse
- id: UUID
- userId: UUID
- name: String
- accountType: AccountType
- initialBalance: BigDecimal
- currentBalance: BigDecimal
- currency: String
- createdAt: LocalDateTime
- active: Boolean

#### TransactionResponse
- id: UUID
- accountId: UUID
- userId: UUID
- amount: BigDecimal
- transactionType: TransactionType
- description: String
- transactionDate: LocalDate
- categoryId: UUID (opcional)
- tags: List<String>
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

---

## Casos de Uso (Use Cases)

Los casos de uso encapsulan la lógica de aplicación. Cada caso de uso es independiente, transaccional y sigue el principio de responsabilidad única.

### Casos de Uso de Usuario

#### RegisterUserUseCase
Responsabilidad: Registrar un nuevo usuario en el sistema
Flujo:
1. Valida request (contraseñas coinciden, fortaleza)
2. Verifica que email no exista
3. Encripta contraseña con BCrypt
4. Crea usuario en dominio
5. Guarda usuario
6. Asigna plan FREE por defecto
7. Crea suscripción activa
8. Genera token JWT
9. Retorna AuthResponse

Dependencias:
- UserRepository
- MembershipPlanRepository
- SubscriptionRepository
- PasswordEncoder
- GenerateTokenUseCase
- ValidatePasswordStrengthUseCase

#### AuthenticateUserUseCase
Responsabilidad: Autenticar un usuario existente
Flujo:
1. Busca usuario por email
2. Valida contraseña con BCrypt
3. Verifica que usuario esté activo
4. Genera token JWT
5. Retorna AuthResponse

Dependencias:
- UserRepository
- PasswordEncoder
- GenerateTokenUseCase

#### GenerateTokenUseCase
Responsabilidad: Generar tokens JWT (access y refresh)
Flujo:
1. Genera access token con expiración de 24 horas
2. Genera refresh token con expiración de 7 días
3. Retorna AuthResponse con ambos tokens

Dependencias:
- JwtTokenProvider

#### ValidatePasswordStrengthUseCase
Responsabilidad: Validar que una contraseña cumpla requisitos de seguridad
Flujo:
1. Valida mínimo 8 caracteres
2. Valida al menos una mayúscula
3. Valida al menos una minúscula
4. Valida al menos un número
5. Lanza excepción si no cumple

---

### Casos de Uso de Cuentas

#### CreateAccountUseCase
Responsabilidad: Crear una nueva cuenta bancaria para un usuario
Flujo:
1. Valida que usuario existe
2. Valida límite de cuentas según plan de membresía
3. Crea objeto Account en dominio
4. Guarda cuenta
5. Retorna AccountResponse

Dependencias:
- AccountRepository
- UserRepository
- MembershipPlanRepository
- SubscriptionRepository

#### GetAccountsUseCase
Responsabilidad: Obtener todas las cuentas de un usuario
Flujo:
1. Busca todas las cuentas del usuario
2. Retorna lista de AccountResponse

Dependencias:
- AccountRepository

#### GetAccountByIdUseCase
Responsabilidad: Obtener una cuenta específica por ID
Flujo:
1. Busca cuenta por ID
2. Valida que pertenezca al usuario
3. Retorna AccountResponse

Dependencias:
- AccountRepository

#### UpdateAccountUseCase
Responsabilidad: Actualizar datos de una cuenta existente
Flujo:
1. Busca cuenta por ID
2. Valida que pertenezca al usuario
3. Actualiza campos proporcionados
4. Guarda cambios
5. Retorna AccountResponse actualizado

Dependencias:
- AccountRepository

#### DeleteAccountUseCase
Responsabilidad: Eliminar una cuenta y sus transacciones asociadas
Flujo:
1. Busca cuenta por ID
2. Valida que pertenezca al usuario
3. Elimina todas las transacciones de la cuenta
4. Elimina la cuenta
5. Retorna sin contenido (204)

Dependencias:
- AccountRepository
- TransactionRepository

---

### Casos de Uso de Transacciones

#### CreateTransactionUseCase
Responsabilidad: Crear una nueva transacción y actualizar saldo de cuenta
Flujo:
1. Valida que cuenta existe y pertenece al usuario
2. Valida límite de transacciones mensuales
3. Si es EXPENSE, valida que saldo sea suficiente
4. Crea transacción en dominio
5. Guarda transacción
6. Actualiza saldo de cuenta (incrementa si INCOME, decrementa si EXPENSE)
7. Retorna TransactionResponse

Dependencias:
- TransactionRepository
- AccountRepository
- ValidateTransactionLimitUseCase

#### GetTransactionsUseCase
Responsabilidad: Obtener transacciones de un usuario, opcionalmente filtradas por fecha
Flujo:
1. Busca todas las transacciones del usuario
2. Si se proporcionan fechas, filtra por rango
3. Ordena por fecha descendente
4. Retorna lista de TransactionResponse

Dependencias:
- TransactionRepository

#### ValidateTransactionLimitUseCase
Responsabilidad: Validar que el usuario no exceda su límite mensual de transacciones
Flujo:
1. Obtiene plan de membresía del usuario
2. Cuenta transacciones del mes actual
3. Compara con límite del plan
4. Lanza excepción si excede límite

Dependencias:
- TransactionRepository
- SubscriptionRepository
- MembershipPlanRepository

---

## Principios de Diseño Aplicados

### Single Responsibility Principle (SRP)
Cada caso de uso tiene una única responsabilidad bien definida.

### Dependency Inversion Principle (DIP)
Los casos de uso dependen de interfaces (repositorios del dominio), no de implementaciones concretas.

### Transaction Management
Los casos de uso están marcados con `@Transactional` para garantizar consistencia de datos.

### Separation of Concerns
- Los DTOs solo contienen datos, sin lógica
- Los casos de uso orquestan, no implementan lógica de negocio
- La lógica de negocio está en el dominio

---

## Flujo de Ejecución Típico

1. Controller recibe HTTP request
2. Controller valida Request DTO con `@Valid`
3. Controller llama a UseCase.execute()
4. UseCase:
   - Valida reglas de aplicación
   - Llama a repositorios del dominio (interfaces)
   - Orquesta operaciones
   - Convierte Domain objects a Response DTOs
5. UseCase retorna Response DTO
6. Controller retorna ResponseEntity con Response DTO

---

## Mapeo entre Capas

La capa de aplicación es responsable del mapeo entre:
- Request DTOs → Domain Objects (para operaciones de creación/actualización)
- Domain Objects → Response DTOs (para operaciones de lectura)

Actualmente se hace manualmente, pero se puede implementar MapStruct para automatizar este proceso.


