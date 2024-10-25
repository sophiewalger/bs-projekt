-- src/main/resources/schema.sql
CREATE DATABASE IF NOT EXISTS hausfix;
USE hausfix;

CREATE TABLE IF NOT EXISTS customers (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender ENUM('D', 'M', 'U', 'W') NOT NULL
);

CREATE TABLE IF NOT EXISTS readings (
    id VARCHAR(36) PRIMARY KEY,
    meter_id VARCHAR(255) NOT NULL,
    kind_of_meter ENUM('HEIZUNG', 'STROM', 'WASSER', 'UNBEKANNT') NOT NULL,
    meter_count DOUBLE NOT NULL,
    date_of_reading DATE NOT NULL,
    substitute BOOLEAN NOT NULL DEFAULT FALSE,
    comment TEXT,
    customer_id VARCHAR(36),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
);