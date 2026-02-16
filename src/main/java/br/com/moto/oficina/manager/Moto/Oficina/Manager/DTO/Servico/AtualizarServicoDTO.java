package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico;

import java.math.BigDecimal;

public record AtualizarServicoDTO(
    Long idServico,
    String descricao,
    String observacao,
    BigDecimal valorMaoDeObra,
    String tempoEstimado
) { 
}
