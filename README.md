# TaskAgile

Open source task management tool built with Vue.js 2, Spring Boot 2, and MySQL 5.7+

## Local development

Create `src/main/resources/application-dev.properties` with the following settings to override the settings in `application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_agile?useSSL=false
spring.datasource.username=<your username>
spring.datasource.password=<your password>
```

## Commands

- Use `mvn install` to build both the front-end and the back-end    
  해당 명령어로 커밋 가능 여부를 확인할 수 있다.
- Use `mvn test` to run the tests of the back-end and the front-end
- Use `mvn spring-boot:run` to start the back-end
- Use `npm run serve` inside the `front-end` directory to start the front-end
- Use `java -jar target/app-0.0.1-SNAPSHOT.jar` to start the bundled application

## 09 폼과 검증 - 회원가입 페이지부터 시작하기

- `npm install jquery popper.js bootstrap --save` 를 이용하여 부트스트랩 설치
- 단위 테스트 메서드는 `mvn clean install` 명령어를 실행할 때마다 애플리케이션의 건강을 확인해주는 작은 QA 봇으로 생각해보자.