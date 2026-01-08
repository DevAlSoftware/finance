# Casos de Uso Detallados - DevalFinance

## Índice de Casos de Uso

### Autenticación y Usuarios
- UC01: Registro de Usuario
- UC02: Inicio de Sesión
- UC03: Recuperación de Contraseña
- UC04: Actualización de Perfil

### Membresías
- UC05: Visualización de Planes
- UC06: Actualización de Membresía
- UC07: Cancelación de Suscripción

### Cuentas
- UC08: Crear Cuenta Bancaria
- UC09: Editar Cuenta
- UC10: Eliminar Cuenta
- UC11: Consultar Resumen de Cuentas

### Transacciones
- UC12: Registrar Transacción
- UC13: Editar Transacción
- UC14: Eliminar Transacción
- UC15: Buscar Transacciones
- UC16: Filtrar Transacciones por Período
- UC17: Categorización Automática

### Reportes
- UC18: Generar Reporte Mensual
- UC19: Generar Reporte Anual
- UC20: Generar Reporte para Declaración de Renta
- UC21: Exportar Reporte a PDF
- UC22: Exportar Reporte a Excel

### Dashboard
- UC23: Visualizar Dashboard Principal
- UC24: Ver Gráficos de Tendencias
- UC25: Ver Resumen por Categorías

---

## UC01: Registro de Usuario

**ID:** UC01  
**Nombre:** Registro de Usuario  
**Actor Principal:** Usuario no autenticado  
**Precondiciones:**
- El usuario no existe en el sistema
- El usuario tiene acceso a internet
- El usuario tiene un email válido

**Flujo Principal:**
1. Usuario accede a la página de registro
2. Usuario ingresa email
3. Sistema valida formato de email (debe ser válido)
4. Usuario ingresa contraseña
5. Sistema valida fortaleza de contraseña (mínimo 8 caracteres, mayúscula, minúscula, número)
6. Usuario confirma contraseña
7. Sistema valida que ambas contraseñas coincidan
8. Usuario ingresa nombre y apellido (opcional)
9. Usuario hace clic en "Registrarse"
10. Sistema crea nuevo usuario con email y contraseña hasheada
11. Sistema asigna automáticamente plan Gratuito
12. Sistema crea suscripción activa con plan Gratuito
13. Sistema genera token JWT
14. Sistema retorna token y datos del usuario
15. Frontend almacena token en localStorage
16. Frontend redirige al dashboard

**Flujos Alternativos:**

**3a. Email inválido:**
- Sistema muestra mensaje: "Por favor ingrese un email válido"
- Usuario corrige el email
- Flujo continúa en paso 4

**5a. Contraseña débil:**
- Sistema muestra mensaje: "La contraseña debe tener al menos 8 caracteres, incluir mayúscula, minúscula y número"
- Usuario corrige la contraseña
- Flujo continúa en paso 6

**7a. Contraseñas no coinciden:**
- Sistema muestra mensaje: "Las contraseñas no coinciden"
- Usuario corrige la confirmación
- Flujo continúa en paso 9

**10a. Email ya existe:**
- Sistema detecta que el email ya está registrado
- Sistema muestra mensaje: "Este email ya está registrado. ¿Desea iniciar sesión?"
- Flujo termina

**Postcondiciones:**
- Usuario queda registrado en el sistema
- Usuario tiene plan Gratuito activo
- Usuario está autenticado automáticamente
- Usuario puede acceder al dashboard

---

## UC02: Inicio de Sesión

**ID:** UC02  
**Nombre:** Inicio de Sesión  
**Actor Principal:** Usuario registrado  
**Precondiciones:**
- El usuario está registrado en el sistema
- El usuario tiene credenciales válidas

**Flujo Principal:**
1. Usuario accede a la página de login
2. Usuario ingresa email
3. Usuario ingresa contraseña
4. Usuario hace clic en "Iniciar Sesión"
5. Sistema valida email y contraseña
6. Sistema verifica que el usuario esté activo
7. Sistema genera token JWT con información del usuario
8. Sistema genera refresh token
9. Sistema actualiza último acceso del usuario
10. Sistema retorna token de acceso y refresh token
11. Frontend almacena tokens
12. Frontend redirige al dashboard

