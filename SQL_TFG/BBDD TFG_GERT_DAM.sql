CREATE DATABASE TFG_GERT_DAM;

USE TFG_GERT_DAM;

CREATE TABLE TipoLibro (
	id INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Genero (
	id INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Autor (
	id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Editorial (
	id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Libro (
	id INT NOT NULL AUTO_INCREMENT,
    isbn VARCHAR(20) UNIQUE,
    portada VARCHAR(255),
    titulo VARCHAR(255) NOT NULL,
    editorial_id INT NOT NULL,
    autor_id INT NOT NULL,
    tipolibro_id INT NOT NULL,
    fecha_salida DATE,
    descripcion TEXT,
    precio DECIMAL NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (id),
	FOREIGN KEY (editorial_id) REFERENCES Editorial(id),
    FOREIGN KEY (autor_id) REFERENCES Autor(id),
    FOREIGN KEY (tipolibro_id) REFERENCES TipoLibro(id)
);

CREATE TABLE Libro_Genero (
    libro_id INT NOT NULL,
    genero_id INT NOT NULL,
    PRIMARY KEY (libro_id, genero_id),
    FOREIGN KEY (libro_id) REFERENCES Libro(id),
    FOREIGN KEY (genero_id) REFERENCES Genero(id)
);

CREATE TABLE Cliente (
	id INT NOT NULL AUTO_INCREMENT,
    rol ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
	nombre VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    usuario VARCHAR(255) NOT NULL UNIQUE,
	email VARCHAR(255) UNIQUE,
    contrasenha VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE Direccion (
    id INT NOT NULL AUTO_INCREMENT,
    cliente_id INT NOT NULL,
    calle VARCHAR(255) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    piso VARCHAR(10),
    ciudad VARCHAR(100) NOT NULL,
    provincia VARCHAR(255) NOT NULL,
    codigo_postal VARCHAR(5) NOT NULL,
	activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE Deseado (
	libro_id INT NOT NULL,
    cliente_id INT NOT NULL,
    PRIMARY KEY (libro_id, cliente_id),
    FOREIGN KEY (libro_id) REFERENCES Libro(id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE Venta (
    id INT NOT NULL AUTO_INCREMENT,
    cliente_id INT,
    fecha DATE,
    hora TIME,
    precio_final DECIMAL,
    PRIMARY KEY (id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE LineaVenta (
    id INT NOT NULL AUTO_INCREMENT,
    venta_id INT,
    libro_id INT NOT NULL,
    cantidad INT,
    precio_parcial DECIMAL NOT NULL,
    precio_total DECIMAL NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (venta_id) REFERENCES Venta(id),
    FOREIGN KEY (libro_id) REFERENCES Libro(id)
);





    
    
    
    
    
    