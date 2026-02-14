package br.com.moto.oficina.manager.Moto.Oficina.Manager.Configuracao;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Geficina API")
                        .description("API responsável pelo gerenciamento de oficinas mecânicas, controle de estoque, ordens de serviço e usuários e muito mais.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Henrique B.")
                                .email("pedrohenriqueborba1@gmail.com")
                                .url("https://www.linkedin.com/in/pedroheborba/")));
    }
}
