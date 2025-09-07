package com.demo.token.dto.encrypt;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HybridPayload {

  private String encryptedData;
  private String encryptedKeyAndIv;

  @Builder
  public HybridPayload(String encryptedData, String encryptedKeyAndIv) {
    this.encryptedData = encryptedData;
    this.encryptedKeyAndIv = encryptedKeyAndIv;
  }
}