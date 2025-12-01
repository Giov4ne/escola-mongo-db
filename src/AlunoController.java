
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class AlunoController {
    public void cadastrarAluno(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        System.out.println("Insira os seguintes dados para cadastrar um novo aluno: ");

        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Telefone: ");
        String telefone = input.nextLine();

        System.out.print("Endereco: ");
        String endereco = input.nextLine();

        System.out.print("Cpf: ");
        String cpf = input.nextLine();

        java.sql.Date dtNascimento = null;
        while (dtNascimento == null) {
            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            String dataStr = input.nextLine();
            try {
                dtNascimento = java.sql.Date.valueOf(java.time.LocalDate.parse(dataStr));
            } catch (java.time.format.DateTimeParseException | IllegalArgumentException e) {
                System.err.println("Formato de data invalido! Por favor, use AAAA-MM-DD.");
            }
        }

        MongoCollection<Document> collection = conexao.getDatabase().getCollection("alunos");
        int novoId = (int) collection.countDocuments() + 1;

        Aluno novoAluno = new Aluno(novoId, nome, telefone, cpf, endereco, dtNascimento);

        AlunoModel.create(novoAluno, conexao);
    }

    public void listarAlunos(ConexaoMongoDB conexao) {
        LinkedHashSet all = AlunoModel.listAll(conexao);
        Iterator<Aluno> it = all.iterator();
        while(it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    public void alterarAluno(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        
        listarAlunos(conexao);
        
        System.out.println("Informe o id do aluno a alterar: ");
        int id = input.nextInt();
        input.nextLine();
        
        System.out.print("Nome: ");
        String nome = input.nextLine();
        
        System.out.print("Telefone: ");
        String telefone = input.nextLine();
        
        System.out.print("Endereco: ");
        String endereco = input.nextLine();
        
        System.out.print("Cpf: ");
        String cpf = input.nextLine();
        
        Date dtNascimento = null;
        while (dtNascimento == null) {
            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            String dataStr = input.nextLine();
            try {
                dtNascimento = Date.valueOf(LocalDate.parse(dataStr));
            } catch (DateTimeParseException | IllegalArgumentException e) {
                System.err.println("Formato de data invalido! Por favor, use AAAA-MM-DD.");
            }
        }
        
        
        Aluno aluno = new Aluno(id, nome, telefone, cpf, endereco, dtNascimento);
        
        AlunoModel.update(aluno, conexao);
        System.out.println("Aluno atualizado com sucesso!!");
    }
    
    public void removerAluno(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarAlunos(conexao);

        System.out.print("\nInforme o id do aluno a remover: ");
        int id = input.nextInt();
        input.nextLine();

        AlunoModel.remove(id, conexao);

        System.out.println("Aluno removido com sucesso!!");
    }
    
    public void atribuirResponsavel(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarAlunos(conexao);

        System.out.print("\nInforme o id do aluno: ");
        int idAluno = input.nextInt();
        input.nextLine();

        System.out.println("\n--- DADOS DO RESPONSAVEL ---");

        System.out.print("ID (Numerico) do Responsavel: ");
        int idResp = input.nextInt();
        input.nextLine(); 

        System.out.print("Nome do Responsavel: ");
        String nomeResp = input.nextLine();

        System.out.print("Telefone: ");
        String telResp = input.nextLine();

        System.out.print("CPF: ");
        String cpfResp = input.nextLine();

        Document docResponsavel = new Document("id_sql", idResp)
                .append("nome", nomeResp)
                .append("telefone", telResp)
                .append("cpf", cpfResp);

        AlunoModel.assignResponsavel(idAluno, docResponsavel, conexao);
    }

    public void desatribuirResponsavel(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarAlunos(conexao);

        System.out.print("\nInforme o id do aluno para remover o responsavel: ");
        int idAluno = input.nextInt();
        input.nextLine();

        AlunoModel.unassignResponsavel(idAluno, conexao);
    }
}
