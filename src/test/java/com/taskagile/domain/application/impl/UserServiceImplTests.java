package com.taskagile.domain.application.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskagile.domain.application.commands.RegistrationCommand;
import com.taskagile.domain.common.event.DomainEventPublisher;
import com.taskagile.domain.common.mail.MailManager;
import com.taskagile.domain.common.mail.MessageVariable;
import com.taskagile.domain.model.user.EmailAddressExistsException;
import com.taskagile.domain.model.user.RegistrationException;
import com.taskagile.domain.model.user.RegistrationManagement;
import com.taskagile.domain.model.user.User;
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
  
  @InjectMocks
  private UserServiceImpl instance;

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