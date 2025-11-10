# Todo Growth 게임화 요소 설계

> ⚠️ **주의**: 이 문서는 초기 설계 문서입니다. 현재 구현된 시스템과 다를 수 있습니다.
> 
> **현재 구현된 게임 로직**:
> - Todo 완료: EXP +5
> - Todo 실패: EXP -5 (최소 0)
> - 레벨업: EXP >= 20이면 레벨 +1, EXP = 0으로 초기화
> 
> 자세한 내용은 [README.md](README.md)를 참고하세요.

## 1. 핵심 게임화 메커니즘 (현재 구현)

### 1.1 캐릭터 시스템
- **레벨 (Level)**: 캐릭터의 성장 단계
- **경험치 (XP)**: Todo 달성 시 획득, 레벨업에 필요

### 1.2 보상 시스템 (Todo 달성 시)
```
- 경험치: +5 XP
- 레벨업: EXP >= 20이면 레벨 +1, EXP = 0으로 초기화
```

### 1.3 패널티 시스템 (Todo 실패 시)
```
- 경험치 감소: -5 XP (최소 0)
- 레벨업 가능: 실패해도 EXP >= 20이면 레벨업 가능
```

### 1.4 레벨업 시스템
```
레벨업 필요 경험치: 항상 20
예: 경험치가 20 이상이면 레벨 +1, 경험치 = 0
```

## 2. 데이터베이스 설계

### 2.1 Users 테이블
```sql
- id (PK)
- username
- email
- password_hash
- created_at
```

### 2.2 Characters 테이블
```sql
- id (PK)
- user_id (FK)
- level (기본값: 1)
- experience (기본값: 0)
- updated_at
```

### 2.3 Todos 테이블
```sql
- id (PK)
- user_id (FK)
- title (Todo 제목)
- description
- created_at
- is_active (활성화 여부)
```

### 2.4 TodoLogs 테이블 (일일 달성 기록)
```sql
- id (PK)
- todo_id (FK)
- user_id (FK)
- date (날짜, YYYY-MM-DD)
- completed (완료 여부: true/false)
- created_at
- UNIQUE(todo_id, date) -- 하루에 한 번만 기록
```

## 3. 게임 로직 플로우

### 3.1 Todo 완료 처리
```
1. 사용자가 Todo 완료 버튼 클릭
2. 오늘 날짜로 TodoLog 생성 (completed = true)
3. 경험치 추가: EXP +5
4. Character 업데이트:
   - experience += 5
5. 레벨업 체크:
   - experience >= 20이면:
     - level += 1
     - experience = 0
```

### 3.2 Todo 실패 처리
```
1. 사용자가 Todo 실패 버튼 클릭
2. 오늘 날짜로 TodoLog 생성 (completed = false)
3. 경험치 감소: EXP -5 (최소 0)
4. Character 업데이트:
   - experience = max(0, experience - 5)
5. 레벨업 체크:
   - experience >= 20이면:
     - level += 1
     - experience = 0
```

### 3.3 자동 실패 체크 (스케줄러)
```
매일 자정에 실행:
1. 어제 날짜의 완료되지 않은 Todo 찾기
2. 각 Todo에 대해 실패 처리 실행
3. 경험치 감소 및 레벨업 체크
```

## 4. UI 흐름

### 4.1 메인화면
- Todo 추가 버튼
- Todo 목록 버튼
- 캐릭터 상태 보기 버튼
- 앱 종료 버튼

### 4.2 Todo 목록 화면
- 각 Todo에 대해:
  - 완료 버튼 (EXP +5)
  - 실패 버튼 (EXP -5)
  - 오늘 완료 여부 표시

### 4.3 캐릭터 상태 화면
- 레벨 표시
- 경험치 바 (현재 EXP / 20)
- 경험치 퍼센트
