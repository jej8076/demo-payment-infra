//package com.demo.payment.encrypt;
//
//import com.demo.payment.exception.CryptException;
//import com.demo.payment.utils.AES128;
//
//public class AES128Encryptor {
//
//  private final AES128 aes128;
//  private final byte[] key;
//  private final byte[] iv;
//
//  public AES128Encryptor(AES128 aes128, byte[] key, byte[] iv) {
//    this.aes128 = aes128;
//    this.key = key;
//    this.iv = iv;
//  }
//
//  public String encrypt(String plainText) throws CryptException {
//    return aes128.encrypt(key, iv, plainText);
//  }
//
//  public String decrypt(String encryptedText) throws CryptException {
//    return aes128.decrypt(key, iv, encryptedText);
//  }
//}