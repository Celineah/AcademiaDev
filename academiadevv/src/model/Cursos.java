package model;

import model.enums.NivelDificuldade;
import model.enums.StatusCurso;

public class Cursos {
    private String titulo;
    private String descricao;
    private String nomeInstrutor;
    private int duracao;
    private NivelDificuldade nivel;
    private StatusCurso status;

    public Cursos(String titulo, String descricao, String nomeInstrutor, int duracao, NivelDificuldade nivel, StatusCurso status){
        this.titulo = titulo;
        this.descricao = descricao;
        this.nomeInstrutor = nomeInstrutor;
        this.duracao = duracao;
        this.nivel = nivel;
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNomeInstrutor() {
        return nomeInstrutor;
    }

    public int getDuracaoHoras() {
        return duracao;
    }

    public NivelDificuldade getNivel() {
        return nivel;
    }

    public StatusCurso getStatus() {
        return status;
    }

    public void setStatus(StatusCurso status) {
        this.status = status;
    }
}
