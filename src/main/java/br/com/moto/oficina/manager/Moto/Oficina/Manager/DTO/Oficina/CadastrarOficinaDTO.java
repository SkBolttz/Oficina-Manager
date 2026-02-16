package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;

public record CadastrarOficinaDTO (
    String razaoSocial,
    String situacao,
    String dataAbertura,
    String naturezaJuridica,
    String porte,
    String email,
    String telefone,
    EnderecoDTO endereco
){
}
