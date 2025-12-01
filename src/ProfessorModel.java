
import java.util.LinkedHashSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ProfessorModel {
public static void create(Professor professor, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("professores");

        Document contato = new Document("telefone", professor.getTelefone())
                .append("endereco", professor.getEndereco());

        Document docProfessor = new Document("id_sql", professor.getId())
                .append("nome", professor.getNome())
                .append("cpf", professor.getCpf())
                .append("especialidade", professor.getEspecialidade())
                .append("contato", contato);

        collection.insertOne(docProfessor);
    }
    
    static LinkedHashSet<Professor> listAll(ConexaoMongoDB conn) {
        LinkedHashSet<Professor> list = new LinkedHashSet<>();
        MongoCollection<Document> collection = conn.getDatabase().getCollection("professores");

        for (Document doc : collection.find()) {
            
            Document contato = (Document) doc.get("contato");
            String telefone = "";
            String endereco = "";
            
            if (contato != null) {
                telefone = contato.getString("telefone");
                endereco = contato.getString("endereco");
            } else {
                telefone = doc.getString("telefone");
                endereco = doc.getString("endereco");
            }

            list.add(new Professor(
                doc.getInteger("id_sql"),
                doc.getString("nome"),
                telefone,
                doc.getString("cpf"),
                endereco,
                doc.getString("especialidade")
            ));
        }
        return list;
    }
    
    public static void remove(int idProfessor, ConexaoMongoDB conn) {
        MongoCollection<Document> colProfessores = conn.getDatabase().getCollection("professores");
        MongoCollection<Document> colTurmas = conn.getDatabase().getCollection("turmas");

        colTurmas.updateMany(
            Filters.eq("id_professor_sql", idProfessor), 
            Updates.set("id_professor_sql", null) 
        );

        colProfessores.deleteOne(Filters.eq("id_sql", idProfessor));
    }
    
    static void update(Professor professor, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("professores");

        Bson filtro = Filters.eq("id_sql", professor.getId());

        Bson operacoes = Updates.combine(
            Updates.set("nome", professor.getNome()),
            Updates.set("cpf", professor.getCpf()),
            Updates.set("especialidade", professor.getEspecialidade()),

            Updates.set("contato.telefone", professor.getTelefone()),
            Updates.set("contato.endereco", professor.getEndereco())
        );

        collection.updateOne(filtro, operacoes);
    }
}
