# Configuración de PostgreSQL - DevalFinance

## Resumen

La aplicación utiliza **PostgreSQL** como base de datos y **Flyway** para gestionar las migraciones de esquema. Solo necesitas crear la base de datos vacía; Flyway creará automáticamente todas las tablas cuando inicies la aplicación.

**📖 Configuración Multi-Entorno:** Ver [docs/CONFIGURACION_ENTORNOS.md](CONFIGURACION_ENTORNOS.md) para detalles completos sobre DEV, TEST y PROD.

## Entornos Configurados

- **DEV (Local)**: localhost:5441 - Base de datos: `devalfinance_dev`
- **TEST/QA (Servidor)**: 3.82.60.54:5437 - Base de datos: `devalfinance_test`
- **PROD (Servidor)**: 3.82.60.54:5438 - Base de datos: `devalfinance_main`

---

## Configuración Rápida por Entorno

### DEV (Desarrollo Local) - Puerto 5441

```bash
# 1. Crear base de datos
psql -U postgres -h localhost -p 5441 -f scripts/create_database-dev.sql

# 2. Iniciar aplicación (usa perfil dev por defecto)
mvn clean spring-boot:run
```

### TEST/QA - Puerto 5437 (Servidor)

```bash
# 1. Crear base de datos
psql -U postgres -h 3.82.60.54 -p 5437 -f scripts/create_database-test.sql

# 2. Iniciar aplicación con perfil test
mvn clean spring-boot:run -Dspring-boot.run.profiles=test
```

### PROD - Puerto 5438 (Servidor)

```bash
# 1. Crear base de datos
psql -U postgres -h 3.82.60.54 -p 5438 -f scripts/create_database-prod.sql

# 2. Iniciar aplicación con perfil prod
mvn clean spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Paso 1: Instalar PostgreSQL

### Windows

1. **Descargar PostgreSQL:**
   - Ir a: https://www.postgresql.org/download/windows/
   - Descargar el instalador oficial
   - O usar el instalador interactivo: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

2. **Instalación:**
   - Ejecutar el instalador
   - **Puerto:** Dejar el predeterminado (5432) ✅
   - **Superusuario (postgres):** Configurar una contraseña (guárdala, la necesitarás)
   - **Componentes:** Instalar PostgreSQL Server, pgAdmin 4 (opcional, herramienta gráfica)

3. **Verificar instalación:**
   ```bash
   psql --version
   ```

### Alternativa: Docker (Recomendado para desarrollo)

Si prefieres usar Docker para DEV (puerto 5441):

```bash
# Crear y ejecutar contenedor PostgreSQL para DEV
docker run --name devalfinance-postgres-dev \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=1234 \
  -e POSTGRES_DB=postgres \
  -p 5441:5432 \
  -d postgres:16

# Verificar que está corriendo
docker ps

# Crear la base de datos dentro del contenedor
docker exec -it devalfinance-postgres-dev psql -U postgres -c "CREATE DATABASE devalfinance_dev;"
```

---

## Paso 2: Crear la Base de Datos

Tienes **3 opciones** para crear la base de datos:

### Opción A: Usando Script SQL (Recomendado)

```bash
# Para DEV (localhost:5441)
psql -U postgres -h localhost -p 5441 -f scripts/create_database-dev.sql

# Para TEST (servidor:5437)
psql -U postgres -h 3.82.60.54 -p 5437 -f scripts/create_database-test.sql

# Para PROD (servidor:5438)
psql -U postgres -h 3.82.60.54 -p 5438 -f scripts/create_database-prod.sql
```

### Opción B: Usando psql (Línea de comandos)

```bash
# Para DEV
psql -U postgres -h localhost -p 5441

# Crear la base de datos
CREATE DATABASE devalfinance_dev;

# Verificar que se creó
\l

# Salir
\q
```

### Opción B: Usando pgAdmin 4 (Interfaz gráfica)

1. Abrir **pgAdmin 4**
2. Conectar al servidor PostgreSQL (usar la contraseña del superusuario)
3. Clic derecho en **Databases** → **Create** → **Database**
4. Nombre: `devalfinance`
5. Owner: `postgres`
6. Clic en **Save**

### Opción C: Desde terminal de Windows

```powershell
# Para DEV
$env:PGPASSWORD='1234'
psql -U postgres -h localhost -p 5441 -c "CREATE DATABASE devalfinance_dev;"

