package br.com.raizesdonordeste.api.controller;

import br.com.raizesdonordeste.api.dto.LogAuditoriaResponse;
import br.com.raizesdonordeste.api.service.AuditoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogAuditoriaResponse> listarTodos() {
        return service.listarTodos();
    }
}