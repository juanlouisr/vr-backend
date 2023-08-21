package com.juan.itb.vrbackend.service.impl;

import com.juan.itb.vrbackend.dto.enums.ResponseStatus;
import com.juan.itb.vrbackend.dto.request.CreateQuizRequest;
import com.juan.itb.vrbackend.dto.request.CreateResponseRequest;
import com.juan.itb.vrbackend.dto.request.FinalizeResponseRequest;
import com.juan.itb.vrbackend.dto.response.OptionResponse;
import com.juan.itb.vrbackend.dto.response.QuestionDto;
import com.juan.itb.vrbackend.dto.response.QuizDto;
import com.juan.itb.vrbackend.dto.response.QuizIdentityResponse;
import com.juan.itb.vrbackend.entity.Option;
import com.juan.itb.vrbackend.entity.Question;
import com.juan.itb.vrbackend.entity.Quiz;
import com.juan.itb.vrbackend.entity.Response;
import com.juan.itb.vrbackend.repository.OptionRepository;
import com.juan.itb.vrbackend.repository.QuestionRepository;
import com.juan.itb.vrbackend.repository.QuizRepository;
import com.juan.itb.vrbackend.repository.ResponseRepository;
import com.juan.itb.vrbackend.service.api.QuizService;
import com.juan.itb.vrbackend.service.api.TokenGeneratorService;
import com.juan.itb.vrbackend.util.BeanMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private OptionRepository optionRepository;

  @Autowired
  private ResponseRepository responseRepository;

  @Autowired
  private TokenGeneratorService tokenGeneratorService;

  @Override
  public Mono<Quiz> createQuiz(CreateQuizRequest createQuizRequest) {
    Quiz quiz = Quiz.builder()
        .quizName(createQuizRequest.getName())
        .description(createQuizRequest.getDescription())
        .build();

    return quizRepository.save(quiz)
        .doOnNext(createdQuiz -> log.info("Created Quiz: {}", createdQuiz))
        .flatMap(createdQuiz -> Flux.fromIterable(createQuizRequest.getQuestions())
            .map(questionRequest -> Question.builder()
                .questionText(questionRequest.getQuestionText())
                .quizId(createdQuiz.getId())
                .build())
            .collectList()
            .flatMapMany(questionRepository::saveAll)
            .doOnNext(createdQuestion -> log.info("created question: {}", createdQuestion))
            .zipWithIterable(createQuizRequest.getQuestions())
            .flatMap(question -> Flux.fromIterable(question.getT2().getOptions())
                .map(option -> Option.builder()
                    .questionId(question.getT1().getId())
                    .optionText(option.getOptionText())
                    .isCorrect(option.getIsCorrect())
                    .build())
            )
            .collectList()
            .flatMapMany(optionRepository::saveAll)
            .collectList()
            .doOnNext(options -> log.info("created options: {}", options))
            .thenReturn(createdQuiz)
        );
  }

  @Override
  public Mono<QuizDto> getQuiz(Long id) {
    Mono<QuizDto> quizMono = quizRepository.findById(id)
        .map(quiz -> BeanMapper.map(quiz, QuizDto.class));
    Mono<List<QuestionDto>> questions = questionRepository.findQuestionByQuizId(id)
        .map(question -> BeanMapper.map(question, QuestionDto.class)).collectList();

    return Mono.zip(quizMono, questions)
        .doOnNext(tuple -> tuple.getT1().setQuestions(tuple.getT2()))
        .flatMap(tuple -> Flux.fromIterable(tuple.getT2()).map(QuestionDto::getId).collectList()
            .flatMap(questionIds -> optionRepository.findAllByQuestionIdIn(questionIds)
                .map(option -> BeanMapper.map(option, OptionResponse.class))
                .collectList()
                .doOnNext(options -> mapOptionsToQuestions(tuple.getT2(), options))
                .thenReturn(tuple.getT1())));
  }

  @Override
  public Mono<Long> getQuizScoreForUser(Long userId, Long quizId) {
    return responseRepository.calculateScore(userId, quizId);
  }

  @Override
  public Mono<List<Response>> getResponse(Long userId, Long quizId) {
    return responseRepository.findAllByUserIdAndQuizId(userId, quizId).collectList();
  }

  @Override
  public Mono<Long> getQuizQuestionCount(Long quizId) {
    return questionRepository.findQuestionByQuizId(quizId).count();
  }

  @Override
  public Mono<Response> createResponse(CreateResponseRequest createResponseRequest) {
    return responseRepository.findByUserIdAndQuizIdAndQuestionId(createResponseRequest.getUserId(),
            createResponseRequest.getQuizId(), createResponseRequest.getQuestionId())
        .doOnNext(this::validateNotFinal)
        .doOnNext(response -> response.setOptionId(createResponseRequest.getOptionId()))
        .flatMap(response -> responseRepository.updateResponse(response.getOptionId(),
                response.getUserId(), response.getQuizId(), response.getQuestionId())
            .thenReturn(response))
        .switchIfEmpty(Mono.defer(
            () -> responseRepository.save(BeanMapper.map(createResponseRequest, Response.class))))
        .doOnNext(response -> log.info("quiz response: {}", response));
  }

  @Override
  public Mono<Long> finalizeResponse(FinalizeResponseRequest request) {
    return responseRepository.updateResponseStatus(ResponseStatus.FINAL, request.getUserId(),
        request.getQuizId());
  }

  @Override
  public Mono<QuizIdentityResponse> decryptToken(String token) {
    return Mono.just(tokenGeneratorService.decryptToken(token))
        .map(data -> data.split("-"))
        .map(data -> QuizIdentityResponse.builder()
            .userId(Long.valueOf(data[0]))
            .quizId(Long.valueOf(data[1]))
            .build());
  }

  private void mapOptionsToQuestions(List<QuestionDto> questionDtos,
      List<OptionResponse> optionResponses) {
    Map<Long, QuestionDto> questionDtoMap = questionDtos.stream()
        .collect(Collectors.toMap(QuestionDto::getId, Function.identity()));
    optionResponses.forEach(optionResponse -> {
      QuestionDto questionDto = questionDtoMap.get(optionResponse.getQuestionId());
      if (questionDto.getOptions() == null) {
        questionDto.setOptions(new ArrayList<>());
      }
      questionDto.getOptions().add(optionResponse);
    });
  }

  private void validateNotFinal(Response response) {
    if (ResponseStatus.FINAL.equals(response.getStatus())) {
      throw new RuntimeException("finalized answer, can't update");
    }
  }

}
