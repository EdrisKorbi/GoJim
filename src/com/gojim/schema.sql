CREATE DATABASE IF NOT EXISTS gojim_db;
USE gojim_db;

-- ========================
-- Subscriptions (Plans)
-- ========================
CREATE TABLE IF NOT EXISTS subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plan_name VARCHAR(100) NOT NULL,
    duration_days INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

-- ========================
-- Members
-- ========================
CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    subscription_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

-- ========================
-- Trainers
-- ========================
CREATE TABLE IF NOT EXISTS trainers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    experience VARCHAR(50) NOT NULL
);

-- ========================
-- Payments
-- ========================
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- ========================
-- Default Plans
-- ========================
INSERT INTO subscriptions (plan_name, duration_days, price) VALUES
('Monthly', 30, 50.00),
('3 Months', 90, 120.00),
('Yearly', 365, 400.00);