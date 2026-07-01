# PAUZE
더 많이 감각하는 HSP를 위한 감정 안정 서비스 
<br><br>
### 👥 팀원 소개
: 단/이다은(팀장), 서야/신한서, 쭈요/이주연, 하늘/박한을
### 🛠️ 기술 스택
: Kotlin, MVVM, Jetpack Compose
### 📁 폴더 구조
```text
project
├── data
│   ├── model
│   ├── repository
│   └── datasource
└── ui
    ├── home
    ├── report
    ├── pauze
    ├── curation
    ├── mypage
    ├── splash
    ├── login
    └── condition
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
- **커밋**: `[태그명]/커밋 내용 (#이슈 번호)`
- `main`은 결과물 제출 시에만 머지하며, 일반적인 개발 및 기능 통합은 `develop`브랜치에 머지한다
#### PR 컨벤션
- PR 제목은 브랜치명과 동일하게 작성한다
- 제공된 템플릿을 사용해 작업 내용 및 변경 사항을 작성한다
- 코드 리뷰 확인 후 **Squash & Merge** 방식으로 `develop` 브랜치에 머지한다
#### 이슈 관리
- 작업 시작 전 GitHub Issue를 생성한다
- 이슈 생성 시 담당자(Assignee)를 본인으로 지정한다.
- Kanban Board를 통해 작업 상태를 관리한다
- PR과 Issue를 연결해 작업을 관리한다
___
### 🚀 빌드 및 실행 방법
1. 프로젝트 클론 후 안드로이드 스튜디오에서 프로젝트를 연다
2. Gradle Sync가 완료될 때까지 기다린다
3. 필요한 local.properties 또는 환경 변수를 설정한다
4. 실행할 디바이스를 설정한다
5. Run 버튼이나 Shift + F10을 눌러 애플리케이션을 실행한다
