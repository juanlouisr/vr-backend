package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.dto.enums.ResponseStatus;
import com.juan.itb.vrbackend.entity.Response;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ResponseRepository extends R2dbcRepository<Response, Long> {

  @Query("SELECT COUNT(*) FROM responses r " +
      "JOIN options o ON r.option_id = o.id " +
      "WHERE r.user_id = :userId AND r.quiz_id = :quizId AND o.is_correct = TRUE")
  Mono<Long> calculateScore(Long userId, Long quizId);

  Flux<Response> findAllByUserIdAndQuizId(Long userId, Long quizId);

  Mono<Response> findByUserIdAndQuizIdAndQuestionId(Long userId, Long quizId, Long questionId);

  @Modifying
  @Query("UPDATE responses SET option_id = :newOptionId WHERE user_id = :userId AND quiz_id = :quizId AND question_id = :questionId")
  Mono<Long> updateResponse(Long newOptionId, Long userId,
      Long quizId, Long questionId);

  @Modifying
  @Query("UPDATE responses SET status = :status WHERE user_id = :userId AND quiz_id = :quizId")
  Mono<Long> updateResponseStatus(ResponseStatus status, Long userId, Long quizId);
}
