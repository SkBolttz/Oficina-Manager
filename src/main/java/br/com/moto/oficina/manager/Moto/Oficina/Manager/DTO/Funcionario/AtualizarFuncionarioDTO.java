package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Funcionario.Cargos;

public record AtualizarFuncionarioDTO(
        String cpfFuncionario,
        String nome,
        String email,
        String telefone,
        Cargos cargo,
        LocalDate dataAdmissao,
        EnderecoDTO endereco
) {}