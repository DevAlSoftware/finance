# Configuración de Base de Datos Local - Paso a Paso

## Requisitos

- PostgreSQL instalado y corriendo
- Acceso como usuario postgres (superusuario)

## Paso 1: Conectar a PostgreSQL

Abre una terminal (PowerShell o CMD) y conectate a PostgreSQL:

```bash
psql -U postgres
```

Si te pide contraseña, ingresa la contraseña del usuario postgres que configuraste al instalar PostgreSQL.

## Paso 2: Verificar puerto de PostgreSQL

Primero, verificamos en qué puerto está corriendo PostgreSQL. Dentro de psql, ejecuta:

```sql
SHOW port;
```

Si te muestra `5432` o cualquier otro puerto, necesitas configurar PostgreSQL para que escuche en el puerto 5441.

## Paso 3: Configurar PostgreSQL en puerto 5441

### Opción A: Si tienes acceso a postgresql.conf

1. Encuentra el archivo `postgresql.conf` (normalmente en la carpeta de datos de PostgreSQL)
2. Busca la línea que dice `#port = 5432`
3. Cámbiala por: `port = 5441`
4. Guarda el archivo
5. Reinicia el servicio de PostgreSQL

### Opción B: Si no puedes cambiar el puerto principal

Si PostgreSQL ya está usando el puerto 5432 para otros proyectos, puedes crear la BD en ese puerto y luego cambiar solo la configuración de la aplicación. En ese caso:

1. Usa el puerto actual de PostgreSQL (probablemente 5432)
2. Crea la base de datos normalmente
3. Actualiza `application.properties` con el puerto correcto

## Paso 4: Crear la base de datos

Dentro de psql (conectado como postgres), ejecuta:

```sql
CREATE DATABASE devalfinance;
```

Para verificar que se creó correctamente:

```sql
\l
```

Deberías ver `devalfinance` en la lista de bases de datos.

Para salir de psql:

```sql
\q
```

## Paso 5: Verificar configuración en application.properties

Abre el archivo `src/main/resources/application.properties` y verifica:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5441/devalfinance
spring.datasource.username=postgres
spring.datasource.password=1234
```

Ajusta estos valores según tu configuración:
- Si tu PostgreSQL está en otro puerto (no 5441), cambia el puerto en la URL
- Si tu contraseña no es 1234, cambia `spring.datasource.password`

## Paso 6: Probar la conexión

Inicia la aplicación:

```bash
mvn clean spring-boot:run
```

Si todo está bien configurado, deberías ver en los logs que:
- Flyway ejecuta las migraciones
- Se crean las tablas automáticamente
- La aplicación inicia correctamente

## Paso 7: Verificar que las tablas se crearon

Conectate nuevamente a PostgreSQL:

```bash
psql -U postgres -d devalfinance
```

Lista las tablas:

```sql
\dt
```

Deberías ver estas tablas:
- flyway_schema_history
- membership_plans
- users
- subscriptions
- accounts
- transactions
- categories
- transaction_limits

Ver datos iniciales:

```sql
SELECT * FROM membership_plans;
```

Para salir:

```sql
\q
```

## Problemas Comunes

### Error: "FATAL: la autenticación password falló"

La contraseña en `application.properties` no coincide con la de PostgreSQL.

**Solución:** Actualiza `spring.datasource.password` en `application.properties` con tu contraseña correcta.

### Error: "Connection refused" o "Could not connect"

PostgreSQL no está corriendo o está en otro puerto.

**Solución:** 
1. Verifica que el servicio de PostgreSQL esté corriendo
2. Verifica el puerto en `application.properties`
3. Prueba conectarte manualmente con: `psql -U postgres -h localhost -p 5441`

### Error: "database 'devalfinance' does not exist"

La base de datos no se creó correctamente.

**Solución:** 
1. Conectate a PostgreSQL: `psql -U postgres`
2. Crea la base de datos: `CREATE DATABASE devalfinance;`
3. Verifica: `\l`

### Error: "port 5441 is already in use"

Otro proceso está usando el puerto 5441.

**Solución:**
1. Usa otro puerto para PostgreSQL (por ejemplo 5442)
2. O cambia la configuración en `application.properties` para usar el puerto que PostgreSQL ya tiene configurado

