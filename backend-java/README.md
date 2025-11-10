# Todo Growth Backend

Todo 달성 시 캐릭터가 성장하는 게임화 Todo 애플리케이션의 백엔드 서버입니다.


## 🎮 프로젝트 소개

Todo Growth는 Todo 관리를 게임처럼 재미있게 만드는 애플리케이션입니다.

### 주요 기능
- **게임화 요소**: Todo 완료 시 경험치 +5, 실패 시 경험치 -5
- **레벨업 시스템**: 경험치가 20 이상이면 레벨 +1, 경험치 초기화
- **자동 체크**: 매일 자정에 완료되지 않은 Todo 자동 체크

## 🛠 기술 스택

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: 데이터베이스 ORM
- **MySQL**: 데이터베이스
- **Gradle**: 빌드 도구
- **Lombok**: 보일러플레이트 코드 감소

## 📚 API 문서

### 기본 URL
```
http://localhost:8080/api
```

### 엔드포인트

#### 1. 캐릭터 상태 조회
```http
GET /api/game/users/{userId}/character
```

**응답 예시:**
```json
{
    "success": true,
    "data": {
        "level": 2,
        "experience": 15,
        "requiredExperience": 20,
        "experienceProgress": 75.0
    }
}
```

#### 2. Todo 목록 조회
```http
GET /api/todos/users/{userId}
```

**응답 예시:**
```json
{
    "success": true,
    "data": [
        {
            "id": 1,
            "title": "매일 운동하기",
            "description": "30분 이상 운동",
            "isActive": true,
            "todayCompleted": false
        }
    ]
}
```

#### 3. Todo 생성
```http
POST /api/todos/users/{userId}
Content-Type: application/json

{
    "title": "Todo 제목",
    "description": "Todo 설명 (선택사항)"
}
```

#### 4. Todo 완료
```http
POST /api/game/users/{userId}/todos/{todoId}/complete
```

**응답 예시:**
```json
{
    "success": true,
    "message": "레벨업했습니다!",
    "experienceGained": 5,
    "currentLevel": 2,
    "currentExperience": 0,
    "leveledUp": true,
    "character": {
        "level": 2,
        "experience": 0,
        "requiredExperience": 20,
        "experienceProgress": 0.0
    }
}
```

#### 5. Todo 실패
```http
POST /api/game/users/{userId}/todos/{todoId}/fail
```

**응답 예시:**
```json
{
    "success": true,
    "message": "Todo를 달성하지 못했습니다.",
    "experienceLost": -5,
    "currentExperience": 10,
    "currentLevel": 1,
    "leveledUp": false,
    "character": {
        "level": 1,
        "experience": 10,
        "requiredExperience": 20,
        "experienceProgress": 50.0
    }
}
```

## 📁 프로젝트 구조

```
backend-java/
├── src/
│   ├── main/
│   │   ├── java/com/todogrowth/
│   │   │   ├── entity/              # JPA 엔티티
│   │   │   │   ├── User.java
│   │   │   │   ├── Character.java   # 게임 로직 포함
│   │   │   │   ├── Todo.java
│   │   │   │   └── TodoLog.java
│   │   │   │   ├── repository/          # JPA 리포지토리
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CharacterRepository.java
│   │   │   │   ├── TodoRepository.java
│   │   │   │   └── TodoLogRepository.java
│   │   │   ├── service/             # 비즈니스 로직
│   │   │   │   └── GameService.java
│   │   │   ├── controller/          # REST 컨트롤러
│   │   │   │   ├── GameController.java
│   │   │   │   └── TodoController.java
│   │   │   ├── dto/                  # 데이터 전송 객체
│   │   │   │   ├── CharacterStatusDto.java
│   │   │   │   ├── CompleteTodoResponseDto.java
│   │   │   │   └── FailTodoResponseDto.java
│   │   │   ├── scheduler/            # 스케줄러
│   │   │   │   └── DailyCheckScheduler.java
│   │   │   └── TodoGrowthApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                         # 테스트 코드
├── build.gradle
├── settings.gradle
├── INIT_DATA.sql
└── README.md
```

## 🔧 주요 기능 설명

### 1. 게임화 메커니즘

#### 경험치 시스템
- **Todo 완료 시**: EXP +5
- **Todo 실패 시**: EXP -5 (최소 0)
- **레벨업 조건**: EXP >= 20이면 레벨 +1, EXP = 0으로 초기화

#### 레벨업 시스템
- 경험치가 20 이상이면 자동으로 레벨업
- 레벨업 시 경험치는 0으로 초기화
- 레벨은 무한정 증가 가능

### 2. 자동 스케줄러

`DailyCheckScheduler`가 매일 자정에 실행되어:
- 어제 완료되지 않은 Todo를 자동으로 찾아
- 실패 처리 및 경험치 감소 적용
