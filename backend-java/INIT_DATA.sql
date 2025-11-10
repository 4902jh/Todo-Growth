-- 초기 테스트 데이터 생성 스크립트
-- MySQL에 접속하여 실행하세요

USE todogrowth;

-- User 생성 (ID가 1인 User)
INSERT INTO users (id, username, email, password_hash, created_at) 
VALUES (1, 'testuser', 'test@example.com', 'dummy', NOW())
ON DUPLICATE KEY UPDATE username=username;

-- Character 생성 (User ID 1에 대한 캐릭터)
INSERT INTO characters (user_id, level, experience, updated_at)
VALUES (1, 1, 0, NOW())
ON DUPLICATE KEY UPDATE user_id=user_id;

-- Todo 생성 (예시)
INSERT INTO todos (user_id, title, description, is_active, created_at)
VALUES 
    (1, '매일 운동하기', '30분 이상 운동', 1, NOW()),
    (1, '책 읽기', '하루 30페이지', 1, NOW())
ON DUPLICATE KEY UPDATE title=title;

-- 확인
SELECT * FROM users WHERE id = 1;
SELECT * FROM characters WHERE user_id = 1;
SELECT * FROM todos WHERE user_id = 1;

