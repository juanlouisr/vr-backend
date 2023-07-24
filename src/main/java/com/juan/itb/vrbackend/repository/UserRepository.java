package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

  Mono<User> findUserByUsername(String username);
}
