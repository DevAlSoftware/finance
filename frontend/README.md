# DevalFinance Frontend

Frontend de la aplicación DevalFinance construido con React, TypeScript y Tailwind CSS.

## Arquitectura

```
frontend/
├── src/
│   ├── components/      # Componentes reutilizables
│   ├── pages/           # Páginas de la aplicación
│   ├── layouts/         # Layouts y plantillas
│   ├── services/        # Servicios de API
│   ├── context/         # Contextos de React (Auth, etc.)
│   ├── hooks/           # Custom hooks
│   ├── types/           # Definiciones TypeScript
│   └── utils/           # Utilidades y helpers
```

## Diseño Espartano

El diseño sigue una metodología minimalista con:

- **Paleta de colores**: Grises neutros (primary-50 a primary-900) con un acento azul sutil
- **Tipografía**: Inter (sans-serif)
- **Componentes**: Limpios y funcionales, sin decoraciones innecesarias
- **Responsive**: Mobile-first con breakpoints estándar

## Scripts

- `npm run dev` - Inicia el servidor de desarrollo
- `npm run build` - Construye para producción
- `npm run preview` - Previsualiza el build de producción

## Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto:

```
VITE_API_BASE_URL=http://localhost:8888/api
```

## Tecnologías

- React 19
- TypeScript
- Vite
- Tailwind CSS
- React Router DOM
- Axios

