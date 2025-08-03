
-- Очистка таблиц
DELETE FROM comments;
DELETE FROM booking;
DELETE FROM items;
DELETE FROM requests;
DELETE FROM users;

-- Сброс последовательностей ID
ALTER TABLE users ALTER COLUMN id RESTART WITH 1L;
ALTER TABLE requests ALTER COLUMN id RESTART WITH 1L;
ALTER TABLE items ALTER COLUMN id RESTART WITH 1L;
ALTER TABLE booking ALTER COLUMN id RESTART WITH 1L;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1L;

-- Пользователи
INSERT INTO users (name, email) VALUES
('Петр Иванов', 'petrov@mail.com'),
('Жанна Сидорова', 'sidorova@mail.ru'),
('Иван Иванов', 'ivanov@mail.com');

-- Запросы
INSERT INTO requests (description, requester_id, created) VALUES
('Нужен переходник', 2L, '2025-06-01 10:00:00'),
('Нужен чайник', 1L, '2025-06-02 15:30:00'),
('Нужен фен', 3L, '2025-06-03 09:15:00');

-- Вещи
INSERT INTO items (name, description, available, owner_id, request_id) VALUES
('Фонарик', 'Фонарик с батарейкой', true, 1L, 1L),
('Тепловизор', 'Тепловизор с зарядным устройством', true, 2L, 2L),
('Чайник', 'Чайник электрический', false, 3L, 3L),
('Акб', 'Аккумулятор 60Ah', true, 2L, null);

-- Бронирования
INSERT INTO booking (start_date, end_date, item_id, booker_id, status) VALUES
('2025-06-10 09:00:00', '2025-06-12 18:00:00', 1L, 2L, 'APPROVED'),
('2025-06-15 10:00:00', '2025-06-20 20:00:00', 2L, 3L, 'WAITING'),
('2025-07-01 08:00:00', '2025-07-10 22:00:00', 3L, 1L, 'REJECTED'),
('2025-05-01 08:00:00', '2025-05-10 22:00:00', 4L, 1L, 'APPROVED'),
('2025-04-01 08:00:00', '2025-04-10 22:00:00', 1L, 2L, 'APPROVED');

-- Комментарии
INSERT INTO comments (text, item_id, author_id, created) VALUES
('Спасибо', 1L, 2L, '2025-06-13 11:20:00'),
('Диван огонь', 2L, 3L, '2025-06-21 14:30:00'),
('Не очень', 3L, 1L, '2025-07-11 10:15:00'),
('Все супер', 4L, 1L, '2025-07-12 10:15:00'),
('Хороший фен', 1L, 2L, '2025-07-13 10:15:00');