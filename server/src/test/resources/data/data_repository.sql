
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
('Иван Петров', 'petrov@example.com'),
('Мария Сидорова', 'sidorova@mail.ru'),
('Алексей Иванов', 'ivanov@gmail.com'),
('Елена Козлова', 'kozlova@yandex.ru'),
('Дмитрий Смирнов', 'smirnov@mail.com');

-- Запросы
INSERT INTO requests (description, requester_id, created) VALUES
('Нужен переходник для телефона', 2L, '2025-06-01 10:00:00'),
('Ищу лопату для дачи', 1L, '2025-06-02 15:30:00'),
('Нужен чайник', 3L, '2025-06-03 09:15:00'),
('Нужен фен на выходные', 4L, '2025-06-04 12:00:00'),
('Ищу бензопилу', 5L, '2025-06-05 11:20:00');

-- Вещи
INSERT INTO items (name, description, available, owner_id, request_id) VALUES
('Фонарик', 'Фонарик с батарейкой', true, 1L, 1L),
('Тепловизор', 'Тепловизор с зарядным устройством', true, 2L, 2L),
('Чайник', 'Чайник электрический', false, 3L, 3L),
('Акб', 'Аккумулятор 60Ah', true, 4L, 4L),
('Диван', 'Диван раскладной', true, 5L, 5L);

-- Бронирования
INSERT INTO booking (start_date, end_date, item_id, booker_id, status) VALUES
('2025-06-10 09:00:00', '2025-06-12 18:00:00', 1L, 2L, 'APPROVED'),
('2025-06-15 10:00:00', '2025-06-20 20:00:00', 2L, 3L, 'WAITING'),
('2025-07-01 08:00:00', '2025-07-10 22:00:00', 3L, 1L, 'REJECTED'),
('2025-05-01 08:00:00', '2025-05-10 22:00:00', 4L, 1L, 'APPROVED'),
('2025-04-01 08:00:00', '2025-04-10 22:00:00', 5L, 2L, 'CANCELED');

-- Комментарии
INSERT INTO comments (text, item_id, author_id, created) VALUES
('Спасибо Работает без нареканий', 1L, 2L, '2025-06-13 11:20:00'),
('Диван огонь', 2L, 3L, '2025-06-21 14:30:00'),
('Не очень', 3L, 1L, '2025-07-11 10:15:00'),
('Все супер', 4L, 4L, '2025-05-15 09:10:00'),
('Хороший фен', 5L, 5L, '2025-04-15 16:45:00');