**Flujos Alternativos:**

**5a. Credenciales inválidas:**
- Sistema detecta email o contraseña incorrectos
- Sistema muestra mensaje: "Email o contraseña incorrectos"
- Flujo termina
- Usuario puede intentar nuevamente o recuperar contraseña

**6a. Usuario inactivo:**
- Sistema detecta que el usuario está inactivo
- Sistema muestra mensaje: "Su cuenta está inactiva. Contacte al soporte"
- Flujo termina

**Postcondiciones:**
- Usuario está autenticado en el sistema
- Tokens de acceso están almacenados en el cliente
- Usuario puede acceder a funcionalidades según su plan

---

## UC12: Registrar Transacción

**ID:** UC12  
**Nombre:** Registrar Transacción  
**Actor Principal:** Usuario autenticado  
**Precondiciones:**
- Usuario está autenticado
- Usuario tiene al menos una cuenta creada
- Usuario no ha excedido el límite de transacciones mensuales (si plan Gratuito)

**Flujo Principal:**
1. Usuario accede a la sección de transacciones
2. Usuario hace clic en "Nueva Transacción"
3. Sistema muestra formulario de transacción
4. Usuario selecciona tipo de transacción (Ingreso o Gasto)
5. Usuario ingresa monto (número positivo)
6. Usuario selecciona cuenta asociada
7. Usuario selecciona categoría (o sistema sugiere si es Premium/Business)
8. Usuario ingresa descripción (opcional)
9. Usuario selecciona fecha de transacción (por defecto fecha actual)
10. Usuario puede agregar tags (si plan Premium/Business)
11. Usuario hace clic en "Guardar"
12. Sistema valida que el usuario no exceda límite mensual (si plan Gratuito)
13. Sistema valida que la cuenta exista y pertenezca al usuario
14. Sistema valida que la categoría exista y sea válida
15. Sistema crea transacción
16. Sistema actualiza saldo de la cuenta
17. Sistema incrementa contador de transacciones mensuales (si aplica)
18. Sistema retorna transacción creada
19. Frontend muestra mensaje de éxito
20. Frontend actualiza lista de transacciones
21. Frontend actualiza saldo en dashboard

**Flujos Alternativos:**

**12a. Límite de transacciones excedido (Plan Gratuito):**
- Sistema detecta que el usuario ha alcanzado 50 transacciones del mes
- Sistema muestra mensaje: "Ha alcanzado el límite de 50 transacciones mensuales. Actualice a Premium para transacciones ilimitadas"
- Sistema muestra botón "Actualizar Plan"
- Flujo termina (no se crea transacción)

**13a. Cuenta no válida:**
- Sistema detecta que la cuenta no existe o no pertenece al usuario
- Sistema muestra mensaje: "Cuenta no válida"
- Usuario selecciona otra cuenta
- Flujo continúa en paso 14

**14a. Categoría no válida:**
- Sistema detecta que la categoría no existe
- Sistema muestra mensaje: "Categoría no válida"
- Usuario selecciona otra categoría
- Flujo continúa en paso 15

**Postcondiciones:**
- Transacción queda registrada en el sistema
- Saldo de la cuenta se actualiza
- Contador mensual se incrementa (si aplica)
- Dashboard muestra información actualizada

---

## UC06: Actualizar Membresía

**ID:** UC06  
**Nombre:** Actualizar Membresía  
**Actor Principal:** Usuario autenticado  
**Precondiciones:**
- Usuario está autenticado
- Usuario tiene un plan activo
- Usuario puede actualizar a un plan superior

