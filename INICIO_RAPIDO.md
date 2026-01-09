# Inicio Rápido - DevalFinance

Guía rápida para ejecutar el proyecto completo (Backend + Frontend).

## Requisitos

- PostgreSQL corriendo
- Java 17+
- Node.js 18+
- Base de datos `devalfinance` creada

## Pasos Rápidos

### 1. Terminal 1 - Backend (Spring Boot)

```bash
# En la raíz del proyecto
cd C:\devalFinance

# Ejecutar backend
mvn spring-boot:run
```

**Espera a ver:** `Started DevalFinanceApplication` en la consola

**Verifica:** Abre `http://localhost:8888/api/swagger-ui/index.html`

---

### 2. Terminal 2 - Frontend (React)

```bash
# Navegar a frontend
cd C:\devalFinance\frontend

# Instalar dependencias (solo primera vez)
npm install

# Crear archivo .env (si no existe)
# Crea manualmente el archivo frontend/.env con este contenido:
# VITE_API_BASE_URL=http://localhost:8888/api

# Ejecutar frontend
npm run dev
```

**Espera a ver:** `Local: http://localhost:5173`

**Abre:** `http://localhost:5173` en tu navegador

---

## Probar la Aplicación

1. **Registro de Usuario**
   - Ve a `http://localhost:5173`
   - Haz clic en "Regístrate aquí"
   - Completa el formulario
   - Serás redirigido al Dashboard

2. **Dashboard**
   - Verás métricas financieras (inicialmente vacías)
   - Navega usando el menú superior

3. **Verificar Backend**
   - Abre Swagger: `http://localhost:8888/api/swagger-ui/index.html`
   - Deberías ver todos los endpoints documentados

---

## Solución de Problemas

### Backend no inicia
- Verifica que PostgreSQL esté corriendo
- Verifica credenciales en `src/main/resources/application.properties`
- Verifica que la base de datos exista

### Frontend no se conecta al backend
- Verifica que el backend esté corriendo
- Verifica que exista `frontend/.env` con: `VITE_API_BASE_URL=http://localhost:8888/api`
- Reinicia el servidor de desarrollo: `Ctrl+C` y luego `npm run dev`

### Error CORS
- El backend ya tiene CORS configurado
- Si usas otro puerto, actualiza `WebConfig.java`

---

## Comandos Útiles

```bash
# Backend
mvn spring-boot:run           # Ejecutar
mvn clean install             # Compilar

# Frontend  
npm run dev                   # Desarrollo
npm run build                 # Producción
npm run preview               # Ver build
```

---

## Estructura de URLs

- **Backend API**: `http://localhost:8888/api`
- **Swagger UI**: `http://localhost:8888/api/swagger-ui/index.html`
- **Frontend**: `http://localhost:5173`

---

## Próximos Pasos

Una vez que todo funcione:
- Podemos implementar las páginas de Cuentas
- Podemos implementar las páginas de Transacciones
- Podemos mejorar el Dashboard

