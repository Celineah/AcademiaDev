package model;

import java.util.ArrayList;
import java.util.List;

import model.enums.TipoPlano;

public class Aluno extends Usuario {
    private TipoPlano plano;
    private List<Matricula> matriculas;

    public Aluno(String nome, String email, TipoPlano plano) {
        super(nome, email);
        this.plano = plano;
        this.matriculas = new ArrayList<>();
    }

    public TipoPlano getPlano() {
        return plano;
    }

    public void setPlano(TipoPlano plano) {
        this.plano = plano;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void adicionarMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    public void removerMatricula(Matricula matricula) {
        this.matriculas.remove(matricula);
    }
}
