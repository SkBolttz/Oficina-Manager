package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Servico;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.AtualizarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.CadastrarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.ServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Servico;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "oficina", ignore = true)
    Servico toEntity(CadastrarServicoDTO dto);

    ServicoDTO toDto(Servico servico);

    void atualizarServico(
            AtualizarServicoDTO dto,
            @MappingTarget Servico servico);
}
