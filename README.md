# JWT Auth Tester

**Spring Security + JWT(Json Web Token)** 기반의 인증/인가 방식을 학습하고 테스트하기 위한 실습용 애플리케이션입니다.  
브라우저에서 간단한 UI를 통해 회원가입, 로그인, 토큰 재발급, 권한 검증 요청 등을 실행해볼 수 있습니다.

---

## 실행 환경
- **Backend**: Spring Boot, Spring Security, JWT  
- **Database**: MySQL  
- **Frontend(UI)**: 단순 HTML/JS 페이지 (토큰 발급 및 API 호출 테스트용)

---

## 기능 개요

### 화면
<img width="1077" height="574" alt="image" src="https://github.com/user-attachments/assets/61d4b2b3-b6e4-4875-893e-59fe1a0c0c01" />

### 회원가입 / 로그인
- Username, Password, Role(USER/ADMIN)을 입력하여 회원가입 및 로그인 가능  
- 로그인 시 **Access Token**과 **Refresh Token** 발급  

### 토큰 관리
- Access Token → 클라이언트 메모리에 저장 후 요청 시 헤더에 포함  
- Refresh Token → HttpOnly 쿠키에 저장되어 필요 시 토큰 재발급에 사용  

### 권한 검증 요청
- `/role/user` → USER 권한 이상 접근 가능  
- `/role/admin` → ADMIN 권한 필요  
- 토큰의 유효성과 권한을 확인할 수 있음  

### 토큰 재발급
- Access Token 만료 시 Refresh Token을 이용하여 새로운 Access Token 발급  

### 로그(Log) 출력
- 요청/응답 내역이 하단 로그 창에 표시되어 디버깅 및 동작 확인 용이  

---

## 사용 흐름
1. **회원가입(Signup)** 으로 계정 생성  
2. **로그인(Login)** → Access Token + Refresh Token 발급  
3. **권한 검증 요청**: `role/user`, `role/admin` 버튼 실행  
4. Access Token 만료 시 **auth/refresh** 버튼으로 새 토큰 발급  
5. 로그 영역에서 전체 요청/응답 과정을 확인  

---

## 목적
- JWT 기반 인증/인가 구조 이해  
- Access Token과 Refresh Token의 역할 차이 학습  
- 권한별 API 접근 제어 실습  
- HttpOnly 쿠키 기반 Refresh Token 관리 방식 체험  
