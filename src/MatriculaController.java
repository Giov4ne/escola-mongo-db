
import java.util.Scanner;

public class MatriculaController {
    public void matricularAlunoCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n--- Matricular Aluno em Curso ---");

        System.out.println("Alunos disponiveis:");
        new AlunoController().listarAlunos(conexao); 
        
        System.out.print("\nDigite o ID do aluno que deseja matricular: ");
        int idAluno = Integer.parseInt(input.nextLine());

        System.out.println("\nCursos disponiveis:");
        new CursoController().listarCursos(conexao);
        
        System.out.print("\nDigite o ID (id_sql) do curso: ");
        int idCurso = Integer.parseInt(input.nextLine());
        
        System.out.print("Digite o periodo/semestre do aluno: ");
        String periodo = input.nextLine();

        MatriculaModel.matricular(idAluno, idCurso, periodo, conexao);
    }

    public void desmatricularAlunoCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        System.out.println("\n--- Desmatricular Aluno de Curso ---");

        for (String m : MatriculaModel.listarMatriculasAtivas(conexao)) {
            System.out.println(m);
        }

        System.out.print("\nDigite o numero da matricula que deseja remover: ");
        String matricula = input.nextLine();

        MatriculaModel.desmatricular(matricula, conexao);
        System.out.println("\nMatricula removida com sucesso!");
    }
}