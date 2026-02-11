package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados pessoais
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpfCnpj;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    private LocalDate dataNascimento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    // Controle
    @Column(nullable = false)
    private LocalDate dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    // Dados da Oficina que o cliente está vinculado.
    @ManyToOne
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;
}
