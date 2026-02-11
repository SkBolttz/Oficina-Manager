package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteResumoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaResumoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.*;

public record VeiculoDTO(

    Long id,
    String placa,
    MarcaMoto marca,
    ModeloMoto modelo,
    String cor,
    Integer ano,
    TipoVeiculo tipo,
    TipoCombustivel combustivel,
    ClienteResumoDTO cliente,
    OficinaResumoDTO oficina

) {
}
