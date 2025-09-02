package service;

import java.util.*;
import java.util.stream.Collectors;

import model.*;
import model.enums.NivelDificuldade;
import model.enums.StatusCurso;

public class CursoService {
    private Map<String, Cursos> cursos = new HashMap<>();

    public void adicionarCurso(Cursos curso) {
        cursos.put(curso.getTitulo(), curso);
    }

    public Optional<Cursos> buscarPorTitulo(String titulo) {
        return Optional.ofNullable(cursos.get(titulo));
    }

    public List<Cursos> listarCursosAtivos() {
        return cursos.values().stream()
            .filter(c -> c.getStatus() == StatusCurso.ACTIVE)
            .collect(Collectors.toList());
    }

    public List<Cursos> listarCursosPorNivel(NivelDificuldade nivel) {
        return cursos.values().stream()
            .filter(c -> c.getNivel() == nivel)
            .sorted(Comparator.comparing(Cursos::getTitulo))
            .collect(Collectors.toList());
    }

    public Set<String> listarInstrutoresDeCursosAtivos() {
        return cursos.values().stream()
            .filter(c -> c.getStatus() == StatusCurso.ACTIVE)
            .map(Cursos::getNomeInstrutor)
            .collect(Collectors.toSet());
    }

    public void alterarStatusCurso(String titulo, StatusCurso novoStatus) {
        Cursos curso = cursos.get(titulo);
        if (curso != null) {
            curso.setStatus(novoStatus);
        }
    }

    public Collection<Cursos> getTodosCursos() {
        return cursos.values();
    }
}
