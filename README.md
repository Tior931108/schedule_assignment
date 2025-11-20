# �Schedule_Assignment

Spring boot를 활용한 일정 프로젝트 과제입니다.

## 📋 프로젝트 개요

이 프로젝트는 Spring boot를 이용하여 RESTful API를 구현한 일정관리 프로그램 입니다.

## 🎯 주요 기능

### [필수] Lv.1 - 일정 CRUD
- 일정을 생성, 조회, 수정, 삭제할 수 있습니다.
- 일정은 아래 필드를 가집니다.
- `작성 유저명`, `할일 제목`, `할일 내용`, `작성일`, `수정일` 필드
- `작성일`, `수정일` 필드는 `JPA Auditing`을 활용합니다.

### [필수] Lv.2 - 유저 CRUD
- 유저를 생성, 조회, 수정, 삭제할 수 있습니다.
- 유저는 아래와 같은 필드를 가집니다.
- `유저명`, `이메일`, `작성일` , `수정일` 필드
- `작성일`, `수정일` 필드는 `JPA Auditing`을 활용합니다.
- 연관관계 구현
- 일정은 이제 `작성 유저명` 필드 대신 `유저 고유 식별자` 필드를 가집니다.

### [필수] Lv.3 - 회원가입
- 유저에 `비밀번호` 필드를 추가합니다.
- 비밀번호 암호화는 도전 기능에서 수행합니다.
     
### [필수] Lv.4 - 로그인(인증)
- 키워드 **인터페이스**
- HttpServletRequest / HttpServletResponse : 각 HTTP 요청에서 주고받는 값들을 담고 있습니다.
- **Cookie/Session**을 활용해 로그인 기능을 구현합니다.
- `이메일`과 `비밀번호`를 활용해 로그인 기능을 구현합니다.
- 회원가입, 로그인 요청은 인증 처리에서 제외합니다.
- 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 HTTP Status code 401을 반환합니다.

### [도전] Lv.5 - 다양한 예외처리 적용
- Validation을 활용해 다양한 예외처리를 적용합니다.
- 정해진 예외처리 항목이 있는것이 아닌 프로젝트를 분석하고 예외사항을 지정해 봅니다.

### [도전] Lv.6 - 비밀번호 암호화
- Lv.3에서 추가한 `비밀번호` 필드에 들어가는 비밀번호를 암호화합니다.
- 암호화를 위한 `PasswordEncoder`를 직접 만들어 사용합니다.

### [도전] Lv.7 - 댓글 CRUD
- 생성한 일정에 댓글을 남길 수 있습니다.
- 댓글과 일정은 연관관계를 가집니다. →  `3주차 연관관계 매핑 참고!`
- 댓글을 저장, 조회, 수정, 삭제할 수 있습니다.
- 댓글은 아래와 같은 필드를 가집니다.
- `댓글 내용`, `작성일`, `수정일`, `유저 고유 식별자`, `일정 고유 식별자` 필드
- `작성일`, `수정일` 필드는 `JPA Auditing`을 활용하여 적용합니다.

### [도전] Lv.8 - 일정 페이징 조회
- 일정을 Spring Data JPA의 `Pageable`과 `Page` 인터페이스를 활용하여 페이지네이션을 구현
- `페이지 번호`와 `페이지 크기`를 쿼리 파라미터로 전달하여 요청하는 항목을 나타냅니다.
- `할일 제목`, `할일 내용`, `댓글 개수`, `일정 작성일`, `일정 수정일`, `일정 작성 유저명` 필드를 조회합니다.
- 디폴트 `페이지 크기`는 10으로 적용합니다.
- 일정의 `수정일`을 기준으로 내림차순 정렬합니다.

