# Endpoints de la API - DevalFinance

## URL Base
```
http://localhost:8888/api
```

---

## 🔐 Autenticación

### 1. Registrar Usuario
**POST** `/auth/register`

**Body:**
```json
{
  "email": "test@ejemplo.com",
  "password": "Password123",
  "confirmPassword": "Password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Response (201):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": { ... }
}
```

---

### 2. Iniciar Sesión
**POST** `/auth/login`

**Body:**
```json
{
  "email": "test@ejemplo.com",
  "password": "Password123"
}
```

**Response (200):** Similar al registro

---

## 💰 Cuentas (Requiere Token)

### 3. Crear Cuenta
**POST** `/accounts`
**Headers:** `Authorization: Bearer {token}`

**Body:**
```json
{
  "name": "Cuenta de Ahorros",
  "accountType": "SAVINGS",
  "initialBalance": 100000.00,
  "currency": "COP"
}
```

**Tipos:** `SAVINGS`, `CHECKING`, `CASH`

---

### 4. Listar Cuentas
**GET** `/accounts`
**Headers:** `Authorization: Bearer {token}`

---

### 5. Obtener Cuenta por ID
**GET** `/accounts/{accountId}`
**Headers:** `Authorization: Bearer {token}`

---

### 6. Actualizar Cuenta
**PUT** `/accounts/{accountId}`
**Headers:** `Authorization: Bearer {token}`

**Body:**
```json
{
  "name": "Cuenta Actualizada",
  "accountType": "SAVINGS",
  "currency": "USD"
}
```

---

### 7. Eliminar Cuenta
**DELETE** `/accounts/{accountId}`
**Headers:** `Authorization: Bearer {token}`

---

## 💳 Transacciones (Requiere Token)

### 8. Crear Transacción
**POST** `/transactions`
**Headers:** `Authorization: Bearer {token}`

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

**Tipos:** `INCOME`, `EXPENSE`

---

### 9. Listar Transacciones
**GET** `/transactions?startDate=2024-01-01&endDate=2024-01-31`
**Headers:** `Authorization: Bearer {token}`

**Query params (opcionales):**
- `startDate` - YYYY-MM-DD
- `endDate` - YYYY-MM-DD

---

## 📝 Notas

- Todos los endpoints de autenticación son públicos
- Los demás endpoints requieren el header `Authorization: Bearer {token}`
- El token tiene validez de 24 horas
- Usa Postman para probar todos los endpoints

