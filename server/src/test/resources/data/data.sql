
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
INSERT INTO users (id, name, email) VALUES
(1, 'Петр Иванов', 'petrov@mail.com'),
(2, 'Жанна Сидорова', 'sidorova@mail.ru'),
(3, 'Иван Иванов', 'ivanov@mail.com');

-- Запросы
INSERT INTO requests (id, description, requester_id, created) VALUES
(1, 'Нужен переходник', 2, '2025-06-01 10:00:00'),
(2, 'Нужен чайник', 1, '2025-06-02 15:30:00'),
(3, 'Нужен фен', 3, '2025-06-03 09:15:00');

-- Вещи
INSERT INTO items (id, name, description, available, owner_id, request_id) VALUES
(1, 'Фонарик', 'Фонарик с батарейкой', true, 1, 1),
(2, 'Тепловизор', 'Тепловизор с зарядным устройством', true, 2, 2),
(3, 'Чайник', 'Чайник электрический', false, 3, 3),
(4, 'Акб', 'Аккумулятор 60Ah', true, 2, null);

-- Бронирования
INSERT INTO booking (id, start_time, end_time, item_id, booker_id, status) VALUES
(1, '2025-06-10 09:00:00', '2025-06-12 18:00:00', 1, 2, 'APPROVED'),
(2, '2025-06-15 10:00:00', '2025-06-20 20:00:00', 2, 3, 'WAITING'),
(3, '2025-07-01 08:00:00', '2025-07-10 22:00:00', 3, 1, 'REJECTED'),
(4, '2025-05-01 08:00:00', '2025-05-10 22:00:00', 4, 1, 'APPROVED'),
(5, '2025-04-01 08:00:00', '2025-04-10 22:00:00', 1, 2, 'APPROVED');

-- Комментарии
INSERT INTO comments (id, text, item_id, author_id, created) VALUES
(1, 'Спасибо', 1, 2, '2025-06-13 11:20:00'),
(2, 'Диван огонь', 2, 3, '2025-06-21 14:30:00'),
(3, 'Не очень', 3, 1, '2025-07-11 10:15:00'),
(4, 'Все супер', 4, 1, '2025-07-12 10:15:00'),
(5, 'Хороший фен', 1, 2, '2025-07-13 10:15:00');