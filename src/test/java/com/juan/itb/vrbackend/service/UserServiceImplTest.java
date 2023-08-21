package com.juan.itb.vrbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.juan.itb.vrbackend.dto.request.RegisterRequest;
import com.juan.itb.vrbackend.entity.User;
import com.juan.itb.vrbackend.repository.UserRepository;
import com.juan.itb.vrbackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserRepository userRepository;

  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Test
  void testRegisterUser() {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .email("test@example.com")
        .username("testuser")
        .password("testpassword")
        .build();

    String encodedPassword = "encodedPassword"; // Replace with your expected encoded password

    when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(encodedPassword);
    when(userRepository.save(any(User.class))).thenReturn(Mono.just(new User()));

    StepVerifier.create(userService.registerUser(registerRequest))
        .expectNextCount(1)
        .expectComplete()
        .verify();

    verify(passwordEncoder).encode(registerRequest.getPassword());
    verify(userRepository).save(userCaptor.capture());

    User capturedUser = userCaptor.getValue();
    assertEquals(registerRequest.getEmail(), capturedUser.getEmail());
    assertEquals(registerRequest.getUsername(), capturedUser.getUsername());
    assertEquals(encodedPassword, capturedUser.getHashedPassword());
  }
}
