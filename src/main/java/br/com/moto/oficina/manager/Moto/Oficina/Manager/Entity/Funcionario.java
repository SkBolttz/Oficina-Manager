package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import java.time.LocalDate;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Funcionario.Cargos;

@Entity
@Table(name = "funcionarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 100)
    @Email
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Cargos cargo;         

    private LocalDate dataAdmissao;

    @ManyToOne
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    private Boolean ativo;
}

