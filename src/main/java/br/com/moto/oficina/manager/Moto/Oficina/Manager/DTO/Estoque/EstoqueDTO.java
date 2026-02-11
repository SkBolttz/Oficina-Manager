package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaResumoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Estoque.UnidadeMedida;

public record EstoqueDTO(

    String descricao,
    String codigo,
    BigDecimal precoCompra,
    BigDecimal precoVenda,
    Integer estoqueAtual,
    Integer estoqueMinimo,
    Integer estoqueMaximo,
    UnidadeMedida unidadeMedida,
    LocalDate ultimaReposicao,
    OficinaResumoDTO oficina

) {
}
