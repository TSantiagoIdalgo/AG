package com.ancore.ancoregaming;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableMethodSecurity(securedEnabled = true)
public class AncoregamingApplication {

  public static void main(String[] args) { SpringApplication.run(AncoregamingApplication.class, args); }

  @Bean
  public OpenAPI ancoreAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("Ancore Gaming API")
                    .version("0.0.0")
                    .description("This is the Ancore Gaming E-Commerce API. AG is a video game store"));
  }
}
