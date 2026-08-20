-- 1. Crear el esquema si no existe
IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'users_schema')
BEGIN
    EXEC('CREATE SCHEMA [users_schema]');
END
GO

-- 2. Crear la tabla de usuarios
CREATE TABLE users_schema.users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
GO