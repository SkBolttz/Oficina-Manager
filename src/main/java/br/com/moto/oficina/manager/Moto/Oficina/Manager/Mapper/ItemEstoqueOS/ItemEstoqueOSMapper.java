package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.ItemEstoqueOS;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.ItemEstoqueOSResponseDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.ItemEstoqueOS;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemEstoqueOSMapper {


    @Mapping(source = "produto.descricao", target = "descricao")
    @Mapping(source = "valorUnitario", target = "valorUnitario")
    @Mapping(source = "quantidade", target = "quantidade")
    @Mapping(source = "valorTotal", target = "valorTotal")
    ItemEstoqueOSResponseDTO toDTO(ItemEstoqueOS estoque);
}