## 📁 프로젝트 구조
```
src/main/java/com/example
├── 📦 comment                          # 댓글 관리 도메인
│   ├── 📂 controller
│   │   └── CommentController
│   ├── 📂 dto
│   │   ├── CreateCommentRequest
│   │   ├── CreateCommentResponse
│   │   ├── ReadAllCommentsResponse
│   │   ├── ReadOneCommentResponse
│   │   ├── UpdateCommentRequest
│   │   └── UpdateCommentResponse
│   ├── 📂 entity
│   │   └── Comment
│   ├── 📂 repository
│   │   └── CommentRepository
│   └── 📂 service
│       └── CommentService
│
├── 📦 common                           # 공통 모듈
│   ├── 📂 annotation                   # 커스텀 어노테이션
│   │   ├── EnumValidator
│   │   ├── LoginUser
│   │   └── RoleRequired
│   ├── 📂 config                       # 설정 클래스
│   │   ├── AuditingConfig
│   │   ├── FilterConfig
│   │   └── WebConfig
│   ├── 📂 entity                       # 공통 엔티티
│   │   └── BaseEntity
│   ├── 📂 exception                    # 예외 처리
│   │   ├── CustomException
│   │   ├── ErrorMessage
│   │   ├── ExistAndNewPasswordException
│   │   ├── ExistEmailException
│   │   ├── ExistNicknameException
│   │   ├── GlobalExceptionHandler
│   │   ├── NeedToLoginException
│   │   ├── NotFoundAuthorizedExcpetion
│   │   ├── NotFoundCommentException
│   │   ├── NotFoundScheduleException
│   │   ├── NotFoundUserException
│   │   ├── NotMatchEmailException
│   │   ├── NotMatchPasswordException
│   │   ├── OnlyOwnerAccessException
│   │   └── RejectAuthorizedExcpetion
│   ├── 📂 filter                       # 필터
│   │   └── LoginCheckFilter
│   ├── 📂 interceptor                  # 인터셉터
│   │   └── RoleCheckInterceptor
│   ├── 📂 resolver                     # Argument Resolver
│   │   └── LoginUserArgumentResolver
│   └── 📂 util                         # 유틸리티
│       ├── AccessValidator
│       ├── AdminInitializer
│       ├── EnumValidatorImpl
│       └── PasswordEncoder
│
├── 📦 schedule                         # 일정 관리 도메인
│   ├── 📂 controller
│   │   └── ScheduleController
│   ├── 📂 dto
│   │   ├── CreateScheduleRequest
│   │   ├── CreateScheduleResponse
│   │   ├── ReadAllScheduleResponse
│   │   ├── ReadOneScheduleResponse
│   │   ├── UpdateScheduleRequest
│   │   └── UpdateScheduleResponse
│   ├── 📂 entity
│   │   └── Schedule
│   ├── 📂 repository
│   │   └── ScheduleRepository
│   └── 📂 service
│       └── ScheduleService
│
├── 📦 user                             # 사용자 관리 도메인
│   ├── 📂 controller
│   │   ├── AuthController
│   │   └── UserController
│   ├── 📂 dto
│   │   ├── CreateUserRequest
│   │   ├── CreateUserResponse
│   │   ├── LoginUserRequest
│   │   ├── LoginUserResponse
│   │   ├── ReadAllUsersResponse
│   │   ├── ReadOneUserResponse
│   │   ├── SessionUser
│   │   ├── UpdateUserRequest
│   │   ├── UpdateUserResponse
│   │   ├── UpdateUserRoleRequest
│   │   └── UpdateUserRoleResponse
│   ├── 📂 entity
│   │   ├── User
│   │   └── UserRole
│   ├── 📂 repository
│   │   └── UserRepository
│   └── 📂 service
│       └── UserService
│
└── 📄 ScheduleApplication              # 메인 애플리케이션 클래스
```

### 📋 패키지 설명

#### 🔹 **comment** - 댓글 관리
일정에 대한 댓글 CRUD 기능을 담당하는 도메인 레이어

#### 🔹 **common** - 공통 기능
- **annotation**: 커스텀 어노테이션 (인증, 권한, 유효성 검증)
- **config**: Spring 설정 클래스 (Auditing, Filter, Web)
- **entity**: 공통 엔티티 (BaseEntity - 생성일/수정일 관리)
- **exception**: 전역 예외 처리 및 커스텀 예외
- **filter**: 서블릿 필터 (로그인 체크)
- **interceptor**: 인터셉터 (역할 기반 권한 체크)
- **resolver**: ArgumentResolver (로그인 사용자 정보 주입)
- **util**: 유틸리티 클래스 (암호화, 검증, 초기화)

