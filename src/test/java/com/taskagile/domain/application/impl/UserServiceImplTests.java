package com.taskagile.domain.application.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.taskagile.domain.application.commands.RegistrationCommand;
import com.taskagile.domain.common.event.DomainEventPublisher;
import com.taskagile.domain.common.mail.MailManager;
import com.taskagile.domain.common.mail.MessageVariable;
import com.taskagile.domain.model.user.EmailAddressExistsException;
import com.taskagile.domain.model.user.RegistrationException;
import com.taskagile.domain.model.user.RegistrationManagement;
import com.taskagile.domain.model.user.SimpleUser;
import com.taskagile.domain.model.user.User;
import com.taskagile.domain.model.user.UserRepository;
import com.taskagile.domain.model.user.UsernameExistsException;
import com.taskagile.domain.model.user.events.UserRegisteredEvent;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

  @Mock
  private RegistrationManagement registrationManagementMock;
  
  @Mock
  private DomainEventPublisher domainEventPublisherMock;
  
  @Mock
  private MailManager mailManagerMock;
  
  @Mock
  private UserRepository userRepositoryMock;
  
  @InjectMocks
  private UserServiceImpl instance;
  
//-------------------------------------------
  // Method loadUserByUsername()
  //-------------------------------------------

  @Test
  public void loadUserByUsername_emptyUsername_shouldFail() {
    Exception exception = null;
    try {
      instance.loadUserByUsername("");
    } catch (Exception e) {
      exception = e;
    }
    Assertions.assertNotNull(exception);
    Assertions.assertTrue(exception instanceof UsernameNotFoundException);
    BDDMockito.then(userRepositoryMock.findByUsername("")).shouldHaveNoInteractions();
    BDDMockito.then(userRepositoryMock.findByEmailAddress("")).shouldHaveNoInteractions();
  }
  
  @Test
  public void loadUserByUsername_notExistUsername_shouldFail() {
    String notExistUsername = "NotExistUsername";
    when(userRepositoryMock.findByUsername(notExistUsername)).thenReturn(null);
    Exception exception = null;
    try {
      instance.loadUserByUsername(notExistUsername);
    } catch (Exception e) {
      exception = e;
    }
    Assertions.assertNotNull(exception);
    Assertions.assertTrue(exception instanceof UsernameNotFoundException);
    BDDMockito.then(userRepositoryMock).should(times(1)).findByUsername(notExistUsername);
    BDDMockito.then(userRepositoryMock.findByUsername(notExistUsername)).shouldHaveNoInteractions();
  }
  
  @Test
  public void loadUserByUsername_existUsername_shouldSucceed() throws IllegalAccessException {
    String existUsername = "ExistUsername";
    User foundUser = User.create(existUsername, "user@taskagile.com", "EncryptedPassword!");
    foundUser.updateName("Test", "User");
    // Found user from the database should have id. And since no setter of
    // id is available in User, we have to write the value to it using reflection
    //
    // Besides creating an actual instance of User, we can also create a user
    // mock, like the following.
    // User mockUser = Mockito.mock(User.class);
    // when(mockUser.getUsername()).thenReturn(existUsername);
    // when(mockUser.getPassword()).thenReturn("EncryptedPassword!");
    // when(mockUser.getId()).thenReturn(1L);
    FieldUtils.writeField(foundUser, "id", 1L, true);
    when(userRepositoryMock.findByUsername(existUsername)).thenReturn(foundUser);
    Exception exception = null;
    UserDetails userDetails = null;
    try {
      userDetails = instance.loadUserByUsername(existUsername);
    } catch (Exception e) {
      exception = e;
    }
    Assertions.assertNull(exception);
    BDDMockito.then(userRepositoryMock).should(times(1)).findByUsername(existUsername);
    BDDMockito.then(userRepositoryMock.findByEmailAddress(existUsername)).shouldHaveNoInteractions();
    Assertions.assertNotNull(userDetails);
    Assertions.assertEquals(existUsername, userDetails.getUsername());
    Assertions.assertTrue(userDetails instanceof SimpleUser);
  }

  //-------------------------------------------
  // Method register()
  //-------------------------------------------

  @Test
  public void register_nullCommand_shouldFail() throws RegistrationException {
    // given
    
    // when
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      instance.register(null);      
    });
    // then
  }

  @Test
  public void register_existingUsername_shouldFail() throws RegistrationException {
    String username = "existing";
    String emailAddress = "sunny@taskagile.com";
    String password = "MyPassword!";
    doThrow(UsernameExistsException.class).when(registrationManagementMock)
      .register(username, emailAddress, password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    
    Assertions.assertThrows(RegistrationException.class, () -> {
      instance.register(command);      
    });
  }

  @Test
  public void register_existingEmailAddress_shouldFail() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "existing@taskagile.com";
    String password = "MyPassword!";
    doThrow(EmailAddressExistsException.class).when(registrationManagementMock)
      .register(username, emailAddress, password);

    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
    
    Assertions.assertThrows(RegistrationException.class, () -> {
      instance.register(command);      
    });
  }

  @Test
  public void register_validCommand_shouldSucceed() throws RegistrationException {
    String username = "sunny";
    String emailAddress = "sunny@taskagile.com";
    String password = "MyPassword!";
    User newUser = User.create(username, emailAddress, password);
    when(registrationManagementMock.register(username, emailAddress, password))
      .thenReturn(newUser);
    RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);

    instance.register(command);

    verify(mailManagerMock).send(
      emailAddress,
      "Welcome to TaskAgile",
      "welcome.ftl",
      MessageVariable.from("user", newUser)
    );
    verify(domainEventPublisherMock).publish(new UserRegisteredEvent(newUser));
  }
}