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

---
### ** 회원 에그리거트 **

### 회원(Member)
_Aggregate Root_
#### 속성
- id : Long
- email : 이메일 ID - Natural ID
- nickname : 닉네임
- passwordHash : 비밀번호 해시
- status : 상태
- `detail` : `MemberDetail` 1 : 1 
#### 행위
- static register() : 회원 등록: email, nickname, password, passwordEncoder
- activate() : 등록 완료시킨다.
- deactivate() : 탈퇴 시킨다.
- changeNickname() 
- changePassowrd()
- verifyPassword() : 비밀번호 검증
- `updateInfo()` : 회원 닉네임, 주소, 자기소 수정한다.
#### 규칙
- 회원 생성 후 상태는 등록 대기
- 일정 조건을 만족하면 등록 완료가 된다.
- 등록 대기 상태에서만 등록 완료가 될 수 있다.
- 등록 완료 상태에서는 탈퇴할 수 있다.
- 회원 비밀번호는 해시를 만들어서 저장한다.
- 등록 완료 상태에서만 정보를 수정할 수 있다.
- 프로필 주소는 중복을 허용하지 않는다. 기존 프로필 주소를 제거할 수 있다.

### 회원 상세(MemberDetail)
_Entitiy_
- `id` : `Long`
- `profile` : 프로필 주소. 모든 회원은 고유한 프로필 주소를 가져야 한다.
- `introduction` : 자기 소개
- `registeredAt` : 등록 일시
- `activatedAt` : 등록 완료 일시
- `deactivatedAt` : 탈퇴 일시
#### 행위
- `static create()` : 회원 등록, 현재 시간을 등록 일시로 저장한다.
- `activate()` : 등록 완료와 관련 작업 수행. 등록 완료 일시 저장한다.
- `deactivate()` : 탈퇴와 관련된 작업 수정. 탈퇴 일시 저장한다.
- `updateInfo()` : 상세 정보 수정
#### 규칙


### 회원상태(MemberStatus)
_Enum_
#### 상수
- PENDING : 등록 대기
- ACTIVE : 등록 완료
- DEACTIVATED : 탈퇴

### DuplicateEmailException
_Exception_

### 패스워드 인코더(Password Encoder)
_Domain Service_
#### 행위
- encode() : 암호화하기
- matches() : 비밀번호 일치하는지 확인하기

### 프로필 주소(Profile)
_Value Object_
#### 속성
- `address` : 프로필 주소

---

### Email
_Value Object_
#### 속성
- `address` : 이메일 주소
