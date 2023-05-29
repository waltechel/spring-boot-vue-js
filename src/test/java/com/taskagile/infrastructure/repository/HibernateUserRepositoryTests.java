package com.taskagile.infrastructure.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.taskagile.domain.model.user.User;
import com.taskagile.domain.model.user.UserRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class HibernateUserRepositoryTests {

  @TestConfiguration
  public static class UserRepositoryTestContextConfiguration {
    @Bean
    public UserRepository userRepository(EntityManager entityManager) {
      return new HibernateUserRepository(entityManager);
    }
  }

  @Autowired
  private UserRepository repository;

  @Test
  public void save_nullUsernameUser_shouldFail() {
    User inavlidUser = User.create(null, "sunny@taskagile.com", "MyPassword!");
    Assertions.assertThrows(PersistenceException.class, () -> {
      repository.save(inavlidUser);
    });
  }

  @Test
  public void save_nullEmailAddressUser_shouldFail() {
    User inavlidUser = User.create("sunny", null, "MyPassword!");
    Assertions.assertThrows(PersistenceException.class, () -> {
      repository.save(inavlidUser);
    });
  }

  @Test
  public void save_nullPasswordUser_shouldFail() {
    User inavlidUser = User.create("sunny", "sunny@taskagile.com", null);
    Assertions.assertThrows(PersistenceException.class, () -> {
      repository.save(inavlidUser);
    });
  }

  @Test
  public void save_validUser_shouldSuccess() {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User newUser = User.create(username, emailAddress, "MyPassword!");
    repository.save(newUser);
    Assertions.assertAll("결괏값 검증", () -> {
      Assertions.assertNotNull(newUser.getId());
      Assertions.assertNotNull(newUser.getCreatedDate());
      Assertions.assertEquals(username, newUser.getUsername());
      Assertions.assertEquals(emailAddress, newUser.getEmailAddress());
      Assertions.assertEquals("", newUser.getFirstName());
      Assertions.assertEquals("", newUser.getLastName());
    });
  }

  @Test
  public void save_usernameAlreadyExist_shouldFail() {
    // Create already exist user
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User alreadyExist = User.create(username, emailAddress, "MyPassword!");
    repository.save(alreadyExist);

    try {
      User newUser = User.create(username, "new@taskagile.com", "MyPassword!");
      repository.save(newUser);
    } catch (Exception e) {
      Assertions.assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
    }
  }

  @Test
  public void save_emailAddressAlreadyExist_shouldFail() {
    // Create already exist user
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User alreadyExist = User.create(username, emailAddress, "MyPassword!");
    repository.save(alreadyExist);

    try {
      User newUser = User.create("new", emailAddress, "MyPassword!");
      repository.save(newUser);
    } catch (Exception e) {
      Assertions.assertEquals(ConstraintViolationException.class.toString(), e.getCause().getClass().toString());
    }
  }

  @Test
  public void findByEmailAddress_notExist_shouldReturnEmptyResult() {
    String emailAddress = "sunny@taskagile.com";
    User user = repository.findByEmailAddress(emailAddress);
    Assertions.assertNull(user);
  }

  @Test
  public void findByEmailAddress_exist_shouldReturnResult() {
    String emailAddress = "sunny@taskagile.com";
    String username = "sunny";
    User newUser = User.create(username, emailAddress, "MyPassword!");
    repository.save(newUser);
    User found = repository.findByEmailAddress(emailAddress);
    Assertions.assertEquals("Username should match", username, found.getUsername());
  }

  @Test
  public void findByUsername_notExist_shouldReturnEmptyResult() {
    String username = "sunny";
    User user = repository.findByUsername(username);
    Assertions.assertNull(user, "No user should by found");
  }

  @Test
  public void findByUsername_exist_shouldReturnResult() {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    User newUser = User.create(username, emailAddress, "MyPassword!");
    repository.save(newUser);
    User found = repository.findByUsername(username);
    Assertions.assertEquals("Email address should match", emailAddress, found.getEmailAddress());
  }
}