package geulsam.archive.global.swagger;

import geulsam.archive.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@OpenAPIDefinition(servers = {
        @Server(url = "https://geulsaem.store", description = "실제 사용 도메인"),
        @Server(url = "http://localhost:8080", description = "로컬 도메인")})
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {

        // accessToken 으로 인증 가능
        SecurityScheme accessTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("AccessToken")
                .description("AccessToken");

        // refreshToken 으로 입력 가능
        SecurityScheme refreshTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("RefreshToken")
                .description("RefreshToken");

        return new OpenAPI().components(
                new Components()
                        .addSecuritySchemes("AccessToken",accessTokenScheme)
                        .addSecuritySchemes("RefreshToken", refreshTokenScheme))
                .info(getInfo());

    }

    private Info getInfo() {
        return new Info()
                .version("1.0.0")
                .description("글샘 아카이브 프로젝트")
                .title("Guelsam Archive");
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation operation, org.springframework.web.method.HandlerMethod handlerMethod) -> {

            Parameter accessTokenHeader = new Parameter()
                    .in("header")
                    .name("accessToken")
                    .description("Access Token Header")
                    .required(false)
                    .schema(new StringSchema());

            Parameter refreshTokenHeader = new Parameter()
                    .in("header")
                    .name("refreshToken")
                    .description("Refresh Token Header")
                    .required(false)
                    .schema(new StringSchema());

            operation.addParametersItem(accessTokenHeader);
            operation.addParametersItem(refreshTokenHeader);
            return operation;
        };
    }
}
