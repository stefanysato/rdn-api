package br.com.raizesdonordeste.api.controller;


import br.com.raizesdonordeste.api.dto.AtualizarPrecoRequest;
import br.com.raizesdonordeste.api.dto.EstoqueUnidadeRequest;
import br.com.raizesdonordeste.api.dto.EstoqueUnidadeResponse;
import br.com.raizesdonordeste.api.dto.MovimentarEstoqueRequest;
import br.com.raizesdonordeste.api.service.EstoqueUnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        name = "Estoque",
        description = "Gerenciamento do estoque de produtos por unidade."
)
@RestController
@RequestMapping
public class EstoqueUnidadeController {

    private final EstoqueUnidadeService estoqueService;

    public EstoqueUnidadeController(EstoqueUnidadeService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @Operation(
            summary = "Cadastrar item",
            description = "Adiciona um produto do catálogo no estoque da unidade. Acesso restrito ao gerente da unidade.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/estoque")
    public ResponseEntity<EstoqueUnidadeResponse> cadastrar(
            @Valid @RequestBody EstoqueUnidadeRequest request
    ) {
        EstoqueUnidadeResponse response = estoqueService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Consultar estoque",
            description = "Consulta a disponibilidade dos produtos de uma unidade. Acesso restrito ao gerente da unidade e administrador.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque consultado"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso à unidade negado"),
            @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    })
    @GetMapping("/unidades/{unidadeId}/estoque")
    public ResponseEntity<List<EstoqueUnidadeResponse>> listarPorUnidade(
            @PathVariable Long unidadeId
    ) {
        return ResponseEntity.ok(estoqueService.listarPorUnidade(unidadeId));
    }

    @Operation(
            summary = "Buscar item por ID",
            description = "Consulta item no estoque pelo seu ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto localizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/estoque/{id}")
    public ResponseEntity<EstoqueUnidadeResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(estoqueService.buscarPorId(id));
    }

    @Operation(
            summary = "Atualizar preço",
            description = "Atualiza o preço de venda de um produto associado ao estoque de uma unidade."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preço atualizado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Preço inválido"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não possui permissão para acessar o estoque da unidade"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registro de estoque não encontrado"
            )
    })
    @PatchMapping("/estoque/{id}/preco")
    public ResponseEntity<EstoqueUnidadeResponse> atualizarPreco(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPrecoRequest request
    ) {
        return ResponseEntity.ok(estoqueService.atualizarPreco(id, request));
    }

    @Operation(
            summary = "Registrar entrada de estoque",
            description = "Adiciona uma quantidade de produtos ao estoque da unidade."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Entrada de estoque registrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quantidade inválida"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não possui permissão para acessar o estoque da unidade"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registro de estoque não encontrado"
            )
    })
    @PatchMapping("/estoque/{id}/entrada")
    public ResponseEntity<EstoqueUnidadeResponse> registrarEntrada(
            @PathVariable Long id,
            @Valid @RequestBody MovimentarEstoqueRequest request
            ) {
        return ResponseEntity.ok(estoqueService.registrarEntrada(id, request));
    }

    @Operation(
            summary = "Registrar saída de estoque",
            description = "Retira uma quantidade de produtos do estoque da unidade, desde que exista quantidade disponível suficiente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saída de estoque registrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quantidade inválida ou estoque insuficiente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não possui permissão para acessar o estoque da unidade"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registro de estoque não encontrado"
            )
    })
    @PatchMapping("/estoque/{id}/saida")
    public ResponseEntity<EstoqueUnidadeResponse> registrarSaida(
            @PathVariable Long id,
            @Valid @RequestBody MovimentarEstoqueRequest request
    ) {
        return ResponseEntity.ok(estoqueService.registrarSaida(id, request));
    }
}
