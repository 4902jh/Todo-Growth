# Todo Growth 🎮

Todo를 달성하고 캐릭터를 성장시키는 게임화 Todo 앱입니다.

## 📋 프로젝트 개요

매일 Todo를 완료하면 경험치를 획득하고, 경험치가 20 이상이면 레벨업하는 간단한 게임화 시스템을 제공합니다.

### 주요 기능

- ✅ **Todo 관리**: Todo 추가, 완료, 실패 처리
- 🎮 **게임화 요소**: 경험치 시스템, 레벨업
- 📊 **캐릭터 상태**: 레벨, 경험치 확인
- 📱 **직관적인 UI**: 메인화면 기반 네비게이션

### 게임 로직

- **Todo 완료**: EXP +5
- **Todo 실패**: EXP -5 (최소 0)
- **레벨업**: EXP >= 20이면 레벨 +1, EXP = 0으로 초기화

## 🛠️ 기술 스택

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL
- Gradle

### Frontend
- React
- JavaScript (ES6+)
- CSS3

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- Node.js 18 이상
- MySQL 8.0 이상
- Gradle (또는 Gradle Wrapper 사용)



## 📁 프로젝트 구조

```
Todo Growth/
├── backend-java/          # Spring Boot 백엔드
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/todogrowth/
│   │       │       ├── controller/    # REST API 컨트롤러
│   │       │       ├── service/       # 비즈니스 로직
│   │       │       ├── entity/        # JPA 엔티티
│   │       │       ├── repository/    # 데이터 접근 계층
│   │       │       └── dto/           # 데이터 전송 객체
│   │       └── resources/
│   │           └── application.properties
│   └── build.gradle
├── frontend/              # React 프론트엔드
│   ├── src/
│   │   ├── components/    # React 컴포넌트
│   │   ├── App.js         # 메인 앱 컴포넌트
│   │   └── index.js       # 진입점
│   └── package.json
└── README.md
```

## 📚 API 문서

### 캐릭터 상태 조회
```
GET /api/game/users/{userId}/character
```

### Todo 목록 조회
```
GET /api/todos/users/{userId}
```

### Todo 생성
```
POST /api/todos/users/{userId}
Content-Type: application/json

{
  "title": "Todo 제목",
  "description": "Todo 설명 (선택사항)"
}
```

### Todo 완료
```
POST /api/game/users/{userId}/todos/{todoId}/complete
```

### Todo 실패
```
POST /api/game/users/{userId}/todos/{todoId}/fail
```

## 📖 상세 문서

- [백엔드 README](backend-java/README.md) - 백엔드 상세 가이드
- [게임 디자인](GAME_DESIGN.md) - 게임화 요소 설계
- [게임화 UI 가이드](GAMIFICATION_UI_GUIDE.md) - UI 요소 설명


