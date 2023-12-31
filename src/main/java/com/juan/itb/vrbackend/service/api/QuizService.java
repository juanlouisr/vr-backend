package com.juan.itb.vrbackend.service.api;

import com.juan.itb.vrbackend.dto.request.CreateQuizRequest;
import com.juan.itb.vrbackend.dto.request.CreateResponseRequest;
import com.juan.itb.vrbackend.dto.request.FinalizeResponseRequest;
import com.juan.itb.vrbackend.dto.response.QuizDto;
import com.juan.itb.vrbackend.dto.response.QuizIdentityResponse;
import com.juan.itb.vrbackend.entity.Quiz;
import com.juan.itb.vrbackend.entity.Response;
import java.util.List;
import reactor.core.publisher.Mono;

public interface QuizService {
  Mono<Quiz> createQuiz(CreateQuizRequest createQuizRequest);

  Mono<QuizDto> getQuiz(Long id);

  Mono<Long> getQuizScoreForUser(Long userId, Long quizId);

  Mono<Long> getQuizQuestionCount(Long quizId);

  Mono<List<Response>> getResponse(Long userId, Long quizId);

  Mono<Response> createResponse(CreateResponseRequest createResponseRequest);

  Mono<Long> finalizeResponse(FinalizeResponseRequest finalizeResponseRequest);

  Mono<QuizIdentityResponse> decryptToken(String token);
}
