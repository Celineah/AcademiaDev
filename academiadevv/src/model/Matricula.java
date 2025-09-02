package model;

public class Matricula {
    private Aluno aluno;
    private Cursos curso;
    private double progresso; //0 a 100...
      private boolean ativo;  
    public Matricula(Aluno aluno, Cursos curso) {
        this.aluno = aluno;
        this.curso = curso;
        this.progresso = 0.0;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Cursos getCurso() {
        return curso;
    }

    public double getProgresso() {
        return progresso;
    }

    public void setProgresso(double progresso) {
        if(progresso < 0) {
            this.progresso = 0;
        } else if(progresso > 100) {
            this.progresso = 100;
        } else {
            this.progresso = progresso;
        }
    }
        public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
