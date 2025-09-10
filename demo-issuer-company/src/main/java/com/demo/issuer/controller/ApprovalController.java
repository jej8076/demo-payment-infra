package com.demo.issuer.controller;

import com.demo.issuer.dto.ApproveTokenResponse;
import com.demo.issuer.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issuer")
@RequiredArgsConstructor
public class ApprovalController {

  private final ApprovalService approvalService;

  @PostMapping("/approve/token")
  public ResponseEntity<ApproveTokenResponse> approveToken(@RequestBody String tokenValue) {
    ApproveTokenResponse result = approvalService.approveToken(tokenValue);
    return ResponseEntity.ok(result);
  }
}
