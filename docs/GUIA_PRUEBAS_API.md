# Guía de Pruebas API - DevalFinance

## ¿Qué Hace el Backend Actual?

Según la propuesta de negocio, DevalFinance es un **sistema de contabilidad personal** que permite a los usuarios gestionar sus finanzas, llevar control de ingresos y gastos, y preparar su declaración de renta.

### Funcionalidades Implementadas Actualmente

#### 1. Gestión de Usuarios y Autenticación
- **RF01.1** ✅ Registro de nuevos usuarios con email y contraseña
- **RF01.2** ✅ Autenticación de usuarios existentes con JWT
- **RF01.5** ✅ Asignación automática de plan GRATUITO a usuarios nuevos

#### 2. Gestión de Membresías
- **RF02.1** ✅ Sistema gestiona 3 planes: FREE, PREMIUM, BUSINESS (datos iniciales en BD)
- **RF02.3** ✅ Validación de límites según plan del usuario
- **RF02.4** ✅ Validación de permisos antes de permitir acciones

#### 3. Gestión de Cuentas Bancarias
- **RF03.1** ✅ Crear cuentas bancarias (Tipos: SAVINGS, CHECKING, CASH)
- **RF03.2** ✅ Cálculo automático de saldo por cuenta
- **RF03.3** ✅ Editar y eliminar cuentas
- **RF03.4** ✅ Limitación de número de cuentas según plan (Gratuito: 1 cuenta máximo)
- **RF03.5** ✅ Consultar resumen de todas las cuentas del usuario

#### 4. Gestión de Transacciones
- **RF04.1** ✅ Registrar transacciones (INCOME - Ingresos, EXPENSE - Gastos)
- **RF04.4** ✅ Asociar transacciones a cuentas específicas
- **RF04.5** ✅ Filtrar transacciones por período (fechas inicio y fin)
- **RF04.6** ✅ Validación de límite de transacciones mensuales (Gratuito: 50 máximo)

### Funcionalidades Pendientes
- RF04.2: Categorización con códigos DIAN (estructura lista, falta llenar datos)
- RF04.3: Editar y eliminar transacciones
- RF05: Categorización automática (Premium/Business)
- RF06: Reportes y análisis
- RF07: Historial completo (Premium/Business)
- RF08: Dashboard con visualizaciones
- RF09: Notificaciones y alertas

---

## Endpoints Disponibles

### Base URL
```
http://localhost:8888/api
```

---

## 1. Autenticación (Sin Token)

### POST /api/auth/register - Registrar Usuario

**Descripción:** Crea un nuevo usuario y asigna automáticamente plan GRATUITO.

**Request Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "Password123",
  "confirmPassword": "Password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "usuario@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "membershipPlan": "FREE"
  }
}
```

**Validaciones:**
- Email debe ser válido
- Password mínimo 8 caracteres
- Password y confirmPassword deben coincidir

---

### POST /api/auth/login - Iniciar Sesión

**Descripción:** Autentica un usuario existente.

**Request Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "Password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "usuario@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "membershipPlan": "FREE"
  }
}
```

---

## 2. Cuentas (Requiere Token JWT)

**Headers requeridos:**
```
Authorization: Bearer {token}
```

### POST /api/accounts - Crear Cuenta

**Descripción:** Crea una nueva cuenta bancaria. Usuarios con plan GRATUITO solo pueden tener 1 cuenta.

**Request Body:**
```json
{
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "initialBalance": 1000000.00,
  "currency": "COP"
}
```

**Tipos de cuenta válidos:**
- `SAVINGS` - Cuenta de Ahorros
- `CHECKING` - Cuenta Corriente
- `CASH` - Efectivo

**Response (201 Created):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "balance": 1000000.00,
  "currency": "COP",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-01-08T10:00:00",
  "updatedAt": "2024-01-08T10:00:00"
}
```

**Errores posibles:**
- `400 Bad Request`: Si usuario GRATUITO intenta crear más de 1 cuenta
- `401 Unauthorized`: Si el token no es válido o está expirado

---

### GET /api/accounts - Listar Cuentas

**Descripción:** Obtiene todas las cuentas del usuario autenticado.

**Response (200 OK):**
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Cuenta de Ahorros Principal",
    "accountType": "SAVINGS",
    "balance": 1000000.00,
    "currency": "COP",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "createdAt": "2024-01-08T10:00:00",
    "updatedAt": "2024-01-08T10:00:00"
  }
]
```

---

### GET /api/accounts/{accountId} - Obtener Cuenta por ID

**Descripción:** Obtiene los detalles de una cuenta específica.

