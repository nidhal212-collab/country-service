-- ==============================================
-- Script SQL : Création de la base et de la table
-- Micro-service : country-service
-- ==============================================

-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS countries;

-- Utiliser la base de données
USE countries;

-- Créer la table country
CREATE TABLE IF NOT EXISTS country (
    idCountry INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capital VARCHAR(100) NOT NULL
);

-- Insertion de données de test (optionnel)
INSERT INTO country (name, capital) VALUES ('France', 'Paris');
INSERT INTO country (name, capital) VALUES ('Maroc', 'Rabat');
INSERT INTO country (name, capital) VALUES ('Tunisie', 'Tunis');
INSERT INTO country (name, capital) VALUES ('Allemagne', 'Berlin');
INSERT INTO country (name, capital) VALUES ('Espagne', 'Madrid');
