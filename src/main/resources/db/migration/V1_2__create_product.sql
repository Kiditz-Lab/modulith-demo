CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sku VARCHAR(20) NOT NULL UNIQUE,
    price DECIMAL(10,2) NOT NULL
);

