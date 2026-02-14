package br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Role;

public enum Role {
    OFICINA("Oficina"),
    ADMIN("Admin");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
