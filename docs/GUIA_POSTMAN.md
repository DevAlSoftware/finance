# Guía para Probar la API con Postman

## Configuración Inicial

1. **URL Base:**
   ```
   http://localhost:8888/api
   ```

2. **Headers por defecto:**
   - `Content-Type: application/json`
   - `Accept: application/json`

## 1. Registrar Usuario

**Endpoint:** `POST /api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "test@ejemplo.com",
  "password": "Password123",
  "confirmPassword": "Password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Response esperado (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "test@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "membershipPlanId": "...",
    "createdAt": "2024-01-08T10:00:00",
    "active": true
  }
}
```

**⚠️ IMPORTANTE:** Guarda el `accessToken` para usar en los siguientes requests.

---

## 2. Iniciar Sesión

**Endpoint:** `POST /api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "test@ejemplo.com",
  "password": "Password123"
}
```

**Response esperado (200 OK):** Similar al registro, incluye el token JWT.

---

## 3. Crear Cuenta (Requiere Token)

**Endpoint:** `POST /api/accounts`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token_aqui}
```

**Body (raw JSON):**
```json
{
  "name": "Cuenta de Ahorros",
  "accountType": "SAVINGS",
  "initialBalance": 100000.00,
  "currency": "COP"
}
```

**Tipos de cuenta válidos:**
- `SAVINGS` - Cuenta de ahorros
- `CHECKING` - Cuenta corriente
- `CASH` - Efectivo

**Response esperado (201 Created):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Cuenta de Ahorros",
  "accountType": "SAVINGS",
  "initialBalance": 100000.00,
  "currentBalance": 100000.00,
  "currency": "COP",
  "createdAt": "2024-01-08T10:00:00",
  "active": true
}
```

---

## 4. Listar Cuentas (Requiere Token)

**Endpoint:** `GET /api/accounts`

**Headers:**
```
Authorization: Bearer {tu_token_aqui}
```

**Response esperado (200 OK):**
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Cuenta de Ahorros",
    "accountType": "SAVINGS",
    "initialBalance": 100000.00,
    "currentBalance": 100000.00,
    "currency": "COP",
    "createdAt": "2024-01-08T10:00:00",
    "active": true
  }
]
```

---

## 5. Obtener Cuenta por ID (Requiere Token)

**Endpoint:** `GET /api/accounts/{accountId}`

**Headers:**
```
Authorization: Bearer {tu_token_aqui}
```

**Ejemplo:**
```
GET /api/accounts/660e8400-e29b-41d4-a716-446655440001
```

**Response esperado (200 OK):** Objeto de cuenta individual.

---

## 6. Actualizar Cuenta (Requiere Token)

**Endpoint:** `PUT /api/accounts/{accountId}`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token_aqui}
```

**Body (raw JSON):**
```json
{
  "name": "Cuenta de Ahorros Actualizada",
  "accountType": "SAVINGS",
  "currency": "USD"
}
```

**Response esperado (200 OK):** Cuenta actualizada.

---

## 7. Eliminar Cuenta (Requiere Token)

**Endpoint:** `DELETE /api/accounts/{accountId}`

**Headers:**
```
Authorization: Bearer {tu_token_aqui}
```

**Response esperado (204 No Content):** Sin body.

---

## 8. Crear Transacción (Requiere Token)

**Endpoint:** `POST /api/transactions`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token_aqui}
```

**Body (raw JSON):**
```json
{
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "amount": 50000.00,
  "transactionType": "EXPENSE",
  "description": "Compra en supermercado",
  "transactionDate": "2024-01-08",
  "tags": ["supermercado", "compras"]
}
```

**Tipos de transacción válidos:**
- `INCOME` - Ingreso
- `EXPENSE` - Gasto

**Response esperado (201 Created):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 50000.00,
  "transactionType": "EXPENSE",
  "description": "Compra en supermercado",
  "transactionDate": "2024-01-08",
  "categoryId": null,
  "tags": ["supermercado", "compras"],
  "createdAt": "2024-01-08T10:00:00",
  "updatedAt": "2024-01-08T10:00:00"
}
```

---

## 9. Listar Transacciones (Requiere Token)

**Endpoint:** `GET /api/transactions`

**Headers:**
```
Authorization: Bearer {tu_token_aqui}
```

**Query Parameters (opcionales):**
- `startDate` - Fecha de inicio (formato: YYYY-MM-DD)
- `endDate` - Fecha de fin (formato: YYYY-MM-DD)

**Ejemplo:**
```
GET /api/transactions?startDate=2024-01-01&endDate=2024-01-31
```

**Response esperado (200 OK):** Lista de transacciones.

---

## Configuración de Variables en Postman

### Variables de Entorno

Crea un entorno en Postman con estas variables:

```
base_url: http://localhost:8888/api
token: (se actualiza después del login)
account_id: (se actualiza después de crear una cuenta)
```

### Script de Tests para Automatizar

En el request de **Login** o **Register**, agrega este script en la pestaña "Tests":

```javascript
if (pm.response.code === 200 || pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.accessToken);
    console.log("Token guardado:", jsonData.accessToken);
}
```

En el request de **Crear Cuenta**, agrega:

```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("account_id", jsonData.id);
    console.log("Account ID guardado:", jsonData.id);
}
```

Luego usa `{{token}}` y `{{account_id}}` en los otros requests.

---

## Errores Comunes

### 401 Unauthorized
- Verifica que el token esté en el header `Authorization: Bearer {token}`
- Verifica que el token no haya expirado (validez: 24 horas)

### 400 Bad Request
- Verifica el formato JSON del body
- Verifica que los campos requeridos estén presentes
- Verifica que los valores cumplan con las validaciones (email válido, contraseña de mínimo 8 caracteres, etc.)

### 404 Not Found
- Verifica la URL completa
- Verifica que el recurso exista (por ejemplo, que el accountId sea válido)

### 500 Internal Server Error
- Revisa los logs del servidor
- Verifica que la base de datos esté conectada
- Verifica que las migraciones de Flyway se hayan ejecutado correctamente

---

## Colección de Postman

Para facilitar, puedes crear una colección con todos estos endpoints organizados por carpetas:
- **Auth** (register, login)
- **Accounts** (crear, listar, obtener, actualizar, eliminar)
- **Transactions** (crear, listar)

