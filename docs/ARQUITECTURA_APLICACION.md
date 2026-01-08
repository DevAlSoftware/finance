# Arquitectura de la Capa Application - DevalFinance

## Propósito de la Capa Application

La capa Application (también conocida como capa de Casos de Uso) es el orquestador entre la capa de Presentación y el Dominio. Su responsabilidad principal es:

1. **Orquestar casos de uso:** Coordinar las operaciones entre múltiples repositorios y servicios
2. **Transformar DTOs:** Convertir entre objetos de dominio y DTOs para comunicación externa
3. **Validar reglas de aplicación:** Validaciones que no pertenecen al dominio pero sí a la aplicación
4. **Gestionar transacciones:** Controlar el ámbito transaccional de las operaciones

## Estructura de la Capa Application

```
application/
├── usecase/                    # Casos de uso organizados por dominio
│   ├── user/
│   │   ├── RegisterUserUseCase.java
│   │   ├── AuthenticateUserUseCase.java
│   │   └── GenerateTokenUseCase.java
│   ├── account/
│   │   └── CreateAccountUseCase.java
│   ├── transaction/
│   ├── membership/
│   └── report/
│
└── dto/                        # Data Transfer Objects
    ├── request/               # DTOs de entrada
    │   ├── RegisterUserRequest.java
    │   ├── LoginRequest.java
    │   └── CreateAccountRequest.java
    └── response/              # DTOs de salida
        ├── UserResponse.java
        ├── AuthResponse.java
        └── AccountResponse.java
```

## Principios de la Capa Application

### 1. Un Caso de Uso = Una Responsabilidad

Cada caso de uso tiene una única responsabilidad clara:

```java
@Component
public class RegisterUserUseCase {
    // Solo se encarga de registrar usuarios
    public AuthResponse execute(RegisterUserRequest request) { ... }
}
```

### 2. DTOs para Comunicación Externa

Los casos de uso trabajan con DTOs, no con entidades de dominio directamente en la interfaz:

```java
// ✅ CORRECTO: Usa DTOs
public AuthResponse execute(RegisterUserRequest request)

// ❌ INCORRECTO: Expone entidades de dominio
public User execute(User user)
```

### 3. Transacciones en la Capa Application

Las transacciones se gestionan en los casos de uso:

```java
@Transactional
public AccountResponse execute(CreateAccountRequest request, UUID userId) {
    // Operaciones atómicas
}
```

### 4. Validaciones de Aplicación

Validaciones que no son reglas de negocio pero sí de aplicación:

```java
private void validateAccountLimit(UUID userId) {
    // Valida límites según plan de membresía
    // Esto es lógica de aplicación, no de dominio
}
```

## Flujo Completo: Registro de Usuario

```
1. Controller recibe RegisterUserRequest (DTO)
   ↓
2. Controller llama: registerUserUseCase.execute(request)
   ↓
3. RegisterUserUseCase:
   - Valida request (contraseñas coinciden, etc.)
   - Verifica que email no exista
   - Crea User (domain)
   - Guarda User usando UserRepository (domain port)
   - Crea Subscription con plan FREE
   - Genera tokens usando GenerateTokenUseCase
   ↓
4. Retorna AuthResponse (DTO)
   ↓
5. Controller retorna respuesta HTTP
```

## Casos de Uso Implementados

### 1. RegisterUserUseCase

**Responsabilidad:** Registrar un nuevo usuario en el sistema

**Flujo:**
1. Valida que las contraseñas coincidan
2. Verifica que el email no exista
3. Crea usuario con contraseña hasheada
4. Asigna plan FREE automáticamente
5. Crea suscripción activa
6. Genera tokens JWT
7. Retorna AuthResponse

**Dependencias:**
- UserRepository (domain)
- MembershipPlanRepository (domain)
- SubscriptionRepository (domain)
- PasswordEncoder (infrastructure)
- GenerateTokenUseCase (application)

### 2. AuthenticateUserUseCase

**Responsabilidad:** Autenticar un usuario existente

**Flujo:**
1. Busca usuario por email
2. Valida contraseña
3. Verifica que usuario esté activo
4. Genera tokens JWT
5. Retorna AuthResponse

