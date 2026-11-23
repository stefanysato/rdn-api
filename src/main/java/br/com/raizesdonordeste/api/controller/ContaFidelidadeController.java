package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.model.MovimentacaoPontos;
import br.com.raizesdonordeste.api.security.UsuarioDetails;
import br.com.raizesdonordeste.api.service.ContaFidelidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "Fidelidade",
        description = "Consulta de saldo, histórico de movimentações e resgate de pontos."
)
@RestController
@RequestMapping("/fidelidade")
public class ContaFidelidadeController {

    private final ContaFidelidadeService contaFidelidadeService;

    public ContaFidelidadeController(
            ContaFidelidadeService contaFidelidadeService
    ) {
        this.contaFidelidadeService = contaFidelidadeService;
    }

    @Operation(
            summary = "Consultar conta de fidelidade",
            description = "Retorna o saldo de pontos e os dados da conta de fidelidade do cliente autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta retornada"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Conta de fidelidade não encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/minha-conta")
    public Map<String, Object> consultarMinhaConta(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        var conta = contaFidelidadeService.buscarPorUsuario(usuarioDetails.getId());

        return Map.of(
                "id", conta.getId(),
                "saldoPontos", conta.getSaldoPontos(),
                "criadoEm", conta.getCriadoEm()
        );
    }

    @Operation(
            summary = "Consultar movimentações de pontos",
            description = "Retorna o histórico de créditos e resgates da conta de fidelidade do cliente autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico retornado"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/minha-conta/movimentacoes")
    public List<Map<String, Object>> listarMovimentacoes(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        List<MovimentacaoPontos> movimentacoes =
                contaFidelidadeService.listarMovimentacoes(
                        usuarioDetails.getId()
                );

        return movimentacoes.stream()
                .map(movimentacao -> {
                    Map<String, Object> dados = new HashMap<>();

                    dados.put("id", movimentacao.getId());
                    dados.put("tipo", movimentacao.getTipo());
                    dados.put("pontos", movimentacao.getPontos());
                    dados.put("criadoEm", movimentacao.getCriadoEm());

                    if (movimentacao.getPedido() != null) {
                        dados.put("pedidoId", movimentacao.getPedido().getId());
                    }

                    return dados;
                })
                .toList();
    }

    @Operation(
            summary = "Resgatar pontos",
            description = "Realiza o resgate de pontos da conta de fidelidade do cliente autenticado, desde que exista saldo suficiente."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/minha-conta/resgates")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resgatarPontos(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @RequestParam int pontos) {

        contaFidelidadeService.resgatarPontos(
                usuarioDetails.getId(),
                pontos
        );
    }

}
