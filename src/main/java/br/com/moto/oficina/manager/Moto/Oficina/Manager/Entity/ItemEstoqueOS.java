package br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "item_estoque_os")
@Data
public class ItemEstoqueOS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrdemServico ordemServico;

    @ManyToOne
    private Estoque produto;

    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
}

