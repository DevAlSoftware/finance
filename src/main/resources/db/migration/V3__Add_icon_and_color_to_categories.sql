-- Agregar campos icon y color a la tabla categories
ALTER TABLE categories 
ADD COLUMN icon VARCHAR(10),
ADD COLUMN color VARCHAR(7);

-- Actualizar categorías existentes con valores por defecto
UPDATE categories 
SET icon = '📁', 
    color = '#6B7280' 
WHERE icon IS NULL OR color IS NULL;

