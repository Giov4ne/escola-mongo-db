
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Scanner;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ProfessorController {
public void cadastrarProfessor(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        System.out.println("Insira os seguintes dados para cadastrar um novo professor: ");
        
        System.out.print("Nome: ");
        String nome = input.nextLine();
        
        System.out.print("Telefone: ");
        String telefone = input.nextLine();
        
        System.out.print("Endereco: ");
        String endereco = input.nextLine();
        
        System.out.print("Cpf: ");
        String cpf = input.nextLine();
        
        System.out.print("Especialidade: ");
        String especialidade = input.nextLine();
        
        int novoId = getProximoIdProfessor(conexao);
        
        Professor novoProf = new Professor(novoId, nome, telefone, cpf, endereco, especialidade);
        
        ProfessorModel.create(novoProf, conexao);
        System.out.println("Professor criado com sucesso!!");
    }
    
    public int getProximoIdProfessor(ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("professores");
        long quantidade = collection.countDocuments();
        return (int) quantidade + 1;
    }
    
    public void listarProfessores(ConexaoMongoDB conexao) {
        LinkedHashSet<Professor> all = ProfessorModel.listAll(conexao);
        Iterator<Professor> it = all.iterator();
        while(it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }
    
    public void removerProfessor(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarProfessores(conexao);

        System.out.println("Informe o id do professor a remover: ");
        int id = input.nextInt();
        input.nextLine();

        ProfessorModel.remove(id, conexao);

        System.out.println("Professor removido e desvinculado das turmas com sucesso!!");
    }
    
    public void alterarProfessor(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarProfessores(conexao);

        System.out.println("Informe o id do professor a alterar: ");
        int id = input.nextInt();
        input.nextLine(); // Limpar buffer

        System.out.print("Nome: ");
        String nome = input.nextLine();

        System.out.print("Telefone: ");
        String telefone = input.nextLine();

        System.out.print("Endereco: ");
        String endereco = input.nextLine();

        System.out.print("Cpf: ");
        String cpf = input.nextLine();

        System.out.print("Especialidade: ");
        String especialidade = input.nextLine();

        Professor prof = new Professor(id, nome, telefone, cpf, endereco, especialidade);

        ProfessorModel.update(prof, conexao);
        System.out.println("Professor atualizado com sucesso!!");
    }
}
