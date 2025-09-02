package model;

public enum PlanoAssinatura {
    BASICO(29.90, 3, "Plano Básico: até 3 cursos simultâneos"),
    PREMIUM(59.90, Integer.MAX_VALUE, "Plano Premium: cursos ilimitados");

    private final double preco;
    private final int maximoCursos;
    private final String descricao;

    PlanoAssinatura(double preco, int maximoCursos, String descricao) {
        this.preco = preco;
        this.maximoCursos = maximoCursos;
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public int getMaximoCursos() {
        return maximoCursos;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return name() + " (" + descricao + ") - R$ " + preco;
    }
}
