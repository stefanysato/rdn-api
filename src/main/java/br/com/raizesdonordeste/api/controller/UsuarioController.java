package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.FuncionarioRequest;
import br.com.raizesdonordeste.api.dto.FuncionarioResponse;
import br.com.raizesdonordeste.api.dto.UsuarioRequest;
import br.com.raizesdonordeste.api.dto.UsuarioResponse;
import br.com.raizesdonordeste.api.security.UsuarioDetails;
import br.com.raizesdonordeste.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Usuários",
        description = "Operações relacionadas ao cadastro e gerenciamento de usuários do sistema."
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cadastra novo usuário no sistema. O cadastro público é destinado aos clientes."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado"
            )
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse usuario = service.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @Operation(
            summary = "Cadastrar funcionário",
            description = "Cadastra um funcionário e associa seu perfil e unidade de atuação. Operação restrita ao administrador do sistema.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Funcionário cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Apenas o administrador pode cadastrar funcionários"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado"
            )
    })
    @PostMapping("/funcionarios")
    public ResponseEntity<FuncionarioResponse> cadastrarFuncionario(
            @Valid @RequestBody FuncionarioRequest request
    ) {
        FuncionarioResponse funcionario = service.cadastrarFuncionario(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(funcionario);
    }

    @Operation(
            summary = "Listar usuários",
            description = "Retorna uma lista com todos os usuários cadastrados no sistema. Acesso restrito ao administrador.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado"
            )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    @Operation(
            summary = "Buscar usuário",
            description = "Retorna os dados de um usuário específico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));

    }

    @Operation(
            summary = "Consultar perfil autenticado",
            description = "Retorna os dados do usuário logado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    })
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> buscarMeuPerfil(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        return ResponseEntity.ok(
                service.buscarPorId(usuarioDetails.getId())
        );
    }

    @Operation(
            summary = "Ativar usuário",
            description = "Ativa um usuário previamente desativado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarUsuario(@PathVariable Long id) {
        service.ativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar usuário",
            description = "Desativa um usuário sem remover seus dados do sistema.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        service.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    
}
