package com.crediya.request.api.config.path;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths.loan-application")
public class LoanApplicationPath {
    private String create;
    private String paginated;
    private String changeStatus;
}
