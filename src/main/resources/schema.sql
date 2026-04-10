-- ==============================================
-- Script SQL : H2 compatible
-- Micro-service : country-service
-- ==============================================

-- ❌ REMOVE: CREATE DATABASE (not supported in H2)
-- ❌ REMOVE: USE countries

-- Create table
CREATE TABLE IF NOT EXISTS country (
    idCountry INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capital VARCHAR(100) NOT NULL
);

-- Insert test data
INSERT INTO country (name, capital) VALUES ('France', 'Paris');
INSERT INTO country (name, capital) VALUES ('Maroc', 'Rabat');
INSERT INTO country (name, capital) VALUES ('Tunisie', 'Tunis');
INSERT INTO country (name, capital) VALUES ('Allemagne', 'Berlin');
INSERT INTO country (name, capital) VALUES ('Espagne', 'Madrid');