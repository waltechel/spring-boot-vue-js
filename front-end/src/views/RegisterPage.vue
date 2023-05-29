<template>
  <!-- 컴포넌트의 루트 요소 -->
  <div class="container">
    <!-- 가운데 정렬을 위한 row -->
    <div class="row justify-content-center">
      <!-- 회원 가입 폼을 담은 컨테이너 -->
      <div class="register-form">
        <!-- 로고와 태그라인 -->
        <div class="logo-wrapper">
          <img class="logo" src="/static/images/logo.png" />
          <div class="tagline">Open source task management tool</div>
        </div>
        <!-- 회원 가입 폼 -->
        <form @submit.prevent="submitForm">
          <div v-show="errorMessage" class="alert alert-danger failed">
            {{ errorMessage }}
          </div>
          <!-- 사용자명 입력 -->
          <div class="form-group">
            <label for="username">Username</label>
            <input
              type="text"
              class="form-control"
              id="username"
              v-model="form.username"
            />
            <div class="field-error" v-if="$v.form.username.$dirty">
              <div class="error" v-if="!$v.form.username.required">
                Username is required
              </div>
              <div class="error" v-if="!$v.form.username.alphaNum">
                Username can only contain letters and numbers
              </div>
              <div class="error" v-if="!$v.form.username.minLength">
                Username must have at least
                {{ $v.form.username.$params.minLength.min }} letters.
              </div>
              <div class="error" v-if="!$v.form.username.maxLength">
                Username is too long. It can contains maximium
                {{ $v.form.username.$params.maxLength.max }} letters.
              </div>
            </div>
          </div>
          <!-- 이메일 주소 입력 -->
          <div class="form-group">
            <label for="emailAddress">Email address</label>
            <input
              type="email"
              class="form-control"
              id="emailAddress"
              v-model="form.emailAddress"
            />
            <div class="field-error" v-if="$v.form.emailAddress.$dirty">
              <div class="error" v-if="!$v.form.emailAddress.required">
                Email address is required
              </div>
              <div class="error" v-if="!$v.form.emailAddress.email">
                This is not a valid email address
              </div>
              <div class="error" v-if="!$v.form.emailAddress.maxLength">
                Email address is too long. It can contains maximium
                {{ $v.form.emailAddress.$params.maxLength.max }} letters.
              </div>
            </div>
          </div>
          <!-- 비밀번호 입력 -->
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              class="form-control"
              id="password"
              v-model="form.password"
            />
            <div class="field-error" v-if="$v.form.password.$dirty">
              <div class="error" v-if="!$v.form.password.required">
                Password is required
              </div>
              <div class="error" v-if="!$v.form.password.minLength">
                Password is too short. It can contains at least
                {{ $v.form.password.$params.minLength.min }} letters.
              </div>
              <div class="error" v-if="!$v.form.password.maxLength">
                Password is too long. It can contains maximium
                {{ $v.form.password.$params.maxLength.max }} letters.
              </div>
            </div>
          </div>
          <!-- 계정 생성 버튼 -->
          <button type="submit" class="btn btn-primary btn-block">
            Create account
          </button>
          <!-- 약관 및 개인 정보 보호 동의 문구 -->
          <p class="accept-terms text-muted">
            By clicking “Create account”, you agree to our
            <a href="#">terms of service</a> and <a href="#">privacy policy</a>.
          </p>
          <!-- 이미 계정이 있는 경우 로그인 링크 -->
          <p class="text-center text-muted">
            Already have an account?
            <a href="/login">Sign in</a>
          </p>
        </form>
      </div>
    </div>
    <!-- 페이지 하단의 푸터 -->
    <footer class="footer">
      <!-- 저작권 정보 -->
      <span class="copyright">&copy; 2023 TaskAgile.com</span>
      <!-- 푸터 링크들 -->
      <ul class="footer-links list-inline float-right">
        <li class="list-inline-item">
          <a href="#">About</a>
        </li>
        <li class="list-inline-item">
          <a href="#">Terms of Service</a>
        </li>
        <li class="list-inline-item">
          <a href="#">Privacy Policy</a>
        </li>
        <li class="list-inline-item">
          <a
            href="https://github.com/taskagile/vuejs.spring-boot.mysql"
            target="_blank"
            >GitHub</a
          >
        </li>
      </ul>
    </footer>
  </div>
</template>
<script>
import {
  required,
  email,
  minLength,
  maxLength,
  alphaNum,
} from "vuelidate/lib/validators";
import registrationService from "@/services/registration";

export default {
  name: "RegisterPage",
  data() {
    return {
      form: {
        username: "",
        emailAddress: "",
        password: "",
      },
      errorMessage: "",
    };
  },
  validations: {
    form: {
      username: {
        required,
        minLength: minLength(2),
        maxLength: maxLength(50),
        alphaNum,
      },
      emailAddress: {
        required,
        email,
        maxLength: maxLength(100),
      },
      password: {
        required,
        minLength: minLength(6),
        maxLength: maxLength(30),
      },
    },
  },
  methods: {
    submitForm() {
      this.$v.$touch();
      if (this.$v.$invalid) {
        return;
      }

      registrationService
        .register(this.form)
        .then(() => {
          this.$router.push({ name: "LoginPage" });
        })
        .catch((error) => {
          this.errorMessage = "Failed to register user. " + error.message;
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  max-width: 900px;
}
.register-form {
  margin-top: 50px;
  max-width: 320px;
}
.logo-wrapper {
  text-align: center;
  margin-bottom: 40px;
  .tagline {
    line-height: 180%;
    color: #666;
  }
 .logo {
    max-width: 150px;
    margin: 0 auto;
  }
}
.register-form {
  .form-group label {
    font-weight: bold;
    color: #555;
  }
  .accept-terms {
    margin: 20px 0 40px 0;
  }
}
.footer {
  width: 100%;
  font-size: 13px;
  color: #666;
  line-height: 40px;
  border-top: 1px solid #ddd;
  margin-top: 50px;
  .list-inline-item {
    margin-right: 10px;
  }
  a {
    color: #666;
  }
}
</style>