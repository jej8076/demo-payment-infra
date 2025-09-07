package com.demo.token.config;

import com.demo.token.encrypt.HybridDecryptor;
import com.demo.token.utils.AES128;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class EncryptConfig {

  // 암호화는 굳이 할 필요 없다
  @Value("${encrypt.rsa.public-key-path}")
  private Resource publicKeyResource;

  @Value("${encrypt.rsa.private-key-path}")
  private Resource privateKeyResource;

  @Bean
  public HybridDecryptor hybridDecryptor(AES128 aes128) throws Exception {
    String rawKey = new String(privateKeyResource.getInputStream().readAllBytes(),
        StandardCharsets.UTF_8);

    String base64Key = rawKey
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s+", "");

    byte[] keyBytes = Base64.getDecoder().decode(base64Key);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = kf.generatePrivate(spec);

    return new HybridDecryptor(aes128, privateKey);
  }

}