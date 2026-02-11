package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Endereco;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Endereco;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    
        void atualizarEndereco(
        EnderecoDTO dto,
        @MappingTarget Endereco endereco
    );

    Endereco toEntity(EnderecoDTO dto);

    EnderecoDTO toDto(Endereco endereco);
}