#### 🔹 **schedule** - 일정 관리
일정 CRUD 기능을 담당하는 핵심 도메인 레이어 : 일정 전체 조회시 페이징 처리

#### 🔹 **user** - 사용자 관리
사용자 인증/인가 및 사용자 정보 관리를 담당하는 도메인 레이어
- **AuthController**: 로그인/로그아웃 처리
- **UserController**: 사용자 CRUD 및 역할 관리
- **UserRole**: 사용자 권한 Enum (USER, MANAGER, ADMIN)


## 📝 API 명세서

- **POSTMAN API 명세서**

https://documenter.getpostman.com/view/49691093/2sB3WyJwNK


| 기능           | Method | URL                                           | Request 예시                                                                                          | Response 예시                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | 상황별 코드          |
| ------------ | ------ | --------------------------------------------- | --------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| 일정 생성        | POST   | /schedules                                    | [요청 body]  {   "userId": 1,   "title": "프로젝트 회의",   "content": "API 설계 논의" }                        | {   "id": 7,   "userId": 2,   "nickname": "홍길동1(수정)",   "title": "프로젝트 회의6",   "content": "API 설계 논의6",   "createdAt": "2025-11-20T11:20:02.7367515",   "modifiedAt": "2025-11-20T11:20:02.7367515" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | 201: CREATED    |
| 일정 단건 조회     | GET    | /schedules/{scheduleId}                       | [요청 param]  scheduleId: 3                                                                           | {   "id": 7,   "userId": 2,   "nickname": "홍길동1(수정)",   "title": "프로젝트 회의6",   "content": "API 설계 논의6",   "createdAt": "2025-11-20T11:20:02.736752",   "modifiedAt": "2025-11-20T11:20:02.736752",   "comments": [],   "commentCount": 0 }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | 200: OK         |
| 일정 전체 조회     | GET    | /schedules                                    | [요청  param (선택)]  nickname: 홍길동 (선택) page: 1 (선택) size: 10 (선택)                                     | {   "size": 10,   "last": true,   "totalPages": 1,   "currentPage": 0,   "content": [     {       "scheduleId": 5,       "title": "프로젝트 회의2",       "content": "API 설계 논의2",       "commentCount": 0,       "createdAt": "2025-11-14T02:51:40.1937",       "modifiedAt": "2025-11-14T02:51:40.1937",       "nickname": "홍길동1(수정)"     },     {       "scheduleId": 4,       "title": "프로젝트 회의1",       "content": "API 설계 논의1",       "commentCount": 0,       "createdAt": "2025-11-14T02:51:33.865613",       "modifiedAt": "2025-11-14T02:51:33.865613",       "nickname": "홍길동1(수정)"     },     {       "scheduleId": 2,       "title": "프로젝트 회의2",       "content": "API 설계 논의2",       "commentCount": 0,       "createdAt": "2025-11-14T02:51:15.591607",       "modifiedAt": "2025-11-14T02:51:15.591607",       "nickname": "아무개1"     },     {       "scheduleId": 1,       "title": "프로젝트 회의1",       "content": "API 설계 논의1",       "commentCount": 3,       "createdAt": "2025-11-14T02:51:05.384065",       "modifiedAt": "2025-11-14T02:51:05.384065",       "nickname": "아무개1"     }   ],   "first": true,   "totalElements": 4,   "empty": false } | 200: OK         |
| 일정 수정        | PATCH  | /schedules/{scheduleId}                       | {   "title": "수정제목3",   "content" : "수정내용3" }                                                       | {   "id": 7,   "userId": 2,   "nickname": "홍길동1(수정)",   "title": "수정제목3",   "content": "수정내용3",   "createdAt": "2025-11-20T11:20:02.736752",   "modifiedAt": "2025-11-20T11:24:25.653201" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | 200: OK         |
| 일정 삭제        | DELETE | /schedules/{scheduleId}                       | 요청 param  scheduleId: 1                                                                             | .                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 204:NO_CONTENT  |
| 유저 생성 (회원가입) | POST   | /auth/register                                | [요청 body]  {   "email": "fff@example.com",   "password": "password123",   "nickname": "아무개6" }'     | {   "id": 18,   "email": "fff@example.com",   "nickname": "아무개6",   "role": "USER",   "createdAt": "2025-11-20T11:13:23.0010378",   "modifiedAt": "2025-11-20T11:13:23.0010378" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 201: CREATED    |
| 유저 단건 조회     | GET    | /users/{userId}                               | [요청 param]  userId: 2                                                                               | {   "id": 2,   "email": "bbb@example.com",   "nickname": "아무개2",   "role": "USER",   "createdAt": "2025-11-14T02:48:13.017273",   "modifiedAt": "2025-11-14T02:48:13.017273" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      | 200: OK         |
| 유저 전체 조회     | GET    | /users                                        | [요청 param (선택)]  nickname : 홍길동(선택) page: 1 (선택) size: 10 (선택)                                      | [   {     "id": 1,     "email": "aaa@example.com",     "nickname": "아무개1",     "role": "MANAGER",     "createdAt": "2025-11-14T02:48:01.932273",     "modifiedAt": "2025-11-18T12:47:18.580912"   },   {     "id": 2,     "email": "bbb@example.com",     "nickname": "홍길동1(수정)",     "role": "USER",     "createdAt": "2025-11-14T02:48:13.017273",     "modifiedAt": "2025-11-20T11:17:50.337119"   },   {     "id": 4,     "email": "ccc@example.com",     "nickname": "아무개3",     "role": "USER",     "createdAt": "2025-11-14T16:06:20.267993",     "modifiedAt": "2025-11-14T16:06:20.267993"   } ]                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | 200: OK         |
| 유저 정보수정      | PATCH  | /users/{userId}                               | {   "currentPassword" : "password123",   "newPassword": "password111",   "nickname" : "홍길동1(수정)" }' | {   "id": 2,   "email": "bbb@example.com",   "nickname": "홍길동1(수정)",   "role": "USER",   "createdAt": "2025-11-14T02:48:13.017273",   "modifiedAt": "2025-11-20T11:17:50.3371186" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | 200: OK         |
| 유저 권한수정      | PATCH  | /users/{userId}/role                          | {   "role": "MANAGER" }                                                                             | {   "id": 1,   "email": "aaa@example.com",   "nickname": "아무개1",   "role": "MANAGER",   "createdAt": "2025-11-14T02:48:01.932273",   "modifiedAt": "2025-11-18T12:47:18.580912" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 200: OK         |
| 유저 삭제        | DELETE | /users/{userId}                               | [요청 param]  userId: 1                                                                               | .                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 204: NO_CONTENT |
| 로그인          | POST   | /auth/login                                   | [요청 body]  {   "email": "bbb@example.com",   "password": "password123" }                            | ● Response 로그인 정보 (Session 생성)  {     "userId": 2,     "email": "bbb@example.com",     "nickname": "아무개2",     "role": "USER",     "createdAt": "2025-11-14T02:48:13.017273",     "modifiedAt": "2025-11-14T02:48:13.017273" } ● Response Header  Set-Cookie: JSESSIONID=A1B2C3D4E5F6G7H8I9J0; Path=/; HttpOnly                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | 200: OK         |
| 로그아웃         | POST   | /auth/logout                                  | [●요청 Cookie]  Cookie: JSESSIONID=A1B2C3D4E5F6G7H8I9J0                                               |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | 204: NO_CONTENT |
| 댓글 저장        | POST   | /schedules/{scheduleId}/comments              | {   "userId": 4,   "content": "시간은 조정될 수 있습니다!" }                                                   | {   "id": 5,   "scheduleId": 7,   "userId": 2,   "nickname": "홍길동1(수정)",   "content": "시간은 조정될 수 있습니다!",   "createdAt": "2025-11-20T11:25:32.4751809",   "modifiedAt": "2025-11-20T11:25:32.4751809" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | 201: CREATED    |
| 댓글 단건 조회     | GET    | /schedules/{scheduleId}/comments/{commentsId} | [요청 param]  scheduleId: 1 commentId: 1                                                              | {   "commentId": 1,   "scheduleId": 1,   "userId": 1,   "userName": "홍길동",   "content": "회의 참석하겠습니다!",   "createdAt": "2024-11-10T10:30:00",   "updatedAt": "2024-11-10T10:30:00" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | 200: OK         |
| 댓글 전체 조회     | GET    | /schedules/{scheduleId}/comments              | [요청 param]  scheduleId: 1                                                                           | [   {     "id": 3,     "scheduleId": 1,     "userId": 6,     "nickname": "관리자1",     "content": "감사합니다 ㅎㅎ",     "createdAt": "2025-11-18T14:30:28.708481",     "modifiedAt": "2025-11-18T14:30:28.708481"   },   {     "id": 1,     "scheduleId": 1,     "userId": 6,     "nickname": "관리자1",     "content": "댓글 수정입니다.",     "createdAt": "2025-11-18T12:56:57.874187",     "modifiedAt": "2025-11-18T14:30:06.789048"   },   {     "id": 2,     "scheduleId": 1,     "userId": 6,     "nickname": "관리자1",     "content": "혹시 마감 시간 미리 알 수 있을까요?",     "createdAt": "2025-11-18T12:57:28.597262",     "modifiedAt": "2025-11-18T12:57:28.597262"   } ]                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |                 |
| 댓글 수정        | PATCH  | /schedules/{scheduleId}/comments/{commentsId} | {   "content" : "댓글 수정입니다." }                                                                       | {   "id": 5,   "scheduleId": 7,   "userId": 2,   "nickname": "홍길동1(수정)",   "content": "댓글 수정입니다.",   "createdAt": "2025-11-20T11:25:32.475181",   "modifiedAt": "2025-11-20T11:29:24.9587699" }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | 200: OK         |
| 댓글 삭제        | DELETE | /schedules/{scheduleId}/comments/{commentsId} | [요청 param]  scheduleId: 1 commentId: 1                                                              | .                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 204: NO_CONTENT |



## 🧑‍🏫 ERD
<img width="1371" height="551" alt="image" src="https://github.com/user-attachments/assets/400e0463-32f7-4bec-a417-44dae003f422" />


## 📚 학습 내용

- `3 Layer Architecture` 에 따라 각 Layer의 목적에 맞게 개발
- `controller` : HTTP 요청 매핑, 요청 파라미터 검증, 응답 데이터 변환, 예외 처리 및 에러 응답
- `service` : 비즈니스 규칙 구현, 데이터 유효성 검증, 여러 데이터 소스 조합, 트랜잭션 경계 설정
- `repository` : CRUD 연산 구현, 쿼리 최적화, 데이터 매핑
- `JPA Auditing` : 엔티티의 성성 및 수정 시간을 자동으로 관리 `@CreateDate` , `@LastModifiedDate`
- `validation` 의존성 추가로 유효성 검사 기능 활용
- Request DTO 필드에 `@NotBlank`, `@Size` 등 유효성 검사 어노테이션 적용
- Controller 메서드에 `@Valid` 적용
- `@ExceptionHandler` 또는 `@ControllerAdvice`를 사용한 전역 예외 처리 구현
- `FIlter`를 사용하여 로그인 체크 구현
- `Interceptor`를 활용하여 유저 권한에 따른 역할 분담
- `ArgumentResolver` 기능을 활용하여 세션정보 `Service` 전달하여 URL 임의 조작 방지 : `IDOR` 보안 관리
- 커스텀 어노테이션 기능 활용 : `@RoleRequired` 역할권한, `@LoginUser` 세션 정보 통합관리, `@EnumValidator` Enum 클래스 Valid 구현


## 👤 작성자

Tior931108 Ch.3 Spring 숙련 주차 3조 꾸러기들 정용준

