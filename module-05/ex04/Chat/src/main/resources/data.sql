TRUNCATE TABLE users_chat_rooms, messages, chat_rooms, users RESTART IDENTITY CASCADE;

INSERT INTO users (login, password) VALUES
('user1', 'password1'),
('user2', 'password2'),
('user3', 'password3'),
('user4', 'password4'),
('user5', 'password5');

INSERT INTO chatrooms (name, owner_id) VALUES
('Chat Room 1', 1),
('Chat Room 2', 2),
('Chat Room 3', 3),
('Chat Room 4', 4),
('Chat Room 5', 5);

INSERT INTO messages (author_id, room_id, text) VALUES
(1, 1, 'Hello from user1 in Chat Room 1'),
(2, 1, 'Hello from user2 in Chat Room 1'),
(3, 2, 'Hello from user3 in Chat Room 2'),
(4, 3, 'Hello from user4 in Chat Room 3'),
(5, 4, 'Hello from user5 in Chat Room 4');

INSERT INTO user_chatrooms (user_id, room_id) VALUES
(1, 1), (1, 2), (1, 3),
(2, 1), (2, 2), (2, 4),
(3, 1), (3, 3),
(4, 1), (4, 4),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5);
