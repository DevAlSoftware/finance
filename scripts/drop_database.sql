-- Script para eliminar la base de datos DevalFinance
-- ADVERTENCIA: Esto eliminará TODOS los datos
-- Solo usar en desarrollo

-- Terminar todas las conexiones activas
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'devalfinance' AND pid <> pg_backend_pid();

-- Eliminar la base de datos
DROP DATABASE IF EXISTS devalfinance;

