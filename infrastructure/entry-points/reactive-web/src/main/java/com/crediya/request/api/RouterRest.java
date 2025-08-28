package com.crediya.request.api;

import com.crediya.request.api.config.path.LoanApplicationPath;
import com.crediya.request.api.config.path.StatePath;
import com.crediya.request.api.config.path.TypeLoanPath;
import com.crediya.request.api.dto.CreateLoanApplicationDto;
import com.crediya.request.api.dto.CreateStateDto;
import com.crediya.request.api.dto.CreateTypeLoanDto;
import com.crediya.request.api.exception.GlobalWebExceptionHandler;
import com.crediya.request.api.handler.LoanApplicationHandler;
import com.crediya.request.api.handler.StateHandler;
import com.crediya.request.api.handler.TypeLoanHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {
    private final StatePath statePath;
    private final TypeLoanPath typeLoanPath;
  private final LoanApplicationPath loanApplicationPath;
    private final LoanApplicationHandler loanApplicationHandler;
    private final StateHandler stateHandler;
    private final TypeLoanHandler typeLoanHandler;
    private final GlobalWebExceptionHandler globalWebExceptionHandler;

    @RouterOperations(
            value = {
                    @RouterOperation(
                            path = "/api/v1/state",
                            method = {RequestMethod.POST},
                            beanClass = StateHandler.class,
                            beanMethod = "listenCreateState",
                            operation = @Operation(
                                    operationId = "createState",
                                    tags = {"State"},
                                    summary = "Create a new state",
                                    description = "Create a new state",
                              requestBody = @RequestBody(
                                required = true,
                                content = @Content(schema = @Schema(implementation = CreateStateDto.class))
                              ),
                              responses = {
                                @ApiResponse(responseCode = "201", description = "Created Status success"),
                                @ApiResponse(responseCode = "400", description = "Invalid input",
                                  content = @Content(mediaType = "application/json")),
                                @ApiResponse(responseCode = "409", description = "State already exists")
                              }
                            )
                    ),
                    @RouterOperation(
                            path = "/api/v1/typeloan",
                            method = {RequestMethod.POST},
                            beanClass = TypeLoanHandler.class,
                            beanMethod = "saveTypeLoan",
                            operation = @Operation(
                                    operationId = "createTypeLoan",
                                    tags = {"Type Loan"},
                                    summary = "Create a new type loan",
                                    description = "Create a new type loan",
                                    requestBody = @RequestBody(
                                            required = true,
                                            content = @Content(schema = @Schema(implementation = CreateTypeLoanDto.class))
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "201", description = "Created Status success"),
                                            @ApiResponse(responseCode = "400", description = "Invalid input",
                                                    content = @Content(mediaType = "application/json")),
                                            @ApiResponse(responseCode = "409", description = "Type Loan already exists")
                                    }
                            )
                    ),
                    @RouterOperation(
                            path = "/api/v1/loan-application",
                            method = {RequestMethod.POST},
                            beanClass = LoanApplicationHandler.class,
                            beanMethod = "listenCreateLoan",
                            operation = @Operation(
                                    operationId = "createLoanApplication",
                                    tags = {"Loan Application"},
                                    summary = "Create a new loan application",
                                    description = "Create a new loan application",
                                    requestBody = @RequestBody(
                                            required = true,
                                            content = @Content(schema = @Schema(implementation = CreateLoanApplicationDto.class))
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "201", description = "Created Status success"),
                                            @ApiResponse(responseCode = "400", description = "Invalid input",
                                                    content = @Content(mediaType = "application/json"))
                                    }
                            )
                    )

            }
    )
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST(statePath.getCreate()), stateHandler::listenCreateState)
          .andRoute(POST(typeLoanPath.getCreate()), typeLoanHandler::saveTypeLoan)
          .andRoute(POST(loanApplicationPath.getCreate()), loanApplicationHandler::listenCreateLoan)
          .filter(globalWebExceptionHandler);
    }
}
