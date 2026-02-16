package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.FormaPagamento;
import java.math.BigDecimal;

public record FinalizarOsDTO(
        Long osId,
        Integer quilometragemSaida,
        String diagnostico,
        FormaPagamento formaPagamento,
        Integer parcelas,
        Integer garantiaDias,
        BigDecimal desconto
) {
}
