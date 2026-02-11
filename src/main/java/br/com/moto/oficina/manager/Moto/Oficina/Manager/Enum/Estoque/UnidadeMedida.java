package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Estoque;

public enum UnidadeMedida {

    UNIDADE("Unidade", "un"),
    QUILOGRAMA("Quilograma", "kg"),
    GRAMA("Grama", "g"),
    TONELADA("Tonelada", "t"),
    LITRO("Litro", "l"),
    MILILITRO("Mililitro", "ml"),
    METRO("Metro", "m"),
    CENTIMETRO("Centímetro", "cm"),
    MILIMETRO("Milímetro", "mm"),
    METRO_QUADRADO("Metro Quadrado", "m²"),
    METRO_CUBICO("Metro Cúbico", "m³"),
    CAIXA("Caixa", "cx"),
    PACOTE("Pacote", "pct"),
    FARDO("Fardo", "fd"),
    PAR("Par", "par");

    private final String descricao;
    private final String sigla;

    UnidadeMedida(String descricao, String sigla) {
        this.descricao = descricao;
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSigla() {
        return sigla;
    }
}

