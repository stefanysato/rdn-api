package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.model.Pagamento;
import br.com.raizesdonordeste.api.model.StatusPagamento;
import br.com.raizesdonordeste.api.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(
        name = "Pagamentos",
        description = "Solicitação, processamento e consulta de pagamentos dos pedidos."
)
@RestController
@RequestMapping("/pedidos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @Operation(
            summary = "Processar pagamento",
            description = "Solicita o processamento do pagamento de um pedido. A aprovação ou recusa é simulada pelo serviço de pagamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento processado"),
            @ApiResponse(responseCode = "400", description = "Pedido inválido ou pagamento não permitido"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso ao pedido negado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{pedidoId}/pagamentos")
    public Map<String, Object> processarPagamento(
            @PathVariable Long pedidoId) {

        Pagamento pagamento = pagamentoService.processarPagamento(
                pedidoId
        );

        String mensagem = pagamento.getStatus() == StatusPagamento.APROVADO
                ? "Pagamento aprovado."
                : "Pagamento recusado.";

        return Map.of(
                "pagamentoId", pagamento.getId(),
                "pedidoId", pagamento.getPedido().getId(),
                "valor", pagamento.getValor(),
                "status", pagamento.getStatus(),
                "mensagem", mensagem,
                "criadoEm", pagamento.getCriadoEm()
        );
    }

    @Operation(
            summary = "Consultar pagamentos do pedido",
            description = "Retorna os registros de pagamento associados ao pedido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamentos retornados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso ao pedido negado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{pedidoId}/pagamentos")
    public java.util.List<Map<String, Object>> listarPagamentos(
            @PathVariable Long pedidoId) {

        return pagamentoService.listarPagamentosPedido(
                pedidoId
        );
    }

}
