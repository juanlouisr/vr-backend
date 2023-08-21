package com.juan.itb.vrbackend.service.api;

import com.juan.itb.vrbackend.dto.request.RegisterRequest;
import com.juan.itb.vrbackend.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
  Mono<User> registerUser(RegisterRequest registerRequest);
}
