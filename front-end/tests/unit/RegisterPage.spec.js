import { mount, createLocalVue } from "@vue/test-utils";
import RegisterPage from "@/views/RegisterPage";
import VueRouter from "vue-router";
import Vuelidate from "vuelidate";
import registrationService from "@/services/registration";

// vm.$router에 접근할 수 있도록
// 테스트에 Vue Router 추가
const localVue = createLocalVue();
localVue.use(VueRouter);
localVue.use(Vuelidate);
const router = new VueRouter();

// registratioService의 모의 객체
jest.mock("@/services/registration");

describe("RegisterPage.vue", () => {
  let wrapper;
  let fieldUsername;
  let fieldEmailAddress;
  let fieldPassword;
  let buttonSubmit;
  let registerSpy;

  beforeEach(() => {
    wrapper = mount(RegisterPage, {
      localVue,
      router,
    });
    fieldUsername = wrapper.find("#username");
    fieldEmailAddress = wrapper.find("#emailAddress");
    fieldPassword = wrapper.find("#password");
    buttonSubmit = wrapper.find('form button[type="submit"]');
    // Create spy for registration service
    registerSpy = jest.spyOn(registrationService, "register");
  });

  afterEach(() => {
    registerSpy.mockReset();
    registerSpy.mockRestore();
  });

  afterAll(() => {
    jest.restoreAllMocks();
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
    const emailAddress = "sunny@taskagile.com";
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
  it("should register when it is a new user", async () => {
    expect.assertions(2);
    const stub = jest.fn();
    // 스텁을 만들어 리다이렉트가 발생했는지를 확인
    wrapper.vm.$router.push = stub;
    wrapper.vm.form.username = "sunny";
    wrapper.vm.form.emailAddress = "sunny@taskagile.com";
    wrapper.vm.form.password = "JestRocks!";
    wrapper.vm.submitForm();
    expect(registerSpy).toBeCalled();
    await wrapper.vm.$nextTick();
    expect(stub).toHaveBeenCalledWith({ name: "LoginPage" });
  });

  // 회원가입 실패를 검증하는 테스트(여기가 틀리는데 사실 원인을 몰라서 넘어감)
  it("should fail it is not a new user", async () => {
    expect.assertions(3);
    // 모의 객체에서는 sunny@taskagile.com'만이 새로운 사용자가 된다.
    const username = "bad";
    const emailAddress = "bad@taskagile.com";
    const password = "JestRocks!";
    wrapper.vm.form.username = username;
    wrapper.vm.form.emailAddress = emailAddress;
    wrapper.vm.form.password = password;
    expect(wrapper.find(".failed").isVisible()).toBe(false);
    wrapper.vm.submitForm();
    expect(registerSpy).toBeCalled();
    await wrapper.vm.$nextTick();
    expect(wrapper.find(".failed").isVisible()).toBe(true);
  });

  it("should fail when the email address is invalid", () => {
    wrapper.vm.form.username = "test";
    wrapper.vm.form.emailAddress = "bad-email-address";
    wrapper.vm.form.password = "JestRocks!";
    wrapper.vm.submitForm();
    expect(registerSpy).not.toHaveBeenCalled();
  });

  it("should fail when the username is invalid", () => {
    wrapper.vm.form.username = "a";
    wrapper.vm.form.emailAddress = "test@taskagile.com";
    wrapper.vm.form.password = "JestRocks!";
    wrapper.vm.submitForm();
    expect(registerSpy).not.toHaveBeenCalled();
  });

  it("should fail when the password is invalid", () => {
    wrapper.vm.form.username = "test";
    wrapper.vm.form.emailAddress = "test@taskagile.com";
    wrapper.vm.form.password = "bad!";
    wrapper.vm.submitForm();
    expect(registerSpy).not.toHaveBeenCalled();
  });
});
