# Guía de Contribución - DevalFinance

## Configuración Inicial

### 1. Clonar el Repositorio

```bash
git clone git@github.com:DevAlSoftware/finance.git
cd finance
```

### 2. Configurar Ramas Remotas

```bash
git fetch origin
git checkout develop
git branch --set-upstream-to=origin/develop develop
```

## Flujo de Trabajo

### Crear una Nueva Funcionalidad

1. **Actualizar develop:**
   ```bash
   git checkout develop
   git pull origin develop
   ```

2. **Crear rama feature:**
   ```bash
   git checkout -b feature/nombre-funcionalidad
   ```
   
   Ejemplos de nombres:
   - `feature/UC-12-registrar-transaccion`
   - `feature/exportar-reporte-pdf`
   - `feature/autenticacion-2fa`

3. **Desarrollar:**
   - Hacer cambios en el código
   - Hacer commits frecuentes con mensajes descriptivos
   - Seguir convenciones de commits

4. **Commit con mensaje descriptivo:**
   ```bash
   git add .
   git commit -m "feat(transaction): agregar validación de límites mensuales"
   ```

5. **Push de la rama:**
   ```bash
   git push -u origin feature/nombre-funcionalidad
   ```

6. **Crear Pull Request:**
   - Ir a GitHub
   - Crear PR desde `feature/nombre-funcionalidad` hacia `develop`
   - Llenar template de PR
   - Esperar review y aprobación

7. **Después del merge:**
   ```bash
   git checkout develop
   git pull origin develop
   git branch -d feature/nombre-funcionalidad
   ```

## Convenciones de Commits

### Formato Estándar

```
<tipo>(<alcance>): <descripción>

[descripción detallada opcional]
```

### Tipos

- `feat`: Nueva funcionalidad
- `fix`: Corrección de bug
- `docs`: Documentación
- `style`: Formato (sin cambios de código)
- `refactor`: Refactorización
- `test`: Tests
- `chore`: Tareas de mantenimiento
- `perf`: Mejoras de rendimiento

### Ejemplos

```bash
git commit -m "feat(account): agregar validación de límite de cuentas por plan"

git commit -m "fix(transaction): corregir cálculo de saldo en transacciones múltiples

El cálculo de saldo no estaba actualizando correctamente cuando 
se creaban múltiples transacciones en la misma cuenta. Se corrigió
la lógica de actualización de saldo."

git commit -m "docs(api): actualizar documentación de endpoints de transacciones"

git commit -m "refactor(domain): separar validaciones en casos de uso dedicados"
```

## Código de Conducta

### Estándares de Código

1. **Clean Code:** Código legible y autodocumentado
2. **SOLID:** Aplicar principios SOLID
3. **DRY:** Don't Repeat Yourself
4. **KISS:** Keep It Simple, Stupid
5. **YAGNI:** You Aren't Gonna Need It

### Revisión de Código

- Todo código debe ser revisado antes de merge
- Los PRs requieren al menos 1 aprobación
- Responder a comentarios de manera constructiva
- Aprender de las sugerencias

### Testing

- Agregar tests para nuevas funcionalidades
- Mantener cobertura de tests > 70%
- Tests deben pasar antes de merge

## Preguntas Frecuentes

### ¿Cómo resuelvo conflictos?

1. Actualizar tu rama con los últimos cambios de develop
2. Resolver conflictos manualmente
3. Commit de la resolución
4. Continuar con el PR

### ¿Puedo hacer push directo a develop?

No, siempre usar Pull Requests para mantener calidad del código.

### ¿Qué hacer si mi PR tiene muchos commits?

Puedes hacer squash merge o rebase interactivo para limpiar el historial.

## Recursos

- [Git Workflow Documentation](./GIT_WORKFLOW.md)
- [Arquitectura del Sistema](./ARQUITECTURA.md)
- [Casos de Uso](./CASOS_DE_USO.md)

