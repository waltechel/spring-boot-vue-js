package com.taskagile.domain.model.user;

import org.springframework.stereotype.Component;

import com.taskagile.domain.common.security.PasswordEncryptor;

import lombok.RequiredArgsConstructor;

/**
 * User registration domain service
 */
@Component
@RequiredArgsConstructor
public class RegistrationManagement {

  private final UserRepository repository;
  private final PasswordEncryptor passwordEncryptor;

  public User register(String username, String emailAddress, String password)
    throws RegistrationException {
    User existingUser = repository.findByUsername(username);
    if (existingUser != null) {
      throw new UsernameExistsException();
    }

    existingUser = repository.findByEmailAddress(emailAddress.toLowerCase());
    if (existingUser != null) {
      throw new EmailAddressExistsException();
    }

    String encryptedPassword = passwordEncryptor.encrypt(password);
    User newUser = User.create(username, emailAddress.toLowerCase(), encryptedPassword);
    repository.save(newUser);
    return newUser;
  }
}