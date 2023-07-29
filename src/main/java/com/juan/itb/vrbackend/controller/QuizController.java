package com.juan.itb.vrbackend.controller;

import com.juan.itb.vrbackend.dto.request.CreateQuizRequest;
import com.juan.itb.vrbackend.dto.request.CreateResponseRequest;
import com.juan.itb.vrbackend.dto.request.FinalizeResponseRequest;
import com.juan.itb.vrbackend.dto.response.BaseResponse;
import com.juan.itb.vrbackend.dto.response.QuizDto;
import com.juan.itb.vrbackend.entity.Quiz;
import com.juan.itb.vrbackend.entity.Response;
import com.juan.itb.vrbackend.service.api.QuizService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizController {
  @Autowired
  private QuizService quizService;

  @PostMapping
  public Mono<BaseResponse<Quiz>> createQuiz(@Valid @RequestBody CreateQuizRequest createQuizRequest) {
    log.info("Creating Quiz: {}", createQuizRequest);
    return quizService.createQuiz(createQuizRequest)
        .map(BaseResponse::ok);
  }

  @GetMapping(path = "/{id}")
  public Mono<BaseResponse<QuizDto>> getQuiz(@PathVariable Long id) {
    return quizService.getQuiz(id)
        .map(BaseResponse::ok);
  }

  @GetMapping(path = "/score")
  public Mono<BaseResponse<Long>> getScore(@Valid @RequestParam Long userId, @Valid @RequestParam Long quizId) {
    return quizService.getQuizScoreForUser(userId, quizId)
        .map(BaseResponse::ok);
  }

  @GetMapping(path = "/question-count")
  public Mono<BaseResponse<Long>> getQuestionCount(@Valid @RequestParam Long quizId) {
    return quizService.getQuizQuestionCount(quizId)
        .map(BaseResponse::ok);
  }

  @GetMapping(path = "/response")
  public Mono<BaseResponse<List<Response>>> getResponse(@Valid @RequestParam Long userId, @Valid @RequestParam Long quizId) {
    return quizService.getResponse(userId, quizId)
        .map(BaseResponse::ok);
  }

  @PostMapping(path = "/response")
  public Mono<BaseResponse<Response>> saveAnswer(@Valid @RequestBody CreateResponseRequest createResponseRequest) {
    return quizService.createResponse(createResponseRequest)
        .map(BaseResponse::ok);
  }

  @PostMapping(path = "/response/finalize")
  public Mono<BaseResponse<Long>> finalizeResponse(@Valid @RequestBody FinalizeResponseRequest request) {
    return quizService.finalizeResponse(request)
        .map(BaseResponse::ok);
  }
}