**Flujo Principal:**
1. Usuario accede a la sección de Configuración > Membresía
2. Sistema muestra plan actual del usuario
3. Sistema muestra planes disponibles
4. Usuario selecciona nuevo plan (Premium o Business)
5. Sistema muestra comparación de características
6. Sistema muestra precio del plan seleccionado
7. Usuario confirma actualización
8. Sistema valida método de pago (si aplica - futuro)
9. Sistema actualiza suscripción del usuario
10. Sistema establece nueva fecha de inicio
11. Sistema calcula fecha de fin (si es suscripción con duración)
12. Sistema actualiza membresía activa
13. Sistema aplica nuevos límites inmediatamente
14. Sistema envía email de confirmación
15. Sistema retorna confirmación de actualización
16. Frontend muestra mensaje de éxito
17. Frontend actualiza UI según nuevo plan
18. Frontend redirige al dashboard

**Flujos Alternativos:**

**4a. Usuario selecciona plan igual o inferior:**
- Sistema muestra mensaje: "Ya tiene este plan o uno superior"
- Flujo termina

**8a. Método de pago no válido (futuro):**
- Sistema detecta problema con método de pago
- Sistema muestra mensaje: "Error al procesar el pago. Verifique su método de pago"
- Flujo termina

**Postcondiciones:**
- Usuario tiene nuevo plan activo
- Límites del usuario se actualizan inmediatamente
- Usuario puede acceder a funcionalidades del nuevo plan
- Suscripción anterior queda registrada en historial

---

## UC20: Generar Reporte para Declaración de Renta

**ID:** UC20  
**Nombre:** Generar Reporte para Declaración de Renta  
**Actor Principal:** Usuario Premium o Business  
**Precondiciones:**
- Usuario está autenticado
- Usuario tiene plan Premium o Business activo
- Usuario tiene transacciones registradas en el año fiscal

**Flujo Principal:**
1. Usuario accede a la sección de Reportes
2. Usuario selecciona "Reporte para Declaración de Renta"
3. Sistema muestra selector de año fiscal
4. Usuario selecciona año (por defecto año actual)
5. Usuario hace clic en "Generar Reporte"
6. Sistema valida plan del usuario
7. Sistema consulta todas las transacciones del año seleccionado
8. Sistema agrupa transacciones por categorías según códigos DIAN
9. Sistema calcula totales por categoría
10. Sistema organiza datos según formato requerido por DIAN
11. Sistema genera estructura de reporte con:
    - Ingresos totales por tipo
    - Gastos deducibles por categoría DIAN
    - Gastos no deducibles
    - Resumen por trimestre
    - Totales anuales
12. Sistema presenta reporte en pantalla con formato tabular
13. Usuario puede revisar el reporte
14. Sistema ofrece opciones de exportación (PDF, Excel)
15. Usuario puede exportar el reporte

**Flujos Alternativos:**

**6a. Usuario con plan Gratuito:**
- Sistema detecta que el usuario no tiene plan Premium/Business
- Sistema muestra mensaje: "Esta funcionalidad está disponible para planes Premium y Business. Actualice su plan para acceder"
- Sistema muestra botón "Actualizar Plan"
- Flujo termina

**7a. No hay transacciones en el año:**
- Sistema detecta que no hay transacciones registradas
- Sistema muestra mensaje: "No hay transacciones registradas para el año seleccionado"
- Flujo termina

**Postcondiciones:**
- Reporte queda generado y disponible
- Usuario puede visualizar y exportar el reporte
- Reporte está formateado según requerimientos DIAN

---

## UC23: Visualizar Dashboard Principal

**ID:** UC23  
**Nombre:** Visualizar Dashboard Principal  
**Actor Principal:** Usuario autenticado  
**Precondiciones:**
- Usuario está autenticado
- Usuario tiene al menos una cuenta creada (opcional para vista inicial)

**Flujo Principal:**
1. Usuario accede a la aplicación después de login
2. Sistema carga dashboard automáticamente
3. Sistema consulta cuentas del usuario
4. Sistema calcula saldo total consolidado
5. Sistema consulta transacciones del mes actual
6. Sistema calcula ingresos totales del mes
7. Sistema calcula gastos totales del mes
8. Sistema calcula balance neto del mes
9. Sistema agrupa gastos por categorías principales
10. Sistema consulta transacciones de últimos 6 meses (para gráfico de tendencias)
11. Sistema genera datos para gráficos
12. Sistema presenta dashboard con:
    - Saldo total consolidado
    - Resumen del mes (Ingresos, Gastos, Balance)
    - Gráfico de tendencias últimos 6 meses
    - Top 5 categorías de gastos (gráfico circular)
    - Lista de últimas 10 transacciones
    - Alertas y notificaciones (si aplica)
