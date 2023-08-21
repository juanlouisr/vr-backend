package com.juan.itb.vrbackend.service.api;

public interface TokenGeneratorService {
  String generateToken(String data);
  String decryptToken(String data);
}
