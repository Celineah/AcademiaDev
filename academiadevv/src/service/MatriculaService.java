package service;

import exception.EnrollmentException;
import model.Aluno;
import model.Cursos;
import model.Matricula;
import model.enums.StatusCurso;
import model.enums.TipoPlano;

import java.util.*;

public class MatriculaService {

    private UsuarioService usuarioService;

    public MatriculaService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void enroll(Aluno aluno, Cursos curso) throws EnrollmentException {
        if (curso.getStatus() != StatusCurso.ACTIVE) {
            throw new EnrollmentException("Curso está inativo.");
        }

        long matriculasAtivas = aluno.getMatriculas().stream()
                .filter(m -> m.getCurso().getStatus() == StatusCurso.ACTIVE)
                .count();

        boolean jaMatriculado = aluno.getMatriculas().stream()
                .anyMatch(m -> m.getCurso().getTitulo().equalsIgnoreCase(curso.getTitulo()));

        if (jaMatriculado) {
            throw new EnrollmentException("Aluno já está matriculado neste curso.");
        }

        if (aluno.getPlano() == TipoPlano.BASICO && matriculasAtivas >= 3) {
            throw new EnrollmentException("Plano BASICO permite no máximo 3 cursos ativos.");
        }

        Matricula nova = new Matricula(aluno, curso);
        aluno.adicionarMatricula(nova);
    }

    public List<Matricula> findByAluno(Aluno aluno) {
        return new ArrayList<>(aluno.getMatriculas());
    }

    public Optional<Matricula> findEnrollment(Aluno aluno, String tituloCurso) {
        return aluno.getMatriculas().stream()
                .filter(m -> m.getCurso().getTitulo().equalsIgnoreCase(tituloCurso))
                .findFirst();
    }

    public void cancelEnrollment(Aluno aluno, String tituloCurso) throws EnrollmentException {
        Optional<Matricula> matriculaOpt = findEnrollment(aluno, tituloCurso);

        if (matriculaOpt.isEmpty()) {
            throw new EnrollmentException("Aluno não está matriculado neste curso.");
        }

        aluno.removerMatricula(matriculaOpt.get());
    }

    public double getAverageProgress() {
        List<Aluno> alunos = usuarioService.getAllStudents();

        return alunos.stream()
                .flatMap(a -> a.getMatriculas().stream())
                .mapToDouble(Matricula::getProgresso)
                .average()
                .orElse(0.0);
    }

    public Optional<Aluno> getStudentWithMostEnrollments() {
        List<Aluno> alunos = usuarioService.getAllStudents();

        return alunos.stream()
                .max(Comparator.comparingInt(a -> a.getMatriculas().size()));
    }
}
