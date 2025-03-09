CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    status VARCHAR(10)
);

CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    orders INT references orders(id)
);
