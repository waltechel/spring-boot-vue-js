package com.taskagile.infrastructure.mail;

import com.taskagile.domain.common.mail.SimpleMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AsyncMailerTests {

  @Mock
  private JavaMailSender mailSenderMock;
  
  @InjectMocks
  private AsyncMailer instance;

  @Test
  public void send_nullMessage_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send(null);      
    });
  }

  @Test
  public void send_validMessage_shouldSucceed() {
    String from = "system@taskagile.com";
    String to = "console.output@taskagile.com";
    String subject = "A test message";
    String body = "Username: test, Email Address: test@taskagile.com";

    SimpleMessage message = new SimpleMessage(to, subject, body, from);
    instance.send(message);

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(from);
    simpleMailMessage.setTo(to);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText("Username: test, Email Address: test@taskagile.com");
    verify(mailSenderMock).send(simpleMailMessage);
  }

}