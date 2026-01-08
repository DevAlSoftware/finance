# Arquitectura de Base de Datos - DevalFinance

## Principios de Arquitectura Hexagonal Aplicados

### Separación de Responsabilidades

La arquitectura hexagonal garantiza que el dominio (core business) esté completamente desacoplado de la infraestructura de persistencia:

```
┌─────────────────────────────────────────────────────────┐
│                    DOMAIN LAYER                          │
│  ┌──────────────────────────────────────────────────┐  │
│  │  User (POJO puro)                                │  │
│  │  - Sin anotaciones JPA                           │  │
│  │  - Sin dependencias de Spring                    │  │
│  │  - Solo lógica de negocio                       │  │
│  └──────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────┐  │
│  │  UserRepository (Interface - Port)               │  │
│  │  - Define contrato de persistencia               │  │
│  │  - No conoce implementación                      │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                        │
                        │ Implementa
                        ▼
┌─────────────────────────────────────────────────────────┐
│              INFRASTRUCTURE LAYER                       │
│  ┌──────────────────────────────────────────────────┐  │
│  │  UserEntity (JPA Entity)                         │  │
│  │  - @Entity, @Table, @Column                     │  │
│  │  - Mapeo a tabla de BD                           │  │
│  └──────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────┐  │
│  │  JpaUserRepository (Spring Data JPA)             │  │
│  │  - Extiende JpaRepository                        │  │
│  │  - Consultas automáticas                        │  │
│  └──────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────┐  │
│  │  UserRepositoryAdapter (Adapter)                 │  │
│  │  - Implementa UserRepository (Port)               │  │
│  │  - Convierte Entity <-> Domain                   │  │
│  │  - Usa UserMapper para transformaciones          │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                        │
                        │ Usa
                        ▼
┌─────────────────────────────────────────────────────────┐
│              DATABASE LAYER                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │  PostgreSQL Database                             │  │
│  │  - Tablas creadas con Flyway (SQL puro)          │  │
│  │  - Migraciones versionadas                       │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Gestión de Base de Datos con Flyway

### ¿Por qué Flyway?

1. **Versionado de Esquema:** Cada cambio de BD está versionado
2. **SQL Puro:** Escribimos SQL nativo, no dependemos de JPA para crear tablas
3. **Reproducible:** Cualquier entorno puede recrear la BD exactamente igual
4. **Rollback Controlado:** Podemos gestionar migraciones hacia atrás si es necesario

### Estructura de Migraciones

```
src/main/resources/db/migration/
├── V1__Create_initial_schema.sql      # Schema inicial completo
├── V2__Insert_default_membership_plans.sql  # Datos iniciales
├── V3__Add_indexes.sql                # Futuras optimizaciones
└── ...
```

### Convenciones de Nomenclatura

- `V{version}__{descripción}.sql`
- Versiones secuenciales: V1, V2, V3...
- Descripción clara en snake_case
- Ejemplo: `V5__Add_user_preferences_table.sql`

## Patrones de Diseño Aplicados

### 1. Repository Pattern

**En el Dominio (Port):**
```java
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
```

**En la Infraestructura (Adapter):**
```java
@Component
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    
    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }
}
```

### 2. Adapter Pattern

El adapter convierte entre dos interfaces incompatibles:
- **Domain Model** (User) ↔ **JPA Entity** (UserEntity)
- El dominio no conoce JPA
- La infraestructura adapta JPA al dominio

### 3. Mapper Pattern

Separamos la lógica de mapeo en clases dedicadas:

```java
@Component
public class UserMapper {
    public UserEntity toEntity(User user) { ... }
    public User toDomain(UserEntity entity) { ... }
}
```

**Ventajas:**
- Código más limpio y testeable
- Reutilizable en diferentes contextos
- Fácil de mantener y modificar

### 4. Dependency Inversion Principle (SOLID)

```
Domain define: UserRepository (interface)
     ↑
     │ implements
     │
Infrastructure: UserRepositoryAdapter
```

El dominio define el contrato, la infraestructura lo implementa.

## Flujo Completo de Persistencia

### Crear un Usuario

```
1. Use Case recibe User (domain)
   ↓
2. Use Case llama: userRepository.save(user)
   ↓
3. UserRepositoryAdapter (implementa UserRepository)
   ↓
4. UserMapper.toEntity(user) → UserEntity
   ↓
5. JpaUserRepository.save(entity) → UserEntity guardado
   ↓
6. UserMapper.toDomain(savedEntity) → User (domain)
   ↓
7. Retorna User al Use Case
```

### Consultar un Usuario

```
1. Use Case llama: userRepository.findByEmail(email)
   ↓
2. UserRepositoryAdapter
   ↓
3. JpaUserRepository.findByEmail(email) → Optional<UserEntity>
   ↓
4. Si existe: UserMapper.toDomain(entity) → User
   ↓
5. Retorna Optional<User> al Use Case
```

## Ventajas de esta Arquitectura

### 1. Testabilidad

```java
// Test del dominio sin BD
@Test
void testUserBusinessLogic() {
    UserRepository mockRepo = mock(UserRepository.class);
    UserService service = new UserService(mockRepo);
    // Test puro de lógica de negocio
}
```

### 2. Flexibilidad

- Podemos cambiar de JPA a JDBC sin tocar el dominio
- Podemos cambiar de PostgreSQL a MySQL fácilmente
- Podemos agregar cache sin afectar el dominio

### 3. Mantenibilidad

- Código organizado por responsabilidades
- Fácil de entender y modificar
- Separación clara de concerns

### 4. Escalabilidad

- Preparado para migrar a microservicios
- Cada módulo puede tener su propia BD
- Fácil de distribuir

## Reglas de Oro

### ✅ HACER

1. **Domain solo tiene POJOs puros** - Sin anotaciones JPA
2. **Domain define interfaces** - Los repositorios son ports
3. **Infrastructure implementa** - Los adapters implementan los ports
4. **SQL en Flyway** - Las tablas se crean con SQL puro
5. **Mappers separados** - Lógica de transformación en clases dedicadas

### ❌ NO HACER

1. **NO poner @Entity en domain** - Solo en infrastructure
2. **NO importar JPA en domain** - El dominio no conoce JPA
3. **NO usar JpaRepository directamente** - Siempre a través de adapter
4. **NO crear tablas con JPA ddl-auto** - Usar Flyway siempre
5. **NO mezclar lógica de negocio con persistencia** - Separar siempre

## Ejemplo Completo: Crear Usuario

### 1. Domain Model (User.java)
```java
// Sin anotaciones, POJO puro
public class User {
    private UUID id;
    private String email;
    // ... métodos de negocio
}
```

### 2. Domain Repository (Port)
```java
public interface UserRepository {
    User save(User user);
}
```

### 3. JPA Entity (Infrastructure)
```java
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // ...
}
```

### 4. JPA Repository (Infrastructure)
```java
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    // Consultas específicas
}
```

### 5. Adapter (Infrastructure)
```java
@Component
public class UserRepositoryAdapter implements UserRepository {
    // Implementa el port usando JPA
}
```

### 6. Flyway Migration (SQL)
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    -- ...
);
```

## Conclusión

Esta arquitectura garantiza:
- ✅ Código limpio y mantenible
- ✅ Separación clara de responsabilidades
- ✅ Testabilidad completa
- ✅ Flexibilidad para cambios futuros
- ✅ Base de datos gestionada con SQL puro (Flyway)
- ✅ Patrones de diseño bien aplicados

El dominio es el núcleo, la infraestructura es intercambiable.

