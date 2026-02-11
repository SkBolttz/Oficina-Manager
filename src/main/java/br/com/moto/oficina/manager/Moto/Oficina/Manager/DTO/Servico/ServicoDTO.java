package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico;

import java.math.BigDecimal;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaResumoDTO;

public record ServicoDTO(

    Long id,
    String descricao,
    String observacao,
    BigDecimal valorMaoDeObra,
    String tempoEstimado

) {
}
