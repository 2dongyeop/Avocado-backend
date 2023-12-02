# TEAM Avocado

> ### *Who are we?*
- 한밭대학교 2023년 캡스톤 디자인 팀으로 멤버 구성은 아래와 같습니다.
- 팀장 : [임세나](https://github.com/LimSeNa), 담당 : Front-end Web Server 개발
- 팀원 : [이동엽](https://github.com/2dongyeop), 담당 : Back-end WAS 개발

<br/>

<img src="https://github.com/HBNU-Avocado/Avocado-backend/blob/main/document/image/prototype1.jpg" width = 600/>

<br/>

> ### *Overview.*
*현재 수많은 플랫폼에서 리뷰 서비스를 제공하지만, 아직까지 병원비를 공개하는 플랫폼은 없다.*

*리뷰를 통해 가격 비교를 할 수 있고, 나아가 예약 서비스와 픽업 서비스를 부가 기능으로 제공한다.*

*따라서, 고령화 사회에 맞춰 고령층을 포함한 모든 연령층이 사용하기 쉬운 웹 리뷰 플랫폼을 제작한다.*

<br/>

<br/>

> ### *Document.*
  - [프로젝트 노션 링크](https://leedongyeop.notion.site/Avocado-287972df87fa4a8bb976bba7649919ca)
    - [요구사항 설게](https://www.notion.so/leedongyeop/d7c5da5175b14c71b6cbe21650607ebd)
    - [데이터베이스 설계](https://www.notion.so/leedongyeop/0badd5357d8a41bfb9b78db729c24ed7)
    - [시퀀스 다이어그램 및 ERD](https://www.notion.so/leedongyeop/204db9578a2b44be877399d3e5d0b6b4)
    - [API 설계도](https://www.notion.so/leedongyeop/API-102a2f25e370479195fa616572f369ff)
    - [트러블슈팅](https://www.notion.so/leedongyeop/0459864398e84439bfa679d2470aeb8e)
  - [프론트 웹 서버 레포](https://github.com/HBNU-Avocado/Avocado-frontend)
  - [백엔드 API 서버 레포](https://github.com/HBNU-Avocado/Avocado-backend)

<br/>

<br/>

> ### *Features*
- [헥사고날 아키텍처 리팩토링 레포](https://github.com/HBNU-Avocado/Avocado-backend-with-hexagonal)
- [이메일 송신 기능 성능 개선. 7초 -> 1.7초](https://velog.io/@dongvelop/CompletableFuture%EB%A1%9C-%EB%B9%84%EB%8F%99%EA%B8%B0-%EC%9E%91%EC%97%85-%EC%95%88%EC%A0%95%ED%99%94-%EB%B0%8F-%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8)
- [214개의 테스트 코드 작성](https://github.com/HBNU-Avocado/Avocado-backend/pull/174) 및 [테스트 커버리지 72% 유지](https://github.com/HBNU-Avocado/Avocado-backend/issues/175)
- [악의적 외부 API 호출로 인한 과금을 막기 위한 처리율 제한 장치 구축](https://velog.io/@dongvelop/Spring-Boot-Bucket4j%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4-%EC%84%9C%EB%B2%84-%EC%B8%A1-%EC%B2%98%EB%A6%AC%EC%9C%A8-%EC%A0%9C%ED%95%9C-%EC%9E%A5%EC%B9%98-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0)
- [결제 및 결제 취소 기능 구축](https://www.notion.so/leedongyeop/e12cf498c0d643a5947d284e19884e4e)
- [Swagger 사용시 중복 코드 최소화하기](https://velog.io/@dongvelop/Swagger-%EB%8F%84%EC%9E%85%EC%9C%BC%EB%A1%9C-%EC%9D%B8%ED%95%9C-%EC%A4%91%EB%B3%B5-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%B6%94%EC%83%81%ED%99%94%ED%95%98%EA%B8%B0)

<br/>

<br/>

> ### *Sequence Diagram*

<img src="https://github.com/HBNU-Avocado/Avocado-backend/blob/main/document/image/Sequence-diagram-v3.png" width = 800/>


<br/>

<br/>

> ### *Environment.*
- OS
  - macOS Ventura `13.2.1`
- Language
  - Java 17
- Framework
  - org.springframework.boot `3.0.2`
  - JUnit5
  - Rest Assured
- Datebase
  - PostgreSQL
  - Redis
- Etc
  - Docker
