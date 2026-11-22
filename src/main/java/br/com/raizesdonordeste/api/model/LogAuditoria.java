package br.com.raizesdonordeste.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcaoAuditoria acao;

    @Column(nullable = false)
    private String entidade;

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(length = 500)
    private String detalhes;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    protected LogAuditoria() {}

    public LogAuditoria(
            Usuario usuario,
            AcaoAuditoria acao,
            String entidade,
            Long entidadeId,
            String detalhes
    ) {
        this.usuario = usuario;
        this.acao = acao;
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.detalhes = detalhes;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public AcaoAuditoria getAcao() {
        return acao;
    }

    public String getEntidade() {
        return entidade;
    }

    public Long getEntidadeId() {
        return entidadeId;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}