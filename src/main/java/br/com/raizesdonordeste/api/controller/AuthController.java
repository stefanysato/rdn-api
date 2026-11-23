package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.LoginRequest;
import br.com.raizesdonordeste.api.dto.LoginResponse;
import br.com.raizesdonordeste.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Autenticação",
        description = "Operações de autenticação e obtenção de token JWT."
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário por meio de suas credenciais e retorna um token JWT para acessar endpoints protegidos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de login inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "E-mail ou senha incorretos"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
            ) {

        String token = authService.autenticar(request);

        LoginResponse response = new LoginResponse(token, "Bearer");

        return ResponseEntity.ok(response);
    }

}
