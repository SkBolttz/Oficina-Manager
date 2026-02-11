package br.com.moto.oficina.manager.Moto.Oficina.Manager.Mapper.Funcionario;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.AtualizarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.CadastrarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.FuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Funcionario;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    FuncionarioDTO toResponseDTO(Funcionario funcionario);

    Funcionario toEntity(CadastrarFuncionarioDTO dto);

    void atualizarFuncionario(
            AtualizarFuncionarioDTO dto,
            @MappingTarget Funcionario endereco);

    List<FuncionarioDTO> toResponseDTOList(List<Funcionario> funcionarios);
}


