package com.example.minispring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "My API",
       version = "v1",
       description = "This is description"))
@SecurityScheme(
       name = "bearerAuth",
       type = SecuritySchemeType.HTTP,
       scheme = "bearer",
       in = SecuritySchemeIn.HEADER
)

@SpringBootApplication
public class MinispringApplication {
	public static void main(String[] args) {
		SpringApplication.run(MinispringApplication.class, args);
	}
}
