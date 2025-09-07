package com.demo.token.encrypt;

import com.demo.token.dto.encrypt.HybridPayload;
import com.demo.token.exception.CryptException;
import com.demo.token.utils.AES128;
import java.security.PrivateKey;
import java.util.Base64;
import javax.crypto.Cipher;

public class HybridDecryptor {

  private final AES128 aes128;
  private final PrivateKey privateKey;

  public HybridDecryptor(AES128 aes128, PrivateKey privateKey) {
    this.aes128 = aes128;
    this.privateKey = privateKey;
  }

  public String decrypt(HybridPayload payload) throws CryptException {
    try {
      // 1. RSA로 AES key + IV 복호화
      byte[] encryptedKeyAndIv = Base64.getDecoder().decode(payload.getEncryptedKeyAndIv());
      Cipher rsaCipher = Cipher.getInstance("RSA");
      rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] keyAndIv = rsaCipher.doFinal(encryptedKeyAndIv);

      // 2. key, iv 분리
      byte[] aesKeyBytes = new byte[16];
      byte[] ivBytes = new byte[16];
      System.arraycopy(keyAndIv, 0, aesKeyBytes, 0, 16);
      System.arraycopy(keyAndIv, 16, ivBytes, 0, 16);

      // 3. AES 복호화
      return aes128.decrypt(aesKeyBytes, ivBytes, payload.getEncryptedData());

    } catch (Exception e) {
      throw new CryptException("HybridDecryptor", e);
    }
  }
}