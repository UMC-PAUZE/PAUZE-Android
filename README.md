# PAUZE
더 많이 감각하는 HSP를 위한 감정 안정 서비스
<br><br>
### 👥 팀원 소개
: 단/이다은(팀장), 서야/신한서, 쭈요/이주연, 하늘/박한을
### 🛠️ 기술 스택
: Kotlin, MVVM, Jetpack Compose

### 📚 주요 라이브러리
#### 언어/기본
- Kotlin stdlib: 2.3.20
#### UI
- Jetpack Compose (BOM 2024.09.00)
- Activity Compose 1.13.0
- Material 3 1.4.0
- Navigation Compose 2.9.0
- Lottie Compose 6.5.2
#### 아키텍처
- Lifecycle ViewModel Compose 2.9.0
- Lifecycle ViewModel KTX: 2.10.0
- Hilt Android: 2.5.8
- Hilt Compiler: 2.5.8
- Hilt Navigation Compose: 1.2.0
- Datastore: 1.2.1
#### 네트워크 및 데이터 직렬화
- Retrofit2: 2.11.0
- Retrofit Converter Json: 2.11.0
- OkHttp3: 4.12.0
- OkHttp3 Logging Interceptor: 4.12.0
- Kotlin Serialization Json: 1.11.0
#### 소셜 로그인
- Kakao SDK v2-user: 2.11.0
#### 이미지
- Coil Compose 2.7.0
#### 오디오
- Media3 Exoplayer: 1.10.1
#### 날짜 및 시간
- Kotlinx DateTime 0.8.0
- Datetime Wheel Picker 1.3.1
#### 오픈소스 라이브러리
- compose-datetime-wheel-picker by darkokoa

### 📁 폴더 구조
```text
project
├── data
│   ├── datasource
│   ├── datastore
│   ├── dummies
│   ├── model
│   ├── module
│   ├── repository
│   └── service
└── ui
    ├── component
    ├── curation
    ├── home
    ├── login
    ├── mypage
    ├── pauze
    ├── report
    └── splash
```
___
### 📚 컨벤션 문서
#### 기본 규칙
- ViewModel은 각 `ui` 폴더 내부에 존재한다
- 데이터 관련 코드는 `data`, 화면 관련 코드는 `ui` 패키지 내부에 작성한다
#### 네이밍 규칙
- 파일명과 클래스명은 ParcelCase를 사용한다
- 변수명과 함수명은 camelCase를 사용한다
- 패키지 구조명은 소문자로 작성하며, '_'나 '-'는 사용하지 않는다
#### 깃 컨벤션
- **브랜치**: `태그명/주요기능`
- **커밋**: `[태그명]커밋 내용 (#이슈 번호)`
- `main`은 결과물 제출 시에만 머지하며, 일반적인 개발 및 기능 통합은 `develop`브랜치에 머지한다

| 태그명        | 설명                                |
|------------|-----------------------------------|
| `feat`     | 새로운 기능을 추가하는 작업                   |
| `fix`      | 버그를 수정하거나 오류를 해결하는 작업             |
| `refactor` | 기능 변경 없이 코드 구조나 품질을 개선하는 작업       |
| `hotfix`   | 서비스 운영 중 발생한 긴급한 버그를 신속하게 수정하는 작업 |
| `docs`     | REAMDE와 같은 문서를 수정하는 작업            |

#### PR 컨벤션
- PR 제목은 `[태그명]작업 내용 (#이슈 번호)` 형식으로 작성한다
- 제공된 템플릿을 사용해 작업 내용 및 변경 사항을 작성한다
- 코드 리뷰 확인 후 **Squash & Merge** 방식으로 `develop` 브랜치에 머지한다
#### 이슈 관리
- 작업 시작 전 GitHub Issue를 생성한다
- 이슈 생성 시 담당자(Assignee)를 본인으로 지정한다.
- 이슈 제목은 `[태그명]작업 내용` 형식으로 작성한다
- Kanban Board를 통해 작업 상태를 관리한다
- PR과 Issue를 연결해 작업을 관리한다
___
### 🚀 빌드 및 실행 방법
1. 프로젝트 클론 후 안드로이드 스튜디오에서 프로젝트를 연다
2. Gradle Sync가 완료될 때까지 기다린다
3. 필요한 local.properties 또는 환경 변수를 설정한다
4. 실행할 디바이스를 설정한다
5. Run 버튼이나 Shift + F10을 눌러 애플리케이션을 실행한다

### 🔗 참조
- compose-date-time-picker<br>
  https://github.com/darkokoa/compose-datetime-wheel-picker

---
### 📱구현 화면 (2026.08.14 기준)
|     화면 이름      |          스크린 ID           |  담당자   |
|:--------------:|:-------------------------:|:------:|
|      스플래시      |       SplashScreen        | 쭈요/이주연  |
|      온보딩       |     OnboardingScreen      | 쭈요/이주연  | 
|      로그인       |        LoginScreen        | 단/이다은  | 
|      회원가입      |       SignUpScreen        | 단/이다은  |
|      홈화면       |        HomeScreen         | 단/이다은  |
|    오늘의 컨디션     | PauzeTodayConditionScreen | 하늘/박한을 |
|    Pauze 시작    |     PauzeStartScreen      | 쭈요/이주연 |
|    즉각 호흡 화면    |   PauzeBreathingScreen    | 쭈요/이주연 |
|    청각 안정 화면    |     PauzeSoundScreen      | 하늘/박한을 |
|  청각 안정 소리 보관함  |   PauzeSoundStashScreen   | 하늘/박한을 |
| 청각 안정 소리 재생 화면 |  PauzeSoundDetailScreen   | 하늘/박한을 |
|    시각 안정 화면    |     PauzeVisualScreen     | 서야/신한서 |
|   과한 에너지 소모    |    PauzeOverloadScreen    | 단/이다은  |
|    예민함 리포트     |       ReportScreen        | 쭈요/이주연 |
|     발견 화면      |    CurationBoardScreen    | 서야/신한서 |
|     마이페이지      |       MyPageScreen        | 쭈요/이주연 |