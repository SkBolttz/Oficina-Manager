package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque;

import java.math.BigDecimal;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Estoque.UnidadeMedida;

public record AtualizarEstoqueDTO(
    Long idItem,
    String descricao,
    String codigo,
    BigDecimal precoCompra,
    BigDecimal precoVenda,
    Integer estoqueAtual,
    Integer estoqueMinimo,
    Integer estoqueMaximo,
    UnidadeMedida unidadeMedida
) {
    
}
