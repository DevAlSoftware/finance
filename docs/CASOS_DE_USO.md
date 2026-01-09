# Casos de Uso - DevalFinance

## Descripción General

Este documento describe los casos de uso funcionales implementados y pendientes en el sistema DevalFinance.

---

## Autenticación y Gestión de Usuarios

### CU-01: Registrar Usuario

**Actor:** Usuario nuevo
**Precondiciones:** Email no registrado previamente
**Flujo Principal:**
1. Usuario envía datos: email, contraseña, confirmación, nombre, apellido
2. Sistema valida formato de email
3. Sistema valida fortaleza de contraseña (mínimo 8 caracteres)
4. Sistema valida que contraseñas coincidan
5. Sistema valida que email no exista
6. Sistema crea usuario con plan FREE por defecto
7. Sistema crea suscripción activa
8. Sistema genera token JWT
9. Sistema retorna token y datos del usuario

**Postcondiciones:** Usuario creado y autenticado, suscripción FREE activa
**Implementado:** RegisterUserUseCase

---

### CU-02: Iniciar Sesión

**Actor:** Usuario registrado
**Precondiciones:** Usuario existe y está activo
**Flujo Principal:**
1. Usuario envía email y contraseña
2. Sistema busca usuario por email
3. Sistema valida contraseña con BCrypt
4. Sistema verifica que usuario esté activo
5. Sistema genera token JWT
6. Sistema retorna token y datos del usuario

**Postcondiciones:** Usuario autenticado
**Implementado:** AuthenticateUserUseCase

---

## Gestión de Cuentas

### CU-03: Crear Cuenta Bancaria

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, no exceder límite de cuentas según plan
**Flujo Principal:**
1. Usuario envía datos: nombre, tipo, saldo inicial, moneda
2. Sistema valida que usuario existe
3. Sistema obtiene membresía del usuario
4. Sistema valida límite de cuentas según plan
5. Sistema crea cuenta con saldo inicial
6. Sistema asocia cuenta al usuario
7. Sistema retorna cuenta creada

**Postcondiciones:** Cuenta creada y asociada al usuario
**Límites por Plan:**
- FREE: 1 cuenta máximo
- PREMIUM: 10 cuentas máximo
- BUSINESS: Ilimitado
**Implementado:** CreateAccountUseCase

---

### CU-04: Listar Cuentas del Usuario

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado
**Flujo Principal:**
1. Sistema obtiene todas las cuentas del usuario autenticado
2. Sistema retorna lista de cuentas con saldos actuales

**Postcondiciones:** Lista de cuentas retornada
**Implementado:** GetAccountsUseCase

---

### CU-05: Obtener Cuenta por ID

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, cuenta existe y pertenece al usuario
**Flujo Principal:**
1. Usuario envía ID de cuenta
2. Sistema busca cuenta por ID
3. Sistema valida que cuenta pertenezca al usuario
4. Sistema retorna cuenta

**Postcondiciones:** Datos de la cuenta retornados
**Implementado:** GetAccountByIdUseCase

---

### CU-06: Actualizar Cuenta

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, cuenta existe y pertenece al usuario
**Flujo Principal:**
1. Usuario envía ID de cuenta y datos a actualizar
2. Sistema busca cuenta por ID
3. Sistema valida que cuenta pertenezca al usuario
4. Sistema actualiza datos de la cuenta
5. Sistema retorna cuenta actualizada

**Postcondiciones:** Cuenta actualizada
**Implementado:** UpdateAccountUseCase

---

### CU-07: Eliminar Cuenta

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, cuenta existe y pertenece al usuario
**Flujo Principal:**
1. Usuario envía ID de cuenta
2. Sistema busca cuenta por ID
3. Sistema valida que cuenta pertenezca al usuario
4. Sistema elimina cuenta y todas sus transacciones asociadas
5. Sistema retorna confirmación

**Postcondiciones:** Cuenta y transacciones eliminadas
**Implementado:** DeleteAccountUseCase

---

## Gestión de Transacciones

### CU-08: Crear Transacción de Ingreso

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, cuenta existe, no exceder límite mensual
**Flujo Principal:**
1. Usuario envía: ID de cuenta, monto, tipo (INCOME), descripción, fecha, tags
2. Sistema valida que cuenta pertenezca al usuario
3. Sistema valida límite de transacciones mensuales según plan
4. Sistema crea transacción de tipo INCOME
5. Sistema incrementa saldo actual de la cuenta
6. Sistema retorna transacción creada

**Postcondiciones:** Transacción creada, saldo de cuenta actualizado
**Límites por Plan:**
- FREE: 50 transacciones/mes
- PREMIUM: 500 transacciones/mes
- BUSINESS: Ilimitado
**Implementado:** CreateTransactionUseCase

---

### CU-09: Crear Transacción de Gasto

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, cuenta existe, saldo suficiente, no exceder límite mensual
**Flujo Principal:**
1. Usuario envía: ID de cuenta, monto, tipo (EXPENSE), descripción, fecha, tags
2. Sistema valida que cuenta pertenezca al usuario
3. Sistema valida que saldo sea suficiente
4. Sistema valida límite de transacciones mensuales según plan
5. Sistema crea transacción de tipo EXPENSE
6. Sistema decrementa saldo actual de la cuenta
7. Sistema retorna transacción creada

