package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Funcionario.Cargos;

public record FuncionarioDTO(
                Long id,
                String nome,
                Cargos cargo,
                String email,
                String telefone,
                LocalDate dataAdmissao,
                Boolean ativo,
                EnderecoDTO endereco) {
}
