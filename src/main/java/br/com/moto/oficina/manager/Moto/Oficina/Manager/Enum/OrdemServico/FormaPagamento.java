package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico;

public enum FormaPagamento {

    DINHEIRO("Dinheiro"),
    PIX("Pix"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    BOLETO("Boleto Bancário"),
    TRANSFERENCIA_BANCARIA("Transferência Bancária"),
    CHEQUE("Cheque"),
    VALE_ALIMENTACAO("Vale Alimentação"),
    VALE_REFEICAO("Vale Refeição"),
    CREDIARIO("Crediário"),
    PARCELADO("Parcelado"),
    FINANCIAMENTO("Financiamento");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

