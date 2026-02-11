package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico;

import java.math.BigDecimal;

public record CadastrarServicoDTO(
        String descricao,
        String observacao,
        BigDecimal valorMaoDeObra,
        String tempoEstimado) {

}
