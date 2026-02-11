package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "oficinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(nullable = false, length = 150)
    private String nome;       

    @Column(length = 20)
    private String situacao;          

    private String abertura;   

    @Column(length = 100)
    private String natureza_juridica;

    @Column(length = 40)
    private String porte;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    private LocalDate dataCadastro;

    private Boolean ativo;
}

