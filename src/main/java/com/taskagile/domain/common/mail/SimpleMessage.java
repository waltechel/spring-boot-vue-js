package com.taskagile.domain.common.mail;

import java.util.Objects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleMessage implements Message {

  private String to;
  private String subject;
  private String body;
  private String from;

  @Override
  public String getTo() {
    return to;
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public String getBody() {
    return body;
  }

  public String getFrom() {
    return from;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SimpleMessage)) return false;
    SimpleMessage that = (SimpleMessage) o;
    return Objects.equals(to, that.to) &&
      Objects.equals(subject, that.subject) &&
      Objects.equals(body, that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(to, subject, body);
  }

  @Override
  public String toString() {
    return "SimpleMessage{" +
      "to='" + to + '\'' +
      ", subject='" + subject + '\'' +
      ", body='" + body + '\'' +
      '}';
  }
}