**Response (200 OK):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "balance": 1000000.00,
  "currency": "COP",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-01-08T10:00:00",
  "updatedAt": "2024-01-08T10:00:00"
}
```

---

### PUT /api/accounts/{accountId} - Actualizar Cuenta

**Descripción:** Actualiza la información de una cuenta existente.

**Request Body:**
```json
{
  "name": "Cuenta de Ahorros Actualizada",
  "accountType": "SAVINGS"
}
```

**Response (200 OK):** Mismo formato que GET /api/accounts/{accountId}

---

### DELETE /api/accounts/{accountId} - Eliminar Cuenta

**Descripción:** Elimina una cuenta del usuario.

**Response (204 No Content):** Sin cuerpo de respuesta

---

## 3. Transacciones (Requiere Token JWT)

### POST /api/transactions - Crear Transacción

**Descripción:** Registra una nueva transacción (ingreso o gasto). Valida límite de 50 transacciones mensuales para plan GRATUITO.

**Request Body:**
```json
{
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "amount": 50000.00,
  "transactionType": "EXPENSE",
  "description": "Compra en supermercado",
  "transactionDate": "2024-01-08",
  "categoryId": null,
  "tags": ["supermercado", "compras"]
}
```

**Tipos de transacción válidos:**
- `INCOME` - Ingreso
- `EXPENSE` - Gasto

**Response (201 Created):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "amount": 50000.00,
  "transactionType": "EXPENSE",
  "description": "Compra en supermercado",
  "transactionDate": "2024-01-08",
  "categoryId": null,
  "tags": ["supermercado", "compras"],
  "createdAt": "2024-01-08T10:00:00"
}
```

**Nota:** El saldo de la cuenta se actualiza automáticamente:
- Si es `INCOME`: se suma al saldo
- Si es `EXPENSE`: se resta del saldo

**Errores posibles:**
- `400 Bad Request`: Si usuario GRATUITO excede 50 transacciones mensuales
- `404 Not Found`: Si la cuenta no existe o no pertenece al usuario

---

### GET /api/transactions - Listar Transacciones

**Descripción:** Obtiene las transacciones del usuario. Opcionalmente filtra por rango de fechas.

**Query Parameters (opcionales):**
- `startDate`: Fecha de inicio (formato: YYYY-MM-DD)
- `endDate`: Fecha de fin (formato: YYYY-MM-DD)

**Ejemplos:**

Listar todas las transacciones:
```
GET /api/transactions
```

Filtrar por rango de fechas:
```
GET /api/transactions?startDate=2024-01-01&endDate=2024-01-31
```

**Response (200 OK):**
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "accountId": "660e8400-e29b-41d4-a716-446655440001",
    "amount": 50000.00,
    "transactionType": "EXPENSE",
    "description": "Compra en supermercado",
    "transactionDate": "2024-01-08",
    "categoryId": null,
    "tags": ["supermercado", "compras"],
    "createdAt": "2024-01-08T10:00:00"
  }
]
```

---

## Cómo Probar en Swagger UI

### Paso 1: Acceder a Swagger

1. Inicia la aplicación
2. Abre tu navegador y ve a una de estas URLs:
   ```
   http://localhost:8888/api/swagger-ui/index.html
   ```
   O simplemente:
   ```
   http://localhost:8888/api/swagger-ui/
   ```
   
   **Nota:** Las rutas de la API tienen el prefijo `/api` porque está configurado como `context-path`.

### Paso 2: Registrar un Usuario

1. Expande el endpoint `POST /api/auth/register`
2. Haz clic en "Try it out"
3. Edita el JSON del request body:
   ```json
   {
     "email": "test@ejemplo.com",
     "password": "Password123",
     "confirmPassword": "Password123",
     "firstName": "Juan",
     "lastName": "Pérez"
   }
   ```
4. Haz clic en "Execute"
5. Copia el `token` de la respuesta

### Paso 3: Autenticar con Token

1. Haz clic en el botón "Authorize" (candado verde) en la parte superior derecha de Swagger
2. En el campo "Value", pega el token que copiaste (puedes pegar solo el token o con el formato: `Bearer tu_token_aqui`)
3. Haz clic en "Authorize" y luego "Close"
4. Ahora todos los endpoints protegidos estarán disponibles con el candado desbloqueado

### Paso 4: Crear una Cuenta

1. Expande `POST /api/accounts`
2. Haz clic en "Try it out"
3. Completa el request body:
   ```json
   {
     "name": "Mi Cuenta de Ahorros",
     "accountType": "SAVINGS",
     "initialBalance": 1000000.00,
     "currency": "COP"
   }
   ```
4. Haz clic en "Execute"
5. Guarda el `id` de la cuenta de la respuesta

### Paso 5: Crear una Transacción

1. Expande `POST /api/transactions`
2. Usa el `accountId` que guardaste antes
3. Completa el request body:
   ```json
   {
     "accountId": "tu-account-id-aqui",
     "amount": 50000.00,
     "transactionType": "EXPENSE",
     "description": "Compra en supermercado",
     "transactionDate": "2024-01-08"
   }
   ```
4. Haz clic en "Execute"

### Paso 6: Listar Transacciones

1. Expande `GET /api/transactions`
2. Opcionalmente agrega query parameters:
   - `startDate`: 2024-01-01
   - `endDate`: 2024-01-31
3. Haz clic en "Execute"

---

## Cómo Probar en Postman

### Paso 1: Configurar Variables de Entorno (Opcional pero Recomendado)

1. Crea un nuevo Environment en Postman
2. Agrega variables:
   - `base_url`: `http://localhost:8888/api`
   - `token`: (se llenará automáticamente después del login)

