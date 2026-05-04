-- changeset evgeny:004

INSERT INTO users (name, surname, birth_date, email, active, created_at, updated_at) VALUES
    ('Ivan', 'Petrov', '1990-01-15', 'ivan.petrov@example.com', TRUE, NOW(), NOW()),
    ('Anna', 'Ivanova', '1985-03-22', 'anna.ivanova@example.com', TRUE, NOW(), NOW()),
    ('Pavel', 'Sidorov', '1992-07-09', 'pavel.sidorov@example.com', TRUE, NOW(), NOW()),
    ('Olga', 'Smirnova', '1988-12-01', 'olga.smirnova@example.com', TRUE, NOW(), NOW()),
    ('Dmitry', 'Kuznetsov', '1995-05-30', 'dmitry.kuznetsov@example.com', TRUE, NOW(), NOW()),
    ('Elena', 'Popova', '1991-09-17', 'elena.popova@example.com', TRUE, NOW(), NOW()),
    ('Sergey', 'Volkov', '1987-11-05', 'sergey.volkov@example.com', TRUE, NOW(), NOW()),
    ('Natalia', 'Morozova', '1993-06-25', 'natalia.morozova@example.com', TRUE, NOW(), NOW()),
    ('Alexey', 'Lebedev', '1990-08-14', 'alexey.lebedev@example.com', TRUE, NOW(), NOW()),
    ('Irina', 'Nikolaeva', '1989-04-02', 'irina.nikolaeva@example.com', TRUE, NOW(), NOW());

INSERT INTO payment_cards (user_id, number, holder, expiration_date, active, created_at, updated_at) VALUES
    (1, '4111111111111111', 'Ivan Petrov', '2026-12-31', TRUE, NOW(), NOW()),
    (2, '4222222222222222', 'Anna Ivanova', '2025-11-30', TRUE, NOW(), NOW()),
    (3, '4333333333333333', 'Pavel Sidorov', '2027-01-15', TRUE, NOW(), NOW()),
    (4, '4444444444444444', 'Olga Smirnova', '2026-06-30', TRUE, NOW(), NOW()),
    (5, '4555555555555555', 'Dmitry Kuznetsov', '2025-09-30', TRUE, NOW(), NOW()),
    (6, '4666666666666666', 'Elena Popova', '2027-03-31', TRUE, NOW(), NOW()),
    (7, '4777777777777777', 'Sergey Volkov', '2026-07-31', TRUE, NOW(), NOW()),
    (8, '4888888888888888', 'Natalia Morozova', '2025-12-31', TRUE, NOW(), NOW()),
    (9, '4999999999999999', 'Alexey Lebedev', '2026-05-31', TRUE, NOW(), NOW()),
    (10, '4000000000000000', 'Irina Nikolaeva', '2027-08-31', TRUE, NOW(), NOW());