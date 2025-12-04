
import java.util.Scanner;
import com.mongodb.client.MongoDatabase;


public class Principal {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        ConexaoMongoDB conexao = new ConexaoMongoDB();
        
        MongoDatabase banco = conexao.getDatabase();
        int op = 0;
        do{
            op = menuPrincipal(input);
            try {
                switch (op) {
                        case 1:
                            submenu(conexao, input, "Alunos");
                            break;
                        case 2:
                            submenu(conexao, input, "Cursos");
                            break;
                        case 3:
                            submenu(conexao, input, "Professores");
                            break;
                        case 4:
                            submenu(conexao, input, "Turmas");
                            break;
                        case 5:
                            new CursoController().adicionarDisciplinaAoCurso(conexao);
                            break;
                        case 6:
                            new CursoController().removerDisciplinaDoCurso(conexao);
                            break;
                        case 7:
                            new AlunoController().atribuirResponsavel(conexao);
                            break;
                        case 8:
                            new AlunoController().desatribuirResponsavel(conexao);
                            break;
                        case 9:
                            new MatriculaController().matricularAlunoCurso(conexao);
                            break;
                        case 10:
                            new MatriculaController().desmatricularAlunoCurso(conexao);
                            break;
                        case 11:
                            for (String m : MatriculaModel.listarMatriculasAtivas(conexao)) {
                                System.out.println(m);
                            }
                            break;
                        case 12:
                            new AvaliacaoController().cadastrarAvaliacao(conexao);
                            break;
                        case 13:
                            new AvaliacaoController().removerAvaliacao(conexao);
                            break;
                        case 14:
                            relatoriosMenu(conexao, input);
                            break;
                        default:
                            System.out.println("Saindo do sistema...");
                            break;
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        } while(op > 0 && op < 15);  
        conexao.closeConnection();
    }    
    
    private static int menuPrincipal(Scanner input) {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("Informe o numero da opcao que deseja executar: ");
        System.out.println("1 - Gerenciar Alunos");
        System.out.println("2 - Gerenciar Cursos");
        System.out.println("3 - Gerenciar Professores");
        System.out.println("4 - Gerenciar Turmas");
        System.out.println("5 - Cadastrar Disciplina para um Curso");
        System.out.println("6 - Remover Disciplina de um Curso");
        System.out.println("7 - Cadastrar um Responsavel para um Aluno");
        System.out.println("8 - Remover um Responsavel de um Aluno");
        System.out.println("9 - Matricular Aluno em um Curso");
        System.out.println("10 - Desmatricular Aluno em um Curso");
        System.out.println("11 - Listar matriculas ativas");
        System.out.println("12 - Cadastrar avaliacao");
        System.out.println("13 - Remover avaliacao");
        System.out.println("14 - Relatorios");
        System.out.println("Digite qualquer outro valor para sair");
        System.out.print("Sua opcao: ");
        
        return input.nextInt();
    }
    
    private static void submenu(ConexaoMongoDB conexao, Scanner input, String nomeEntidade) {
        int opSubmenu = 0;
        do {
            System.out.printf("\n--- Gerenciar %s ---\n", nomeEntidade);
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Voltar ao Menu Principal");
            System.out.print("Sua opcao: ");
            opSubmenu = input.nextInt();

            switch (opSubmenu) {
                case 1: // CADASTRAR
                    switch (nomeEntidade) {
                        case "Alunos":
                            new AlunoController().cadastrarAluno(conexao);
                            break;
                        case "Cursos": 
                            new CursoController().cadastrarCurso(conexao);
                            break;
                        case "Professores":
                            new ProfessorController().cadastrarProfessor(conexao);
                            break;
                        case "Turmas": 
                            new TurmaController().cadastrarTurma(conexao);
                            break;
                    }
                    break;
                case 2: // LISTAR
                     switch (nomeEntidade) {
                        case "Alunos":
                            new AlunoController().listarAlunos(conexao);
                            break;
                        case "Cursos": 
                            new CursoController().listarCursos(conexao);
                            break;
                        case "Professores":
                            new ProfessorController().listarProfessores(conexao);
                            break;
                        case "Turmas": 
                            new TurmaController().listarTurmas(conexao);
                            break;
                    }
                    break;
                case 3: // ATUALIZAR
                     switch (nomeEntidade) {
                        case "Alunos":
                            new AlunoController().alterarAluno(conexao);
                            break;
                        case "Cursos": 
                            new CursoController().alterarCurso(conexao);
                            break;
                        case "Professores":
                            new ProfessorController().alterarProfessor(conexao);
                            break;
                        case "Turmas": 
                            new TurmaController().alterarTurma(conexao);
                            break;
                    }
                    break;
                case 4: // EXCLUIR
                    switch (nomeEntidade) {
                        case "Alunos":
                            new AlunoController().removerAluno(conexao);
                            break;
                        case "Cursos": 
                            new CursoController().removerCurso(conexao);
                            break;
                        case "Professores":
                            new ProfessorController().removerProfessor(conexao);
                            break;
                        case "Turmas": 
                            new TurmaController().removerTurma(conexao);
                            break;
                    }
                    break;
                case 5:
                    System.out.println("Retornando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        } while (opSubmenu != 5);
    }
    
    private static void relatoriosMenu(ConexaoMongoDB conexao, Scanner input) {
        int opSubmenu = 0;
        Relatorios relatoriosController = new Relatorios();
        
        do {
            System.out.println("\n--- MENU DE RELATORIOS ---");
            System.out.println("1 - Boletim escolar");
            System.out.println("2 - Disciplinas por curso");
            System.out.println("3 - Quantidade de alunos por curso");
            System.out.println("4 - Lista de avaliacoes");
            System.out.println("5 - Voltar ao Menu Principal");
            System.out.print("Sua opcao: ");
            opSubmenu = input.nextInt();

            switch (opSubmenu) {
                case 1:
                    relatoriosController.boletimAlunos(conexao);
                    break;
                case 2:
                    relatoriosController.disciplinasPorCurso(conexao);
                    break;
                case 3:
                    relatoriosController.quantidadeAlunosPorCurso(conexao);
                    break;
                case 4:
                    new AvaliacaoController().listarAvaliacoes(conexao);
                    break;
                case 5:
                    System.out.println("Retornando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        } while (opSubmenu != 5);
    }
}