**Postcondiciones:** Transacción creada, saldo de cuenta actualizado
**Implementado:** CreateTransactionUseCase

---

### CU-10: Listar Transacciones del Usuario

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado
**Flujo Principal:**
1. Usuario opcionalmente envía fechas de inicio y fin
2. Sistema obtiene todas las transacciones del usuario
3. Sistema filtra por fecha si se proporcionan
4. Sistema retorna lista de transacciones ordenadas por fecha

**Postcondiciones:** Lista de transacciones retornada
**Implementado:** GetTransactionsUseCase

---

### CU-11: Actualizar Transacción

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, transacción existe y pertenece al usuario
**Flujo Principal:**
1. Usuario envía ID de transacción y datos a actualizar
2. Sistema busca transacción por ID
3. Sistema valida que transacción pertenezca al usuario
4. Sistema recalcula saldo de cuenta si monto cambió
5. Sistema actualiza transacción
6. Sistema retorna transacción actualizada

**Postcondiciones:** Transacción actualizada, saldo recalculado
**Pendiente:** No implementado

---

### CU-12: Eliminar Transacción

**Actor:** Usuario autenticado
**Precondiciones:** Usuario autenticado, transacción existe y pertenece al usuario
**Flujo Principal:**
1. Usuario envía ID de transacción
2. Sistema busca transacción por ID
3. Sistema valida que transacción pertenezca al usuario
4. Sistema ajusta saldo de cuenta (incrementa si era EXPENSE, decrementa si era INCOME)
5. Sistema elimina transacción
6. Sistema retorna confirmación

**Postcondiciones:** Transacción eliminada, saldo ajustado
**Pendiente:** No implementado

---

## Validaciones y Límites

### CU-13: Validar Límite de Cuentas

**Actor:** Sistema
**Precondiciones:** Usuario tiene membresía activa
**Flujo Principal:**
1. Sistema obtiene plan de membresía del usuario
2. Sistema cuenta cuentas activas del usuario
3. Sistema valida que no exceda el límite del plan
4. Sistema lanza excepción si excede límite

**Postcondiciones:** Límite validado
**Implementado:** CreateAccountUseCase

---

### CU-14: Validar Límite de Transacciones Mensuales

**Actor:** Sistema
**Precondiciones:** Usuario tiene membresía activa
**Flujo Principal:**
1. Sistema obtiene plan de membresía del usuario
2. Sistema cuenta transacciones del mes actual del usuario
3. Sistema valida que no exceda el límite del plan
4. Sistema lanza excepción si excede límite

**Postcondiciones:** Límite validado
**Implementado:** ValidateTransactionLimitUseCase

---

### CU-15: Validar Fortaleza de Contraseña

**Actor:** Sistema
**Precondiciones:** Contraseña proporcionada
**Flujo Principal:**
1. Sistema valida que tenga mínimo 8 caracteres
2. Sistema valida que tenga al menos una letra mayúscula
3. Sistema valida que tenga al menos una letra minúscula
4. Sistema valida que tenga al menos un número
5. Sistema lanza excepción si no cumple requisitos

**Postcondiciones:** Contraseña validada
**Implementado:** ValidatePasswordStrengthUseCase

---

## Casos de Uso Pendientes

### CU-16: Categorizar Transacción

**Actor:** Usuario autenticado (PREMIUM/BUSINESS)
**Precondiciones:** Usuario con plan PREMIUM o BUSINESS
**Descripción:** Asignar categoría con código DIAN a transacción
**Pendiente:** No implementado

---

### CU-17: Categorización Automática

**Actor:** Sistema
**Precondiciones:** Usuario con plan PREMIUM o BUSINESS
**Descripción:** Sistema categoriza automáticamente transacciones basándose en descripción o tags
**Pendiente:** No implementado

---

### CU-18: Generar Reporte de Ingresos y Gastos

**Actor:** Usuario autenticado
**Precondiciones:** Usuario tiene transacciones
**Descripción:** Generar reporte mensual/anual de ingresos vs gastos
**Pendiente:** No implementado

---

### CU-19: Exportar Datos

**Actor:** Usuario autenticado (PREMIUM/BUSINESS)
**Precondiciones:** Usuario con plan PREMIUM o BUSINESS
**Descripción:** Exportar transacciones y cuentas a Excel, PDF o CSV
**Pendiente:** No implementado

---

### CU-20: Visualizar Dashboard

**Actor:** Usuario autenticado
**Precondiciones:** Usuario tiene cuentas y transacciones
**Descripción:** Mostrar gráficos y resumen financiero en dashboard
**Pendiente:** No implementado (frontend)

---

### CU-21: Configurar Límites Personalizados

**Actor:** Usuario autenticado (BUSINESS)
**Precondiciones:** Usuario con plan BUSINESS
**Descripción:** Configurar límites personalizados para alertas de gastos
**Pendiente:** No implementado

---

### CU-22: Gestionar Categorías Personalizadas

**Actor:** Usuario autenticado (PREMIUM/BUSINESS)
**Precondiciones:** Usuario con plan PREMIUM o BUSINESS
**Descripción:** Crear, editar y eliminar categorías personalizadas
**Pendiente:** No implementado

---

## Resumen de Implementación

**Casos de Uso Implementados:** 15
**Casos de Uso Pendientes:** 7


