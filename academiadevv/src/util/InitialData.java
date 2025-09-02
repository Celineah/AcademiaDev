package util;

import model.*;
import model.enums.NivelDificuldade;
import model.enums.StatusCurso;
import model.enums.TipoPlano;
import service.*;

public class InitialData {

    private final UsuarioService usuarioService;
    private final CursoService cursoService;

    public InitialData(UsuarioService usuarioService, CursoService cursoService) {
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
    }

    public void carregarDados() {
        // Criando Admins
        Admin admin1 = new Admin("Carlos Silva", "admin1@academiadev.com");
        Admin admin2 = new Admin("Juliana Souza", "admin2@academiadev.com");

        usuarioService.addUser(admin1);
        usuarioService.addUser(admin2);

        // Criando Alunos
        Aluno aluno1 = new Aluno("Maria Oliveira", "maria@gmail.com", TipoPlano.BASICO);
        Aluno aluno2 = new Aluno("João Pereira", "joao@gmail.com", TipoPlano.PREMIUM);
        Aluno aluno3 = new Aluno("Ana Costa", "ana@gmail.com", TipoPlano.BASICO);

        usuarioService.addUser(aluno1);
        usuarioService.addUser(aluno2);
        usuarioService.addUser(aluno3);

        // Criando Cursos
        Cursos curso1 = new Cursos(
                "Java Fundamentos",
                "Curso introdutório de Java",
                "Prof. Ricardo",
                20,
                NivelDificuldade.BEGINNER,
                StatusCurso.ACTIVE
        );

        Cursos curso2 = new Cursos(
                "Spring Boot Avançado",
                "Desenvolvimento de APIs com Spring Boot",
                "Prof. Ricardo",
                30,
                NivelDificuldade.ADVANCED,
                StatusCurso.ACTIVE
        );

        Cursos curso3 = new Cursos(
                "Banco de Dados",
                "Introdução a SQL e modelagem",
                "Profª. Larissa",
                25,
                NivelDificuldade.INTERMEDIATE,
                StatusCurso.INACTIVE
        );

        Cursos curso4 = new Cursos(
                "Git e GitHub",
                "Controle de versão com Git",
                "Prof. Ricardo",
                10,
                NivelDificuldade.BEGINNER,
                StatusCurso.ACTIVE
        );

        cursoService.adicionarCurso(curso1);
        cursoService.adicionarCurso(curso2);
        cursoService.adicionarCurso(curso3);
        cursoService.adicionarCurso(curso4);
    }
}