# Para TEST (desde máquina con acceso)
psql -U postgres -h 3.82.60.54 -p 5437 -c "CREATE DATABASE devalfinance_test;"

# Para PROD (desde máquina con acceso)
psql -U postgres -h 3.82.60.54 -p 5438 -c "CREATE DATABASE devalfinance_main;"
```

---

## Paso 3: Configurar Credenciales en la Aplicación

La configuración está organizada por **perfiles** de Spring Boot:

- `application-dev.properties` - Configuración DEV (localhost:5441)
- `application-test.properties` - Configuración TEST (servidor:5437)
- `application-prod.properties` - Configuración PROD (servidor:5438)

**Ya están configurados con:**
- Host y puerto correctos por entorno
- Base de datos correcta por entorno
- Usuario: `postgres`
- Password: `1234`

**Para cambiar el perfil activo:**

Edita `src/main/resources/application.properties`:
```properties
spring.profiles.active=dev  # o test, o prod
```

O inicia la aplicación con:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**⚠️ IMPORTANTE:**
- Las credenciales están en los archivos de perfil correspondientes
- Para cambiar password, edita el archivo `application-{profile}.properties`
- **En PROD, cambia el password y JWT secret**

---

## Paso 4: Verificar que Todo Funciona

### 4.1 Probar Conexión Manualmente

```bash
# Para DEV
psql -U postgres -h localhost -p 5441 -d devalfinance_dev

# Para TEST
psql -U postgres -h 3.82.60.54 -p 5437 -d devalfinance_test

# Para PROD
psql -U postgres -h 3.82.60.54 -p 5438 -d devalfinance_main

# Verificar que está vacía (no debería haber tablas)
\dt

# Salir
\q
```

### 4.2 Iniciar la Aplicación

```bash
# Desde la raíz del proyecto
# Por defecto usa perfil 'dev'
mvn clean spring-boot:run

# O explícitamente:
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev    # DEV
mvn clean spring-boot:run -Dspring-boot.run.profiles=test   # TEST
mvn clean spring-boot:run -Dspring-boot.run.profiles=prod   # PROD
```

**Flyway ejecutará automáticamente:**
- ✅ Creará todas las tablas definidas en `V1__Create_initial_schema.sql`
- ✅ Insertará los datos iniciales de `V2__Insert_default_membership_plans.sql`
- ✅ Registrará las migraciones en la tabla `flyway_schema_history`

### 4.3 Verificar Tablas Creadas

```bash
# Para DEV
psql -U postgres -h localhost -p 5441 -d devalfinance_dev

# Para TEST
psql -U postgres -h 3.82.60.54 -p 5437 -d devalfinance_test

# Para PROD
psql -U postgres -h 3.82.60.54 -p 5438 -d devalfinance_main

# Ver todas las tablas
\dt

# Deberías ver:
# - flyway_schema_history (tabla de control de Flyway)
# - membership_plans
# - users
# - subscriptions
# - accounts
# - transactions
# - categories
# - transaction_limits

# Ver datos iniciales de planes de membresía
SELECT * FROM membership_plans;

