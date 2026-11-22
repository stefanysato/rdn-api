package br.com.raizesdonordeste.api.dto;

import br.com.raizesdonordeste.api.model.TipoUnidade;

import java.time.LocalDateTime;

public record UnidadeResponse(
        Long id,
        String nome,
        String telefone,
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String cep,
        TipoUnidade tipo,
        boolean ativa,
        LocalDateTime criadaEm
) {}
