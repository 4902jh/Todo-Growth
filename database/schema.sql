-- Todo Growth 데이터베이스 스키마 (MySQL)

-- Users 테이블
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Characters 테이블 (게임화 요소)
CREATE TABLE characters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    level INT DEFAULT 1 NOT NULL,
    experience INT DEFAULT 0 NOT NULL,
    current_hp INT DEFAULT 100 NOT NULL,
    max_hp INT DEFAULT 100 NOT NULL,
    streak INT DEFAULT 0 NOT NULL,
    last_activity_date DATE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Habits 테이블
CREATE TABLE habits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    is_active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- HabitLogs 테이블 (일일 달성 기록)
CREATE TABLE habit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    habit_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    completed TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_habit_date (habit_id, date),
    FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 인덱스 생성 (성능 최적화)
CREATE INDEX idx_characters_user_id ON characters(user_id);
CREATE INDEX idx_habits_user_id ON habits(user_id);
CREATE INDEX idx_habit_logs_user_id ON habit_logs(user_id);
CREATE INDEX idx_habit_logs_habit_id ON habit_logs(habit_id);
CREATE INDEX idx_habit_logs_date ON habit_logs(date);
