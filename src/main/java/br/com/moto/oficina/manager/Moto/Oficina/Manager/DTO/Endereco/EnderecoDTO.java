package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco;

public record EnderecoDTO(
    String cep,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String municipio,
    String uf
) {
    
}
