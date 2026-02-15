package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarOrdemServicoDTO(
        String placaVeiculo,
        String descricaoProblema,
        Integer quilometragemEntrada,
        BigDecimal desconto,
        String observacoes,
        LocalDate prazoEntrega) {
}
