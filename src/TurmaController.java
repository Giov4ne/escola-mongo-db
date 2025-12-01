
import java.util.Iterator;
import java.util.Scanner;
import java.util.LinkedHashSet;
import com.mongodb.client.MongoCollection;

public class TurmaController {

    public void cadastrarTurma(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        System.out.println("Insira os seguintes dados para cadastrar uma nova turma:");

        System.out.print("Codigo da Turma (Ex: TEC-POO): ");
        String codigo = input.nextLine();

        System.out.print("Sala (Ex: B203): ");
        String sala = input.nextLine();

        System.out.print("Nome da Disciplina (Ex: Matematica): ");
        String disciplina = input.nextLine();
        
        System.out.print("Deseja atribuir um professor agora? (s/n): ");
        String opcao = input.nextLine();
        
        Integer idProfessor = null;
        if (opcao.equalsIgnoreCase("s")) {
            new ProfessorController().listarProfessores(conexao);
            System.out.print("Informe o ID do Professor: ");
            idProfessor = Integer.parseInt(input.nextLine());
        }
        
        int novoId = getProximoIdTurma(conexao);

        Turma novaTurma = new Turma(novoId, disciplina, codigo, sala, idProfessor);
        
        TurmaModel.create(novaTurma, conexao);
        System.out.println("Turma cadastrada com sucesso!!");
    }

    public int getProximoIdTurma(ConexaoMongoDB conn) {
        MongoCollection collection = conn.getDatabase().getCollection("turmas");
        return (int) collection.countDocuments() + 1;
    }

    public void listarTurmas(ConexaoMongoDB conexao) {
        LinkedHashSet<Turma> turmas = TurmaModel.listAll(conexao);
        
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
        } else {
            Iterator<Turma> it = turmas.iterator();
            while(it.hasNext()) {
                System.out.println(it.next().toString());
            }
        }
    }

    public void removerTurma(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarTurmas(conexao);

        System.out.print("Informe o ID da turma a ser removida: ");
        int id = input.nextInt();
        input.nextLine();

        TurmaModel.remove(id, conexao);
        System.out.println("Turma removida com sucesso!!");
    }

    public void alterarTurma(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarTurmas(conexao);

        System.out.print("Informe o ID da turma a ser alterada: ");
        int id = Integer.parseInt(input.nextLine());

        System.out.println("Insira os novos dados:");

        System.out.print("Novo Codigo da Turma (Ex: TEC-POO): ");
        String codigo = input.nextLine();

        System.out.print("Nova Sala: ");
        String sala = input.nextLine();

        System.out.print("Novo Nome da Disciplina: ");
        String disciplina = input.nextLine();

        System.out.print("Novo ID do Professor (ou deixe em branco para remover): ");
        String idProfessorStr = input.nextLine();

        Integer idProfessor = null;
        if (!idProfessorStr.isEmpty()) {
            idProfessor = Integer.parseInt(idProfessorStr);
        }

        Turma turmaAtualizada = new Turma(id, disciplina, codigo, sala, idProfessor);

        TurmaModel.update(turmaAtualizada, conexao);
        System.out.println("Turma atualizada com sucesso!!");
    }
}