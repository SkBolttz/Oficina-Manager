package br.com.moto.oficina.manager.Moto.Oficina.Manager.Api;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.ViaCepDTO;

@Service
public class ViaCep {

    private final WebClient webClient;

    public ViaCep(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://viacep.com.br/ws/")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .build();
    }

    public ViaCepDTO buscarCep(String cep) {

        String cepNormalizado = cep.replaceAll("\\D", "");
        System.out.println("cepNormalizado: " + cepNormalizado);

        return webClient.get()
                .uri("{cep}/json/", cepNormalizado)
                .retrieve()
                .bodyToMono(ViaCepDTO.class)
                .block();
    }
}
