CREATE TABLE IF NOT EXISTS test_products (
    product_id INT PRIMARY KEY,
    title VARCHAR(200),
    description VARCHAR(200),
    price varchar(10),
    discount varchar(10),
    discounted_price varchar(10)
);
