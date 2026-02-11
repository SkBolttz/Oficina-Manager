package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;

public record OficinaDTO(

        Long id,
        String cnpj,
        String nome,
        String situacao,
        String abertura,
        String natureza_juridica,
        String porte,
        String email,
        String telefone,
        EnderecoDTO endereco
) {
}