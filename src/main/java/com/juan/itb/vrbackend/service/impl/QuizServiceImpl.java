package com.juan.itb.vrbackend.service.impl;

import com.juan.itb.vrbackend.dto.request.CreateQuizRequest;
import com.juan.itb.vrbackend.dto.request.CreateResponseRequest;
import com.juan.itb.vrbackend.entity.Option;
import com.juan.itb.vrbackend.entity.Question;
import com.juan.itb.vrbackend.entity.Quiz;
import com.juan.itb.vrbackend.entity.Response;
import com.juan.itb.vrbackend.repository.OptionRepository;
import com.juan.itb.vrbackend.repository.QuestionRepository;
import com.juan.itb.vrbackend.repository.QuizRepository;
import com.juan.itb.vrbackend.repository.ResponseRepository;
import com.juan.itb.vrbackend.service.api.QuizService;
import com.juan.itb.vrbackend.util.BeanMapper;
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
  public Mono<Integer> getQuizScoreForUser(Long userId, Long quizId) {
    return responseRepository.calculateScore(userId, quizId);
  }

  @Override
  public Mono<Response> createResponse(CreateResponseRequest createResponseRequest) {
    return responseRepository.save(BeanMapper.map(createResponseRequest, Response.class))
        .doOnNext(response -> log.info("quiz response: {}", response));
  }


}
