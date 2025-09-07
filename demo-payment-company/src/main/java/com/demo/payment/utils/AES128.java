package com.demo.payment.utils;

import com.demo.payment.exception.CryptException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class AES128 {

  public String encrypt(final byte[] key, final byte[] iv, final String plain)
      throws CryptException {
    try {
      SecretKey secretKey = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
      byte[] enc = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(enc);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | IllegalBlockSizeException |
             BadPaddingException e) {
      throw new CryptException("AES128", e);
    }
  }

  public String decrypt(final byte[] key, final byte[] iv, final String encrypted)
      throws CryptException {
    try {
      SecretKey secretKey = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      byte[] enc = Base64.getDecoder().decode(encrypted);
      return new String(cipher.doFinal(enc), StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | IllegalBlockSizeException |
             BadPaddingException e) {
      throw new CryptException("AES128", e);
    }
  }

  public byte[] encrypt(final byte[] key, final byte[] iv, final byte[] plain)
      throws CryptException {
    try {
      SecretKey secretKey = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
      byte[] enc = cipher.doFinal(plain);
      return Base64.getEncoder().encode(enc);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | IllegalBlockSizeException |
             BadPaddingException e) {
      throw new CryptException("AES128", e);
    }
  }

  public byte[] decrypt(final byte[] key, final byte[] iv, final byte[] encoded)
      throws CryptException {
    try {
      SecretKey secretKey = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      byte[] enc = Base64.getDecoder().decode(encoded);
      return cipher.doFinal(enc);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | IllegalBlockSizeException |
             BadPaddingException e) {
      throw new CryptException("AES128", e);
    }
  }
}