package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo;

public enum MarcaMoto {

    HONDA("Honda"),
    YAMAHA("Yamaha"),
    SUZUKI("Suzuki"),
    KAWASAKI("Kawasaki"),
    BMW("BMW Motorrad"),
    DUCATI("Ducati"),
    HARLEY_DAVIDSON("Harley-Davidson"),
    TRIUMPH("Triumph"),
    KTM("KTM"),
    BAJAJ("Bajaj"),
    ROYAL_ENFIELD("Royal Enfield"),
    SHINERAY("Shineray"),
    DAFRA("Dafra"),
    HAOJUE("Haojue"),
    KYMCO("Kymco"),
    MV_AGUSTA("MV Agusta"),
    APRILIA("Aprilia"),
    HUSQVARNA("Husqvarna"),
    CFMOTO("CFMoto"),
    BENELLI("Benelli");

    private final String descricao;

    MarcaMoto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

