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
- `npm install @vue/test-utils --save-dev`로 단위 테스트 라이브러리를 추가한다.
  - `npm install`: npm 패키지 매니저를 사용하여 패키지를 설치
  - `@vue/test-utils`: Vue.js 애플리케이션의 테스트를 작성하기 위한 유틸리티 라이브러리인 @vue/test-utils 패키지.     
    이 패키지는 Vue 컴포넌트를 마운트하고, 조작하고, 검증하는 데 사용
  - `--save-dev`: 패키지를 개발 의존성으로 설치하는 옵션
    - 개발 의존성은 프로젝트의 개발 단계에서 필요한 패키지로, 실제 애플리케이션 실행에는 필요하지 않은 경우 사용
    - 개발 의존성은 package.json 파일의 devDependencies 섹션에 저장되며, npm install 명령어 실행 시 함께 설치
- 계속해서 eslint 문제 생기면 다음 명령어로 해결
```javascript
$ npm run lint -- --fix

> front-end@0.1.0 lint D:\repositories\spring-boot-vue-js\front-end
> vue-cli-service lint "--fix"

The following files have been auto-fixed:

  src\views\RegisterPage.vue     
  tests\unit\RegisterPage.spec.js
  vue.config.js

 DONE  All lint errors auto-fixed.
```
- 다음 명령어로 axios와 axios의 모의 객체 라이브러리인 moxios를 설치하도록 한다.
```powershell
npm install axios --save
npm install moxios --save-dev
```
- 다음 명령어로 Jest 의 에러를 잡을 수 있다.
```powershell
npm install @babel/core
npm install @babel/preset-env
npm install @babel/plugin-transform-modules-commonjs
npm install @babel/plugin-transform-runtime
```

```javascript
module.exports = {
  presets: ["@babel/preset-env"],
  env: {
    test: {
      plugins: [
        '@babel/plugin-transform-modules-commonjs',
        '@babel/plugin-transform-runtime'
      ]
    }
  }
}
```

### 프런트엔드 검증

다음 명령어로 Vuelidate를 설치한다.

```powershell
npm install vuelidate --save
```