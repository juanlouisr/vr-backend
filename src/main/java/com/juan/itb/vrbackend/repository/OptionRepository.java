package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.entity.Option;
import java.util.List;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OptionRepository extends R2dbcRepository<Option, Long> {
  Flux<Option> findAllByQuestionIdIn(List<Long> questionIds);
}
