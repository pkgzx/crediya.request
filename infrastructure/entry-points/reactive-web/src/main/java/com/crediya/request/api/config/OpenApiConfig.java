package com.crediya.request.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI(
    @Value("${springdoc.version}") String appVersion,
    @Value("${springdoc.title}") String appTitle,
    @Value("${springdoc.description}") String appDescription,
    @Value("${springdoc.license.name}") String appLicenseName,
    @Value("${springdoc.license.url}") String appLicenseUrl
  ) {
    return new OpenAPI()
      .components(new Components().addSecuritySchemes("basicScheme",
        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
      .info(new Info().title(appTitle).description(appDescription).version(appVersion)
        .license(new License().name(appLicenseName).url(appLicenseUrl)));
  }

}
