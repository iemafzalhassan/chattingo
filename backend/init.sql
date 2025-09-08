-- Database initialization script for Chattingo
-- This script runs when the MySQL container starts for the first time

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS chattingo_db;

-- Use the database
USE chattingo_db;

-- Create a dedicated user for the application (optional)
-- CREATE USER IF NOT EXISTS 'chattingo_user'@'%' IDENTIFIED BY 'chattingo_pass';
-- GRANT ALL PRIVILEGES ON chattingo_db.* TO 'chattingo_user'@'%';
-- FLUSH PRIVILEGES;

-- Set character set and collation
ALTER DATABASE chattingo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Show databases to confirm
SHOW DATABASES;
