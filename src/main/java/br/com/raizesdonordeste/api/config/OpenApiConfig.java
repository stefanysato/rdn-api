package br.com.raizesdonordeste.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Raízes do Nordeste API",
                version = "1.0.0",
                description = """
                        API Rest para gerenciamento de uma rede de restaurantes.
                        
                        Permite gerenciar usuários, unidades, produtos, pedidos, pagamentos, estoque, fidelidade e auditoria.
                        """
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor local de desenvolvimento"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Informe o token JWT obtido pelo endpoint de login:"
)
public class OpenApiConfig {
}
