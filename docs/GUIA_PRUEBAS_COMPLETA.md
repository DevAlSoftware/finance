# Guía Completa de Pruebas - DevalFinance API

## 📋 Prerequisitos

1. **Aplicación corriendo:**
   ```bash
   mvn spring-boot:run
   ```

2. **Base de datos conectada** (PostgreSQL en `localhost:5432`)

3. **Herramientas para probar:**
   - Postman (recomendado)
   - O curl desde terminal
   - O Swagger UI: `http://localhost:8888/api/swagger-ui/index.html`

---

## 🚀 PASO 1: Registrar un Usuario

**Endpoint:** `POST http://localhost:8888/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "juan.perez@test.com",
  "password": "Password123",
  "confirmPassword": "Password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**✅ Response esperado (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "juan.perez@test.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "membershipPlanId": "...",
    "active": true
  }
}
```

**⚠️ IMPORTANTE:** Copia el `accessToken` y guárdalo. Lo necesitarás para los siguientes requests.

---

## 🔐 PASO 2: Iniciar Sesión (Verificar Login)

**Endpoint:** `POST http://localhost:8888/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "juan.perez@test.com",
  "password": "Password123"
}
```

**✅ Response esperado (200 OK):** Similar al registro, incluye nuevo token.

**Nota:** Si funciona, el sistema de autenticación está correcto.

---

## 💰 PASO 3: Crear Primera Cuenta

**Endpoint:** `POST http://localhost:8888/api/accounts`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token_del_paso_1}
```

**Body:**
```json
{
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "initialBalance": 500000.00,
  "currency": "COP"
}
```

**Tipos válidos de cuenta:**
- `SAVINGS` - Cuenta de ahorros
- `CHECKING` - Cuenta corriente  
- `CASH` - Efectivo

**✅ Response esperado (201 Created):**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Cuenta de Ahorros Principal",
  "accountType": "SAVINGS",
  "initialBalance": 500000.00,
  "currentBalance": 500000.00,
  "currency": "COP",
  "createdAt": "2024-01-08T15:00:00",
  "active": true
}
```

**⚠️ IMPORTANTE:** Guarda el `id` de la cuenta para los siguientes pasos.

---

## 📋 PASO 4: Listar Todas las Cuentas

**Endpoint:** `GET http://localhost:8888/api/accounts`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**✅ Response esperado (200 OK):**
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Cuenta de Ahorros Principal",
    "accountType": "SAVINGS",
    "currentBalance": 500000.00,
    ...
  }
]
```

**Verifica:**
- ✅ Retorna la cuenta creada
- ✅ El saldo inicial es correcto
- ✅ Todos los campos están presentes

---

## 🔍 PASO 5: Obtener Cuenta por ID

**Endpoint:** `GET http://localhost:8888/api/accounts/{accountId}`

**Ejemplo:** `GET http://localhost:8888/api/accounts/660e8400-e29b-41d4-a716-446655440001`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**✅ Response esperado (200 OK):** Objeto de cuenta individual con todos los detalles.

**Verifica:**
- ✅ Retorna la cuenta correcta
- ✅ Los datos coinciden con la creación

---

## ✏️ PASO 6: Actualizar Cuenta

**Endpoint:** `PUT http://localhost:8888/api/accounts/{accountId}`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token}
```

**Body:**
```json
{
  "name": "Cuenta de Ahorros Actualizada",
  "accountType": "SAVINGS",
  "currency": "USD"
}
```

**✅ Response esperado (200 OK):** Cuenta actualizada con el nuevo nombre.

**Verifica:**
- ✅ El nombre cambió
- ✅ Otros campos se mantienen

---

## 💳 PASO 7: Crear Transacción de Ingreso (INCOME)

**Endpoint:** `POST http://localhost:8888/api/transactions`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token}
```

**Body:**
```json
{
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "amount": 100000.00,
  "transactionType": "INCOME",
  "description": "Salario enero",
  "transactionDate": "2024-01-08",
  "tags": ["salario", "ingreso"]
}
```

**Tipos válidos:**
- `INCOME` - Ingreso
- `EXPENSE` - Gasto

**✅ Response esperado (201 Created):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "amount": 100000.00,
  "transactionType": "INCOME",
  "description": "Salario enero",
  "transactionDate": "2024-01-08",
  "tags": ["salario", "ingreso"],
  ...
}
```

