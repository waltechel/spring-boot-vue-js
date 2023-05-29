package com.taskagile.domain.common.mail;

import freemarker.template.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ActiveProfiles("test")
public class DefaultMailManagerTests {

  @TestConfiguration
  static class DefaultMessageCreatorConfiguration {
    @Bean
    public FreeMarkerConfigurationFactoryBean getFreemarkerConfiguration() {
      FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
      factoryBean.setTemplateLoaderPath("/mail-templates/");
      return factoryBean;
    }
  }

  @Autowired
  private Configuration configuration;

  @Mock
  private Mailer mailerMock;

  private DefaultMailManager instance;

  @BeforeEach
  public void setUp() {
    instance = new DefaultMailManager("noreply@taskagile.com", mailerMock, configuration);
  }

  @Test
  public void send_nullEmailAddress_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send(null, "Test subject", "test.ftl");
    });
  }

  @Test
  public void send_emptyEmailAddress_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send("", "Test subject", "test.ftl");
    });
  }

  @Test
  public void send_nullSubject_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send("test@taskagile.com", null, "test.ftl");
    });
  }

  @Test
  public void send_emptySubject_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send("test@taskagile.com", "", "test.ftl");
    });
  }

  @Test
  public void send_nullTemplateName_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send("test@taskagile.com", "Test subject", null);
    });
  }

  @Test
  public void send_emptyTemplateName_shouldFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.send("test@taskagile.com", "Test subject", "");
    });
  }

  @Test
  public void send_validParameters_shouldSucceed() {
    String to = "user@example.com";
    String subject = "Test subject";
    String templateName = "test.ftl";

    instance.send(to, subject, templateName, MessageVariable.from("name", "test"));
    ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    verify(mailerMock).send(messageArgumentCaptor.capture());

    Message messageSent = messageArgumentCaptor.getValue();
    Assertions.assertEquals(to, messageSent.getTo());
    Assertions.assertEquals(subject, messageSent.getSubject());
    Assertions.assertEquals("noreply@taskagile.com", messageSent.getFrom());
    Assertions.assertEquals("Hello, test\n", messageSent.getBody());
  }
}