package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.FuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.ServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.VeiculoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoDTO(
        Long id,
        String numero,
        OficinaDTO oficina,
        ClienteDTO cliente,
        VeiculoDTO veiculo,
        FuncionarioDTO funcionarioResponsavel,
        LocalDateTime dataAbertura,
        LocalDateTime dataConclusao,
        String status,
        Integer quilometragemEntrada,
        Integer quilometragemSaida,
        String descricaoProblema,
        String diagnostico,
        String observacoes,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal desconto,
        BigDecimal valorTotal,
        String formaPagamento,
        Integer parcelas,
        Integer garantiaDias,
        Boolean ativo,
        List<ItemServicoOSResponseDTO> servicos,
        List<ItemEstoqueOSResponseDTO> produtos
) {}

