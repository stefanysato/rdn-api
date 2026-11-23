package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.PedidoRequest;
import br.com.raizesdonordeste.api.dto.PedidoResponse;
import br.com.raizesdonordeste.api.model.CanalPedido;
import br.com.raizesdonordeste.api.model.StatusPedido;
import br.com.raizesdonordeste.api.security.UsuarioDetails;
import br.com.raizesdonordeste.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        name = "Pedidos",
        description = "Registro, consulta, cancelamento e acompanhamento de pedidos."
)
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Registrar pedido",
            description = "Registra um novo pedido para uma unidade com os produtos, quantidades e canal de origem."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido registrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso à unidade negado"),
            @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<PedidoResponse> registrarPedido(
            @Valid @RequestBody PedidoRequest request,
            @AuthenticationPrincipal UsuarioDetails usuario
    ) {
        PedidoResponse response = pedidoService.registrarPedido(
                request,
                usuario.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Consulta os detalhes de um pedido, respeitando as permissões do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos retornados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(
        @PathVariable Long id,
        @AuthenticationPrincipal UsuarioDetails usuario
    ) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id, usuario.getId()));
    }

    @Operation(
            summary = "Listar pedidos",
            description = "Retorna os pedidos de acordo com as permissões do usuário autenticado. Clientes acessam seus pedidos, funcionários acessam os pedidos da própria unidade e administradores possuem acesso global. O método também permite que os pedidos sejam filtrados por canal."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos retornados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos(
            @RequestParam(required = false) CanalPedido canal,
            @AuthenticationPrincipal UsuarioDetails usuario
    ) {
        if (canal != null) {
            return ResponseEntity.ok(
                    pedidoService.listarPorCanal(canal, usuario.getId())
            );
        }
        return ResponseEntity.ok(
                pedidoService.listarTodos(usuario.getId())
        );
    }


    @Operation(
            summary = "Atualizar status do pedido",
            description = "Atualiza o status de um pedido conforme o fluxo permitido. Operação restrita aos funcionários autorizados da unidade e administradores."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso ao pedido negado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        return ResponseEntity.ok(
                pedidoService.atualizarStatus(
                        id,
                        status,
                        usuarioDetails.getId()
                )
        );
    }

    @Operation(
            summary = "Cancelar pedido",
            description = "Solicita o cancelamento de um pedido, respeitando as regras de negócio e o status atual."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails
    ) {
        return ResponseEntity.ok(
                pedidoService.cancelarPedido(id, usuarioDetails.getId())
        );
    }

}
