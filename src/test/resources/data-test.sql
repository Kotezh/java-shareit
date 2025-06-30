-- Добавление тестовых пользователей
INSERT INTO users (name, email) VALUES
('User One', 'user1@example.com'),
('User Two', 'user2@example.com');

-- Добавление тестовых запросов
INSERT INTO requests (description, requester_id, created_at) VALUES
('Need a drill', 1, NOW()),
('Looking for a ladder', 2, NOW());

-- Добавление тестовых предметов
INSERT INTO items (name, description, is_available, owner_id, request_id) VALUES
('Drill', 'Powerful drill', true, 1, 1),
('Ladder', 'Aluminum ladder', true, 2, 2);

-- Добавление тестовых бронирований
INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) VALUES
(NOW() + INTERVAL '1 day', NOW() + INTERVAL '2 days', 1, 2, 1),
(NOW() + INTERVAL '3 days', NOW() + INTERVAL '4 days', 2, 1, 0);