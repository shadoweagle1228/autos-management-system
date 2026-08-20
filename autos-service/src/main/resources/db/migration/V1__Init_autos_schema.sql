CREATE TABLE autos_schema.autos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    brand VARCHAR(255),
    model VARCHAR(255),
    year INT,
    color VARCHAR(100),
    photo VARCHAR(255),
    plate VARCHAR(20),
    status VARCHAR(50),
    user_id VARCHAR(255)
);