-- Script para crear la base de datos DevalFinance
-- Ejecutar como superusuario (postgres)

CREATE DATABASE devalfinance
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Nota: Las tablas se crearán automáticamente mediante Flyway cuando se inicie la aplicación

