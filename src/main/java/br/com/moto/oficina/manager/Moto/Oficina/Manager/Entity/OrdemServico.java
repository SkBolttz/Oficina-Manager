package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.FormaPagamento;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.Status;

@Entity
@Table(name = "ordens_servico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "funcionario_responsavel_id", nullable = false)
    private Funcionario funcionarioResponsavel;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    private LocalDateTime dataConclusao;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer quilometragemEntrada;

    private Integer quilometragemSaida;

    @Column(length = 1000)
    private String descricaoProblema;

    @Column(length = 1000)
    private String diagnostico;

    @Column(length = 1000)
    private String observacoes;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorServicos;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorProdutos;

    @Column(precision = 12, scale = 2)
    private BigDecimal desconto;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(length = 30)
    private FormaPagamento formaPagamento;

    private Integer parcelas;

    private Integer garantiaDias;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemServicoOS> servicos;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEstoqueOS> produtos;

    @NotNull
    private LocalDate prazoEntrega;
}

