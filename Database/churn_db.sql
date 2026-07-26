CREATE DATABASE churn_db;

USE churn_db;

CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(20),
    gender VARCHAR(10),
    senior_citizen INT,
    partner VARCHAR(10),
    dependents VARCHAR(10),
    tenure INT,
    phone_service VARCHAR(10),
    multiple_lines VARCHAR(30),
    internet_service VARCHAR(20),
    online_security VARCHAR(30),
    online_backup VARCHAR(30),
    device_protection VARCHAR(30),
    tech_support VARCHAR(30),
    streaming_tv VARCHAR(30),
    streaming_movies VARCHAR(30),
    contract_type VARCHAR(20),
    paperless_billing VARCHAR(10),
    payment_method VARCHAR(30),
    monthly_charges DOUBLE,
    total_charges DOUBLE,
    churn_actual VARCHAR(5),
    churn_probability DOUBLE,
    offer_status VARCHAR(20) DEFAULT 'Not Offered'
);


SELECT COUNT(*) FROM churn_db.customers;
SELECT * FROM churn_db.customers LIMIT 10;