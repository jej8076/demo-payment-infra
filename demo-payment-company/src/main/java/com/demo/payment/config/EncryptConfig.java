package com.demo.payment.config;

import com.demo.payment.encrypt.HybridEncryptor;
import com.demo.payment.utils.AES128;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class EncryptConfig {

  @Value("${encrypt.rsa.public-key-path}")
  private Resource publicKeyResource;

  @Bean
  public PublicKey rsaPublicKey() throws Exception {
    String rawKey = new String(publicKeyResource.getInputStream().readAllBytes(),
        StandardCharsets.UTF_8);

    String base64Key = rawKey
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s+", "");

    byte[] keyBytes = Base64.getDecoder().decode(base64Key);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(spec);
  }

  @Bean
  public HybridEncryptor hybridEncryptor(AES128 aes128, PublicKey rsaPublicKey) {
    try {
      return new HybridEncryptor(aes128, rsaPublicKey);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}