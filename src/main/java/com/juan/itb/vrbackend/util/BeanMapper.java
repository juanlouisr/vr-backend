package com.juan.itb.vrbackend.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class BeanMapper {
  private final static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

  private BeanMapper() {
  }

  public static <S, C> C map(S source, Class<C> clazz) {
    return source == null ? null : mapper.map(source, clazz);
  }


}