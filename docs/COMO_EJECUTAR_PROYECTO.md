# Cómo Ejecutar el Proyecto Completo

Esta guía explica cómo ejecutar tanto el backend (Spring Boot) como el frontend (React) juntos para probar toda la aplicación.

## Requisitos Previos

- Java 17 o superior instalado
- Node.js 18 o superior instalado
- PostgreSQL instalado y corriendo
- Base de datos `devalfinance` creada

## Paso 1: Configurar la Base de Datos

1. Asegúrate de que PostgreSQL esté corriendo
2. Crea la base de datos (si no existe):
```sql
CREATE DATABASE devalfinance;
```

3. Verifica que las credenciales en `src/main/resources/application.properties` sean correctas:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/devalfinance
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

## Paso 2: Ejecutar el Backend (Spring Boot)

### Opción A: Desde IDE (IntelliJ IDEA, Eclipse, VS Code)
1. Abre el proyecto en tu IDE
2. Localiza `DevalFinanceApplication.java`
3. Ejecuta la clase principal (Run/Debug)
4. Verifica que inicie correctamente en `http://localhost:8888`

### Opción B: Desde Terminal con Maven
```bash
# En la raíz del proyecto (C:\devalFinance)
mvn spring-boot:run
```

### Verificar que el Backend está corriendo:
- Abre tu navegador en: `http://localhost:8888/api/swagger-ui/index.html`
- Deberías ver la interfaz de Swagger con todos los endpoints documentados

## Paso 3: Ejecutar el Frontend (React)

1. Abre una **nueva terminal** (deja el backend corriendo)

2. Navega a la carpeta del frontend:
```bash
cd frontend
```

3. Instala las dependencias (solo la primera vez):
```bash
npm install
```

4. Crea el archivo `.env` con la URL del backend:
```bash
# En Windows PowerShell:
New-Item -ItemType File -Path .env -Force
# Luego edita el archivo y agrega:
# VITE_API_BASE_URL=http://localhost:8888/api
```

O crea el archivo manualmente en `frontend/.env`:
```
VITE_API_BASE_URL=http://localhost:8888/api
```

5. Inicia el servidor de desarrollo:
```bash
npm run dev
```

6. El frontend debería iniciar en: `http://localhost:5173` (o el puerto que indique Vite)

## Paso 4: Probar la Aplicación

### 1. Crear una cuenta de usuario
- Abre `http://localhost:5173` en tu navegador
- Deberías ver la página de Login
- Haz clic en "Regístrate aquí"
- Completa el formulario de registro:
  - Nombre completo
  - Email
  - Contraseña (mínimo 8 caracteres)
- Al registrarte, deberías ser redirigido automáticamente al Dashboard

### 2. Probar el Dashboard
- Una vez autenticado, verás el Dashboard con:
  - Saldo Total (inicialmente 0)
  - Ingresos del Mes
  - Gastos del Mes
  - Balance Neto
  - Lista de cuentas (vacía inicialmente)
  - Transacciones recientes (vacías inicialmente)

### 3. Probar la Navegación
- Usa el menú superior para navegar entre:
  - Dashboard
  - Cuentas
  - Transacciones

## Estructura de Puertos

- **Backend**: `http://localhost:8888`
  - API REST: `http://localhost:8888/api`
  - Swagger UI: `http://localhost:8888/api/swagger-ui/index.html`

- **Frontend**: `http://localhost:5173` (puerto por defecto de Vite)
  - Puede variar si 5173 está ocupado

## Solución de Problemas Comunes

### Error: "Cannot connect to API"
- Verifica que el backend esté corriendo
- Verifica que el archivo `.env` del frontend tenga la URL correcta
- Verifica que no haya errores en la consola del backend

### Error: "Port 5173 already in use"
- Vite usará automáticamente otro puerto
- Revisa el mensaje en la terminal para ver el nuevo puerto

### Error: "CORS error"
- El backend ya está configurado para aceptar CORS desde `http://localhost:5173`
- Si usas otro puerto, verifica la configuración en `WebConfig.java`

### Error: "Database connection failed"
- Verifica que PostgreSQL esté corriendo
- Verifica las credenciales en `application.properties`
- Verifica que la base de datos exista

## Comandos Rápidos

```bash
# Terminal 1 - Backend
cd C:\devalFinance
mvn spring-boot:run

# Terminal 2 - Frontend
cd C:\devalFinance\frontend
npm run dev
```

## Verificar que Todo Funciona

1. ✅ Backend responde en `http://localhost:8888/api/swagger-ui`
2. ✅ Frontend se abre en `http://localhost:5173`
3. ✅ Puedes registrarte y hacer login
4. ✅ El Dashboard carga (aunque esté vacío inicialmente)

## Próximos Pasos

Una vez que verifiques que todo funciona:
- Podemos implementar las páginas de Cuentas
- Podemos implementar las páginas de Transacciones
- Podemos agregar más funcionalidades al Dashboard

