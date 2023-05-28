import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from 'axios';

// Bootstrap axios. 모든 요청에 api 경로를 추가하지 않아도 동작하도록 baseURL을 설정
axios.defaults.baseURL = '/api'
axios.defaults.headers.common.Accept = 'application/json'
// 성공적인 응답은 그대로 리턴하고, 오류 응답은 Promise를 통해 오류 처리를 수행
axios.interceptors.response.use(
  response => response,
  (error) => {
    return Promise.reject(error)
  }
);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
