// moxios 패키지는 Axios 라이브러리와 함께 사용되어 HTTP 요청을 가로채고 가짜 응답을 제공하여 테스트하는 데 사용
import moxios from "moxios";
import registrationService from "@/services/registration";

describe("services/registration", () => {
  // 테스트는 beforeEach와 afterEach 블록을 사용하여 각각 테스트 전에 moxios.install()을 호출하여 moxios를 설치하고,
  // 테스트 후에 moxios.uninstall()을 호출하여 moxios를 해제
  beforeEach(() => {
    moxios.install();
  });

  afterEach(() => {
    moxios.uninstall();
  });

  // 성공적으로 호출했는지를 검증
  it("should pass the response to caller when request succeeded", () => {
    expect.assertions(2);
    moxios.wait(() => {
      let request = moxios.requests.mostRecent();
      expect(request).toBeTruthy();
      request.respondWith({
        status: 200,
        response: { result: "success" },
      });
    });
    return registrationService.register().then((data) => {
      expect(data.result).toEqual("success");
    });
  });

  // HTTP 요청이 실패하는 시나리오의 테스트
  it("should propagate the error to caller when request failed", () => {
    expect.assertions(2);
    moxios.wait(() => {
      let request = moxios.requests.mostRecent();
      expect(request).toBeTruthy();
      request.reject({
        response: {
          status: 400,
          response: { message: "Bad request" },
        },
      });
    });
    return registrationService.register().catch((error) => {
      expect(error.message).toEqual("Bad request");
    });
  });

  // 누락된 테스트 추가
  it("should call `/registrations` API", () => {
    expect.assertions(1);
    moxios.wait(() => {
      let request = moxios.requests.mostRecent();
      expect(request.url).toEqual("/registrations");
      request.respondWith({
        status: 200,
        response: { result: "success" },
      });
    });
    return registrationService.register();
  });
});
