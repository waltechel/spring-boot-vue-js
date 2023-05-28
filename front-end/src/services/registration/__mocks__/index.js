export default {
  register (detail) {
    return new Promise((resolve, reject) => {
      // 입력된 이메일 주소가 'sunny@local'인 경우 성공 리턴
      // 그 외의 경우 'User already exist'라는 에러 리턴
      detail.emailAddress === 'sunny@local'
        ? resolve({result: 'success'})
        : reject(new Error('User already exist'))
    })
  }
}