### 3. GenerateTokenUseCase

**Responsabilidad:** Generar tokens JWT para autenticación

**Flujo:**
1. Genera access token
2. Genera refresh token
3. Mapea User a UserResponse
4. Retorna AuthResponse

**Nota:** Usa JwtTokenProvider de infrastructure para generar tokens

### 4. CreateAccountUseCase

**Responsabilidad:** Crear una nueva cuenta bancaria

**Flujo:**
1. Valida que usuario exista
2. Valida límite de cuentas según plan
3. Crea Account (domain)
4. Guarda Account
5. Retorna AccountResponse

## Separación de Responsabilidades

### Domain vs Application

**Domain (Reglas de Negocio):**
- ¿Qué es un User válido?
- ¿Cuáles son las reglas de negocio?

**Application (Casos de Uso):**
- ¿Cómo registro un usuario?
- ¿Qué pasos debo seguir?
- ¿Qué validaciones de aplicación aplico?

### Ejemplo de Separación

```java
// DOMAIN: Regla de negocio
public class User {
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}

// APPLICATION: Caso de uso que usa la regla
public class AuthenticateUserUseCase {
    private void validateUserIsActive(User user) {
        if (!user.isActive()) {
            throw new IllegalStateException("Cuenta inactiva");
        }
    }
}
```

## DTOs (Data Transfer Objects)

### Request DTOs

DTOs que vienen del exterior (HTTP requests):

```java
public class RegisterUserRequest {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 8)
    private String password;
    // ...
}
```

**Características:**
- Validaciones con Jakarta Validation
- No contienen lógica de negocio
- Representan datos de entrada

### Response DTOs

DTOs que se envían al exterior (HTTP responses):

```java
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    // NO incluye password por seguridad
}
```

**Características:**
- Solo datos necesarios para el cliente
- No exponen información sensible
- Formato optimizado para API

## Ventajas de esta Arquitectura

### 1. Testabilidad

```java
@Test
void testRegisterUser() {
    UserRepository mockRepo = mock(UserRepository.class);
    RegisterUserUseCase useCase = new RegisterUserUseCase(mockRepo, ...);
    
    // Test puro del caso de uso
    AuthResponse response = useCase.execute(request);
    
    // Assertions
}
```

### 2. Reutilización

Los casos de uso pueden ser reutilizados desde diferentes controladores o servicios.

### 3. Mantenibilidad

Cada caso de uso es independiente y fácil de modificar.

### 4. Trazabilidad

Es fácil rastrear qué operaciones se realizan en cada caso de uso.

## Reglas de Oro

### ✅ HACER

1. **Un caso de uso por operación:** Cada operación tiene su propio caso de uso
2. **Usar DTOs:** Siempre trabajar con DTOs en la interfaz pública
3. **Transacciones en casos de uso:** Gestionar transacciones aquí
4. **Validaciones de aplicación:** Validar reglas de aplicación aquí
5. **Orquestar, no implementar:** Los casos de uso orquestan, no implementan

### ❌ NO HACER

1. **NO poner lógica de negocio:** Eso va en el dominio
2. **NO exponer entidades de dominio:** Usar DTOs siempre
3. **NO hacer consultas directas a BD:** Usar repositorios del dominio
4. **NO mezclar responsabilidades:** Un caso de uso, una responsabilidad
5. **NO depender de infraestructura directamente:** Usar abstracciones

## Próximos Casos de Uso a Implementar

### Transacciones
- CreateTransactionUseCase
- GetTransactionsUseCase
- UpdateTransactionUseCase
- DeleteTransactionUseCase

### Cuentas
- GetAccountsUseCase
- UpdateAccountUseCase
- DeleteAccountUseCase

### Membresías
- UpgradeMembershipUseCase
- GetMembershipInfoUseCase

### Reportes
- GenerateMonthlyReportUseCase
- GenerateTaxReportUseCase
- ExportReportUseCase

## Conclusión

La capa Application es el corazón de la orquestación en arquitectura hexagonal. Conecta la presentación con el dominio de forma limpia y mantenible, siguiendo principios SOLID y Clean Code.

