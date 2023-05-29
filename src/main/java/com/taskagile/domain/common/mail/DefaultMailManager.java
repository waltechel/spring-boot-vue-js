package com.taskagile.domain.common.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultMailManager implements MailManager {

  private final static Logger log = LoggerFactory.getLogger(DefaultMailManager.class);

  @Value("${app.mail-from}")
  private String mailFrom;
  
  private final Mailer mailer;
  private final Configuration configuration;

  @Override
  public void send(String emailAddress, String subject, String template, MessageVariable... variables) {
    Assert.hasText(emailAddress, "Parameter `emailAddress` must not be blank");
    Assert.hasText(subject, "Parameter `subject` must not be blank");
    Assert.hasText(template, "Parameter `template` must not be blank");

    String messageBody = createMessageBody(template, variables);
    Message message = new SimpleMessage(emailAddress, subject, messageBody, mailFrom);
    mailer.send(message);
  }

  private String createMessageBody(String templateName, MessageVariable... variables) {
    try {
      Template template = configuration.getTemplate(templateName);
      Map<String, Object> model = new HashMap<>();
      if (variables != null) {
        for (MessageVariable variable : variables) {
          model.put(variable.getKey(), variable.getValue());
        }
      }
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    } catch (Exception e) {
      log.error("Failed to create message body from template `" + templateName + "`", e);
      return null;
    }
  }

}