# Salir
\q
```

---

## Estructura de la Base de Datos

### Tablas Principales

1. **membership_plans** - Planes de membresía (FREE, PREMIUM, BUSINESS)
2. **users** - Usuarios del sistema
3. **subscriptions** - Suscripciones activas de usuarios
4. **accounts** - Cuentas bancarias/financieras de usuarios
5. **transactions** - Transacciones (ingresos/gastos)
6. **categories** - Categorías de transacciones
7. **transaction_limits** - Límites de transacciones por plan

### Diagrama de Relaciones

```
membership_plans (1) ──→ (N) users
users (1) ──→ (N) subscriptions
users (1) ──→ (N) accounts
users (1) ──→ (N) transactions
transactions (N) ──→ (1) accounts
transactions (N) ──→ (1) categories
membership_plans (1) ──→ (N) transaction_limits
```

---

## Solución de Problemas Comunes

### Error: "FATAL: la autenticación password falló para el usuario postgres"

**Causa:** La contraseña en `application.properties` no coincide con la de PostgreSQL.

**Solución:**
1. Verificar la contraseña del usuario `postgres`
2. Actualizar `spring.datasource.password` en `application.properties`
3. Reiniciar la aplicación

### Error: "database 'devalfinance' does not exist"

**Causa:** La base de datos no se creó correctamente.

**Solución:**
```bash
# Crear la base de datos manualmente
psql -U postgres -c "CREATE DATABASE devalfinance;"
```

### Error: "permission denied for database devalfinance"

**Causa:** El usuario no tiene permisos sobre la base de datos.

**Solución:**
```bash
# Conectar como superusuario y otorgar permisos
psql -U postgres
GRANT ALL PRIVILEGES ON DATABASE devalfinance TO postgres;
\q
```

### Error: "Connection refused" o "Could not connect"

**Causa:** PostgreSQL no está corriendo.

**Solución (Windows):**
```powershell
# Verificar servicio
Get-Service postgresql*

# Iniciar servicio (reemplazar x-x con tu versión)
Start-Service postgresql-x-x-*

# O desde Services (services.msc)
# Buscar "postgresql" y iniciar el servicio
```

**Solución (Docker):**
```bash
# Verificar que el contenedor está corriendo
docker ps

# Si no está, iniciarlo
docker start devalfinance-postgres
```

### Flyway no crea las tablas

**Verificar:**
1. `spring.flyway.enabled=true` en `application.properties` ✅
2. Las migraciones están en `src/main/resources/db/migration/` ✅
3. Los archivos siguen el formato: `V{version}__{descripción}.sql` ✅
4. Revisar logs de la aplicación para errores de Flyway

**Revisar historial de Flyway:**
```bash
psql -U postgres -d devalfinance
SELECT * FROM flyway_schema_history;
```

---

## Comandos Útiles de PostgreSQL

### Conexión
```bash
# Conectar como usuario postgres
psql -U postgres

# Conectar a base de datos específica
psql -U postgres -d devalfinance

# Conectar con host y puerto específicos
psql -h localhost -p 5432 -U postgres -d devalfinance
```

### Gestión de Base de Datos
```sql
-- Listar todas las bases de datos
\l

-- Crear base de datos
CREATE DATABASE nombre_db;

-- Eliminar base de datos (¡CUIDADO!)
DROP DATABASE nombre_db;

-- Conectar a una base de datos
\c nombre_db
```

### Gestión de Tablas
```sql
-- Listar tablas
\dt

-- Describir estructura de una tabla
\d nombre_tabla

-- Ver datos de una tabla
SELECT * FROM nombre_tabla LIMIT 10;

-- Contar registros
SELECT COUNT(*) FROM nombre_tabla;
```

### Usuarios y Permisos
```sql
-- Crear usuario
CREATE USER mi_usuario WITH PASSWORD 'mi_password';

-- Otorgar privilegios
GRANT ALL PRIVILEGES ON DATABASE devalfinance TO mi_usuario;

-- Listar usuarios
\du
```

### Salir
```sql
\q
```

---

## Resumen Rápido

```bash
# 1. Instalar PostgreSQL (si no lo tienes)

# 2. Crear base de datos
psql -U postgres -c "CREATE DATABASE devalfinance;"

# 3. Verificar credenciales en application.properties
#    - Usuario: postgres (o el que configuraste)
#    - Contraseña: tu_password
#    - Base de datos: devalfinance

# 4. Iniciar aplicación
mvn clean spring-boot:run

# 5. Flyway creará automáticamente todas las tablas
#    ✅ Listo para usar
```

---

## Próximos Pasos

Una vez que la base de datos esté configurada:

1. ✅ Verificar que la aplicación inicia correctamente
2. ✅ Probar endpoints de autenticación (registro/login)
3. ✅ Verificar que las tablas se crearon con Flyway
4. ✅ Continuar con el desarrollo de funcionalidades

---

## Referencias

- [Documentación oficial de PostgreSQL](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Database Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html)

