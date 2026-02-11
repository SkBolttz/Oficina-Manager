package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.MarcaMoto;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.ModeloMoto;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.TipoCombustivel;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo.TipoVeiculo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String placa;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private MarcaMoto marca;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ModeloMoto modelo;

    @Column(length = 20)
    private String cor;

    @Column(length = 4)
    private Integer ano;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private TipoCombustivel combustivel;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;

    private Boolean ativo;
}

