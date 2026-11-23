package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.ProdutoRequest;
import br.com.raizesdonordeste.api.dto.ProdutoResponse;
import br.com.raizesdonordeste.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Produtos",
        description = "Cadastro, consulta e atualização de produtos do cardápio."
)
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {this.service = service;}

    @Operation(
            summary = "Cadastrar produto",
            description = "Cadastra um novo produto no catálogo da rede."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito ao administrador")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse produto = service.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @Operation(
            summary = "Listar produtos",
            description = "Retorna os produtos cadastrados no catálogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarProdutos() {
        return ResponseEntity.ok(service.listarProdutos());
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza informações de um produto cadastrado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request
    ) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Operation(
            summary = "Ativar produto",
            description = "Torna um produto disponível no catálogo.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar produto",
            description = "Torna um produto indisponível no catálogo.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
