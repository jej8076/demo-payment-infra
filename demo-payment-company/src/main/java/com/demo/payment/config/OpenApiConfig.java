package com.demo.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${springdoc.server.url}")
  private String requestServerUrl;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("DEMO_BLUE_WALNUT")
            .version("v0.1")
            .description("DEMO API 문서")
            .contact(new Contact()
                .name("정의진")
                .email("jej8076@gmai.com")))
        .servers(List.of(new Server().url(requestServerUrl)));
  }
}
