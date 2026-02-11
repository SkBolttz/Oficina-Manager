package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Oficina;

import java.util.Optional;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.AtualizarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaBuscaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaResumoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Oficina;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Endereco.EnderecoMapper;

@Mapper(componentModel = "spring", uses = EnderecoMapper.class)
public interface OficinaMapper {

    Oficina toEntity(OficinaDTO oficinaDTO);

    OficinaDTO toDto(Oficina oficinaLocalizada);

    void atualizarOficina(
            AtualizarOficinaDTO dto,
            @MappingTarget Oficina oficina);

    OficinaResumoDTO toResumoDto(Optional<Oficina> oficina);

    OficinaBuscaDTO toDtoBusca(Oficina oficina);
}
