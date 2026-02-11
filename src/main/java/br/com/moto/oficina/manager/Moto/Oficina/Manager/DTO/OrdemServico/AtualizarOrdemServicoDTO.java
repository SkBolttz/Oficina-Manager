package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico;

import java.math.BigDecimal;

public record AtualizarOrdemServicoDTO(
        String descricaoProblema,
        String diagnostico,
        String observacoes,
        Integer quilometragemSaida,
        BigDecimal desconto,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal valorTotal,
        String formaPagamento,
        Integer parcelas,
        Integer garantiaDias,
        String status
) {}