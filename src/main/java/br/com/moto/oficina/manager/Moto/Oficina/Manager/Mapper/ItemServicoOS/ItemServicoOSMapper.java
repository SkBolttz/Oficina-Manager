package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.ItemServicoOS;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.ItemServicoOSResponseDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.ItemServicoOS;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemServicoOSMapper {

    @Mapping(source = "servico.descricao", target = "descricao")
    ItemServicoOSResponseDTO toDTO(ItemServicoOS item);
}
