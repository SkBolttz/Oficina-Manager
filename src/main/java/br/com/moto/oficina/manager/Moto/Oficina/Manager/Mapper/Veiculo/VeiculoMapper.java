package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Veiculo;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.AtualizarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.CadastrarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.VeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Veiculo;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {

    Veiculo toEntity(CadastrarVeiculoDTO cadastrarVeiculoDTO);

    VeiculoDTO toDTO(Veiculo veiculo);

    void atualizarVeiculo(AtualizarVeiculoDTO dto,
            @MappingTarget Veiculo veiculo);

    List<VeiculoDTO> toDtoList(List<Veiculo> veiculos);
}