### Paso 2: Registrar un Usuario

**Request:**
- Method: `POST`
- URL: `{{base_url}}/auth/register`
- Headers:
  ```
  Content-Type: application/json
  ```
- Body (raw JSON):
  ```json
  {
    "email": "test@ejemplo.com",
    "password": "Password123",
    "confirmPassword": "Password123",
    "firstName": "Juan",
    "lastName": "Pérez"
  }
  ```

**Response:**
- Guarda el `token` de la respuesta
- Configura el token en la variable de entorno `token`

### Paso 3: Login (Opcional)

**Request:**
- Method: `POST`
- URL: `{{base_url}}/auth/login`
- Body:
  ```json
  {
    "email": "test@ejemplo.com",
    "password": "Password123"
  }
  ```

### Paso 4: Crear Cuenta

**Request:**
- Method: `POST`
- URL: `{{base_url}}/accounts`
- Headers:
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- Body:
  ```json
  {
    "name": "Mi Cuenta de Ahorros",
    "accountType": "SAVINGS",
    "initialBalance": 1000000.00,
    "currency": "COP"
  }
  ```

**Response:**
- Guarda el `id` de la cuenta para usar en transacciones

### Paso 5: Listar Cuentas

**Request:**
- Method: `GET`
- URL: `{{base_url}}/accounts`
- Headers:
  ```
  Authorization: Bearer {{token}}
  ```

### Paso 6: Crear Transacción

**Request:**
- Method: `POST`
- URL: `{{base_url}}/transactions`
- Headers:
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- Body:
  ```json
  {
    "accountId": "tu-account-id-aqui",
    "amount": 50000.00,
    "transactionType": "EXPENSE",
    "description": "Compra en supermercado",
    "transactionDate": "2024-01-08"
  }
  ```

### Paso 7: Listar Transacciones

**Request:**
- Method: `GET`
- URL: `{{base_url}}/transactions?startDate=2024-01-01&endDate=2024-01-31`
- Headers:
  ```
  Authorization: Bearer {{token}}
  ```

---

## Flujo Completo de Prueba Recomendado

### Escenario: Usuario Nuevo Registra Sus Finanzas

1. **Registrar usuario** → Obtener token
2. **Crear cuenta de ahorros** → Guardar accountId
3. **Crear transacción INCOME** (salario) → Verificar que saldo aumenta
4. **Crear transacción EXPENSE** (compra) → Verificar que saldo disminuye
5. **Listar todas las transacciones** → Verificar que aparecen
6. **Listar transacciones por fecha** → Verificar filtrado
7. **Obtener cuenta por ID** → Verificar saldo actualizado
8. **Actualizar cuenta** → Cambiar nombre
9. **Listar todas las cuentas** → Verificar actualización

---

## Validaciones y Límites Implementados

### Plan GRATUITO
- ✅ Máximo 1 cuenta
- ✅ Máximo 50 transacciones por mes
- ✅ Validación automática al crear cuenta
- ✅ Validación automática al crear transacción

### Seguridad
- ✅ Contraseñas hasheadas con BCrypt
- ✅ Autenticación JWT
- ✅ Validación de permisos por usuario (solo puede ver/editar sus propios datos)
- ✅ Validación de formato de email
- ✅ Validación de fortaleza de contraseña (mínimo 8 caracteres)

---

## Errores Comunes y Soluciones

### Error 401 Unauthorized
- **Causa:** Token JWT no válido o expirado
- **Solución:** Vuelve a hacer login y obtén un nuevo token

### Error 400 Bad Request - "El usuario ya tiene el número máximo de cuentas permitidas"
- **Causa:** Usuario con plan GRATUITO intenta crear más de 1 cuenta
- **Solución:** Elimina la cuenta existente o actualiza el plan

### Error 400 Bad Request - "El usuario ha excedido el límite de transacciones mensuales"
- **Causa:** Usuario con plan GRATUITO ha creado 50 transacciones este mes
- **Solución:** Espera al próximo mes o actualiza el plan

### Error 404 Not Found
- **Causa:** Recurso (cuenta/transacción) no existe o no pertenece al usuario
- **Solución:** Verifica el ID y que pertenezca al usuario autenticado

---

## Próximos Pasos de Desarrollo

1. Editar y eliminar transacciones
2. Gestión de categorías con códigos DIAN
3. Dashboard con métricas y gráficos
4. Reportes para declaración de renta
5. Exportación a PDF/Excel
6. Sistema de notificaciones

