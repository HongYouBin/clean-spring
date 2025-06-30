# Splean

## 개발 가이드
- 헥사고날 아키텍처
- 도메인 모델 패턴

### 계층
- Domain Layer
- Application Layer
- Adapter Layer

> 외부(actor) -> 어댑터 -> 애플리케이션 -> 도메인

## 패키지
- domain
- application
  - required   
  - provided
- adapter
  - webapi
  - persistence
  - integration
  - security

## 도메인 모델

### 회원(Member)
_Entity_
#### 속성
- email : 이메일 ID
- nickname : 닉네임
- passwordHash : 비밀번호 해시
- status : 상태
#### 행위
- static create : 회원생성: email, nickname, password, passwordEncoder
- activate() : 가입 완료시킨다.
- deactivate() : 탈퇴 시킨다.
- changeNickname() 
- changePassowrd()
- verifyPassword() : 비밀번호 검증
#### 규칙
- 회원 생성 후 상태는 가입 대기
- 일정 조건을 만족하면 가입 완료가 된다.
- 가입 대기 상태에서만 가입 완료가 될 수 있다.
- 가입 완료 상태에서는 탈퇴할 수 있다.
- 회원 비밀번호는 해시를 만들어서 저장한다.

### 회원상태(MemberStatus)
_Enum_
#### 상수
- PENDING : 가입 대기
- ACTIVE : 가입 완료
- DEACTIVATED : 탈퇴

### 패스워드 인코더(Password Encoder)
_Domain Service_
#### 행위
- encode() : 암호화하기
- matches() : 비밀번호 일치하는지 확인하기
