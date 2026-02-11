package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico;

import java.math.BigDecimal;

public record ItemServicoOSResponseDTO(
        String descricao,
        BigDecimal valorUnitario
) {}