CREATE TABLE IF NOT EXISTS users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       hashed_password VARCHAR(100) NOT NULL
);



CREATE TABLE IF NOT EXISTS quizzes (
                         id SERIAL PRIMARY KEY,
                         quiz_name VARCHAR(100) NOT NULL,
                         description TEXT,
                         created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS questions (
                           id SERIAL PRIMARY KEY,
                           quiz_id INTEGER REFERENCES quizzes(id) ON DELETE CASCADE,
                           question_text TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS options (
                         id SERIAL PRIMARY KEY,
                         question_id INTEGER REFERENCES questions(id) ON DELETE CASCADE,
                         option_text TEXT NOT NULL,
                         is_correct BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS responses (
                           user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                           quiz_id INTEGER REFERENCES quizzes(id) ON DELETE CASCADE,
                           question_id INTEGER REFERENCES questions(id) ON DELETE CASCADE,
                           option_id INTEGER REFERENCES options(id) ON DELETE CASCADE,
                           response_time TIMESTAMP DEFAULT NOW(),
                           status VARCHAR(10) NOT NULL,
                           PRIMARY KEY (user_id, quiz_id, question_id)
);