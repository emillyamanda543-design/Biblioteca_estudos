-- Execute no cliente MariaDB. Preserva tabelas e registros existentes.
CREATE DATABASE IF NOT EXISTS biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca;

CREATE TABLE IF NOT EXISTS livros (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    codigo VARCHAR(100) NOT NULL UNIQUE,
    editora VARCHAR(255) NOT NULL,
    data_cadastro DATE NOT NULL,
    genero VARCHAR(255) NOT NULL
) ENGINE=InnoDB;
ALTER TABLE livros ADD COLUMN IF NOT EXISTS genero VARCHAR(255) NULL;

CREATE TABLE IF NOT EXISTS leitores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    endereco VARCHAR(255) NOT NULL
) ENGINE=InnoDB;
ALTER TABLE leitores ADD COLUMN IF NOT EXISTS endereco VARCHAR(255) NULL;

CREATE TABLE IF NOT EXISTS funcionarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(255) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    data_admissao DATE NOT NULL,
    salario DECIMAL(12,2) NOT NULL
) ENGINE=InnoDB;
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS salario DECIMAL(12,2) NULL;

CREATE TABLE IF NOT EXISTS editoras (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    data_fundacao DATE NOT NULL,
    cnpj VARCHAR(255) NOT NULL UNIQUE,
    cidade VARCHAR(255) NOT NULL
) ENGINE=InnoDB;
ALTER TABLE editoras ADD COLUMN IF NOT EXISTS cnpj VARCHAR(255) NULL;
ALTER TABLE editoras ADD COLUMN IF NOT EXISTS cidade VARCHAR(255) NULL;

-- Novos campos ficam NULL em cadastros antigos: complete-os pelo botão Editar.
-- CREATE TABLE IF NOT EXISTS não altera restrições de tabelas já existentes.
