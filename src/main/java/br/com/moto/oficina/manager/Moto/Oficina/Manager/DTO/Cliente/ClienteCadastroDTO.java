package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente;

import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;

public record ClienteCadastroDTO(
    String nome,
    String cpfCnpj,
    String email,
    String telefone,
    LocalDate dataNascimento,
    EnderecoDTO endereco
) {
    
}
