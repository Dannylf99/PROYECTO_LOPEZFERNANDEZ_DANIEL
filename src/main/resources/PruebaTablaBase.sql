-- ===================================================
-- CREACIÓN DE LA BASE DE DATOS
-- ===================================================
DROP DATABASE IF EXISTS gestiona;
CREATE DATABASE gestiona;
USE gestiona;

-- ===================================================
-- TABLA EMPRESAS
-- ===================================================
CREATE TABLE empresa (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cif VARCHAR(50) NOT NULL,
    direccion VARCHAR(200) NOT NULL
);

-- ===================================================
-- TABLA ALUMNOS
-- ===================================================
CREATE TABLE alumno (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    empresa_asignada INT,
    tutor_centro INT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_alumno_empresa FOREIGN KEY (empresa_asignada) REFERENCES empresa(id_empresa) ON DELETE SET NULL
);

-- ===================================================
-- TABLA COORDINADORES
-- ===================================================
CREATE TABLE coordinador (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ===================================================
-- TABLA GESTORES
-- ===================================================
CREATE TABLE gestor (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    id_empresa INT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_gestor_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE SET NULL
);

-- ===================================================
-- TABLA ADMINISTRACIÓN
-- ===================================================
CREATE TABLE administracion (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ===================================================
-- TABLA DOCUMENTOS
-- ===================================================
CREATE TABLE documento (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    fecha_subida DATE,
    estado ENUM('VALIDADO', 'FIRMADO', 'ELIMINADO') DEFAULT 'VALIDADO',
    ruta_archivo VARCHAR(200),
    CONSTRAINT fk_documento_usuario FOREIGN KEY (id_usuario) REFERENCES alumno(id_usuario) ON DELETE CASCADE
);

-- ===================================================
-- TABLA PRÁCTICAS
-- ===================================================
CREATE TABLE practica (
    id_practica INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    id_empresa INT NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    horas_totales INT,
    CONSTRAINT fk_practica_alumno FOREIGN KEY (id_alumno) REFERENCES alumno(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_practica_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE
);

-- ===================================================
-- TABLA NOTIFICACIONES
-- ===================================================
CREATE TABLE notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(255),
    fecha DATE,
    leida BOOLEAN DEFAULT FALSE
);

-- ===================================================
-- DATOS DE PRUEBA (Contraseñas: Prueba123!)
-- ===================================================

-- Empresa
INSERT INTO empresa (nombre, cif, direccion)
VALUES ('EmpresaX', 'B12345678', 'Calle Falsa 123');

-- Alumno (contraseña: Prueba123!)
INSERT INTO alumno (dni, nombre, apellidos, email, contrasenya, empresa_asignada, tutor_centro, activo)
VALUES ('12345678A', 'Juan', 'Pérez', 'alumno@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', 1, NULL, TRUE);

-- Coordinador (contraseña: Prueba123!)
INSERT INTO coordinador (dni, nombre, apellidos, email, contrasenya, activo)
VALUES ('23456789B', 'Marta', 'García', 'coordinador@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', TRUE);

-- Gestor (contraseña: Prueba123!)
INSERT INTO gestor (dni, nombre, apellidos, email, contrasenya, id_empresa, activo)
VALUES ('34567890C', 'Luis', 'Martín', 'gestor@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', 1, TRUE);

-- Administración (contraseña: Prueba123!)
INSERT INTO administracion (dni, nombre, apellidos, email, contrasenya, activo)
VALUES ('45678901D', 'Ana', 'López', 'admin@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', TRUE);