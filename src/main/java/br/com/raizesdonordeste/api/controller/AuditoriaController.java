package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.LogAuditoriaResponse;
import br.com.raizesdonordeste.api.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Auditoria",
        description = "Consulta dos registros de auditoria das operações do sistema."
)
@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @Operation(
            summary = "Consultar auditoria",
            description = "Retorna registros de auditoria contendo ações realizadas no sistema. Acesso restrito ao administrador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros retornados"),
            @ApiResponse(responseCode = "401", description = "Autenticação necessária"),
            @ApiResponse(responseCode = "403", description = "Acesso restrito ao administrador")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public List<LogAuditoriaResponse> listarTodos() {
        return service.listarTodos();
    }
}