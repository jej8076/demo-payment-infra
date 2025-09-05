package com.demo.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient tokenRestClient(RestClient.Builder builder) {
    return builder.baseUrl("http://localhost:8001").build();
  }

  @Bean
  public RestClient issuerRestClient(RestClient.Builder builder) {
    return builder.baseUrl("http://localhost:8002").build();
  }
}
