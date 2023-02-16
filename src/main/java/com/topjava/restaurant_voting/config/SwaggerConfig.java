package com.topjava.restaurant_voting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant voting application",
                description = """
                        Application for user voting for the best restaurant for lunch.<br>
                        It is a simple REST API and uses basic authorization.

                        <b>Credentials:</b><br>
                        User: user_zero@yandex.ru / pass12<br>
                        User: user@yandex.ru / password<br>
                        Admin: admin@gmail.com / admin""",
                version = "1.1.0",
                contact = @Contact(
                        name = "Anton Dvorko",
                        email = "antlighter@yandex.ru"
                )
        )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class SwaggerConfig {
}
