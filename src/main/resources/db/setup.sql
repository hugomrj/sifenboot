CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password TEXT
);


-- El password es 'admin' (BCrypt con 10 rounds)
INSERT INTO usuarios (username, password)
VALUES ('admin', '$2a$10$vI84WZ16W.9A9vQ.mYJz8uALvXupUshshPsvuS.uH5A2oX.vP7t7G')
ON CONFLICT (username) DO NOTHING;