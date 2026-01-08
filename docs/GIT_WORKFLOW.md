# Flujo de Trabajo Git - DevalFinance

## Estrategia de Ramas (Git Flow)

### Ramas Principales

**main (producción)**
- Rama principal estable
- Solo código listo para producción
- Protegida contra push directo
- Solo se actualiza mediante Pull Requests desde `develop` o `release/*`
- Cada commit en main debe tener un tag de versión

**develop (desarrollo)**
- Rama de integración para desarrollo continuo
- Contiene todas las nuevas funcionalidades
- Está siempre lista para desplegar a staging/testing
- No debe estar rota (debe compilar y pasar tests básicos)

### Ramas de Soporte

**feature/* (nuevas funcionalidades)**
- Se crean desde `develop`
- Nomenclatura: `feature/UC-12-crear-transaccion` o `feature/autenticacion-jwt`
- Se mergean de vuelta a `develop` cuando están completas
- Se eliminan después del merge

**bugfix/* (corrección de bugs)**
- Se crean desde `develop` (si el bug está en desarrollo)
- O desde `main` (si el bug está en producción)
- Nomenclatura: `bugfix/correccion-saldo-cuenta`
- Se mergean a `develop` y/o `main` según corresponda

**hotfix/* (correcciones urgentes en producción)**
- Se crean desde `main`
- Para bugs críticos en producción que necesitan corrección inmediata
- Se mergean a `main` y `develop`
- Nomenclatura: `hotfix/seguridad-token-expirado`

**release/* (preparación de release)**
- Se crean desde `develop` cuando se va a liberar una versión
- Solo para ajustes finales (versionado, documentación)
- Se mergean a `main` y `develop`

## Convenciones de Commits

### Formato

```
<tipo>(<alcance>): <descripción corta>

<descripción detallada opcional>

<footer opcional>
```

### Tipos de Commit

- **feat:** Nueva funcionalidad
- **fix:** Corrección de bug
- **docs:** Cambios en documentación
- **style:** Cambios de formato (espacios, punto y coma, etc.)
- **refactor:** Refactorización de código (sin cambiar funcionalidad)
- **test:** Agregar o modificar tests
- **chore:** Tareas de mantenimiento (build, config, etc.)
- **perf:** Mejoras de rendimiento
- **ci:** Cambios en CI/CD
- **build:** Cambios en sistema de build

### Ejemplos de Commits

```
feat(auth): implementar autenticación JWT

- Agregar JwtTokenProvider para generar tokens
- Implementar JwtAuthenticationFilter
- Configurar Spring Security con JWT

feat(account): agregar validación de límites por plan

fix(transaction): corregir cálculo de saldo en transacciones múltiples

refactor(domain): separar mappers en clases dedicadas

docs(api): actualizar documentación de endpoints

test(user): agregar tests para casos de uso de usuario
```

### Reglas para Commits

1. **Un commit, un cambio lógico:** No mezclar funcionalidades diferentes
2. **Mensajes descriptivos:** Explicar QUÉ y POR QUÉ, no CÓMO
3. **En español:** Todos los mensajes en español
4. **Máximo 50 caracteres en el título:** Después de 50 se corta
5. **Cuerpo opcional:** Si el cambio necesita explicación, agregar cuerpo

## Flujo de Trabajo Diario

### Para Nueva Funcionalidad

```bash
# 1. Actualizar develop
git checkout develop
git pull origin develop

# 2. Crear rama feature
git checkout -b feature/nombre-funcionalidad

# 3. Trabajar en la funcionalidad
# ... hacer cambios ...

# 4. Hacer commits
git add .
git commit -m "feat(nombre): descripción del cambio"

# 5. Push de la rama
git push -u origin feature/nombre-funcionalidad

# 6. Crear Pull Request en GitHub
# - Desde feature/nombre-funcionalidad hacia develop
# - Solicitar review
# - Pasar CI/CD

# 7. Después del merge, limpiar
git checkout develop
git pull origin develop
git branch -d feature/nombre-funcionalidad
```

### Para Corrección de Bug

```bash
# 1. Crear rama bugfix desde develop
git checkout develop
git pull origin develop
git checkout -b bugfix/descripcion-bug

# 2. Corregir el bug
# ... hacer cambios ...

# 3. Commits
git commit -m "fix(componente): descripción de la corrección"

# 4. Push y Pull Request
git push -u origin bugfix/descripcion-bug
# Crear PR a develop
```

### Para Hotfix en Producción

```bash
# 1. Crear rama desde main
git checkout main
git pull origin main
git checkout -b hotfix/descripcion-urgente

# 2. Corregir
# ... hacer cambios ...

# 3. Commits
git commit -m "hotfix(componente): corrección urgente"

# 4. Merge a main y develop
git checkout main
git merge hotfix/descripcion-urgente
git push origin main
git tag -a v1.0.1 -m "Hotfix: descripción"

git checkout develop
git merge hotfix/descripcion-urgente
git push origin develop
```

## Pull Requests

### Cuándo Crear un PR

- Cuando una funcionalidad está completa
- Cuando un bug está corregido y testeado
- Antes de mergear a `develop` o `main`

### Requisitos para un PR

1. **Código compila:** Sin errores de compilación
2. **Tests pasan:** Todos los tests deben pasar
3. **Linter limpio:** Sin warnings críticos
4. **Descripción clara:** Explicar qué cambia y por qué
5. **Commits organizados:** Commits atómicos y bien descriptos

### Plantilla de Pull Request

```markdown
## Descripción
Breve descripción de los cambios realizados

## Tipo de Cambio
- [ ] Nueva funcionalidad
- [ ] Corrección de bug
- [ ] Refactorización
- [ ] Documentación
- [ ] Otro

## Cambios Realizados
- Cambio 1
- Cambio 2
- Cambio 3

## Checklist
- [ ] Código compila
- [ ] Tests pasan
- [ ] Documentación actualizada
- [ ] Sin warnings críticos
- [ ] Revisado por otro desarrollador

## Screenshots (si aplica)

## Issues Relacionados
Closes #issue-number
```

## Tags y Versiones

### Versionado Semántico

`MAJOR.MINOR.PATCH`

- **MAJOR:** Cambios incompatibles con versiones anteriores
- **MINOR:** Nueva funcionalidad compatible
- **PATCH:** Corrección de bugs compatible

### Crear Tag

```bash
# Tag anotado (recomendado)
git tag -a v1.0.0 -m "Release versión 1.0.0: Primera versión estable"

# Push del tag
git push origin v1.0.0
```

## Protección de Ramas

### main
- Requiere Pull Request para merge
- Requiere aprobación de al menos 1 reviewer
- Requiere que CI/CD pase
- No permite push directo

### develop
- Requiere Pull Request para merge (desde features)
- Requiere que CI/CD pase
- Permite push directo en casos excepcionales

## Buenas Prácticas

### ✅ HACER

1. **Commit frecuente:** Commits pequeños y frecuentes
2. **Mensajes claros:** Explicar el propósito del cambio
3. **Pull antes de push:** Siempre actualizar antes de push
4. **Revisar antes de commit:** `git status` y `git diff`
5. **Usar branches:** Nunca trabajar directamente en main/develop

### ❌ NO HACER

1. **NO hacer commit de archivos generados:** `.class`, `target/`, `node_modules/`
2. **NO hacer commit de secrets:** Contraseñas, API keys, etc.
3. **NO hacer commit sin revisar:** Revisar qué se está commitando
4. **NO mezclar cambios:** Un commit, un propósito
5. **NO hacer force push a main/develop:** Nunca

## Comandos Útiles

```bash
# Ver estado
git status
git log --oneline --graph --all

# Ver cambios
git diff
git diff --staged

# Deshacer cambios
git restore <archivo>              # Descartar cambios sin commit
git restore --staged <archivo>     # Quitar del staging
git reset HEAD~1                   # Deshacer último commit (mantiene cambios)

# Limpiar
git clean -fd                      # Eliminar archivos no trackeados
git branch -d <rama>               # Eliminar rama local
git branch -D <rama>               # Forzar eliminación de rama local

# Información
git log --oneline -10              # Últimos 10 commits
git branch -a                      # Todas las ramas
git remote -v                      # Remotes configurados
```

## Resolución de Conflictos

### Cuando hay conflicto en merge

```bash
# 1. Ver archivos en conflicto
git status

# 2. Abrir archivos conflictivos y resolver
# Buscar marcadores: <<<<<<< ======= >>>>>>>

# 3. Agregar archivos resueltos
git add <archivo>

# 4. Completar merge
git commit
```

## Workflow Completo: Ejemplo

```bash
# 1. Actualizar develop
git checkout develop
git pull origin develop

# 2. Crear feature branch
git checkout -b feature/nueva-funcionalidad

# 3. Desarrollo (múltiples commits)
git add .
git commit -m "feat(feature): primer cambio"
git add .
git commit -m "feat(feature): segundo cambio"
git add .
git commit -m "test(feature): agregar tests"

# 4. Push
git push -u origin feature/nueva-funcionalidad

# 5. Crear Pull Request en GitHub
# - Título: feat: nueva funcionalidad
# - Descripción completa
# - Asignar reviewers
# - Esperar aprobación

# 6. Después del merge
git checkout develop
git pull origin develop
git branch -d feature/nueva-funcionalidad
```

Este flujo garantiza un historial limpio, código estable en main, y desarrollo organizado.

