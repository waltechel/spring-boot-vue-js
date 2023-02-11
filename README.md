# TaskAgile

Open source task management tool built with Vue.js 2, Spring Boot 2, and MySQL 5.7+

- 지금 이 소스는 <스프링 5와 Vue.js2로 시작하는 모던 웹 애플리케이션 개발>을 보고 따라한다.

## Local development

Create `src/main/resources/application-dev.properties` with the following settings to override the settings in `application.properties`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_agile?useSSL=false
spring.datasource.username=<your username>
spring.datasource.password=<your password>
```

## Commands

- Use `mvn install` to build both the front-end and the back-end
- Use `mvn test` to run the tests of the back-end and the front-end
- Use `mvn spring-boot:run` to start the back-end
- Use `npm run serve` inside the `front-end` directory to start the front-end
- Use `java -jar target/app-0.0.1-SNAPSHOT.jar` to start the bundled application