package com.juan.itb.vrbackend.controller;

import com.juan.itb.vrbackend.dto.request.RegisterRequest;
import com.juan.itb.vrbackend.dto.response.BaseResponse;
import com.juan.itb.vrbackend.entity.User;
import com.juan.itb.vrbackend.service.api.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  @Autowired
  private UserService userService;

  @PostMapping(path = "/register")
  public Mono<BaseResponse<User>> register(@Valid @RequestBody RegisterRequest registerRequest) {
    return userService.registerUser(registerRequest)
         .map(BaseResponse::ok);
  }
}
