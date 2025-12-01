import java.util.Scanner;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class AvaliacaoController {

    public void cadastrarAvaliacao(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        System.out.println("--- NOVA AVALIACAO ---");
        
        new AlunoController().listarAlunos(conexao);
        System.out.print("\nInforme o ID do Aluno: ");
        int idAluno = Integer.parseInt(input.nextLine());

        listarTurmasDoAluno(idAluno, conexao);
        
        System.out.print("Informe o ID da Turma: ");
        int idTurma = Integer.parseInt(input.nextLine());

        System.out.print("Descricao (Ex: Prova 1): ");
        String descricao = input.nextLine();

        System.out.print("Data (AAAA-MM-DD): ");
        String dataStr = input.nextLine();
        
        System.out.print("Nota (0 a 10): ");
        double nota = Double.parseDouble(input.nextLine().replace(",", "."));

        Date dataAvaliacao = new Date();
        try {
            if (nota < 0 || nota > 10) {
                System.out.println("Erro: A nota deve estar entre 0 e 10.");
                return;
            }
            LocalDate ld = LocalDate.parse(dataStr);
            dataAvaliacao = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data invalido. Use AAAA-MM-DD.");
            return;
        }

        int idAvaliacao = (int) (System.currentTimeMillis() % 100000); 

        Document novaAvaliacao = new Document("id_avaliacao_sql", idAvaliacao)
                .append("descricao", descricao)
                .append("data", dataAvaliacao)
                .append("nota", nota);

        AvaliacaoModel.create(idAluno, idTurma, novaAvaliacao, conexao);
        
        System.out.println("Avaliacao cadastrada com sucesso!");
    }

    public void removerAvaliacao(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("--- REMOVER AVALIACAO ---");
        listarAvaliacoes(conexao);
        
        System.out.print("\nInforme o ID da avaliacao a ser removida: ");
        int idAvaliacao = input.nextInt();
        
        AvaliacaoModel.remove(idAvaliacao, conexao);
        System.out.println("Avaliacao removida com sucesso!");
    }

    public void listarAvaliacoes(ConexaoMongoDB conexao) {
        for (String av : AvaliacaoModel.listAllWithDetails(conexao)) {
            System.out.println(av);
        }
    }

    private void listarTurmasDoAluno(int idAluno, ConexaoMongoDB conn) {
        MongoCollection<Document> col = conn.getDatabase().getCollection("alunos");
        Document aluno = col.find(Filters.eq("id_sql", idAluno)).first();

        System.out.println("--- MATERIAS CURSADAS PELO ALUNO ---");
        if (aluno != null) {
            List<Document> historico = aluno.getList("historico_academico", Document.class);
            if (historico != null) {
                for (Document h : historico) {
                    System.out.println("ID Turma: " + h.getInteger("id_turma_sql") + 
                                       " - " + h.getString("disciplina"));
                }
            }
        }
    }
}