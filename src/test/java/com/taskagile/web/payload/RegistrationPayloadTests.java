package com.taskagile.web.payload;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationPayloadTests {

  private Validator validator;

  @BeforeEach
  public void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void validate_blankPayload_shouldFail() {
    RegistrationPayload payload = new RegistrationPayload();

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(3, violations.size());
  }


  @Test
  public void validate_payloadWithInvalidEmail_shouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress("BadEmailAddress");
    payload.setUsername("MyUsername");
    payload.setPassword("MyPassword");

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size());
  }

  @Test
  public void validate_payloadWithEmailAddressLongerThan100_shouldFail() {
    // The maximium allowed localPart is 64 characters
    // http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    int maxLocalParthLength = 64;
    String localPart = RandomStringUtils.random(maxLocalParthLength, true, true);
    int usedLength = maxLocalParthLength + "@".length() + ".com".length();
    String domain = RandomStringUtils.random(101 - usedLength, true, true);

    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailAddress(localPart + "@" + domain + ".com");
    payload.setUsername("MyUsername");
    payload.setPassword("MyPassword");

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size());
  }

  @Test
  public void validate_payloadWithUsernameShorterThan2_shouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    String usernameTooShort = RandomStringUtils.random(1);
    payload.setUsername(usernameTooShort);
    payload.setPassword("MyPassword");
    payload.setEmailAddress("sunny@taskagile.com");

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size());
  }

  @Test
  public void validate_payloadWithUsernameLongerThan50_shouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    String usernameTooLong = RandomStringUtils.random(51);
    payload.setUsername(usernameTooLong);
    payload.setPassword("MyPassword");
    payload.setEmailAddress("sunny@taskagile.com");

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size());
  }

  @Test
  public void validate_payloadWithPasswordShorterThan6_shouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    String passwordTooShort = RandomStringUtils.random(5);
    payload.setPassword(passwordTooShort);
    payload.setUsername("MyUsername");
    payload.setEmailAddress("sunny@taskagile.com");

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size());
  }

  @Test
  public void validate_payloadWithPasswordLongerThan30_shouldFail() {
    RegistrationPayload payload = new RegistrationPayload();
    String passwordTooLong = RandomStringUtils.random(31);
    payload.setPassword(passwordTooLong);
    payload.setUsername("MyUsername");
    payload.setEmailAddress("sunny@taskagile.com");

    Set<ConstraintViolation<RegistrationPayload>> violations = validator.validate(payload);
    Assertions.assertEquals(1, violations.size());
  }

}