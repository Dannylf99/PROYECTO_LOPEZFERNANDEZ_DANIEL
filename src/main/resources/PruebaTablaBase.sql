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
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(100) NOT NULL,
    empresa_asignada INT,
    tutor_centro INT,
    CONSTRAINT fk_alumno_empresa FOREIGN KEY (empresa_asignada) REFERENCES empresa(id_empresa) ON DELETE SET NULL
);

-- ===================================================
-- TABLA COORDINADORES
-- ===================================================
CREATE TABLE coordinador (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(100) NOT NULL
);

-- ===================================================
-- TABLA GESTORES
-- ===================================================
CREATE TABLE gestor (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(100) NOT NULL,
    id_empresa INT,
    CONSTRAINT fk_gestor_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE SET NULL
);

-- ===================================================
-- TABLA ADMINISTRACIÓN
-- ===================================================
CREATE TABLE administracion (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasenya VARCHAR(100) NOT NULL
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
-- DATOS BÁSICOS PARA PROBAR LOGIN
-- ===================================================

-- Empresa
INSERT INTO empresa (nombre, cif, direccion)
VALUES ('EmpresaX', 'B12345678', 'Calle Falsa 123');

-- Alumno
INSERT INTO alumno (nombre, apellidos, email, contrasenya, empresa_asignada, tutor_centro)
VALUES ('Juan', 'Pérez', 'alumno@test.com', '1234', 1, NULL);

-- Coordinador
INSERT INTO coordinador (nombre, apellidos, email, contrasenya)
VALUES ('Marta', 'García', 'coordinador@test.com', '1234');

-- Gestor
INSERT INTO gestor (nombre, apellidos, email, contrasenya, id_empresa)
VALUES ('Luis', 'Martín', 'gestor@test.com', '1234', 1);

-- Administración
INSERT INTO administracion (nombre, apellidos, email, contrasenya)
VALUES ('Ana', 'López', 'admin@test.com', '1234');

-- Documento
INSERT INTO documento (id_usuario, tipo, fecha_subida, estado, ruta_archivo)
VALUES (1, 'Informe', CURDATE(), 'VALIDADO', '/docs/informe1.pdf');

-- Práctica
INSERT INTO practica (id_alumno, id_empresa, fecha_inicio, fecha_fin, horas_totales)
VALUES (1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 120);

-- Notificación
INSERT INTO notificacion (mensaje, fecha, leida)
VALUES ('Bienvenido al sistema', CURDATE(), FALSE);
