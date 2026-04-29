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

CREATE TABLE documento (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_practica INT,
    tipo ENUM('CONVENIO','MEMORIA_FINAL','INFORME_SEGUIMIENTO','EVALUACION') NOT NULL DEFAULT 'CONVENIO',
    fecha_subida DATE,
    estado ENUM(
        'PENDIENTE_FIRMA_GESTOR',
        'PENDIENTE_FIRMA_COORDINADOR',
        'PENDIENTE_FIRMA_ALUMNO',
        'PENDIENTE_VALIDACION',
        'VALIDADO',
        'RECHAZADO'
    ) NOT NULL DEFAULT 'PENDIENTE_FIRMA_GESTOR',
    ruta_archivo VARCHAR(300),
    fecha_firma_gestor DATETIME,
    fecha_firma_coordinador DATETIME,
    fecha_firma_alumno DATETIME,
    fecha_validacion DATETIME,
    CONSTRAINT fk_documento_alumno FOREIGN KEY (id_usuario) REFERENCES alumno(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_documento_practica FOREIGN KEY (id_practica) REFERENCES practica(id_practica) ON DELETE CASCADE
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

-- ===================================================
-- ADMINISTRACIÓN
-- ===================================================
INSERT INTO administracion (dni, nombre, apellidos, email, contrasenya, activo) VALUES
('00000001A', 'Carmen', 'Rodríguez Soto',  'admin@gestiona.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE),
('00000002B', 'Javier', 'Moreno Castillo', 'javier.moreno@gestiona.es', '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE);

-- ===================================================
-- COORDINADORES
-- ===================================================
INSERT INTO coordinador (dni, nombre, apellidos, email, contrasenya, activo) VALUES
('11111111A', 'Mario', 'Fernández Lago',  'mario.fernandez@gestiona.es', '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE),
('11111112B', 'Lucía', 'Martínez Prieto', 'lucia.martinez@gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE),
('11111113C', 'Pedro', 'González Ramos',  'pedro.gonzalez@gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE),
('11111114D', 'Sonia', 'Álvarez Vega',    'sonia.alvarez@gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE),
('11111115E', 'Rubén', 'Díaz Iglesias',   'ruben.diaz@gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', TRUE);

-- ===================================================
-- EMPRESAS (40)
-- ===================================================
INSERT INTO empresa (nombre, cif, direccion) VALUES
('Telecomunicaciones Norte S.L.',        'B33001001', 'Calle Uría 12, Oviedo'),
('Soluciones Web Asturias S.L.',         'B33001002', 'Calle Covadonga 5, Gijón'),
('Indra Sistemas S.A.',                  'A28001003', 'Parque Tecnológico de Asturias, Llanera'),
('Enerxía Digital S.L.',                 'B33001004', 'Polígono Silvota 3, Llanera'),
('DataSoft Consulting S.L.',             'B33001005', 'Calle Doctor Casal 8, Oviedo'),
('Desarrollos Informáticos Gijón S.L.', 'B33001006', 'Calle Corrida 21, Gijón'),
('Asturcom Tecnología S.A.',             'A33001007', 'Calle Marqués de Santa Cruz 10, Oviedo'),
('CloudBase Asturias S.L.',              'B33001008', 'Parque Empresarial Espíritu Santo, Oviedo'),
('Grupo Informática del Norte S.L.',     'B33001009', 'Calle Magnus Blikstad 4, Gijón'),
('NetWork Solutions S.L.',               'B33001010', 'Calle Foncalada 7, Oviedo'),
('Sistemas Integrados del Cantábrico S.L.', 'B33001011', 'Calle San Bernardo 14, Avilés'),
('Ciberseguridad Astur S.L.',            'B33001012', 'Calle Argüelles 3, Oviedo'),
('Aplicaciones Móviles Norte S.L.',      'B33001013', 'Calle Pelayo 9, Gijón'),
('TechPyme Asturias S.L.',               'B33001014', 'Polígono Industrial Silvota, Llanera'),
('Consultora IT Cantábrica S.L.',        'B33001015', 'Calle Fruela 2, Oviedo'),
('Diseño y Desarrollo Web S.L.',         'B33001016', 'Calle Jovellanos 11, Gijón'),
('Automatización Industrial Norte S.A.', 'A33001017', 'Polígono de Somonte, Cenero'),
('ERP Solutions Asturias S.L.',          'B33001018', 'Calle Wifredo Ricart 1, Oviedo'),
('Gestión Documental Digital S.L.',      'B33001019', 'Calle Schultz 6, Oviedo'),
('Infraestructuras Cloud Norte S.L.',    'B33001020', 'Parque Tecnológico de Asturias, Llanera'),
('Multimedia Astur S.L.',                'B33001021', 'Calle San Francisco 18, Oviedo'),
('Software Factory Gijón S.L.',          'B33001022', 'Calle Cabrales 33, Gijón'),
('Analítica de Datos Norte S.L.',        'B33001023', 'Calle Álvarez Garaya 5, Oviedo'),
('Redes y Comunicaciones S.L.',          'B33001024', 'Polígono Industrial La Ferrería, Avilés'),
('Soporte Tecnológico Astur S.L.',       'B33001025', 'Calle Río San Pedro 2, Oviedo'),
('Innovación Digital Cantábrica S.L.',   'B33001026', 'Calle Caveda 7, Oviedo'),
('Proyectos Software Norte S.L.',        'B33001027', 'Calle Los Prados 12, Oviedo'),
('Desarrollos Empresariales TIC S.L.',   'B33001028', 'Calle San Antonio 4, Gijón'),
('Inteligencia Artificial Norte S.L.',   'B33001029', 'Parque Tecnológico de Asturias, Llanera'),
('Cibernética Industrial S.A.',          'A33001030', 'Polígono de Somonte, Cenero'),
('Páginas Web Asturias S.L.',            'B33001031', 'Calle Caveda 22, Oviedo'),
('Marketing Digital Norte S.L.',         'B33001032', 'Calle Uría 45, Oviedo'),
('Robótica y Automatización S.L.',       'B33001033', 'Polígono Silvota 7, Llanera'),
('Seguridad Informática Cantábrica S.L.','B33001034', 'Calle Fruela 15, Oviedo'),
('Aplicaciones de Gestión S.L.',         'B33001035', 'Calle Fuertes Acevedo 3, Oviedo'),
('Plataformas Digitales Norte S.L.',     'B33001036', 'Calle Asturias 8, Gijón'),
('Servicios Cloud Astur S.L.',           'B33001037', 'Calle Independencia 5, Oviedo'),
('Formación Tecnológica Norte S.L.',     'B33001038', 'Calle Cervantes 2, Avilés'),
('Transformación Digital S.L.',          'B33001039', 'Calle Toreno 9, Oviedo'),
('Sistemas de Información Astur S.L.',   'B33001040', 'Calle Rosal 14, Oviedo');

-- ===================================================
-- GESTORES (65)
-- ===================================================
INSERT INTO gestor (dni, nombre, apellidos, email, contrasenya, id_empresa, activo) VALUES
('22000001A','Carlos',   'López Menéndez',    'carlos.lopez@tecnorte.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 1,  TRUE),
('22000002B','Elena',    'García Suárez',     'elena.garcia@tecnorte.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 1,  TRUE),
('22000003C','Miguel',   'Fernández Cano',    'miguel.fernandez@solweb.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 2,  TRUE),
('22000004D','Patricia', 'Martínez Oliva',    'patricia.martinez@solweb.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 2,  TRUE),
('22000005E','Roberto',  'González Blanco',   'roberto.gonzalez@indra.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 3,  TRUE),
('22000006F','Nuria',    'Álvarez Pardo',     'nuria.alvarez@indra.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 3,  TRUE),
('22000007G','Sergio',   'Díaz Castañón',     'sergio.diaz@enerxia.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 4,  TRUE),
('22000008H','Laura',    'Pérez Vallina',     'laura.perez@enerxia.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 4,  TRUE),
('22000009I','Andrés',   'Rodríguez Navia',   'andres.rodriguez@datasoft.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 5,  TRUE),
('22000010J','Beatriz',  'Sánchez Tuñón',     'beatriz.sanchez@datasoft.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 5,  TRUE),
('22000011K','Daniel',   'Torres Campa',      'daniel.torres@devgij.es',        '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 6,  TRUE),
('22000012L','Marta',    'Iglesias Quirós',   'marta.iglesias@devgij.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 6,  TRUE),
('22000013M','Pablo',    'Moreno Acebal',     'pablo.moreno@asturcom.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 7,  TRUE),
('22000014N','Cristina', 'Vega Buylla',       'cristina.vega@asturcom.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 7,  TRUE),
('22000015O','Javier',   'Ramos Fidalgo',     'javier.ramos@cloudbase.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 8,  TRUE),
('22000016P','Raquel',   'Cuervo Prendes',    'raquel.cuervo@cloudbase.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 8,  TRUE),
('22000017Q','Fernando', 'Blanco Nespral',    'fernando.blanco@gitnorte.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 9,  TRUE),
('22000018R','Silvia',   'Prieto Margolles',  'silvia.prieto@gitnorte.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 9,  TRUE),
('22000019S','Óscar',    'Castillo Argüelles','oscar.castillo@network.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 10, TRUE),
('22000020T','Vanesa',   'Menéndez Faes',     'vanesa.menendez@network.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 10, TRUE),
('22000021U','Ignacio',  'Suárez Valdés',     'ignacio.suarez@sicantab.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 11, TRUE),
('22000022V','Lorena',   'Otero Laviada',     'lorena.otero@sicantab.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 11, TRUE),
('22000023W','Adrián',   'Fuentes Carballo',  'adrian.fuentes@cibersec.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 12, TRUE),
('22000024X','Natalia',  'Vidal Guardado',    'natalia.vidal@cibersec.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 12, TRUE),
('22000025Y','Guillermo','Artime Soto',       'guillermo.artime@appmovil.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 13, TRUE),
('22000026Z','Amparo',   'Méndez Corripio',   'amparo.mendez@appmovil.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 13, TRUE),
('22000027A','Héctor',   'Coto Montes',       'hector.coto@techpyme.es',        '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 14, TRUE),
('22000028B','Inés',     'Palacio Trabanco',  'ines.palacio@techpyme.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 14, TRUE),
('22000029C','Tomás',    'Braña Llanes',      'tomas.brana@consultit.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 15, TRUE),
('22000030D','Rebeca',   'Ovies Uría',        'rebeca.ovies@consultit.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 15, TRUE),
('22000031E','Álvaro',   'Junco Miyar',       'alvaro.junco@disweb.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 16, TRUE),
('22000032F','Miriam',   'Cienfuegos Pis',    'miriam.cienfuegos@disweb.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 16, TRUE),
('22000033G','Borja',    'Avello Hevia',      'borja.avello@autoindustria.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 17, TRUE),
('22000034H','Yolanda',  'Muñiz Costales',    'yolanda.muniz@autoindustria.es', '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 17, TRUE),
('22000035I','Emilio',   'Figaredo Bello',    'emilio.figaredo@erpsol.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 18, TRUE),
('22000036J','Teresa',   'Cortina Naves',     'teresa.cortina@erpsol.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 18, TRUE),
('22000037K','Iván',     'Buylla Riesgo',     'ivan.buylla@gestdoc.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 19, TRUE),
('22000038L','Alicia',   'Trabanco Muñoz',    'alicia.trabanco@gestdoc.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 19, TRUE),
('22000039M','Samuel',   'Quirós Pumares',    'samuel.quiros@infracloud.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 20, TRUE),
('22000040N','Eva',      'Prendes Dorado',    'eva.prendes@infracloud.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 20, TRUE),
('22000041O','Marcos',   'Laviada Feito',     'marcos.laviada@multimedia.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 21, TRUE),
('22000042P','Diana',    'Nava Carrocera',    'diana.nava@multimedia.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 21, TRUE),
('22000043Q','Nicolás',  'Campa Solís',       'nicolas.campa@softfactory.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 22, TRUE),
('22000044R','Claudia',  'Hevia Costales',    'claudia.hevia@softfactory.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 22, TRUE),
('22000045S','Víctor',   'Corripio Fresno',   'victor.corripio@analitica.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 23, TRUE),
('22000046T','Rocío',    'Llanes Sariego',    'rocio.llanes@analitica.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 23, TRUE),
('22000047U','Gonzalo',  'Margineda Coto',    'gonzalo.margineda@redes.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 24, TRUE),
('22000048V','Pilar',    'Bello Menéndez',    'pilar.bello@redes.es',           '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 24, TRUE),
('22000049W','Alberto',  'Solís Valdés',      'alberto.solis@soporte.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 25, TRUE),
('22000050X','Clara',    'Riesgo Francos',    'clara.riesgo@soporte.es',        '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 25, TRUE),
('22000051Y','Enrique',  'Dorado Iglesias',   'enrique.dorado@innovdig.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 26, TRUE),
('22000052Z','Paula',    'Sariego Carballo',  'paula.sariego@innovdig.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 26, TRUE),
('22000053A','Ricardo',  'Fresno Suárez',     'ricardo.fresno@proysoft.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 27, TRUE),
('22000054B','Sofía',    'Francos Cuervo',    'sofia.francos@proysoft.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 27, TRUE),
('22000055C','Jorge',    'Carballo Acebal',   'jorge.carballo@devtic.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 28, TRUE),
('22000056D','Lucía',    'Pumares Buylla',    'lucia.pumares@devtic.es',        '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 28, TRUE),
('22000057E','Rubén',    'Feito Trabanco',    'ruben.feito@ianorte.es',         '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 29, TRUE),
('22000058F','Andrea',   'Carrocera Coto',    'andrea.carrocera@ianorte.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 29, TRUE),
('22000059G','Hugo',     'Costales Prendes',  'hugo.costales@ciberind.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 30, TRUE),
('22000060H','Natalia',  'Solano Quirós',     'natalia.solano@ciberind.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 30, TRUE),
('22000061I','Alejandro','Uría Riesgo',       'alejandro.uria@paginasweb.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 31, TRUE),
('22000062J','Marina',   'Valdés Campa',      'marina.valdes@paginasweb.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 31, TRUE),
('22000063K','Iker',     'Francos Hevia',     'iker.francos@mktdigital.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 32, TRUE),
('22000064L','Noelia',   'Acebal Figaredo',   'noelia.acebal@mktdigital.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 32, TRUE),
('22000065M','Unai',     'Trabanco Buylla',   'unai.trabanco@robotica.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 33, TRUE);

-- ===================================================
-- ALUMNOS - PROMOCIÓN 2023-2025 (FINALIZADA)
-- ===================================================
INSERT INTO alumno (dni, nombre, apellidos, email, contrasenya, empresa_asignada, tutor_centro, curso, horario, activo) VALUES
('33000001A','Alejandro','Pérez Noriega',    'alejandro.perez.23@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 1,  1,'DAW','MANANA',TRUE),
('33000002B','Beatriz',  'López Camblor',    'beatriz.lopez.23@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 2,  1,'DAW','MANANA',TRUE),
('33000003C','Carlos',   'Martínez Fresno',  'carlos.martinez.23@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 3,  1,'DAW','MANANA',TRUE),
('33000004D','Diana',    'González Buylla',  'diana.gonzalez.23@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 4,  1,'DAW','MANANA',TRUE),
('33000005E','Eduardo',  'Sánchez Trabanco', 'eduardo.sanchez.23@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 5,  1,'DAW','MANANA',TRUE),
('33000006F','Fátima',   'Díaz Pumares',     'fatima.diaz.23@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 6,  1,'DAW','MANANA',TRUE),
('33000007G','Gonzalo',  'Rodríguez Prendes','gonzalo.rodriguez.23@alumnos.gestiona.es','$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 7,  1,'DAW','MANANA',TRUE),
('33000008H','Helena',   'Fernández Navia',  'helena.fernandez.23@alumnos.gestiona.es', '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 8,  1,'DAW','MANANA',TRUE),
('33000009I','Iván',     'Álvarez Llanes',   'ivan.alvarez.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 9,  2,'DAW','TARDE', TRUE),
('33000010J','Julia',    'Moreno Sariego',   'julia.moreno.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 10, 2,'DAW','TARDE', TRUE),
('33000011K','Kevin',    'Torres Carballo',  'kevin.torres.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 11, 2,'DAW','TARDE', TRUE),
('33000012L','Laura',    'Ramos Acebal',     'laura.ramos.23@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 12, 2,'DAW','TARDE', TRUE),
('33000013M','Manuel',   'Prieto Feito',     'manuel.prieto.23@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 13, 2,'DAW','TARDE', TRUE),
('33000014N','Nuria',    'Cano Carrocera',   'nuria.cano.23@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 14, 2,'DAW','TARDE', TRUE),
('33000015O','Óliver',   'Vega Solano',      'oliver.vega.23@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 15, 2,'DAW','TARDE', TRUE),
('33000016P','Paula',    'Castillo Uría',    'paula.castillo.23@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 16, 2,'DAW','TARDE', TRUE),
('33000017Q','Quique',   'Blanco Margineda', 'quique.blanco.23@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 17, 3,'DAM','MANANA',TRUE),
('33000018R','Rosa',     'Iglesias Coto',    'rosa.iglesias.23@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 18, 3,'DAM','MANANA',TRUE),
('33000019S','Sergio',   'Fuentes Laviada',  'sergio.fuentes.23@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 19, 3,'DAM','MANANA',TRUE),
('33000020T','Teresa',   'Otero Dorado',     'teresa.otero.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 20, 3,'DAM','MANANA',TRUE),
('33000021U','Uxía',     'Suárez Francos',   'uxia.suarez.23@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 21, 3,'DAM','MANANA',TRUE),
('33000022V','Víctor',   'Menéndez Riesgo',  'victor.menendez.23@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 22, 3,'DAM','MANANA',TRUE),
('33000023W','Wenceslao','Cuervo Campa',     'wenceslao.cuervo.23@alumnos.gestiona.es', '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 23, 3,'DAM','MANANA',TRUE),
('33000024X','Xenia',    'Buylla Hevia',     'xenia.buylla.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 24, 3,'DAM','MANANA',TRUE),
('33000025Y','Yago',     'Trabanco Valdés',  'yago.trabanco.23@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 25, 4,'DAM','TARDE', TRUE),
('33000026Z','Zaira',    'Prendes Costales', 'zaira.prendes.23@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 26, 4,'DAM','TARDE', TRUE),
('33000027A','Adrián',   'Solís Quirós',     'adrian.solis.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 27, 4,'DAM','TARDE', TRUE),
('33000028B','Blanca',   'Figaredo Acebal',  'blanca.figaredo.23@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 28, 4,'DAM','TARDE', TRUE),
('33000029C','César',    'Nava Pumares',     'cesar.nava.23@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 29, 4,'DAM','TARDE', TRUE),
('33000030D','Delia',    'Carrocera Fresno', 'delia.carrocera.23@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 30, 4,'DAM','TARDE', TRUE),
('33000031E','Emilio',   'Hevia Sariego',    'emilio.hevia.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 31, 4,'DAM','TARDE', TRUE),
('33000032F','Fiorella', 'Campa Trabanco',   'fiorella.campa.23@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 32, 4,'DAM','TARDE', TRUE),
('33000033G','Gonzalo',  'Acebal Prendes',   'gonzalo.acebal.23@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 33, 5,'DAW','DISTANCIA',TRUE),
('33000034H','Hilda',    'Riesgo Feito',     'hilda.riesgo.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 34, 5,'DAM','DISTANCIA',TRUE),
('33000035I','Ignacio',  'Dorado Carballo',  'ignacio.dorado.23@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 35, 5,'DAW','DISTANCIA',TRUE),
('33000036J','Jana',     'Francos Laviada',  'jana.francos.23@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 36, 5,'DAM','DISTANCIA',TRUE);

-- ===================================================
-- ALUMNOS - PROMOCIÓN 2024-2026 (ACTIVA)
-- ===================================================
INSERT INTO alumno (dni, nombre, apellidos, email, contrasenya, empresa_asignada, tutor_centro, curso, horario, activo) VALUES
('44000001A','Aaron',    'Menéndez Bobes',   'aaron.menendez.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 1,  1,'DAW','MANANA',TRUE),
('44000002B','Brais',    'López Inclán',     'brais.lopez.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 2,  1,'DAW','MANANA',TRUE),
('44000003C','Candela',  'Martínez Moro',    'candela.martinez.24@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 3,  1,'DAW','MANANA',TRUE),
('44000004D','David',    'González Cañal',   'david.gonzalez.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 4,  1,'DAW','MANANA',TRUE),
('44000005E','Elena',    'Sánchez Nicieza',  'elena.sanchez.24@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 5,  1,'DAW','MANANA',TRUE),
('44000006F','Felipe',   'Díaz Palacio',     'felipe.diaz.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 6,  1,'DAW','MANANA',TRUE),
('44000007G','Gara',     'Rodríguez Tuero',  'gara.rodriguez.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 7,  1,'DAW','MANANA',TRUE),
('44000008H','Hugo',     'Fernández Meana',  'hugo.fernandez.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 8,  1,'DAW','MANANA',TRUE),
('44000009I','Irene',    'Álvarez Bango',    'irene.alvarez.24@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 9,  2,'DAW','TARDE', TRUE),
('44000010J','Jaime',    'Moreno Requejo',   'jaime.moreno.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 10, 2,'DAW','TARDE', TRUE),
('44000011K','Katia',    'Torres Berdasco',  'katia.torres.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 11, 2,'DAW','TARDE', TRUE),
('44000012L','Lucas',    'Ramos Hevia',      'lucas.ramos.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 12, 2,'DAW','TARDE', TRUE),
('44000013M','María',    'Prieto Camblor',   'maria.prieto.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 13, 2,'DAW','TARDE', TRUE),
('44000014N','Nicolás',  'Cano Bello',       'nicolas.cano.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 14, 2,'DAW','TARDE', TRUE),
('44000015O','Olivia',   'Vega Secades',     'olivia.vega.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 15, 2,'DAW','TARDE', TRUE),
('44000016P','Pablo',    'Castillo Ardines', 'pablo.castillo.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 16, 2,'DAW','TARDE', TRUE),
('44000017Q','Quim',     'Blanco Cienfuegos','quim.blanco.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 17, 3,'DAM','MANANA',TRUE),
('44000018R','Rebeca',   'Iglesias Nespral', 'rebeca.iglesias.24@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 18, 3,'DAM','MANANA',TRUE),
('44000019S','Samuel',   'Fuentes Avello',   'samuel.fuentes.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 19, 3,'DAM','MANANA',TRUE),
('44000020T','Tania',    'Otero Junco',      'tania.otero.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 20, 3,'DAM','MANANA',TRUE),
('44000021U','Unai',     'Suárez Cañal',     'unai.suarez.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 21, 3,'DAM','MANANA',TRUE),
('44000022V','Valentina','Menéndez Tuero',   'valentina.menendez.24@alumnos.gestiona.es','$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 22, 3,'DAM','MANANA',TRUE),
('44000023W','Marcos',   'Cuervo Moro',      'marcos.cuervo.24@alumnos.gestiona.es',     '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 23, 3,'DAM','MANANA',TRUE),
('44000024X','Xiana',    'Buylla Inclán',    'xiana.buylla.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 24, 3,'DAM','MANANA',TRUE),
('44000025Y','Yeray',    'Trabanco Ardines', 'yeray.trabanco.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 25, 4,'DAM','TARDE', TRUE),
('44000026Z','Yolanda',  'Prendes Bango',    'yolanda.prendes.24@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 26, 4,'DAM','TARDE', TRUE),
('44000027A','Alán',     'Solís Berdasco',   'alan.solis.24@alumnos.gestiona.es',        '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 27, 4,'DAM','TARDE', TRUE),
('44000028B','Bárbara',  'Figaredo Secades', 'barbara.figaredo.24@alumnos.gestiona.es',  '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 28, 4,'DAM','TARDE', TRUE),
('44000029C','Carlota',  'Nava Requejo',     'carlota.nava.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 29, 4,'DAM','TARDE', TRUE),
('44000030D','Diego',    'Carrocera Bobes',  'diego.carrocera.24@alumnos.gestiona.es',   '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 30, 4,'DAM','TARDE', TRUE),
('44000031E','Elsa',     'Hevia Camblor',    'elsa.hevia.24@alumnos.gestiona.es',        '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 31, 4,'DAM','TARDE', TRUE),
('44000032F','Fabio',    'Campa Meana',      'fabio.campa.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 32, 4,'DAM','TARDE', TRUE),
('44000033G','Gorka',    'Acebal Nicieza',   'gorka.acebal.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 33, 5,'DAW','DISTANCIA',TRUE),
('44000034H','Harriet',  'Riesgo Palacio',   'harriet.riesgo.24@alumnos.gestiona.es',    '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 34, 5,'DAM','DISTANCIA',TRUE),
('44000035I','Iker',     'Dorado Bello',     'iker.dorado.24@alumnos.gestiona.es',       '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 35, 5,'DAW','DISTANCIA',TRUE),
('44000036J','Jana',     'Francos Nespral',  'jana.francos.24@alumnos.gestiona.es',      '$2a$10$yiKHWObTuLpzIc3KxPnrB.Pl2pJt84p/GPR8h0vLxt2daC1nKxob2', 36, 5,'DAM','DISTANCIA',TRUE);

-- ===================================================
-- PRÁCTICAS - PROMOCIÓN 2023-2025 (FINALIZADAS)
-- ===================================================
INSERT INTO practica (id_alumno, id_empresa, id_coordinador, fecha_inicio, fecha_fin, horas_totales, horas_hechas, estado) VALUES
(1,  1,  1,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(2,  2,  1,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(3,  3,  1,'2024-09-02','2025-05-30',400,392,'FINALIZADA'),
(4,  4,  1,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(5,  5,  1,'2024-09-02','2025-05-30',400,380,'FINALIZADA'),
(6,  6,  1,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(7,  7,  1,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(8,  8,  1,'2024-09-02','2025-05-30',400,396,'FINALIZADA'),
(9,  9,  2,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(10,10,  2,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(11,11,  2,'2024-09-02','2025-05-30',400,388,'FINALIZADA'),
(12,12,  2,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(13,13,  2,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(14,14,  2,'2024-09-02','2025-05-30',400,376,'FINALIZADA'),
(15,15,  2,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(16,16,  2,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(17,17,  3,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(18,18,  3,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(19,19,  3,'2024-09-02','2025-05-30',400,384,'FINALIZADA'),
(20,20,  3,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(21,21,  3,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(22,22,  3,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(23,23,  3,'2024-09-02','2025-05-30',400,372,'FINALIZADA'),
(24,24,  3,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(25,25,  4,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(26,26,  4,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(27,27,  4,'2024-09-02','2025-05-30',400,396,'FINALIZADA'),
(28,28,  4,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(29,29,  4,'2024-09-02','2025-05-30',400,380,'FINALIZADA'),
(30,30,  4,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(31,31,  4,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(32,32,  4,'2024-09-02','2025-05-30',400,388,'FINALIZADA'),
(33,33,  5,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(34,34,  5,'2024-09-02','2025-05-30',400,392,'FINALIZADA'),
(35,35,  5,'2024-09-02','2025-05-30',400,400,'FINALIZADA'),
(36,36,  5,'2024-09-02','2025-05-30',400,400,'FINALIZADA');

-- ===================================================
-- PRÁCTICAS - PROMOCIÓN 2024-2026 (ACTIVAS)
-- ===================================================
INSERT INTO practica (id_alumno, id_empresa, id_coordinador, fecha_inicio, fecha_fin, horas_totales, horas_hechas, estado) VALUES
(37,1, 1,'2025-09-01','2026-05-29',400,210,'ACTIVA'),
(38,2, 1,'2025-09-01','2026-05-29',400,195,'ACTIVA'),
(39,3, 1,'2025-09-01','2026-05-29',400,220,'ACTIVA'),
(40,4, 1,'2025-09-01','2026-05-29',400,205,'ACTIVA'),
(41,5, 1,'2025-09-01','2026-05-29',400,230,'ACTIVA'),
(42,6, 1,'2025-09-01','2026-05-29',400,188,'ACTIVA'),
(43,7, 1,'2025-09-01','2026-05-29',400,215,'ACTIVA'),
(44,8, 1,'2025-09-01','2026-05-29',400,200,'ACTIVA'),
(45,9, 2,'2025-09-01','2026-05-29',400,225,'ACTIVA'),
(46,10,2,'2025-09-01','2026-05-29',400,210,'ACTIVA'),
(47,11,2,'2025-09-01','2026-05-29',400,198,'ACTIVA'),
(48,12,2,'2025-09-01','2026-05-29',400,235,'ACTIVA'),
(49,13,2,'2025-09-01','2026-05-29',400,190,'ACTIVA'),
(50,14,2,'2025-09-01','2026-05-29',400,218,'ACTIVA'),
(51,15,2,'2025-09-01','2026-05-29',400,205,'ACTIVA'),
(52,16,2,'2025-09-01','2026-05-29',400,222,'ACTIVA'),
(53,17,3,'2025-09-01','2026-05-29',400,212,'ACTIVA'),
(54,18,3,'2025-09-01','2026-05-29',400,195,'ACTIVA'),
(55,19,3,'2025-09-01','2026-05-29',400,228,'ACTIVA'),
(56,20,3,'2025-09-01','2026-05-29',400,208,'ACTIVA'),
(57,21,3,'2025-09-01','2026-05-29',400,192,'ACTIVA'),
(58,22,3,'2025-09-01','2026-05-29',400,235,'ACTIVA'),
(59,23,3,'2025-09-01','2026-05-29',400,201,'ACTIVA'),
(60,24,3,'2025-09-01','2026-05-29',400,218,'ACTIVA'),
(61,25,4,'2025-09-01','2026-05-29',400,215,'ACTIVA'),
(62,26,4,'2025-09-01','2026-05-29',400,200,'ACTIVA'),
(63,27,4,'2025-09-01','2026-05-29',400,188,'ACTIVA'),
(64,28,4,'2025-09-01','2026-05-29',400,225,'ACTIVA'),
(65,29,4,'2025-09-01','2026-05-29',400,210,'ACTIVA'),
(66,30,4,'2025-09-01','2026-05-29',400,195,'ACTIVA'),
(67,31,4,'2025-09-01','2026-05-29',400,230,'ACTIVA'),
(68,32,4,'2025-09-01','2026-05-29',400,205,'ACTIVA'),
(69,33,5,'2025-09-01','2026-05-29',400,198,'ACTIVA'),
(70,34,5,'2025-09-01','2026-05-29',400,212,'ACTIVA'),
(71,35,5,'2025-09-01','2026-05-29',400,220,'ACTIVA'),
(72,36,5,'2025-09-01','2026-05-29',400,188,'ACTIVA');

-- ===================================================
-- REGISTROS DE HORAS
-- ===================================================
INSERT INTO registro_horas (id_practica, fecha, hora_inicio, hora_fin, horas, estado) VALUES
(37,'2025-09-03','08:00:00','16:00:00',8.00,'VALIDADA'),
(37,'2025-09-04','08:00:00','16:00:00',8.00,'VALIDADA'),
(37,'2025-09-05','08:00:00','16:00:00',8.00,'VALIDADA'),
(37,'2025-09-08','08:00:00','16:00:00',8.00,'VALIDADA'),
(37,'2025-09-09','08:00:00','16:00:00',8.00,'VALIDADA'),
(38,'2025-09-03','08:00:00','16:00:00',8.00,'VALIDADA'),
(38,'2025-09-04','08:00:00','16:00:00',8.00,'VALIDADA'),
(38,'2025-09-05','08:00:00','16:00:00',8.00,'VALIDADA'),
(39,'2025-09-03','08:00:00','16:00:00',8.00,'VALIDADA'),
(39,'2025-09-04','08:00:00','16:00:00',8.00,'VALIDADA'),
(39,'2025-09-05','08:00:00','16:00:00',8.00,'VALIDADA'),
(39,'2025-09-08','08:00:00','16:00:00',8.00,'VALIDADA'),
(40,'2025-09-03','15:00:00','23:00:00',8.00,'VALIDADA'),
(40,'2025-09-04','15:00:00','23:00:00',8.00,'VALIDADA'),
(40,'2025-09-05','15:00:00','23:00:00',8.00,'VALIDADA'),
(41,'2025-09-03','08:00:00','16:00:00',8.00,'VALIDADA'),
(41,'2025-09-04','08:00:00','16:00:00',8.00,'VALIDADA'),
(41,'2025-09-05','08:00:00','16:00:00',8.00,'PENDIENTE'),
(42,'2025-09-03','08:00:00','16:00:00',8.00,'VALIDADA'),
(42,'2025-09-04','08:00:00','16:00:00',8.00,'RECHAZADA'),
(43,'2025-09-03','08:00:00','16:00:00',8.00,'VALIDADA'),
(43,'2025-09-04','08:00:00','16:00:00',8.00,'VALIDADA'),
(44,'2025-09-03','08:00:00','16:00:00',8.00,'PENDIENTE'),
(44,'2025-09-04','08:00:00','16:00:00',8.00,'PENDIENTE');

-- ===================================================
-- DOCUMENTOS - todos con ruta_archivo = documento_prueba.pdf
-- Coloca ese fichero en uploads/documentos/
-- ===================================================

-- Promoción 2023-2025 (todo validado)
INSERT INTO documento (id_usuario, id_practica, tipo, fecha_subida, estado, ruta_archivo, fecha_firma_gestor, fecha_firma_coordinador, fecha_firma_alumno, fecha_validacion) VALUES
(1, 1, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 10:00:00','2024-09-05 11:00:00','2024-09-06 09:00:00','2024-09-07 12:00:00'),
(2, 2, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 10:30:00','2024-09-05 11:30:00','2024-09-06 09:30:00','2024-09-07 12:30:00'),
(3, 3, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 11:00:00','2024-09-05 12:00:00','2024-09-06 10:00:00','2024-09-07 13:00:00'),
(4, 4, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 11:30:00','2024-09-05 12:30:00','2024-09-06 10:30:00','2024-09-07 13:30:00'),
(5, 5, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 12:00:00','2024-09-05 13:00:00','2024-09-06 11:00:00','2024-09-07 14:00:00'),
(6, 6, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 12:30:00','2024-09-05 13:30:00','2024-09-06 11:30:00','2024-09-07 14:30:00'),
(7, 7, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 13:00:00','2024-09-05 14:00:00','2024-09-06 12:00:00','2024-09-07 15:00:00'),
(8, 8, 'CONVENIO',           '2024-09-03','VALIDADO','documento_prueba.pdf','2024-09-04 13:30:00','2024-09-05 14:30:00','2024-09-06 12:30:00','2024-09-07 15:30:00'),
(1, 1, 'INFORME_SEGUIMIENTO','2024-12-10','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2024-12-15 10:00:00'),
(2, 2, 'INFORME_SEGUIMIENTO','2024-12-10','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2024-12-15 10:30:00'),
(3, 3, 'INFORME_SEGUIMIENTO','2024-12-11','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2024-12-16 10:00:00'),
(1, 1, 'MEMORIA_FINAL',      '2025-05-20','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2025-05-25 10:00:00'),
(2, 2, 'MEMORIA_FINAL',      '2025-05-20','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2025-05-25 10:30:00'),
(3, 3, 'MEMORIA_FINAL',      '2025-05-21','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2025-05-26 10:00:00'),
(1, 1, 'EVALUACION',         '2025-05-28','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2025-05-30 10:00:00'),
(2, 2, 'EVALUACION',         '2025-05-28','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2025-05-30 10:30:00'),
(3, 3, 'EVALUACION',         '2025-05-29','VALIDADO','documento_prueba.pdf',NULL,NULL,NULL,'2025-05-30 11:00:00');

-- Promoción 2024-2026 (distintos estados)
INSERT INTO documento (id_usuario, id_practica, tipo, fecha_subida, estado, ruta_archivo, fecha_firma_gestor, fecha_firma_coordinador, fecha_firma_alumno, fecha_validacion) VALUES
-- Convenios validados
(37,37,'CONVENIO','2025-09-02','VALIDADO',                   'documento_prueba.pdf','2025-09-03 10:00:00','2025-09-04 10:00:00','2025-09-05 10:00:00','2025-09-06 10:00:00'),
(43,43,'CONVENIO','2025-09-02','VALIDADO',                   'documento_prueba.pdf','2025-09-03 09:00:00','2025-09-04 09:00:00','2025-09-05 09:00:00','2025-09-06 09:00:00'),
(44,44,'CONVENIO','2025-09-02','VALIDADO',                   'documento_prueba.pdf','2025-09-03 11:00:00','2025-09-04 11:00:00','2025-09-05 11:00:00','2025-09-06 11:00:00'),
-- Convenios pendientes de validación
(38,38,'CONVENIO','2025-09-02','PENDIENTE_VALIDACION',       'documento_prueba.pdf','2025-09-03 10:00:00','2025-09-04 10:00:00','2025-09-05 10:00:00',NULL),
(45,45,'CONVENIO','2025-09-02','PENDIENTE_VALIDACION',       'documento_prueba.pdf','2025-09-03 12:00:00','2025-09-04 12:00:00','2025-09-05 12:00:00',NULL),
-- Convenios pendientes firma alumno
(39,39,'CONVENIO','2025-09-02','PENDIENTE_FIRMA_ALUMNO',     'documento_prueba.pdf','2025-09-03 10:00:00','2025-09-04 10:00:00',NULL,NULL),
(47,47,'CONVENIO','2025-09-02','PENDIENTE_FIRMA_ALUMNO',     'documento_prueba.pdf','2025-09-03 14:00:00','2025-09-04 14:00:00',NULL,NULL),
-- Convenios pendientes firma coordinador
(40,40,'CONVENIO','2025-09-02','PENDIENTE_FIRMA_COORDINADOR','documento_prueba.pdf','2025-09-03 10:00:00',NULL,NULL,NULL),
(46,46,'CONVENIO','2025-09-02','PENDIENTE_FIRMA_COORDINADOR','documento_prueba.pdf','2025-09-03 13:00:00',NULL,NULL,NULL),
-- Convenio pendiente firma gestor
(41,41,'CONVENIO','2025-09-02','PENDIENTE_FIRMA_GESTOR',     'documento_prueba.pdf',NULL,NULL,NULL,NULL),
-- Convenio rechazado
(42,42,'CONVENIO','2025-09-02','RECHAZADO',                  'documento_prueba.pdf',NULL,NULL,NULL,NULL),
-- Informes de seguimiento
(37,37,'INFORME_SEGUIMIENTO','2026-01-15','VALIDADO',            'documento_prueba.pdf',NULL,NULL,NULL,'2026-01-20 10:00:00'),
(38,38,'INFORME_SEGUIMIENTO','2026-01-15','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL),
(39,39,'INFORME_SEGUIMIENTO','2026-01-16','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL),
(43,43,'INFORME_SEGUIMIENTO','2026-01-16','VALIDADO',            'documento_prueba.pdf',NULL,NULL,NULL,'2026-01-21 10:00:00'),
(45,45,'INFORME_SEGUIMIENTO','2026-02-10','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL),
-- Memorias finales
(37,37,'MEMORIA_FINAL','2026-04-10','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL),
(43,43,'MEMORIA_FINAL','2026-04-11','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL),
(45,45,'MEMORIA_FINAL','2026-04-12','VALIDADO',            'documento_prueba.pdf',NULL,NULL,NULL,'2026-04-15 10:00:00'),
-- Evaluaciones
(37,37,'EVALUACION','2026-04-18','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL),
(43,43,'EVALUACION','2026-04-18','PENDIENTE_VALIDACION','documento_prueba.pdf',NULL,NULL,NULL,NULL);

-- ===================================================
-- NOTIFICACIONES
-- ===================================================
INSERT INTO notificacion (id_alumno, mensaje, fecha, leida, borrada) VALUES
(37,'¡Tus prácticas en Telecomunicaciones Norte S.L. han comenzado! Ya puedes empezar a registrar tus horas.','2025-09-01',TRUE, FALSE),
(38,'¡Tus prácticas en Soluciones Web Asturias S.L. han comenzado! Ya puedes empezar a registrar tus horas.', '2025-09-01',TRUE, FALSE),
(39,'¡Tus prácticas en Indra Sistemas S.A. han comenzado! Ya puedes empezar a registrar tus horas.',          '2025-09-01',FALSE,FALSE),
(40,'¡Tus prácticas en Enerxía Digital S.L. han comenzado! Ya puedes empezar a registrar tus horas.',         '2025-09-01',FALSE,FALSE),
(41,'Has subido correctamente el documento: CONVENIO.',                                                        '2025-09-02',TRUE, FALSE),
(42,'Tu documento CONVENIO ha sido rechazado. Contacta con tu coordinador.',                                   '2025-09-03',FALSE,FALSE),
(37,'El gestor de tu empresa ha firmado tu convenio. Ahora está pendiente de firma del coordinador.',          '2025-09-03',TRUE, FALSE),
(37,'Tu coordinador ha firmado tu convenio. Ahora necesitas firmarlo tú para enviarlo a validación.',          '2025-09-04',TRUE, FALSE),
(37,'Tu documento CONVENIO ha sido validado por la administración.',                                           '2025-09-06',TRUE, FALSE),
(38,'Tu documento CONVENIO ha sido validado por la administración.',                                           '2025-09-06',FALSE,FALSE),
(37,'Se ha subido un nuevo documento de tipo INFORME SEGUIMIENTO a tu expediente.',                            '2026-01-15',TRUE, FALSE),
(38,'Se ha subido un nuevo documento de tipo INFORME SEGUIMIENTO a tu expediente.',                            '2026-01-15',FALSE,FALSE),
(42,'Tu registro de horas del 04/09/2025 ha sido rechazado. Revisa los datos.',                                '2025-09-05',FALSE,FALSE),
(44,'¡Tus prácticas en CloudBase Asturias S.L. han comenzado! Ya puedes empezar a registrar tus horas.',     '2025-09-01',FALSE,FALSE);

-- Convenio pendiente de firma del gestor para Aaron Menéndez (práctica 37, empresa 1)
INSERT INTO documento (id_usuario, id_practica, tipo, fecha_subida, estado, ruta_archivo)
VALUES (37, 37, 'CONVENIO', '2026-04-29', 'PENDIENTE_FIRMA_GESTOR', 'documento_prueba.pdf');