package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "enderecos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String logradouro;

    @Column(length = 10)
    private String numero;

    @Column(length = 50)
    private String bairro;

    @Column(length = 50)
    private String municipio;

    @Column(length = 2)
    private String uf;

    @Column(length = 9)
    private String cep;

    @Column(length = 100)
    private String complemento;

    // Um endereço pode ter vários clientes
    @OneToMany(mappedBy = "endereco")
    private List<Cliente> clientes;
}

