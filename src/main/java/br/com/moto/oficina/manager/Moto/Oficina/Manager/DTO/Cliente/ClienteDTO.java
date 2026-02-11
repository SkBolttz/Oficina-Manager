package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;

public record ClienteDTO (
    String nome,
    String email,
    String cpfCnpj,
    String telefone,
    EnderecoDTO endereco
) {
    
}
