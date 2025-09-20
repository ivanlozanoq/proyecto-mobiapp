-- Flyway migration: inicial
-- Crea la tabla "pedidos" alineada a la entidad JPA

CREATE TABLE IF NOT EXISTS pedidos (
    id VARCHAR(36) PRIMARY KEY,
    descripcion TEXT NOT NULL,
    estado VARCHAR(32) NOT NULL
);
