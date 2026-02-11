package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Funcionario.Cargos;

public record CadastrarFuncionarioDTO(
        String nome,
        String cpf,
        String email,
        String telefone,
        Cargos cargo,
        EnderecoDTO endereco
) {}