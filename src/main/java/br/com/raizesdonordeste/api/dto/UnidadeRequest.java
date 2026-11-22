package br.com.raizesdonordeste.api.dto;

import br.com.raizesdonordeste.api.model.TipoUnidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UnidadeRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 20)
        String telefone,

        @NotBlank(message = "O logradouro é obrigatório")
        @Size(max = 150)
        String logradouro,

        @NotBlank(message = "O número é obrigatório")
        @Size(max = 20)
        String numero,

        @NotBlank(message = "O bairro é obrigatório")
        @Size(max = 100)
        String bairro,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 100)
        String cidade,

        @NotBlank(message = "O estado é obrigatório")
        @Pattern(
                regexp = "[A-Za-z]{2}",
                message = "O estado deve conter exatamente duas letras"
        )
        String estado,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String cep,

        @NotNull(message = "O tipo da unidade é obrigatório")
        TipoUnidade tipo

) {}
