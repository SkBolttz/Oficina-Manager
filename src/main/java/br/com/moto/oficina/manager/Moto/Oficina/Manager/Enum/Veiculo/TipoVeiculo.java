package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo;

public enum TipoVeiculo {

    MOTO("Moto"),
    CARRO("Carro"),
    CAMINHAO("Caminhão"),
    ONIBUS("Ônibus"),
    VAN("Van"),
    UTILITARIO("Utilitário"),
    PICKUP("Pickup"),
    TRATOR("Trator"),
    MAQUINA_AGRICOLA("Máquina Agrícola"),
    REBOQUE("Reboque"),
    SEMI_REBOQUE("Semi-Reboque"),
    QUADRICICLO("Quadriciclo"),
    CICLOMOTOR("Ciclomotor"),
    PATINETE_ELETRICO("Patinete Elétrico"),
    BICICLETA("Bicicleta");

    private final String descricao;

    TipoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

