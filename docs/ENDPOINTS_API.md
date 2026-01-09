# Endpoints de la API - DevalFinance

## URL Base

```
http://localhost:8888/api
```

## Autenticación

### POST /auth/register
Registrar un nuevo usuario en el sistema

**Headers:**
```
Content-Type: application/json
```

**Body:**
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
  "accessToken": "eyJhbGciOiJIUzM4NCJ9...",
  "refreshToken": "eyJhbGciOiJIUzM4NCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "usuario@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "membershipPlanId": "...",
    "createdAt": "2024-01-08T10:00:00",
    "active": true
  }
}
```

---

### POST /auth/login
Iniciar sesión con credenciales existentes

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "Password123"
}
```

**Response (200 OK):** Similar al registro, incluye tokens JWT

---

## Cuentas (Requiere Token)

Todos los endpoints de cuentas requieren el header de autorización:
```
Authorization: Bearer {accessToken}
```

### POST /accounts
Crear una nueva cuenta bancaria

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Body:**
```json
{
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "initialBalance": 100000.00,
  "currency": "COP"
}
```

**Tipos válidos de cuenta:**
- SAVINGS - Cuenta de ahorros
- CHECKING - Cuenta corriente
- CASH - Efectivo

**Response (201 Created):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "initialBalance": 100000.00,
  "currentBalance": 100000.00,
  "currency": "COP",
  "createdAt": "2024-01-08T10:00:00",
  "active": true
}
```

---

### GET /accounts
Listar todas las cuentas del usuario autenticado

**Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Cuenta de Ahorros Principal",
    "accountType": "SAVINGS",
    "currentBalance": 100000.00,
    "currency": "COP",
    ...
  }
]
```

---

### GET /accounts/{accountId}
Obtener una cuenta específica por ID

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**
- accountId: UUID de la cuenta

**Response (200 OK):** Objeto AccountResponse individual

**Error (404 Not Found):** Si la cuenta no existe o no pertenece al usuario

---

### PUT /accounts/{accountId}
Actualizar una cuenta existente

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Path Parameters:**
- accountId: UUID de la cuenta

**Body:**
```json
{
  "name": "Cuenta Actualizada",
  "accountType": "SAVINGS",
  "currency": "USD"
}
```

Todos los campos del body son opcionales. Solo se actualizan los campos proporcionados.

**Response (200 OK):** AccountResponse actualizado

---

### DELETE /accounts/{accountId}
Eliminar una cuenta y todas sus transacciones asociadas

**Headers:**
```
Authorization: Bearer {token}
```

**Path Parameters:**
- accountId: UUID de la cuenta

**Response (204 No Content):** Sin body

---

## Transacciones (Requiere Token)

Todos los endpoints de transacciones requieren el header de autorización:
```
Authorization: Bearer {accessToken}
```

### POST /transactions
Crear una nueva transacción (ingreso o gasto)

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**Body:**
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

**Tipos válidos de transacción:**
- INCOME - Ingreso (incrementa el saldo)
- EXPENSE - Gasto (decrementa el saldo)

**Validaciones:**
- El monto debe ser mayor a 0
- La cuenta debe existir y pertenecer al usuario
- Si es EXPENSE, el saldo debe ser suficiente
- No debe exceder el límite mensual de transacciones según el plan

**Response (201 Created):**
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

**Nota:** El saldo de la cuenta se actualiza automáticamente después de crear la transacción.

---

### GET /transactions
Listar todas las transacciones del usuario autenticado

**Headers:**
```
Authorization: Bearer {token}
```

**Query Parameters (opcionales):**
- startDate: Fecha de inicio (formato: YYYY-MM-DD)
- endDate: Fecha de fin (formato: YYYY-MM-DD)

**Ejemplo:**
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
    "tags": ["supermercado", "compras"],
    ...
  }
]
```

Las transacciones se retornan ordenadas por fecha descendente (más recientes primero).

---

## Códigos de Estado HTTP

- 200 OK: Operación exitosa (GET, PUT)
- 201 Created: Recurso creado exitosamente (POST)
- 204 No Content: Operación exitosa sin contenido (DELETE)
- 400 Bad Request: Error de validación en los datos enviados
- 401 Unauthorized: Token de autenticación requerido o inválido
- 404 Not Found: Recurso no encontrado o no pertenece al usuario
- 500 Internal Server Error: Error interno del servidor

---

## Notas Importantes

- Todos los endpoints de autenticación son públicos (no requieren token)
- Los demás endpoints requieren el header `Authorization: Bearer {token}`
- El token tiene validez de 24 horas
- Los UUIDs se generan automáticamente por el sistema
- Las fechas deben estar en formato ISO (YYYY-MM-DD o YYYY-MM-DDTHH:mm:ss)
- Los montos son BigDecimal con precisión de 2 decimales
- Todas las validaciones se realizan automáticamente con Jakarta Validation

---

## Límites por Plan de Membresía

### Plan FREE
- Máximo 1 cuenta
- Máximo 50 transacciones por mes

### Plan PREMIUM
- Máximo 10 cuentas
- Máximo 500 transacciones por mes
- Categorización automática
- Categorías personalizadas

### Plan BUSINESS
- Cuentas ilimitadas
- Transacciones ilimitadas
- Todas las funcionalidades del plan PREMIUM
- Múltiples usuarios
- Acceso a API