**Verifica:**
- ✅ Transacción creada correctamente
- ✅ El saldo de la cuenta debe incrementarse automáticamente

---

## 💳 PASO 8: Verificar que el Saldo de la Cuenta se Actualizó

**Endpoint:** `GET http://localhost:8888/api/accounts/{accountId}`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**✅ Verifica:**
- ✅ El `currentBalance` ahora es 600000.00 (500000 inicial + 100000 ingreso)

---

## 💳 PASO 9: Crear Transacción de Gasto (EXPENSE)

**Endpoint:** `POST http://localhost:8888/api/transactions`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token}
```

**Body:**
```json
{
  "accountId": "660e8400-e29b-41d4-a716-446655440001",
  "amount": 75000.00,
  "transactionType": "EXPENSE",
  "description": "Pago de mercado",
  "transactionDate": "2024-01-08",
  "tags": ["comida", "mercado", "gasto"]
}
```

**✅ Verifica:**
- ✅ Transacción creada
- ✅ El saldo debe decrementar: ahora debería ser 525000.00 (600000 - 75000)

---

## 📊 PASO 10: Listar Todas las Transacciones

**Endpoint:** `GET http://localhost:8888/api/transactions`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**✅ Response esperado (200 OK):**
```json
[
  {
    "id": "...",
    "amount": 100000.00,
    "transactionType": "INCOME",
    "description": "Salario enero",
    ...
  },
  {
    "id": "...",
    "amount": 75000.00,
    "transactionType": "EXPENSE",
    "description": "Pago de mercado",
    ...
  }
]
```

**Verifica:**
- ✅ Aparecen las 2 transacciones
- ✅ Ordenadas correctamente (por fecha)
- ✅ Todos los campos están completos

---

## 🔍 PASO 11: Filtrar Transacciones por Fecha

**Endpoint:** `GET http://localhost:8888/api/transactions?startDate=2024-01-01&endDate=2024-01-31`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**✅ Verifica:**
- ✅ Retorna solo las transacciones en el rango de fechas

---

## ❌ PASO 12: Probar Validaciones - Email Inválido

**Endpoint:** `POST http://localhost:8888/api/auth/register`

**Body:**
```json
{
  "email": "email-invalido",
  "password": "Password123",
  "confirmPassword": "Password123",
  "firstName": "Test",
  "lastName": "User"
}
```

**✅ Response esperado (400 Bad Request):**
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Validation Error",
  "message": "Error de validación en los datos enviados",
  "errors": [
    "debe ser una dirección de correo electrónico válida"
  ]
}
```

---

## ❌ PASO 13: Probar Validaciones - Contraseña Débil

**Endpoint:** `POST http://localhost:8888/api/auth/register`

**Body:**
```json
{
  "email": "test2@test.com",
  "password": "123",
  "confirmPassword": "123",
  "firstName": "Test",
  "lastName": "User"
}
```

**✅ Response esperado (400 Bad Request):** Error indicando que la contraseña debe tener mínimo 8 caracteres.

---

## ❌ PASO 14: Probar Validaciones - Contraseñas No Coinciden

**Endpoint:** `POST http://localhost:8888/api/auth/register`

**Body:**
```json
{
  "email": "test3@test.com",
  "password": "Password123",
  "confirmPassword": "Password456",
  "firstName": "Test",
  "lastName": "User"
}
```

**✅ Response esperado (400 Bad Request):** Error indicando que las contraseñas no coinciden.

---

## ❌ PASO 15: Probar Autenticación - Token Inválido

**Endpoint:** `GET http://localhost:8888/api/accounts`

**Headers:**
```
Authorization: Bearer token_invalido_12345
```

**✅ Response esperado (401 Unauthorized):**
```json
{
  "timestamp": "...",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token inválido o expirado"
}
```

---

## ❌ PASO 16: Probar Autenticación - Sin Token

**Endpoint:** `GET http://localhost:8888/api/accounts`

