package com.juan.itb.vrbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.juan.itb.vrbackend.service.impl.TokenGeneratorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest
@TestPropertySource(properties = {"token.secret-key=vrbackend1234567"})
class TokenGeneratorServiceImplTest {

  @Spy
  @Autowired
  private TokenGeneratorServiceImpl tokenGeneratorService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGenerateToken() {
    String data = "1-1";
    String expectedToken = "92abb6";

    String generatedToken = tokenGeneratorService.generateToken(data);
    assertEquals(expectedToken, generatedToken);
  }

  @Test
  void testDecryptToken() {
    String token = "92abb6";
    String expectedDecryptedData = "1-1";

    String decryptedData = tokenGeneratorService.decryptToken(token);
    assertEquals(expectedDecryptedData, decryptedData);
  }

  @Test
  void testGenerateTokenFailure() {
    String data = "testData";
    when(tokenGeneratorService.generateRandomNonce()).thenThrow(new RuntimeException("Nonce generation failed"));
    assertThrows(RuntimeException.class, () -> tokenGeneratorService.generateToken(data));
  }

  @Test
  void testDecryptTokenFailure() {
    String token = "01020304encryptedData";
    when(tokenGeneratorService.hexToBytes(anyString())).thenThrow(new RuntimeException("Hex decoding failed"));

    assertThrows(RuntimeException.class, () -> tokenGeneratorService.decryptToken(token));
  }

}