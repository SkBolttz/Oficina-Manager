package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico;

public enum Status {

    ABERTA("Aberta"),
    EM_EXECUCAO("Em execução"),
    AGUARDANDO_PECA("Aguardando peça"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
