package com.juan.itb.vrbackend.service.impl;

import com.juan.itb.vrbackend.dto.request.RegisterRequest;
import com.juan.itb.vrbackend.entity.User;
import com.juan.itb.vrbackend.repository.UserRepository;
import com.juan.itb.vrbackend.service.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public Mono<User> registerUser(RegisterRequest registerRequest) {
    log.info("{} register", registerRequest);
    return userRepository.save(User
        .builder()
        .email(registerRequest.getEmail())
        .username(registerRequest.getUsername())
        .hashedPassword(passwordEncoder.encode(registerRequest.getPassword()))
        .build());
  }
}
