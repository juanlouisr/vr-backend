package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.entity.Option;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends R2dbcRepository<Option, Long> {

}
