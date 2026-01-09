# Arquitectura del Sistema - DevalFinance

## Arquitectura Hexagonal (Ports & Adapters)

La aplicación sigue una arquitectura hexagonal que separa la lógica de negocio de los detalles de implementación, facilitando el testing, mantenimiento y futura migración a microservicios.

### Principios de la Arquitectura

- Separación de responsabilidades por capas
- Independencia del dominio de frameworks y tecnologías
- Testabilidad mediante interfaces y mocks
- Flexibilidad para cambiar implementaciones sin afectar el núcleo
- Preparación para escalabilidad horizontal (microservicios)

---

## Capas de la Arquitectura

### 1. Domain Layer (Núcleo de Negocio)

Ubicación: `src/main/java/com/devalFinance/domain/`

Responsabilidades:
- Contiene las entidades de negocio y reglas de dominio
- No tiene dependencias externas (frameworks, librerías)
- Define interfaces (Ports) para repositorios y servicios externos
- Lógica de negocio pura sin acoplamiento técnico

Componentes:
- Modelos de dominio (User, Account, Transaction, etc.)
- Interfaces de repositorio (UserRepository, AccountRepository, etc.)
- Enums y tipos de dominio (AccountType, TransactionType, PlanType, etc.)

Ejemplo:
```java
package com.devalFinance.domain.model;

public class User {
    private UUID id;
    private String email;
    private String password;
    // ... sin anotaciones de JPA
}
```

---

### 2. Application Layer (Casos de Uso)

Ubicación: `src/main/java/com/devalFinance/application/`

Responsabilidades:
- Implementa los casos de uso de la aplicación
- Orquesta las llamadas entre dominio e infraestructura
- Define DTOs para comunicación entre capas
- Contiene la lógica de aplicación (no de negocio)

Componentes:
- Use Cases (RegisterUserUseCase, CreateAccountUseCase, etc.)
- DTOs de Request y Response
- Validaciones de aplicación

Estructura:
```
application/
├── dto/
│   ├── request/    (CreateAccountRequest, RegisterUserRequest, etc.)
│   └── response/   (AccountResponse, AuthResponse, etc.)
└── usecase/
    ├── account/    (CreateAccountUseCase, GetAccountsUseCase, etc.)
    ├── transaction/
    └── user/
```

---

### 3. Infrastructure Layer (Adaptadores)

Ubicación: `src/main/java/com/devalFinance/infrastructure/`

Responsabilidades:
- Implementa las interfaces definidas en Domain
- Adaptadores para persistencia (JPA Entities)
- Adaptadores para seguridad (Spring Security, JWT)
- Adaptadores para servicios externos
- Configuraciones técnicas

Componentes:
- Persistence:
  - Entities (UserEntity, AccountEntity, etc.) con anotaciones JPA
  - Repository Adapters (UserRepositoryAdapter, etc.)
  - JPA Repositories (JpaUserRepository, etc.)
  - Mappers (MapStruct para conversión)
- Security:
  - JwtAuthenticationFilter
  - JwtTokenProvider
  - SecurityConfig
- Config:
  - FlywayConfig
  - OpenApiConfig
  - WebConfig

---

### 4. Presentation Layer (Interfaz REST)

Ubicación: `src/main/java/com/devalFinance/presentation/`

Responsabilidades:
- Controladores REST (exponen endpoints HTTP)
- Manejo de excepciones HTTP
- Validaciones de entrada (Jakarta Validation)
- Argument Resolvers para obtener usuario autenticado

Componentes:
- Controllers (AuthController, AccountController, TransactionController)
- Exception Handlers (GlobalExceptionHandler)
- Argument Resolvers (UserArgumentResolver para @CurrentUser)

---

## Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │AuthController│  │Account       │  │Transaction   │      │
│  │              │  │Controller    │  │Controller    │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                  │                  │              │
└─────────┼──────────────────┼──────────────────┼──────────────┘
          
┌─────────┼──────────────────┼──────────────────┼──────────────┐
│         │    APPLICATION LAYER (Use Cases)     │              │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐          │
│  │RegisterUser │  │CreateAccount│  │CreateTrans  │          │
│  │UseCase      │  │UseCase      │  │UseCase      │          │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘          │
│         │                  │                  │              │
└─────────┼──────────────────┼──────────────────┼──────────────┘
          │                  │                  │
┌─────────┼──────────────────┼──────────────────┼──────────────┐
│         │      DOMAIN LAYER (Core Business)    │              │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐          │
│  │User         │  │Account      │  │Transaction  │          │
│  │(Entity)     │  │(Entity)     │  │(Entity)     │          │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘          │
│         │                  │                  │              │
│  ┌──────▼──────────────────▼──────────────────▼──────┐      │
│  │        Repository Interfaces (Ports)              │      │
│  │  UserRepository │ AccountRepository │ ...         │      │
│  └───────────────────────────────────────────────────┘      │
└──────────────────────────────────────────────────────────────┘
          │                  │                  │
┌─────────┼──────────────────┼──────────────────┼──────────────┐
│         │    INFRASTRUCTURE LAYER (Adapters)   │              │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐          │
│  │User         │  │Account      │  │Transaction  │          │
│  │Repository   │  │Repository   │  │Repository   │          │
│  │Adapter      │  │Adapter      │  │Adapter      │          │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘          │
│         │                  │                  │              │
│  ┌──────▼──────────────────▼──────────────────▼──────┐      │
│  │        JPA Repositories & Entities                 │      │
│  │  JpaUserRepository │ JpaAccountRepository │ ...    │      │
│  └───────────────────────────────────────────────────┘      │
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │PostgreSQL    │  │Spring        │  │JWT           │      │
│  │Database      │  │Security      │  │Provider      │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└──────────────────────────────────────────────────────────────┘
```

---

## Flujo de Datos

### Ejemplo: Crear una Cuenta

1. Request HTTP POST `/api/accounts` → AccountController (Presentation)
2. Controller valida entrada con `@Valid`, convierte `CreateAccountRequest` a dominio
3. Controller llama a `CreateAccountUseCase.execute()` (Application)
4. Use Case orquesta la lógica:
   - Valida que el usuario existe
   - Valida límites de membresía
   - Crea objeto Account (Domain)
   - Llama a `accountRepository.save()` (Domain Interface - Port)
5. Repository Adapter (Infrastructure) implementa la persistencia:
   - Convierte Account (Domain) a AccountEntity (JPA)
   - Llama a JpaAccountRepository.save()
   - Guarda en PostgreSQL
6. Flujo inverso:
   - AccountEntity → Account (Domain)
   - Account → AccountResponse (DTO)
   - Retorna ResponseEntity<AccountResponse>

---

## Ventajas de esta Arquitectura

1. Testabilidad: El dominio puede probarse sin necesidad de base de datos
2. Mantenibilidad: Cambios en infraestructura no afectan el dominio
3. Escalabilidad: Preparado para migrar a microservicios
4. Flexibilidad: Fácil cambiar de PostgreSQL a otro SGBD
5. Separación de responsabilidades: Cada capa tiene un propósito claro
6. Clean Code: Código organizado y fácil de entender

---

## Tecnologías y Patrones Utilizados

- Arquitectura Hexagonal (Ports & Adapters)
- Repository Pattern
- Adapter Pattern
- Dependency Inversion Principle
- Spring Boot para configuración y dependencias
- JPA/Hibernate para persistencia
- MapStruct para mapeo entre capas
- Flyway para migraciones de base de datos
- Spring Security + JWT para autenticación


