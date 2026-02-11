package br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepDTO(

        String cep,
        String logradouro,
        String complemento,
        String bairro,

        @JsonProperty("localidade")
        String municipio,

        @JsonProperty("uf")
        String uf
) {}
