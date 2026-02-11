package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico;

import java.math.BigDecimal;

public record ItemEstoqueOSResponseDTO(
        Long id,
        String descricao,
        BigDecimal valorUnitario,
        Integer quantidade,
        BigDecimal valorTotal
) {}
