package com.taskagile.config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix="app")
@Validated
@Getter
@Setter
public class ApplicationProperties {

  /**
   * Default `from` value of emails sent out by the system
   */
  @Email
  @NotBlank
  private String mailFrom;

}