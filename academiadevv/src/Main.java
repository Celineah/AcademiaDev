import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import model.Admin;
import model.Aluno;
import model.Cursos;
import model.Matricula;
import model.TicketSuporte;
import model.Usuario;
import model.enums.StatusCurso;
import model.enums.TipoPlano;
import model.enums.NivelDificuldade;

import service.CursoService;
import service.MatriculaService;
import service.TicketService;
import service.UsuarioService;
import util.InitialData;
import exception.EnrollmentException;
import exception.NotFoundException;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static UsuarioService usuarioService = new UsuarioService();
    private static CursoService cursoService = new CursoService();
    private static MatriculaService enrollmentService = new MatriculaService(usuarioService);
    private static TicketService ticketService = new TicketService();

    public static void main(String[] args) {
        InitialData initialData = new InitialData(usuarioService, cursoService);
        initialData.carregarDados();

        while (true) { // Loop principal para manter o programa rodando e permitir múltiplos logins
            System.out.println("===== AcademiaDev - Login =====");
            System.out.print("Digite seu email (ou 'sair' para encerrar): ");
            String email = sc.nextLine();

            if (email.equalsIgnoreCase("sair")) {
                System.out.println("Encerrando programa. Até logo!");
                break;
            }

            try {
                Usuario usuario = usuarioService.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

                if (usuario instanceof Aluno) {
                    menuAluno((Aluno) usuario);
                } else if (usuario instanceof Admin) {
                    menuAdmin((Admin) usuario);
                } else {
                    System.out.println("Tipo de usuário desconhecido.");
                }
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    

    private static void menuAluno(Aluno aluno) {
        while (true) {
     
            System.out.println("\n----Menu Aluno----");
            System.out.println("\n");
            System.out.println("[1] Realizar matrícula");
            System.out.println("[2] Consultar matrículas");
            System.out.println("[3] Atualizar progresso");
            System.out.println("[4] Cancelar matrícula");
            System.out.println("[5] Abrir Ticket Suporte");
            System.out.println("[6] Consultar cursos existentes");
            System.out.println("[0] voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    enrollCourseFlow(aluno);
                    break;
                case 2:
                    listEnrollments(aluno);
                    break;
                case 3:
                    updateProgressFlow(aluno);
                    break;
                case 4:
                    cancelEnrollmentFlow(aluno);
                    break;
                case 5:
                    openTicketFlow(aluno);
                    break;
                case 6:
                    listCourses();
                    break;
                case 0:
                    return; // Volta para o main, que apresenta a tela de login novamente
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private static void menuAdmin(Admin admin) {
        while (true) {
            System.out.println("\n===== Menu Admin =====");
            System.out.println("1. Ativar/Inativar curso");
            System.out.println("2. Alterar plano de aluno");
            System.out.println("3. Atender tickets de suporte");
            System.out.println("4. Gerar relatórios");
            System.out.println("5. Exportar dados em CSV");
            System.out.println("0. Voltar para login");
            System.out.print("Opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    manageCourseStatusFlow();
                    break;
                case 2:
                    changeStudentPlanFlow();
                    break;
                case 3:
                    attendSupportTicketsFlow();
                    break;
                case 4:
                    generateReportsFlow();
                    break;
                case 5:
                    exportDataCsvFlow();
                    break;
                case 0:
                    return; // Volta para o main, que apresenta a tela de login novamente
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    // ---------- Fluxos Aluno ----------

    private static void enrollCourseFlow(Aluno aluno) {
        System.out.println("\nDigite o título do curso para matrícula:");
        String title = sc.nextLine();
        Optional<Cursos> cursoOpt = cursoService.buscarPorTitulo(title);

        if (!cursoOpt.isPresent()) {
            System.out.println("Curso não encontrado.");
            return;
        }

        Cursos curso = cursoOpt.get();
        try {
            enrollmentService.enroll(aluno, curso);
            System.out.println("Matrícula realizada com sucesso!");
        } catch (EnrollmentException e) {
            System.out.println("Erro ao matricular: " + e.getMessage());
        }
    }

    private static void listEnrollments(Aluno aluno) {
        List<Matricula> matriculas = enrollmentService.findByAluno(aluno);
        if (matriculas.isEmpty()) {
            System.out.println("Nenhuma matrícula encontrada.");
            return;
        }

        System.out.println("\nSeus cursos matriculados:");
        for (Matricula e : matriculas) {
            System.out.printf("- %s (Progresso: %.2f%%)%n", e.getCurso().getTitulo(), e.getProgresso());
        }
    }

    private static void updateProgressFlow(Aluno aluno) {
        System.out.println("\nDigite o título do curso para atualizar progresso:");
        String title = sc.nextLine();
        Optional<Matricula> enrollmentOpt = enrollmentService.findEnrollment(aluno, title);

        if (!enrollmentOpt.isPresent()) {
            System.out.println("Matrícula não encontrada para esse curso.");
            return;
        }

        Matricula enrollment = enrollmentOpt.get();
        System.out.print("Digite o novo progresso (0 a 100): ");
        try {
            double progress = Double.parseDouble(sc.nextLine());
            if (progress < 0 || progress > 100) {
                System.out.println("Progresso deve ser entre 0 e 100.");
                return;
            }
            enrollment.setProgresso(progress);
            System.out.println("Progresso atualizado com sucesso.");
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido para progresso.");
        }
    }

    private static void cancelEnrollmentFlow(Aluno aluno) {
        System.out.println("\nDigite o título do curso para cancelar matrícula:");
        String title = sc.nextLine();

        try {
            enrollmentService.cancelEnrollment(aluno, title);
            System.out.println("Matrícula cancelada com sucesso.");
        } catch (EnrollmentException e) {
            System.out.println("Erro ao cancelar matrícula: " + e.getMessage());
        }
    }

    private static void openTicketFlow(Aluno aluno) {
        System.out.println("\nAbrir ticket de suporte");
        System.out.print("Título: ");
        String title = sc.nextLine();
        System.out.print("Mensagem: ");
        String message = sc.nextLine();

        TicketSuporte ticket = new TicketSuporte(title, message, aluno);
        ticketService.abrirTicket(ticket);
        System.out.println("Ticket aberto com sucesso.");
    }

    private static void listCourses() {
        List<Cursos> courses = cursoService.listarCursosAtivos();
        if (courses.isEmpty()) {
            System.out.println("Nenhum curso ativo disponível.");
            return;
        }

        System.out.println("\nCatálogo de Cursos Ativos:");
        for (Cursos c : courses) {
            System.out.printf("- %s (%s) - Instrutor: %s - Duração: %d horas%n",
                    c.getTitulo(), c.getNivel(), c.getNomeInstrutor(), c.getDuracaoHoras());
        }
    }

    // ---------- Fluxos Admin ----------

    private static void manageCourseStatusFlow() {
        System.out.println("\nDigite o título do curso para alterar status:");
        String title = sc.nextLine();

        Optional<Cursos> cursoOpt = cursoService.buscarPorTitulo(title);
        if (!cursoOpt.isPresent()) {
            System.out.println("Curso não encontrado.");
            return;
        }

        Cursos curso = cursoOpt.get();
        System.out.println("Status atual: " + curso.getStatus());
        System.out.print("Novo status (ACTIVE/INACTIVE): ");
        String statusStr = sc.nextLine();

        try {
            StatusCurso novoStatus = StatusCurso.valueOf(statusStr.toUpperCase());
            cursoService.alterarStatusCurso(curso.getTitulo(), novoStatus);
            System.out.println("Status alterado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Status inválido.");
        }
    }

    private static void changeStudentPlanFlow() {
        System.out.println("\nDigite o email do aluno para alterar plano:");
        String email = sc.nextLine();

        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);
        if (!usuarioOpt.isPresent() || !(usuarioOpt.get() instanceof Aluno)) {
            System.out.println("Aluno não encontrado.");
            return;
        }

        Aluno aluno = (Aluno) usuarioOpt.get();

        System.out.println("Plano atual: " + aluno.getPlano());
        System.out.print("Novo plano (BASICO/PREMIUM): ");
        String planoStr = sc.nextLine();

        try {
            TipoPlano novoPlano = TipoPlano.valueOf(planoStr.toUpperCase());
            aluno.setPlano(novoPlano);
            System.out.println("Plano alterado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Plano inválido.");
        }
    }

    private static void attendSupportTicketsFlow() {
        System.out.println("\nAtendimento de Tickets (FIFO)");
        TicketSuporte ticket = ticketService.atenderProximo();

        if (ticket == null) {
            System.out.println("Nenhum ticket na fila.");
            return;
        }

        System.out.println("Título: " + ticket.getTitulo());
        System.out.println("Mensagem: " + ticket.getMensagem());
        System.out.println("Usuário: " + ticket.getUsuario().getNome());

        System.out.println("Ticket atendido e removido da fila.");
    }

    private static void generateReportsFlow() {
        System.out.println("\nRelatórios disponíveis:");
        System.out.println("1. Cursos por nível de dificuldade");
        System.out.println("2. Instrutores únicos de cursos ativos");
        System.out.println("3. Alunos agrupados por plano");
        System.out.println("4. Média geral de progresso");
        System.out.println("5. Aluno com maior número de matrículas");
        System.out.print("Opção: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                System.out.print("Digite o nível de dificuldade (BEGINNER, INTERMEDIATE, ADVANCED): ");
                String diffStr = sc.nextLine();
                try {
                    NivelDificuldade nivel = NivelDificuldade.valueOf(diffStr.toUpperCase());
                    var cursos = cursoService.listarCursosPorNivel(nivel);
                    System.out.println("Cursos com dificuldade " + nivel + ":");
                    cursos.forEach(c -> System.out.println("- " + c.getTitulo()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Nível de dificuldade inválido.");
                }
                break;
            case 2:
                var instrutores = cursoService.listarInstrutoresDeCursosAtivos();
                System.out.println("Instrutores únicos de cursos ativos:");
                instrutores.forEach(System.out::println);
                break;
            case 3:
                var agrupamento = usuarioService.groupStudentsBySubscriptionPlan();
                System.out.println("Alunos agrupados por plano:");
                agrupamento.forEach((plan, alunos) -> {
                    System.out.println(plan + ":");
                    alunos.forEach(a -> System.out.println(" - " + a.getNome()));
                });
                break;
            case 4:
                double media = enrollmentService.getAverageProgress();
                System.out.printf("Média geral de progresso: %.2f%%%n", media);
                break;
            case 5:
                Optional<Aluno> topAluno = enrollmentService.getStudentWithMostEnrollments();
                if (topAluno.isPresent()) {
                    System.out.println("Aluno com maior número de matrículas: " + topAluno.get().getNome());
                } else {
                    System.out.println("Nenhum aluno encontrado.");
                }
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void exportDataCsvFlow() {
        System.out.println("\nExportar dados para CSV");
        System.out.println("1. Exportar Cursos");
        System.out.println("2. Exportar Alunos");
        System.out.print("Opção: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                System.out.println("Digite as colunas a exportar (exemplo: titulo,nomeInstrutor,duracaoHoras):");
                String colunasCurso = sc.nextLine();
                List<Cursos> cursos = new ArrayList<>(cursoService.getTodosCursos());
                String csvCursos = service.GenericCsvExporter.exportToCsv(cursos, colunasCurso.split(","));
                System.out.println("\nCSV Gerado:\n" + csvCursos);
                break;
            case 2:
                System.out.println("Digite as colunas a exportar (exemplo: nome,email,tipoPlano):");
                String colunasAluno = sc.nextLine();
                List<Aluno> alunos = usuarioService.getAllStudents();
                String csvAlunos = service.GenericCsvExporter.exportToCsv(alunos, colunasAluno.split(","));
                System.out.println("\nCSV Gerado:\n" + csvAlunos);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
