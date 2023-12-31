# 원티드 프리온보딩 백엔드 인턴십 선발 과제

- [안내 링크]

## 개요

- 기업 채용을 위한 웹 서비스 서버 애플리케이션

## 기술 스택

- Spring Boot 3.1.4
- Spring Boot DevTools
- Spring Web
- Spring Data JPA
- H2 Database
- MySQL Driver
- Validation
- Flyway Migration
- Lombok

## How To Start

### 애플리케이션 실행 요구사항

- Java 17
- Docker
  - mysql:8.0

### 테스트

```bash
./gradlew test
```

### 애플리케이션 실행

```bash
docker run -it -p 3306:3306 \
    --name wanted-pre-onboarding-backend-database \
    -e MYSQL_ROOT_PASSWORD=root-password \
    -e MYSQL_DATABASE=db \
    -d mysql:8.0 \
    --character-set-server=utf8mb4 \
    --collation-server=utf8mb4_unicode_ci

./gradlew bootRun
```

### API 요청 호스트

```text
http://localhost:8000/
```

## 요구사항 분석

> 요구사항 분석에는 [뱅크샐러드 테크 스펙 템플릿]을 참고했습니다.

- [채용공고 등록]
- [채용공고 수정]
- [채용공고 삭제]
- [채용공고 목록 조회]
- [채용공고 검색]
- [채용공고 상세 페이지 조회]
- [채용공고 지원]

[//]: # (외부 링크 모음)

[안내 링크]: https://bow-hair-db3.notion.site/1850bca26fda4e0ca1410df270c03409

[뱅크샐러드 테크 스펙 템플릿]: https://docs.google.com/document/d/1nhozeUvJYKytE_b_9-YP4Fyw0wtykl9haCG4Wwjb9Ws/edit

[채용공고 등록]: https://docs.google.com/document/d/1SV6C79ap69r2hCcXmEoNEVpMQw8lDQBi8aZNWkKEc3E/edit?usp=sharing
[채용공고 수정]: https://docs.google.com/document/d/13wpi87PiPLiV32N2LgIt5I10uRD98t33PpU__pS1x5A/edit?usp=sharing
[채용공고 삭제]: https://docs.google.com/document/d/1tHIuKnIGatT6ntCYUXRNKZoaAIBDZPIFrmPuuEMaZqg/edit?usp=sharing
[채용공고 목록 조회]: https://docs.google.com/document/d/1eelYv_bYp5m2-OpiJ3b6XXzlLdn4p0mlK5Lwl7zA7BI/edit?usp=sharing
[채용공고 검색]: https://docs.google.com/document/d/1fZvR-h3nuPZD04Hcpq3P_UJOQ1_wT1-2FCquPWf7ljY/edit?usp=sharing
[채용공고 상세 페이지 조회]: https://docs.google.com/document/d/1eUNkkd-Mq-2zocZoo9wgbMyTNPZrilI59pnDm4e8s3A/edit?usp=sharing
[채용공고 지원]: https://docs.google.com/document/d/1Kz9dm95Hm4AiQpUXq78fmTDwGiUcKAQlna3h2Kc8VtM/edit?usp=sharing
