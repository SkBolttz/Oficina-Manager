package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Estoque;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.AtualizarEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.CadastroEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.EstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Estoque;

@Mapper(componentModel = "spring")
public interface EstoqueMapper {

    EstoqueDTO toDTO(Estoque estoque);

    Estoque toEntity(CadastroEstoqueDTO dto);

    void atualizarEstoque(
            AtualizarEstoqueDTO dto,
            @MappingTarget Estoque estoque);

    List<EstoqueDTO> toDtoList(List<Estoque> estoques);
    
}
