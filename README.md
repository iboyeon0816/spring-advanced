# SPRING ADVANCED
이 프로젝트는 일정 관리 시스템을 개선하고 테스트 코드를 추가하는 Spring 심화 과제이다. 기존에 작성된 프로젝트의 코드 품질을 향상시키고, 안정성을 강화하며, 테스트 커버리지를 확보하는 것을 목표로 한다.

이 프로젝트는 [f-api/spring-advanced](https://github.com/f-api/spring-advanced)에서 fork하여 가져온 것으로, 원본 프로젝트를 기반으로 코드 리팩토링과 테스트 코드 작성을 진행하였다.

<br>

## 개선 사항
- **Early return 도입**: 불필요한 로직 실행 방지 및 성능 향상
- **불필요한 else문 제거**: 코드 간결화 및 유지보수성 개선
- **Spring Validation 적용**: 비즈니스 로직과 데이터 검증 분리
- **GeneralException 도입 및 공통 예외 처리 적용**: 예외 처리 일관성 유지
- **인증 처리 방식 개선**: 컨트롤러에서 JWT 파싱 로직 제거, @Auth 어노테이션을 활용하여 인증 로직 간소화

<br>

## 테스트 코드 작성 및 개선
### 기존 테스트 코드 수정
- `CommentServiceTest`에서 잘못된 Exception 종류 수정
- `ManagerServiceTest`에서 잘못된 테스트명과 에러 메시지 수정
### 신규 테스트 코드 작성
- `CommentService`의 댓글 조회 기능 테스트 추가
- `ManagerService`의 매니저 삭제 기능 테스트 추가
- `CommentController`의 댓글 관련 기능 테스트 추가
- `ManagerController`의 매니저 관련 기능 테스트 추가
### 테스트 커버리지
<img width="726" alt="스크린샷 2025-02-27 오전 10 56 09" src="https://github.com/user-attachments/assets/1dfbb020-b078-4cd4-bc41-1fef7c5b65e5" />

<br>
<br>

## 추가 기능
- AOP를 이용하여 Admin API 요청 및 응답 로깅 기능 추가
