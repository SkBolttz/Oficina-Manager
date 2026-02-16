package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;

public record ClienteAtualizarDTO(
    String cnpjCpf,
    String nome,
    String email,
    String telefone,
    LocalDate dataNascimento,
    EnderecoDTO endereco
) {
    
}
