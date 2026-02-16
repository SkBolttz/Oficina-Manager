package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.MarcaMoto;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.ModeloMoto;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.TipoCombustivel;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.TipoVeiculo;

public record AtualizarVeiculoDTO(
    String placa,
    MarcaMoto marca,
    ModeloMoto modelo,
    String cor,
    Integer ano,
    TipoVeiculo tipo,
    TipoCombustivel combustivel
) {
    
}