**Sin headers de Authorization**

**✅ Response esperado (401 Unauthorized):** Error de autenticación.

---

## ❌ PASO 17: Probar Validaciones - Cuenta No Encontrada

**Endpoint:** `GET http://localhost:8888/api/accounts/00000000-0000-0000-0000-000000000000`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**✅ Response esperado (404 Not Found):** Error indicando que la cuenta no existe.

---

## ❌ PASO 18: Probar Validaciones - Saldo Negativo en Cuenta

**Endpoint:** `POST http://localhost:8888/api/accounts`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {tu_token}
```

**Body:**
```json
{
  "name": "Cuenta Test",
  "accountType": "SAVINGS",
  "initialBalance": -1000.00,
  "currency": "COP"
}
```

**✅ Response esperado (400 Bad Request):** Error indicando que el saldo inicial debe ser mayor o igual a cero.

---

## 🗑️ PASO 19: Eliminar Cuenta

**Endpoint:** `DELETE http://localhost:8888/api/accounts/{accountId}`

**Headers:**
```
Authorization: Bearer {tu_token}
```

**⚠️ ADVERTENCIA:** Solo elimina si ya probaste todo lo demás, porque también eliminará las transacciones asociadas.

**✅ Response esperado (204 No Content):** Sin body, solo código 204.

---

## 📝 Checklist de Pruebas

### Autenticación
- [ ] POST /auth/register - Usuario nuevo
- [ ] POST /auth/register - Email inválido (400)
- [ ] POST /auth/register - Contraseña débil (400)
- [ ] POST /auth/register - Contraseñas no coinciden (400)
- [ ] POST /auth/login - Login exitoso
- [ ] POST /auth/login - Credenciales incorrectas (401)

### Cuentas
- [ ] POST /accounts - Crear cuenta
- [ ] POST /accounts - Saldo negativo (400)
- [ ] GET /accounts - Listar todas
- [ ] GET /accounts/{id} - Obtener por ID
- [ ] GET /accounts/{id} - Cuenta no encontrada (404)
- [ ] PUT /accounts/{id} - Actualizar cuenta
- [ ] DELETE /accounts/{id} - Eliminar cuenta

### Transacciones
- [ ] POST /transactions - Crear INCOME
- [ ] POST /transactions - Crear EXPENSE
- [ ] GET /transactions - Listar todas
- [ ] GET /transactions?startDate=...&endDate=... - Filtrar por fecha
- [ ] Verificar que el saldo de cuenta se actualiza automáticamente

### Seguridad
- [ ] Endpoints protegidos sin token (401)
- [ ] Endpoints protegidos con token inválido (401)
- [ ] Endpoints protegidos con token válido (200/201)

---

## 💡 Tips para Postman

1. **Variables de entorno:**
   - Crea una variable `base_url`: `http://localhost:8888/api`
   - Crea una variable `token`: se actualiza automáticamente

2. **Script de Test para Login/Register:**
   ```javascript
   if (pm.response.code === 200 || pm.response.code === 201) {
       var jsonData = pm.response.json();
       pm.environment.set("token", jsonData.accessToken);
   }
   ```

3. **Pre-request Script para endpoints protegidos:**
   ```javascript
   pm.request.headers.add({
       key: 'Authorization',
       value: 'Bearer ' + pm.environment.get("token")
   });
   ```

---

## 🐛 Si Algo Falla

1. **Revisa los logs del servidor** - Busca errores específicos
2. **Verifica la base de datos** - Asegúrate de que PostgreSQL está corriendo
3. **Verifica el token** - Puede haber expirado (validez: 24 horas)
4. **Verifica el formato JSON** - Asegúrate de que esté bien formateado
5. **Verifica los tipos de datos** - UUIDs válidos, fechas en formato correcto

---

## ✅ Resultado Esperado

Si todos los tests pasan, significa que:
- ✅ Autenticación JWT funciona correctamente
- ✅ Validaciones funcionan
- ✅ CRUD de cuentas funciona
- ✅ CRUD de transacciones funciona
- ✅ Actualización automática de saldos funciona
- ✅ Seguridad está configurada correctamente


