package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificação do serviço
    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(length = 255)
    private String observacao;

    // Valores
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMaoDeObra;

    @Column(length = 20)
    private String tempoEstimado; // ex: 30min, 1h, 2h

    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;

    // Controle
    @Column(nullable = false)
    private Boolean ativo;
}
