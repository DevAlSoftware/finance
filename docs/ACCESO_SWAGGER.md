# Acceso a Swagger UI - DevalFinance API

## URLs de Acceso

### Swagger UI (Interfaz Interactiva)
```
http://localhost:8888/api/swagger-ui/index.html
```

### OpenAPI JSON (Especificación)
```
http://localhost:8888/api/v3/api-docs
```

## Configuración

- **Puerto:** 8888
- **Context Path:** /api
- **Versión API:** 1.0.0
- **Tecnología:** SpringDoc OpenAPI 2.3.0

## Características de Swagger UI

- Documentación interactiva completa
- Autenticación JWT integrada
- Prueba de endpoints directamente desde el navegador
- Ejemplos de requests y responses
- Validación en tiempo real
- Exportación de especificación OpenAPI

## Uso Rápido

1. Accede a Swagger UI
2. Registra un usuario en `POST /api/auth/register`
3. Copia el `token` de la respuesta
4. Haz clic en "Authorize" y pega el token
5. Prueba los endpoints protegidos

