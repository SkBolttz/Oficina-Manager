package br.com.moto.oficina.manager.Moto.Oficina.Manager.Api;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaDTO;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReceitaWS {

    private final WebClient webClient;

    public ReceitaWS(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://www.receitaws.com.br/v1/cnpj")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .build();
    }

    public OficinaDTO buscarCnpj(String cnpj) {
        return webClient.get()
                .uri("/{cnpj}", cnpj)
                .retrieve()
                .bodyToMono(OficinaDTO.class)
                .block();  // ou use Mono para reatividade total
    }
}
