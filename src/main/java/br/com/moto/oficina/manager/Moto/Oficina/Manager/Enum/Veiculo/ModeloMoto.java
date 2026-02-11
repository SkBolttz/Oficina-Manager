package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Veiculo;

public enum ModeloMoto {

    // HONDA
    CG_160("CG 160", MarcaMoto.HONDA),
    BIZ_125("Biz 125", MarcaMoto.HONDA),
    PCX_160("PCX 160", MarcaMoto.HONDA),
    XRE_300("XRE 300", MarcaMoto.HONDA),
    CB_500("CB 500", MarcaMoto.HONDA),
    AFRICA_TWIN("Africa Twin", MarcaMoto.HONDA),

    // YAMAHA
    FAZER_250("Fazer 250", MarcaMoto.YAMAHA),
    MT_03("MT-03", MarcaMoto.YAMAHA),
    MT_07("MT-07", MarcaMoto.YAMAHA),
    R3("YZF-R3", MarcaMoto.YAMAHA),
    XTZ_250("XTZ 250 Lander", MarcaMoto.YAMAHA),
    NMAX_160("NMax 160", MarcaMoto.YAMAHA),

    // SUZUKI
    GSX_S750("GSX-S750", MarcaMoto.SUZUKI),
    GSX_R1000("GSX-R1000", MarcaMoto.SUZUKI),
    V_STROM_650("V-Strom 650", MarcaMoto.SUZUKI),
    BURGMAN_400("Burgman 400", MarcaMoto.SUZUKI),

    // KAWASAKI
    NINJA_400("Ninja 400", MarcaMoto.KAWASAKI),
    Z400("Z400", MarcaMoto.KAWASAKI),
    Z900("Z900", MarcaMoto.KAWASAKI),
    VERSYS_650("Versys 650", MarcaMoto.KAWASAKI),

    // BMW
    G_310_R("G 310 R", MarcaMoto.BMW),
    G_310_GS("G 310 GS", MarcaMoto.BMW),
    F_750_GS("F 750 GS", MarcaMoto.BMW),
    R_1250_GS("R 1250 GS", MarcaMoto.BMW),

    // DUCATI
    MONSTER_937("Monster 937", MarcaMoto.DUCATI),
    PANIGALE_V4("Panigale V4", MarcaMoto.DUCATI),
    MULTISTRADA_V4("Multistrada V4", MarcaMoto.DUCATI),

    // HARLEY-DAVIDSON
    IRON_883("Iron 883", MarcaMoto.HARLEY_DAVIDSON),
    FAT_BOY("Fat Boy", MarcaMoto.HARLEY_DAVIDSON),
    SPORTSTER_S("Sportster S", MarcaMoto.HARLEY_DAVIDSON),

    // TRIUMPH
    STREET_TRIPLE("Street Triple", MarcaMoto.TRIUMPH),
    TIGER_900("Tiger 900", MarcaMoto.TRIUMPH),
    BONNEVILLE_T120("Bonneville T120", MarcaMoto.TRIUMPH),

    // KTM
    DUKE_390("Duke 390", MarcaMoto.KTM),
    ADVENTURE_390("Adventure 390", MarcaMoto.KTM),

    // ROYAL ENFIELD
    METEOR_350("Meteor 350", MarcaMoto.ROYAL_ENFIELD),
    CLASSIC_350("Classic 350", MarcaMoto.ROYAL_ENFIELD),
    HIMALAYAN_411("Himalayan 411", MarcaMoto.ROYAL_ENFIELD),

    // BAJAJ
    DOMINAR_400("Dominar 400", MarcaMoto.BAJAJ),
    PULSAR_200("Pulsar 200", MarcaMoto.BAJAJ);

    private final String descricao;
    private final MarcaMoto marca;

    ModeloMoto(String descricao, MarcaMoto marca) {
        this.descricao = descricao;
        this.marca = marca;
    }

    public String getDescricao() {
        return descricao;
    }

    public MarcaMoto getMarca() {
        return marca;
    }
}

