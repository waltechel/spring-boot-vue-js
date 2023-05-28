import { mount, createLocalVue } from '@vue/test-utils';
import RegisterPage from "@/views/RegisterPage";
import VueRouter from 'vue-router';

// vm.$router에 접근할 수 있도록
// 테스트에 Vue Router 추가
const localVue = createLocalVue()
localVue.use(VueRouter)
const router = new VueRouter()

// registratioService의 모의 객체
jest.mock('@/services/registration')

describe("RegisterPage.vue", () => {
  let wrapper;
  let fieldUsername;
  let fieldEmailAddress;
  let fieldPassword;
  let buttonSubmit;

  beforeEach(() => {
    wrapper = mount(RegisterPage, {
      localVue,
      router
    });
    fieldUsername = wrapper.find("#username");
    fieldEmailAddress = wrapper.find("#emailAddress");
    fieldPassword = wrapper.find("#password");
    buttonSubmit = wrapper.find('form button[type="submit"]');
  });

  afterAll(() => {
    jest.restoreAllMocks()
  });

  it("should render registration form", () => {
    expect(wrapper.find(".logo").attributes().src).toEqual(
      "/static/images/logo.png"
    );
    expect(wrapper.find(".tagline").text()).toEqual(
      "Open source task management tool"
    );
    expect(fieldUsername.element.value).toEqual("");
    expect(fieldEmailAddress.element.value).toEqual("");
    expect(fieldPassword.element.value).toEqual("");
    expect(buttonSubmit.text()).toEqual("Create account");
  });

  // 모델의 초깃값을 검증하는 테스트
  it("should contain data model with initial values", () => {
    expect(wrapper.vm.form.username).toEqual("");
    expect(wrapper.vm.form.emailAddress).toEqual("");
    expect(wrapper.vm.form.password).toEqual("");
  });

  // 폼의 입력과 데이터 바인딩을 검증하는 테스트
  it("should have form inputs bound with data model", async () => {
    const username = "sunny";
    const emailAddress = "sunny@local";
    const password = "VueJsRocks!";

    wrapper.vm.form.username = username;
    wrapper.vm.form.emailAddress = emailAddress;
    wrapper.vm.form.password = password;
    await wrapper.vm.$nextTick();
    expect(fieldUsername.element.value).toEqual(username);
    expect(fieldEmailAddress.element.value).toEqual(emailAddress);
    expect(fieldPassword.element.value).toEqual(password);
  });

  // 제출 핸들러의 존재 여부를 확인하는 테스트
  it("should have form submit event handler `submitForm`", () => {
    const stub = jest.fn();
    wrapper.setMethods({ submitForm: stub }); // Use `setMethods` to set the method
    // wrapper.vm.$options.methods.submitForm = stub;
    wrapper.find("form").trigger("submit");
    expect(stub).toBeCalled();
  });

  // 성공적인 회원가입을 검증하는 테스트
  it('should register when it is a new user', () => {
    const stub = jest.fn()
    // 스텁을 만들어 리다이렉트가 발생했는지를 확인
    wrapper.vm.$router.push = stub 
    wrapper.vm.form.username = 'sunny'
    wrapper.vm.form.emailAddress = 'sunny@local'
    wrapper.vm.form.password = 'Jest!'
    wrapper.vm.submitForm()
    wrapper.vm.$nextTick(() => {
      expect(stub).toHaveBeenCalledWith({name: 'LoginPage'})
    })
  });

  // 회원가입 실패를 검증하는 테스트
  it('should fail it is not a new user', () => {
    // 모의 객체에서는 sunny@local만이 새로운 사용자가 된다.
    wrapper.vm.form.emailAddress = 'ted@local'
    expect(wrapper.find('.failed').isVisible()).toBe(false)
    wrapper.vm.submitForm()
    wrapper.vm.$nextTick(null, () => {
      expect(wrapper.find('.failed').isVisible()).toBe(true)
    })
  });

});
