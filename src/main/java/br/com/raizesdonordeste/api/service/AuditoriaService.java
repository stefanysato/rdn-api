package br.com.raizesdonordeste.api.service;

import br.com.raizesdonordeste.api.dto.LogAuditoriaResponse;
import br.com.raizesdonordeste.api.model.AcaoAuditoria;
import br.com.raizesdonordeste.api.model.LogAuditoria;
import br.com.raizesdonordeste.api.model.Usuario;
import br.com.raizesdonordeste.api.repository.LogAuditoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    public AuditoriaService(LogAuditoriaRepository logAuditoriaRepository) {
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    public void registrar(
            Usuario usuario,
            AcaoAuditoria acao,
            String entidade,
            Long entidadeId,
            String detalhes
    ) {
        LogAuditoria log = new LogAuditoria(
                usuario,
                acao,
                entidade,
                entidadeId,
                detalhes
        );

        logAuditoriaRepository.save(log);
    }

    public List<LogAuditoriaResponse> listarTodos() {
        return logAuditoriaRepository.findAll()
                .stream()
                .map(log -> new LogAuditoriaResponse(
                        log.getId(),
                        log.getUsuario().getId(),
                        log.getUsuario().getNome(),
                        log.getAcao(),
                        log.getEntidade(),
                        log.getEntidadeId(),
                        log.getDetalhes(),
                        log.getDataHora()
                ))
                .toList();
    }
}