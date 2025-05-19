-- Drop tables in reverse order of dependencies
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS user_chatrooms;
DROP TABLE IF EXISTS chatrooms;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id SERIAL PRIMARY KEY ,
    login VARCHAR(100)  UNIQUE NOT NULL ,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE chatrooms (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(255) NOT NULL ,
    owner_id INT NOT NULL ,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE messages (
    id SERIAL PRIMARY KEY ,
    author_id INT NOT NULL ,
    room_id INT NOT NULL ,
    text TEXT NOT NULL CHECK ( LENGTH(text) > 0 ),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE ,
    FOREIGN KEY (room_id) REFERENCES chatrooms(id) ON DELETE CASCADE
);

CREATE TABLE user_chatrooms (
    user_id INT NOT NULL ,
    room_id INT NOT NULL ,
    PRIMARY KEY (user_id, room_id) ,
    FOREIGN KEY (user_id) REFERENCES users (id) ,
    FOREIGN KEY (room_id) REFERENCES chatrooms (id)
);