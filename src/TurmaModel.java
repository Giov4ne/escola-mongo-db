
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.LinkedHashSet;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

public class TurmaModel {

    public static void create(Turma turma, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("turmas");

        Document doc = new Document("id_sql", turma.getId())
                .append("codigo", turma.getCodigo())
                .append("sala", turma.getSala())
                .append("disciplina", turma.getDisciplina())
                .append("id_professor_sql", turma.getIdProfessor());

        collection.insertOne(doc);
    }
    
    static LinkedHashSet<Turma> listAll(ConexaoMongoDB conn) {
        LinkedHashSet<Turma> list = new LinkedHashSet<>();
        MongoCollection<Document> collection = conn.getDatabase().getCollection("turmas");

        for (Document doc : collection.find()) {
             list.add(new Turma(
                doc.getInteger("id_sql"),
                doc.getString("disciplina"),
                doc.getString("codigo"),
                doc.getString("sala"),
                doc.getInteger("id_professor_sql")
            ));
        }
        return list;
    }

    public static void remove(int id, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("turmas");

        collection.deleteOne(Filters.eq("id_sql", id));
    }

    public static void update(Turma turma, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("turmas");

        Bson filtro = Filters.eq("id_sql", turma.getId());

        Bson operacoes = Updates.combine(
            Updates.set("codigo", turma.getCodigo()),
            Updates.set("sala", turma.getSala()),
            Updates.set("disciplina", turma.getDisciplina()),
            Updates.set("id_professor_sql", turma.getIdProfessor()) 
        );

        collection.updateOne(filtro, operacoes);
    }
}