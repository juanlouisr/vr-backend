package com.juan.itb.vrbackend.service.impl;


import com.juan.itb.vrbackend.service.api.TokenGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

  private static final int NONCE_SIZE = 8; // ChaCha nonce size in bytes

  @Value("${token.secret-key}")
  private String secretKey;

  @Override
  public String generateToken(String data) {
    try {
      byte[] nonce = generateRandomNonce();
      byte[] encryptedData = encrypt(data.getBytes(), nonce, secretKey.getBytes());
      return bytesToHex(encryptedData);
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate token.", e);
    }
  }

  @Override
  public String decodeToken(String token) {
    try {
      byte[] nonce = new byte[NONCE_SIZE];
      token = bytesToHex(nonce) + token;
      byte[] tokenBytes = hexToBytes(token);
      log.info("nonce size: {}", nonce.length);
      byte[] encryptedData = new byte[tokenBytes.length - NONCE_SIZE];

      // Extract the nonce and encrypted data from the token
      System.arraycopy(tokenBytes, 0, nonce, 0, NONCE_SIZE);
      System.arraycopy(tokenBytes, NONCE_SIZE, encryptedData, 0, tokenBytes.length - NONCE_SIZE);

      byte[] decryptedData = decrypt(encryptedData, nonce, secretKey.getBytes());
      return new String(decryptedData);
    } catch (Exception e) {
      throw new RuntimeException("Failed to decode token.", e);
    }
  }

  private byte[] decrypt(byte[] encryptedData, byte[] nonce, byte[] key) {
    StreamCipher chacha = new Salsa20Engine();
    chacha.init(false, new ParametersWithIV(new KeyParameter(key), nonce));
    byte[] decrypted = new byte[encryptedData.length];
    chacha.processBytes(encryptedData, 0, encryptedData.length, decrypted, 0);
    return decrypted;
  }

  private byte[] encrypt(byte[] data, byte[] nonce, byte[] key) {
    StreamCipher chacha = new Salsa20Engine();
    log.info("key size: {}", key.length);
    chacha.init(true, new ParametersWithIV(new KeyParameter(key), nonce));
    byte[] encrypted = new byte[data.length];
    chacha.processBytes(data, 0, data.length, encrypted, 0);
    return encrypted;
  }

  private byte[] hexToBytes(String hex) {
    int len = hex.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
          + Character.digit(hex.charAt(i + 1), 16));
    }
    return data;
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private byte[] generateRandomNonce() {
    // Implement a secure random number generator here to fill the nonce with random bytes
    // For simplicity, we will use all zeros here, but in practice, you should use a secure random generator.
    return new byte[NONCE_SIZE];
  }
}
