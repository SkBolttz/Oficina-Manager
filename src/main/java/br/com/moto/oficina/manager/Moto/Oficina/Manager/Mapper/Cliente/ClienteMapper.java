package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Cliente;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteAtualizarDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteCadastroDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    Cliente toEntity(ClienteCadastroDTO dto);

    ClienteDTO toDto(Cliente cliente);

    Cliente toEntityAtualizar(ClienteAtualizarDTO dto);

    void atualizarCliente(
            ClienteAtualizarDTO dto,
            @MappingTarget Cliente cliente);
}
