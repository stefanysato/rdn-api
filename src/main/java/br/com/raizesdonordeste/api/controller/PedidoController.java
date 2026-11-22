package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.PedidoRequest;
import br.com.raizesdonordeste.api.dto.PedidoResponse;
import br.com.raizesdonordeste.api.model.CanalPedido;
import br.com.raizesdonordeste.api.model.StatusPedido;
import br.com.raizesdonordeste.api.security.UsuarioDetails;
import br.com.raizesdonordeste.api.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(
        @PathVariable Long id,
        @AuthenticationPrincipal UsuarioDetails usuario
    ) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id, usuario.getId()));
    }

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
