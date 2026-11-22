package br.com.raizesdonordeste.api.dto;

import br.com.raizesdonordeste.api.model.AcaoAuditoria;
import java.time.LocalDateTime;

public record LogAuditoriaResponse(
        Long id,
        Long usuarioId,
        String usuarioNome,
        AcaoAuditoria acao,
        String entidade,
        Long entidadeId,
        String detalhes,
        LocalDateTime dataHora
) {}