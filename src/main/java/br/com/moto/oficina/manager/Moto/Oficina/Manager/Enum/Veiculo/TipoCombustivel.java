package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo;

public enum TipoCombustivel {

    GASOLINA("Gasolina"),
    GASOLINA_ADITIVADA("Gasolina Aditivada"),
    ETANOL("Etanol"),
    FLEX("Flex"),
    DIESEL("Diesel"),
    DIESEL_S10("Diesel S-10"),
    DIESEL_S500("Diesel S-500"),
    GNV("Gás Natural Veicular"),
    ELETRICO("Elétrico"),
    HIBRIDO("Híbrido");

    private final String descricao;

    TipoCombustivel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