13. Usuario puede interactuar con gráficos (si plan Premium/Business)
14. Usuario puede filtrar por período (si plan Premium/Business)

**Flujos Alternativos:**

**3a. Usuario sin cuentas:**
- Sistema detecta que el usuario no tiene cuentas
- Sistema muestra mensaje: "Comience creando su primera cuenta"
- Sistema muestra botón "Crear Cuenta"
- Dashboard muestra valores en cero

**13a. Usuario con plan Gratuito:**
- Sistema muestra gráficos básicos sin opción de interacción
- Sistema muestra mensaje sutil: "Actualice a Premium para gráficos interactivos"

**Postcondiciones:**
- Dashboard queda cargado con información actualizada
- Usuario puede ver resumen completo de su situación financiera
- Usuario puede navegar a otras secciones desde el dashboard

---

## UC17: Categorización Automática

**ID:** UC17  
**Nombre:** Categorización Automática  
**Actor Principal:** Usuario Premium o Business  
**Precondiciones:**
- Usuario está autenticado
- Usuario tiene plan Premium o Business
- Usuario tiene transacciones históricas con categorías asignadas

**Flujo Principal:**
1. Usuario está creando una nueva transacción
2. Usuario ingresa descripción de la transacción
3. Sistema analiza la descripción usando algoritmo de similitud
4. Sistema consulta transacciones históricas del usuario
5. Sistema busca descripciones similares en historial
6. Sistema identifica categoría más frecuente para descripciones similares
7. Sistema calcula porcentaje de confianza
8. Sistema sugiere categoría al usuario con indicador de confianza
9. Usuario puede:
    - Aceptar sugerencia
    - Seleccionar otra categoría
    - Crear nueva categoría personalizada
10. Si usuario acepta, sistema guarda transacción con categoría sugerida
11. Sistema refuerza el aprendizaje con esta asignación
12. Si usuario selecciona otra, sistema aprende de la corrección

**Flujos Alternativos:**

**5a. No hay historial suficiente:**
- Sistema detecta que no hay suficientes transacciones históricas
- Sistema no sugiere categoría
- Usuario debe seleccionar manualmente
- Sistema aprende de esta selección para futuras sugerencias

**8a. Confianza muy baja (< 50%):**
- Sistema detecta confianza baja
- Sistema muestra sugerencia con mensaje: "Sugerencia con baja confianza"
- Usuario decide manualmente

**Postcondiciones:**
- Transacción queda categorizada
- Sistema mejora sus sugerencias basado en la decisión del usuario
- Usuario ahorra tiempo en categorización manual

---

## Notas sobre Casos de Uso

### Reglas de Negocio Importantes

1. **Límites de Plan Gratuito:**
   - Se validan al momento de crear transacción
   - El contador se resetea el primer día de cada mes
   - Usuario puede ver cuántas transacciones le quedan disponibles

2. **Validación de Permisos:**
   - Cada endpoint valida permisos antes de ejecutar
   - Se verifica plan del usuario
   - Se verifica límites específicos del plan

3. **Integridad de Datos:**
   - No se pueden eliminar cuentas con transacciones asociadas
   - No se pueden eliminar categorías del sistema
   - Los saldos se calculan siempre basados en transacciones

4. **Historial:**
   - Plan Gratuito: Solo puede ver último año
   - Planes pagos: Acceso completo sin límites

### Flujos Transversales

- **Autenticación:** Todos los casos de uso requieren usuario autenticado (excepto registro/login)
- **Validación de Plan:** Funcionalidades premium validan plan antes de ejecutar
- **Auditoría:** Todas las acciones importantes se registran para auditoría
- **Notificaciones:** Ciertas acciones trigger notificaciones al usuario

