
-- Очистка таблиц
DELETE FROM comments;
DELETE FROM booking;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

-- Сброс последовательностей ID
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;
ALTER TABLE items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE booking ALTER COLUMN id RESTART WITH 1;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;

-- Пользователи
INSERT INTO users (name, email) VALUES
('Иван Петров', 'petrov@example.com'),
('Мария Сидорова', 'sidorova@mail.ru'),
('Алексей Иванов', 'ivanov@gmail.com'),
('Елена Козлова', 'kozlova@yandex.ru'),
('Дмитрий Смирнов', 'smirnov@mail.com');

-- Запросы
INSERT INTO requests (description, requester_id, created) VALUES
('Нужен переходник для телефона', 2, '2025-06-01 10:00:00'),
('Ищу лопату для дачи', 1, '2025-06-02 15:30:00'),
('Нужен чайник', 3, '2025-06-03 09:15:00'),
('Нужен фен на выходные', 4, '2025-06-04 12:00:00'),
('Ищу бензопилу', 5, '2025-06-05 11:20:00');

-- Вещи
INSERT INTO items (name, description, available, owner_id, request_id) VALUES
('Фонарик', 'Фонарик с батарейкой', true, 1, 1),
('Тепловизор', 'Тепловизор с зарядным устройством', true, 2, 2),
('Чайник', 'Чайник электрический', false, 3, 3),
('Акб', 'Аккумулятор 60Ah', true, 4, 4),
('Бензопила', 'Мощная бензопила 45см', true, 5, 5);

-- Бронирования
INSERT INTO booking (start_date, end_date, item_id, booker_id, status) VALUES
('2025-06-10 09:00:00', '2025-06-12 18:00:00', 1, 2, 'APPROVED'),
('2025-06-15 10:00:00', '2025-06-20 20:00:00', 2, 3, 'WAITING'),
('2025-07-01 08:00:00', '2025-07-10 22:00:00', 3, 1, 'REJECTED'),
('2025-05-01 08:00:00', '2025-05-10 22:00:00', 4, 1, 'APPROVED'),
('2025-04-01 08:00:00', '2025-04-10 22:00:00', 5, 2, 'CANCELED');

-- Комментарии
INSERT INTO comments (text, item_id, author_id, created) VALUES
('Спасибо Работает без нареканий', 1, 2, '2025-06-13 11:20:00'),
('Диван огонь', 2, 3, '2025-06-21 14:30:00'),
('Не очень', 3, 1, '2025-07-11 10:15:00'),
('Все супер', 4, 4, '2025-05-15 09:10:00'),
('Хороший фен', 5, 5, '2025-04-15 16:45:00');