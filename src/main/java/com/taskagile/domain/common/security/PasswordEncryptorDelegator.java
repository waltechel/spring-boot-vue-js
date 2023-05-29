package com.taskagile.domain.common.security;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptorDelegator implements PasswordEncryptor {

  @Override
  public String encrypt(String rawPassword) {
    // TODO implement this
    // 일단은 비밀번호를 그대로 전달하는 것으로 작성
    // 원래는 null을 리턴했지만, 아직 스프링 시큐리티 전이므로 평문 그대로 전송하는 것으로 적용
    return rawPassword;
  }
}