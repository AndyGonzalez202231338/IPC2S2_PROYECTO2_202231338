-- ----------------------------------------
--  DCL
-- ----------------------------------------

-- creacion base de datos
CREATE DATABASE IF NOT EXISTS sistema_cine;

-- cracion usuario para la base de datos
CREATE USER 'usuarioCine'@'localhost' IDENTIFIED BY 'cine123';

GRANT ALL PRIVILEGES ON sistema_cine.* TO 'usuarioCine'@'localhost';

FLUSH PRIVILEGES;

-- ----------------------------------------
--  DDL
-- ----------------------------------------

-- uso de base de datos
use sistema_cine;

-- creacion tablas
CREATE TABLE IF NOT EXISTS rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE IF NOT EXISTS cartera_digital (
    id_cartera INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,
    saldo DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cine (
    id_cine INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario_admin INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    direccion TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('ACTIVO', 'INACTIVO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_usuario_admin) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS cartera_cine (
    id_cartera_cine INT AUTO_INCREMENT PRIMARY KEY,
    id_cine INT NOT NULL UNIQUE,
    saldo DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_cine) REFERENCES cine(id_cine) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS costos_cine (
    id_costo INT AUTO_INCREMENT PRIMARY KEY,
    id_cine INT NOT NULL,
    costo_diario DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NULL,
    FOREIGN KEY (id_cine) REFERENCES cine(id_cine) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sala (
    id_sala INT AUTO_INCREMENT PRIMARY KEY,
    id_cine INT NOT NULL,
    nombre_sala VARCHAR(100) NOT NULL,
    filas INT NOT NULL,
    columnas INT NOT NULL,
    permite_comentarios BOOLEAN DEFAULT TRUE,
    estado ENUM('ACTIVA', 'BLOQUEADA') DEFAULT 'ACTIVA',
    FOREIGN KEY (id_cine) REFERENCES cine(id_cine) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categoria_pelicula (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS pelicula (
    id_pelicula INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    sinopsis TEXT,
    duracion_minutos INT NOT NULL,
    director VARCHAR(255),
    reparto TEXT,
    clasificacion ENUM('AA', 'A', 'B12', 'B15', 'C', 'D') NOT NULL,
    fecha_estreno DATE,
    poster_url VARCHAR(500),
    estado ENUM('ACTIVA', 'INACTIVA') DEFAULT 'ACTIVA'
);

CREATE TABLE IF NOT EXISTS pelicula_categorias (
    id_pelicula INT NOT NULL,
    id_categoria INT NOT NULL,
    PRIMARY KEY (id_pelicula, id_categoria),
    FOREIGN KEY (id_pelicula) REFERENCES pelicula(id_pelicula) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categoria_pelicula(id_categoria) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS funcion (
    id_funcion INT AUTO_INCREMENT PRIMARY KEY,
    id_sala INT NOT NULL,
    id_pelicula INT NOT NULL,
    fecha_hora_funcion DATETIME NOT NULL,
    precio_boleto_adulto DECIMAL(8,2) NOT NULL,
    precio_boleto_nino DECIMAL(8,2),
    asientos_disponibles INT NOT NULL,
    estado ENUM('PROGRAMADA', 'CANCELADA', 'COMPLETADA') DEFAULT 'PROGRAMADA',
    FOREIGN KEY (id_sala) REFERENCES sala(id_sala) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES pelicula(id_pelicula) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS boleto (
    id_boleto INT AUTO_INCREMENT PRIMARY KEY,
    id_funcion INT NOT NULL,
    id_usuario INT NOT NULL,
    codigo_boleto VARCHAR(50) UNIQUE NOT NULL,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    precio_pagado DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (id_funcion) REFERENCES funcion(id_funcion) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tipos_anuncio (
    id_tipo_anuncio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT,
    precio_anuncio DECIMAL(8,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS periodos_anuncio (
    id_periodo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    dias_duracion INT NOT NULL,
    precio_duracion DECIMAL(8,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS anuncio (
    id_anuncio INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_tipo_anuncio INT NOT NULL,
    id_periodo INT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    contenido_texto TEXT,
    imagen_url VARCHAR(500),
    video_url VARCHAR(500),
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    costo_total DECIMAL(8,2) NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO', 'VENCIDO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_tipo_anuncio) REFERENCES tipos_anuncio(id_tipo_anuncio) ON DELETE CASCADE,
    FOREIGN KEY (id_periodo) REFERENCES periodos_anuncio(id_periodo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS publicidad (
    id_publicidad INT AUTO_INCREMENT PRIMARY KEY,
    id_anuncio INT NOT NULL,
    id_usuario INT NOT NULL,
    precio_bloqueo DECIMAL(8,2) NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO', 'VENCIDO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_anuncio) REFERENCES anuncio(id_anuncio) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bloqueo_publicidad (
    id_bloqueo_publicidad INT AUTO_INCREMENT PRIMARY KEY,
    id_cine INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    costo_total DECIMAL(8,2) NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cine) REFERENCES cine(id_cine) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comentario_pelicula (
    id_comentario_pelicula INT AUTO_INCREMENT PRIMARY KEY,
    id_pelicula INT NOT NULL,
    id_usuario INT NOT NULL,
    comentario TEXT NOT NULL,
    calificacion INT CHECK (calificacion >= 1 AND calificacion <= 5),
    fecha_comentario DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('ACTIVO', 'OCULTO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_pelicula) REFERENCES pelicula(id_pelicula) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comentario_sala (
    id_comentario_sala INT AUTO_INCREMENT PRIMARY KEY,
    id_sala INT NOT NULL,
    id_usuario INT NOT NULL,
    comentario TEXT NOT NULL,
    calificacion INT CHECK (calificacion >= 1 AND calificacion <= 5),
    fecha_comentario DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('ACTIVO', 'OCULTO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_sala) REFERENCES sala(id_sala) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- ----------------------------------------
--  DML
-- ----------------------------------------

-- inserts roles
INSERT INTO rol (nombre_rol, descripcion) VALUES 
('ADMINISTRADOR DE SISTEMA', 'Usuario encargado de cines, peliculas y reportes de ganancias del sistema en anuncios. Gestiona las cuentas de los usuarios de la web'),
('ADMINISTRADOR DE CINE', 'Ususario encargaddo de un cine, administracion de salas, funciones, bloqueo de anuncios y reportes de ganancias generadas por compra de boletos en funciones'),
('ANUNCIANTE', 'Usuario con capacidad para crear anuncios y bloquear anuncios en la web'),
('COMUN', 'Usuario del sistema que compra boletos a funciones en diferentes cines de la web');

-- Insertar usuario, el usuario debe ser borrado luego de crear un usuario real como administrador
INSERT INTO usuario (id_rol, email, password, nombre_completo, estado)
VALUES (
    1,
    'admin@cineapp.com',
    'admin123',
    'Administrador Sistema',
    'ACTIVO'
);

-- Insertar tipos de anuncio base
INSERT INTO tipos_anuncio (nombre, descripcion, precio_anuncio) VALUES 
('TEXTO', 'Anuncio de solo texto', 200.00),
('TEXTO E IMAGEN', 'Anuncio de texto con imagen', 400.00),
('TEXTO Y VIDEO', 'Anuncio de video con texto', 600.00);

-- Insertar periodos de anuncio base
INSERT INTO periodos_anuncio (nombre, dias_duracion, precio_duracion) VALUES 
('1_DIA', 1, 50.00),
('3_DIAS', 3, 150.00),
('1_SEMANA', 7, 250.00),
('2_SEMANAS', 14, 500.00);

-- Insertar categorías de películas comunes
INSERT INTO categoria_pelicula (nombre_categoria, descripcion) VALUES 
('Acción', 'Películas de alta acción y aventura'),
('Drama', 'Películas dramáticas y emocionales'),
('Comedia', 'Películas cómicas y divertidas'),
('Ciencia Ficción', 'Películas de ciencia ficción y futuristas'),
('Terror', 'Películas de terror y suspenso'),
('Animación', 'Películas animadas y para toda la familia'),
('Romance', 'Películas románticas'),
('Anime', 'Películas de animación japonesa');

