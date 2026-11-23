package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.UnidadeRequest;
import br.com.raizesdonordeste.api.dto.UnidadeResponse;
import br.com.raizesdonordeste.api.service.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Unidades",
        description = "Operações de gerenciamento das unidades da rede de restaurantes."
)
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService service;

    public UnidadeController(UnidadeService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cadastrar unidade",
            description = "Cadastra nova unidade no sistema. Operação restrita ao administrador.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidade cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<UnidadeResponse> cadastrar(
            @Valid @RequestBody UnidadeRequest request
    ) {
        UnidadeResponse unidade = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidade);
    }

    @Operation(
            summary = "Listar unidades",
            description = "Lista todas as unidades cadastradas no sistema.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de unidades"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária")
    })
    @GetMapping
    public ResponseEntity<List<UnidadeResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarUnidades());
    }

    @Operation(
            summary = "Buscar unidade por ID",
            description = "Retorna os dados de uma unidade especifica.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidade encontrada"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Ativar unidade",
            description = "Realiza busca por ID e ativa unidade no sistema.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar unidade",
            description = "Realiza busca por ID e desativa unidade no sistema.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar unidade",
            description = "Atualiza os dados cadastrais de uma unidade.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidade atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito"),
            @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }
}
