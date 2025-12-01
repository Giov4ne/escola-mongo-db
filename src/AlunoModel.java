
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.LinkedHashSet;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class AlunoModel {
    public static void create(Aluno aluno, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        Document contato = new Document("telefone", aluno.getTelefone())
                .append("endereco", aluno.getEndereco());

        Document docAluno = new Document("id_sql", aluno.getId())
                .append("nome", aluno.getNome())
                .append("cpf", aluno.getCpf())
                .append("dt_nascimento", aluno.getDtNascimento())
                .append("contato", contato)
                .append("responsavel", null)
                .append("matricula", null); 

        collection.insertOne(docAluno);
        System.out.println("Aluno cadastrado no MongoDB com sucesso!");
    }
    
    static LinkedHashSet<Aluno> listAll(ConexaoMongoDB conn) {
        LinkedHashSet<Aluno> list = new LinkedHashSet<>();

        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        for (Document doc : collection.find()) {
            Document contato = (Document) doc.get("contato");
            String telefone = (contato != null) ? contato.getString("telefone") : "";
            String endereco = (contato != null) ? contato.getString("endereco") : "";

            Document responsavelDoc = (Document) doc.get("responsavel");
            int idResponsavel = 0;
            if (responsavelDoc != null) {
                idResponsavel = responsavelDoc.getInteger("id_sql", 0); 
            }

            java.util.Date dataMongo = doc.getDate("dt_nascimento");
            java.sql.Date dataSql = null;
            if (dataMongo != null) {
                dataSql = new java.sql.Date(dataMongo.getTime());
            }

            list.add(new Aluno(
                    doc.getInteger("id_sql"),     
                    doc.getString("nome"),  
                    telefone,                     
                    doc.getString("cpf"),        
                    endereco,                     
                    dataSql,                     
                    idResponsavel                 
            ));
        }

        return list;
    }

    static void update(Aluno aluno, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        Bson filtro = Filters.eq("id_sql", aluno.getId());

        Bson operacoes = Updates.combine(
            Updates.set("nome", aluno.getNome()),
            Updates.set("cpf", aluno.getCpf()),
            Updates.set("dt_nascimento", aluno.getDtNascimento()),
            Updates.set("contato.telefone", aluno.getTelefone()),
            Updates.set("contato.endereco", aluno.getEndereco())
        );

        collection.updateOne(filtro, operacoes);
    }
    
    public static void remove(int id, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        collection.deleteOne(Filters.eq("id_sql", id));

        System.out.println("Aluno e todos os seus dados vinculados foram removidos.");
    }
    
    public static void assignResponsavel(int idAluno, Document dadosResponsavel, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        collection.updateOne(
            Filters.eq("id_sql", idAluno),
            Updates.set("responsavel", dadosResponsavel)
        );

        System.out.println("Responsavel atribuido com sucesso!");
    }

    public static void unassignResponsavel(int idAluno, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        // Para remover, definimos o campo como null
        collection.updateOne(
            Filters.eq("id_sql", idAluno),
            Updates.set("responsavel", null)
        );

        System.out.println("Responsavel removido com sucesso!");
    }
}
