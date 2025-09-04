package com.demo.issuer.controller;

import com.demo.issuer.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issuer")
@RequiredArgsConstructor
public class ApprovalController {
    
    private final ApprovalService approvalService;
    
    @PostMapping("/approve-token")
    public ResponseEntity<String> approveToken(@RequestBody String tokenValue) {
        String result = approvalService.approveToken(tokenValue);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyToken(@RequestBody String tokenValue) {
        String result = approvalService.verifyToken(tokenValue);
        return ResponseEntity.ok(result);
    }
}
