package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.entity.Response;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ResponseRepository extends R2dbcRepository<Response, Long> {

  @Query("SELECT COUNT(*) FROM responses r " +
      "JOIN options o ON r.option_id = o.id " +
      "WHERE r.user_id = :userId AND r.quiz_id = :quizId AND o.is_correct = TRUE")
  Mono<Long> calculateScore(Long userId, Long quizId);
}
