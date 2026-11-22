package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.FuncionarioRequest;
import br.com.raizesdonordeste.api.dto.FuncionarioResponse;
import br.com.raizesdonordeste.api.dto.UsuarioRequest;
import br.com.raizesdonordeste.api.dto.UsuarioResponse;
import br.com.raizesdonordeste.api.security.UsuarioDetails;
import br.com.raizesdonordeste.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
            description = "Cadastra novo usuário no sistema.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request) {

        UsuarioResponse usuario = service.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/funcionarios")
    public ResponseEntity<FuncionarioResponse> cadastrarFuncionario(
            @Valid @RequestBody FuncionarioRequest request
    ) {
        FuncionarioResponse funcionario = service.cadastrarFuncionario(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(funcionario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));

    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> buscarMeuPerfil(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        return ResponseEntity.ok(
                service.buscarPorId(usuarioDetails.getId())
        );
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativarUsuario(@PathVariable Long id) {
        service.ativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        service.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    
}
