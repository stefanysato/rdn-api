package br.com.raizesdonordeste.api.repository;

import br.com.raizesdonordeste.api.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAuditoriaRepository
        extends JpaRepository<LogAuditoria, Long> {
}