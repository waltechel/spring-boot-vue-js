package com.taskagile.domain.application.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter // 이건 게터만 사용
public class RegistrationCommand {

  private String username;
  private String emailAddress;
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegistrationCommand that = (RegistrationCommand) o;
    if (username != null ? !username.equals(that.username) : that.username != null) return false;
    if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
    return password != null ? password.equals(that.password) : that.password == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RegistrationCommand{" +
      "username='" + username + '\'' +
      ", emailAddress='" + emailAddress + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
}