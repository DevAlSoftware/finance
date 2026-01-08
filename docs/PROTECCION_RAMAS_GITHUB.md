# Configuración de Protección de Ramas en GitHub

## Proteger la Rama main

Para mantener `main` limpia y lista para despliegue, configura las siguientes protecciones:

### Pasos en GitHub

1. **Ir a Settings del repositorio:**
   - Navegar a: `Settings` → `Branches`

2. **Agregar regla de protección para `main`:**
   - Click en `Add branch protection rule`
   - En `Branch name pattern` escribir: `main`

3. **Configurar las siguientes opciones:**

   ✅ **Require a pull request before merging**
   - ✅ Require approvals: `1` (mínimo 1 aprobación)
   - ✅ Dismiss stale pull request approvals when new commits are pushed
   - ✅ Require review from Code Owners (opcional, si tienes CODEOWNERS)

   ✅ **Require status checks to pass before merging**
   - ✅ Require branches to be up to date before merging
   - Seleccionar checks requeridos (si tienes CI/CD configurado):
     - Build
     - Tests
     - Linter

   ✅ **Require conversation resolution before merging**
   - Todos los comentarios deben ser resueltos

   ✅ **Do not allow bypassing the above settings**
   - Incluso los admins deben seguir las reglas

   ✅ **Restrict who can push to matching branches**
   - No permitir push directo
   - Solo merge mediante Pull Request

4. **Guardar los cambios**

### Proteger la Rama develop (Recomendado)

Para `develop`, configuración menos estricta:

✅ **Require a pull request before merging**
- ✅ Require approvals: `0` o `1` (según tu preferencia)

✅ **Require status checks to pass before merging**
- Checks básicos (build, tests)

❌ **NO marcar:** "Do not allow bypassing" (para permitir hotfixes rápidos si es necesario)

## Resultado

Con esta configuración:

- ✅ **main:** Solo se actualiza mediante Pull Requests aprobados
- ✅ **develop:** Se actualiza mediante Pull Requests (más flexibles)
- ✅ **Historial limpio:** Cada cambio está revisado
- ✅ **Calidad garantizada:** CI/CD debe pasar antes de merge
- ✅ **Sin commits directos:** Imposible hacer push directo a main

## Verificación

Para verificar que funciona:

```bash
# Intentar push directo a main (debe fallar)
git checkout main
git commit --allow-empty -m "test: verificar protección"
git push origin main

# Debe retornar error: "remote: error: GH006: Protected branch update failed"
```

## Nota para Admins

Si eres admin y necesitas hacer un cambio urgente en main:

1. **Opción recomendada:** Usar hotfix branch
   ```bash
   git checkout main
   git checkout -b hotfix/urgente
   # Hacer cambios
   git commit -m "hotfix: cambio urgente"
   git push origin hotfix/urgente
   # Crear PR y mergear
   ```

2. **Opción alternativa:** Temporalmente deshabilitar protección (solo en emergencias)
   - Settings → Branches → Editar regla
   - Deshabilitar temporalmente
   - Hacer cambio
   - Re-habilitar protección inmediatamente

## Configuración de CODEOWNERS (Opcional)

Crear archivo `.github/CODEOWNERS`:

```
# Todos los archivos requieren aprobación
* @tu-usuario-github

# Archivos críticos requieren aprobación adicional
/src/main/java/com/devalFinance/infrastructure/security/ @admin-usuario
/src/main/resources/db/migration/ @admin-usuario
```

Esto asegura que ciertos archivos críticos requieran aprobación específica.

## Configuración Recomendada Final

### main
- ✅ Pull Request requerido
- ✅ 1 aprobación mínima
- ✅ Status checks requeridos
- ✅ Sin push directo
- ✅ Sin bypass para admins

### develop
- ✅ Pull Request requerido (opcional: sin aprobación requerida)
- ✅ Status checks básicos
- ⚠️ Push directo permitido solo para admins (para casos excepcionales)

Esta configuración garantiza código de calidad en main mientras mantiene flexibilidad en develop.

