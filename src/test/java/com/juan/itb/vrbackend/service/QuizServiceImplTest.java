package com.juan.itb.vrbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.juan.itb.vrbackend.dto.enums.ResponseStatus;
import com.juan.itb.vrbackend.entity.Option;
import com.juan.itb.vrbackend.entity.Question;
import com.juan.itb.vrbackend.entity.Quiz;
import com.juan.itb.vrbackend.entity.Response;
import com.juan.itb.vrbackend.repository.OptionRepository;
import com.juan.itb.vrbackend.repository.QuestionRepository;
import com.juan.itb.vrbackend.repository.QuizRepository;
import com.juan.itb.vrbackend.repository.ResponseRepository;
import com.juan.itb.vrbackend.service.api.TokenGeneratorService;
import com.juan.itb.vrbackend.service.impl.QuizServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class QuizServiceImplTest extends QuizTestVariable {

  @InjectMocks
  private QuizServiceImpl quizService;

  @Mock
  private QuizRepository quizRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private OptionRepository optionRepository;

  @Mock
  private ResponseRepository responseRepository;

  @Mock
  private TokenGeneratorService tokenGeneratorService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void after() {
    verifyNoMoreInteractions(quizRepository);
    verifyNoMoreInteractions(questionRepository);
    verifyNoMoreInteractions(optionRepository);
    verifyNoMoreInteractions(responseRepository);
    verifyNoMoreInteractions(tokenGeneratorService);
  }

  @Test
  void testCreateQuiz() {
    when(quizRepository.save(argThat(quiz ->
        CREATE_QUIZ_REQUEST.getName().equals(quiz.getQuizName()) &&
            CREATE_QUIZ_REQUEST.getDescription().equals(quiz.getDescription()))))
        .thenReturn(Mono.just(QUIZ));
    when(questionRepository.saveAll(anyIterable())).thenAnswer(invocation -> {
      Iterable<Question> questions = invocation.getArgument(0);
      assertEquals(2, StreamSupport.stream(questions.spliterator(), false).count());
      return Flux.just(QUESTION_1, QUESTION_2);
    });
    when(optionRepository.saveAll(anyIterable())).thenAnswer(invocation -> {
      Iterable<Option> options = invocation.getArgument(0);
      assertEquals(6, StreamSupport.stream(options.spliterator(), false).count());
      return Flux.just(OPTION_1_1, OPTION_1_2, OPTION_1_3, OPTION_2_1, OPTION_2_2, OPTION_2_3);
    });

    StepVerifier.create(quizService.createQuiz(CREATE_QUIZ_REQUEST))
        .expectSubscription()
        .expectNextMatches(quiz -> quiz.getId().equals(1L))
        .expectComplete()
        .verify();

    verify(quizRepository, times(1)).save(any(Quiz.class));
    verify(questionRepository, times(1)).saveAll(anyIterable());
    verify(optionRepository, times(1)).saveAll(anyIterable());
  }

  @Test
  void testGetQuiz() {
    when(quizRepository.findById(QUIZ.getId())).thenReturn(Mono.just(QUIZ));
    when(questionRepository.findQuestionByQuizId(QUIZ.getId()))
        .thenReturn(Flux.just(QUESTION_1, QUESTION_2));

    when(optionRepository.findAllByQuestionIdIn(
        Arrays.asList(QUESTION_1.getId(), QUESTION_2.getId())))
        .thenReturn(
            Flux.just(OPTION_1_1, OPTION_1_2, OPTION_1_3, OPTION_2_1, OPTION_2_2, OPTION_2_3));

    StepVerifier.create(quizService.getQuiz(QUIZ.getId()))
        .expectSubscription()
        .assertNext(quizDto -> assertEquals(QUIZ_RESPONSE, quizDto))
        .expectComplete()
        .verify();

    verify(quizRepository, times(1)).findById(QUIZ.getId());
    verify(questionRepository, times(1)).findQuestionByQuizId(QUIZ.getId());
    verify(optionRepository, times(1)).findAllByQuestionIdIn(anyList());
  }

  @Test
  void testGetQuizScoreForUser() {
    Long userId = 1L;
    Long quizId = 1L;

    when(responseRepository.calculateScore(userId, quizId)).thenReturn(Mono.just(10L));

    StepVerifier.create(quizService.getQuizScoreForUser(userId, quizId))
        .expectNext(10L)
        .expectComplete()
        .verify();

    verify(responseRepository).calculateScore(userId, quizId);
  }

  @Test
  void testGetResponse() {
    Long userId = 1L;
    Long quizId = 1L;

    List<Response> mockResponses = new ArrayList<>();
    mockResponses.add(new Response());

    when(responseRepository.findAllByUserIdAndQuizId(userId, quizId)).thenReturn(
        Flux.fromIterable(mockResponses));

    StepVerifier.create(quizService.getResponse(userId, quizId))
        .expectNextMatches(responses -> responses.size() == 1)
        .expectComplete()
        .verify();

    verify(responseRepository).findAllByUserIdAndQuizId(userId, quizId);
  }

  @Test
  void testGetQuizQuestionCount() {
    Long quizId = 1L;

    when(questionRepository.findQuestionByQuizId(quizId)).thenReturn(Flux.empty());

    StepVerifier.create(quizService.getQuizQuestionCount(quizId))
        .expectNext(0L)
        .expectComplete()
        .verify();

    verify(questionRepository).findQuestionByQuizId(quizId);
  }

  @Test
  void testUpdateResponse() {
    when(responseRepository.findByUserIdAndQuizIdAndQuestionId(1L, 1L, 1L))
        .thenReturn(Mono.just(RESPONSE_OLD));
    when(responseRepository.updateResponse(2L, 1L, 1L, 1L)).
        thenReturn(Mono.just(1L));

    StepVerifier.create(quizService.createResponse(CREATE_RESPONSE_REQUEST))
        .expectNext(RESPONSE_NEW)
        .expectComplete()
        .verify();

    verify(responseRepository).findByUserIdAndQuizIdAndQuestionId(1L, 1L, 1L);
    verify(responseRepository).updateResponse(2L, 1L, 1L, 1L);
    verify(responseRepository, never()).save(any());
  }

  @Test
  void testCreateResponseResponse() {
    when(responseRepository.findByUserIdAndQuizIdAndQuestionId(1L, 1L, 1L))
        .thenReturn(Mono.empty());
    when(responseRepository.save(RESPONSE_NEW)).thenReturn(Mono.just(RESPONSE_NEW));

    StepVerifier.create(quizService.createResponse(CREATE_RESPONSE_REQUEST))
        .assertNext(response -> assertEquals(RESPONSE_NEW, response))
        .expectComplete()
        .verify();

    verify(responseRepository).findByUserIdAndQuizIdAndQuestionId(1L, 1L, 1L);
    verify(responseRepository, never()).updateResponse(1L, 1L, 1L, 1L);
    verify(responseRepository).save(RESPONSE_NEW);

  }

  @Test
  void testUpdateResponseFinal() {
    when(responseRepository.findByUserIdAndQuizIdAndQuestionId(1L, 1L, 1L))
        .thenReturn(Mono.just(RESPONSE_FINAL));
    when(responseRepository.updateResponse(2L, 1L, 1L, 1L)).
        thenReturn(Mono.just(1L));

    StepVerifier.create(quizService.createResponse(CREATE_RESPONSE_REQUEST))
        .expectErrorMatches(throwable -> throwable instanceof RuntimeException
            && "finalized answer, can't update".equals(throwable.getMessage()))
        .verify();

    verify(responseRepository).findByUserIdAndQuizIdAndQuestionId(1L, 1L, 1L);
    verify(responseRepository, never()).updateResponse(2L, 1L, 1L, 1L);
    verify(responseRepository, never()).save(any());
  }

  @Test
  void testFinalizeResponse() {

    when(responseRepository.updateResponseStatus(any(ResponseStatus.class), anyLong(), anyLong()))
        .thenReturn(Mono.just(1L));

    StepVerifier.create(quizService.finalizeResponse(FINALIZE_RESPONSE_REQUEST))
        .expectNext(1L)
        .expectComplete()
        .verify();

    verify(responseRepository).updateResponseStatus(ResponseStatus.FINAL, 1L, 1L);
  }

  @Test
  void testDecryptToken() {
    String token = "encrypted-userId-quizId";
    when(tokenGeneratorService.decryptToken(token)).thenReturn("1-2"); // Mock decrypted token data

    StepVerifier.create(quizService.decryptToken(token))
        .expectNextMatches(response -> response.getUserId().equals(1L) && response.getQuizId().equals(2L))
        .expectComplete()
        .verify();

    verify(tokenGeneratorService).decryptToken(token);
  }
}
