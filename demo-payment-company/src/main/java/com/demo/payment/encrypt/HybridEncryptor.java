package com.demo.payment.encrypt;

import com.demo.payment.dto.encrypt.HybridPayload;
import com.demo.payment.exception.CryptException;
import com.demo.payment.utils.AES128;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class HybridEncryptor {

  private final AES128 aes128;
  private final PublicKey publicKey;

  public HybridEncryptor(AES128 aes128, PublicKey publicKey) {
    this.aes128 = aes128;
    this.publicKey = publicKey;
  }

  public HybridPayload encrypt(String plainText) throws CryptException {
    try {
      // 1. AES-128 키 생성
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(128);
      SecretKey aesKey = keyGen.generateKey();

      // 2. IV 생성
      byte[] iv = new byte[16];
      new SecureRandom().nextBytes(iv);

      // 3. AES로 평문 데이터 암호화
      String encryptedData = aes128.encrypt(aesKey.getEncoded(), iv, plainText);

      // 4. AES 키 + IV 합치기
      byte[] keyAndIv = concat(aesKey.getEncoded(), iv);

      // 5. RSA로 AES 키 + IV 암호화
      Cipher rsaCipher = Cipher.getInstance("RSA");
      rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encryptedKeyAndIv = rsaCipher.doFinal(keyAndIv);
      String base64EncryptedKeyAndIv = Base64.getEncoder().encodeToString(encryptedKeyAndIv);

      return HybridPayload.builder()
          .encryptedData(encryptedData)
          .encryptedKeyAndIv(base64EncryptedKeyAndIv)
          .build();
    } catch (Exception e) {
      throw new CryptException("HybridEncryptor", e);
    }
  }

  private byte[] concat(byte[] a, byte[] b) {
    byte[] result = new byte[a.length + b.length];
    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, b.length);
    return result;
  }
}