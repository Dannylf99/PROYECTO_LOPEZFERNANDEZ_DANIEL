-- ===================================================
-- CREACIÓN DE LA BASE DE DATOS
-- ===================================================
DROP DATABASE IF EXISTS gestiona;
CREATE DATABASE gestiona;
USE gestiona;

CREATE TABLE empresa (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cif VARCHAR(50) NOT NULL,
    direccion VARCHAR(200) NOT NULL
);

CREATE TABLE coordinador (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE alumno (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    empresa_asignada INT,
    tutor_centro INT,
    curso ENUM('DAW','DAM'),
    horario ENUM('MANANA','TARDE','DISTANCIA'),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_alumno_empresa FOREIGN KEY (empresa_asignada) REFERENCES empresa(id_empresa) ON DELETE SET NULL,
    CONSTRAINT fk_alumno_coordinador FOREIGN KEY (tutor_centro) REFERENCES coordinador(id_usuario) ON DELETE SET NULL
);

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

CREATE TABLE administracion (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(9) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE documento (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    fecha_subida DATE,
    estado ENUM('VALIDADO','FIRMADO','ELIMINADO') DEFAULT 'VALIDADO',
    ruta_archivo VARCHAR(200),
    CONSTRAINT fk_documento_usuario FOREIGN KEY (id_usuario) REFERENCES alumno(id_usuario) ON DELETE CASCADE
);

CREATE TABLE practica (
    id_practica INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    id_empresa INT NOT NULL,
    id_coordinador INT NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    horas_totales INT NOT NULL DEFAULT 0,
    horas_hechas INT NOT NULL DEFAULT 0,
    estado ENUM('PREPARADA','ACTIVA','PARADA','FINALIZADA','CANCELADA') NOT NULL DEFAULT 'PREPARADA',
    CONSTRAINT fk_practica_alumno FOREIGN KEY (id_alumno) REFERENCES alumno(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_practica_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    CONSTRAINT fk_practica_coordinador FOREIGN KEY (id_coordinador) REFERENCES coordinador(id_usuario) ON DELETE CASCADE
);

CREATE TABLE registro_horas (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_practica INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    pausa_inicio TIME DEFAULT NULL,
    pausa_fin TIME DEFAULT NULL,
    horas DECIMAL(4,2) NOT NULL,
    estado ENUM('PENDIENTE','VALIDADA','RECHAZADA') NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_registro_practica FOREIGN KEY (id_practica) REFERENCES practica(id_practica) ON DELETE CASCADE
);

CREATE TABLE notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    leida BOOLEAN NOT NULL DEFAULT FALSE,
    borrada BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_notificacion_alumno FOREIGN KEY (id_alumno) REFERENCES alumno(id_usuario) ON DELETE CASCADE
);

-- DATOS DE PRUEBA
INSERT INTO empresa (nombre, cif, direccion) VALUES ('EmpresaX', 'B12345678', 'Calle Falsa 123');

INSERT INTO coordinador (dni, nombre, apellidos, email, contrasenya, activo)
VALUES ('23456789B', 'Marta', 'García', 'coordinador@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', TRUE);

INSERT INTO alumno (dni, nombre, apellidos, email, contrasenya, empresa_asignada, tutor_centro, curso, horario, activo)
VALUES ('12345678A', 'Juan', 'Pérez', 'alumno@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', 1, 1, 'DAW', 'MANANA', TRUE);

INSERT INTO gestor (dni, nombre, apellidos, email, contrasenya, id_empresa, activo)
VALUES ('34567890C', 'Luis', 'Martín', 'gestor@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', 1, TRUE);

INSERT INTO administracion (dni, nombre, apellidos, email, contrasenya, activo)
VALUES ('45678901D', 'Ana', 'López', 'admin@test.com', '$2a$10$foK9xsUhmBKl8bPPPY5ZreCWV8XkFmD8pGZIk.hRK9pRrZf8EET36', TRUE);

INSERT INTO practica (id_alumno, id_empresa, id_coordinador, fecha_inicio, fecha_fin, horas_totales, horas_hechas, estado)
VALUES (1, 1, 1, '2025-09-01', '2026-06-30', 400, 0, 'ACTIVA');