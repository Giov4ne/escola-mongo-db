
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.LinkedHashSet;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;

public class CursoModel {
    public static void create(Curso curso, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("cursos");

        Document doc = new Document("id_sql", curso.getId())
                .append("nome", curso.getNome())
                .append("tipo", curso.getTipo())
                .append("turno", curso.getTurno())
                .append("grade", new ArrayList<>()); // Inicia grade vazia

        collection.insertOne(doc);
    }

    public static void update(Curso curso, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("cursos");

        Bson filtro = Filters.eq("id_sql", curso.getId());
        
        Bson operacoes = Updates.combine(
            Updates.set("nome", curso.getNome()),
            Updates.set("tipo", curso.getTipo()),
            Updates.set("turno", curso.getTurno())
        );

        collection.updateOne(filtro, operacoes);
    }

    // --- REMOVE (Com desmatricula automatica) ---
    public static void remove(int idCurso, ConexaoMongoDB conn) {
        MongoCollection<Document> colCursos = conn.getDatabase().getCollection("cursos");
        MongoCollection<Document> colAlunos = conn.getDatabase().getCollection("alunos");

        // 1. Desmatricular alunos deste curso
        // Procura alunos onde "matricula.id_curso_sql" seja igual ao ID removido
        colAlunos.updateMany(
            Filters.eq("matricula.id_curso_sql", idCurso),
            Updates.set("matricula", null)
        );
        System.out.println("Alunos matriculados neste curso foram desvinculados.");

        // 2. Remove o curso
        colCursos.deleteOne(Filters.eq("id_sql", idCurso));
    }
    
    public static void adicionarDisciplina(int idCurso, Document disciplinaDoc, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("cursos");

        collection.updateOne(
            Filters.eq("id_sql", idCurso),
            Updates.push("grade", disciplinaDoc)
        );
    }

    public static void removerDisciplina(int idCurso, int idDisciplina, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("cursos");

        collection.updateOne(
            Filters.eq("id_sql", idCurso),
            Updates.pull("grade", new Document("id_disc_sql", idDisciplina))
        );
    }

    public static LinkedHashSet<String> listarNomesCursos(ConexaoMongoDB conn) {
        LinkedHashSet<String> lista = new LinkedHashSet<>();
        MongoCollection<Document> collection = conn.getDatabase().getCollection("cursos");

        for (Document doc : collection.find()) {
            int id = doc.getInteger("id_sql");
            String nome = doc.getString("nome");
            String turno = doc.getString("turno");
            
            java.util.List grade = doc.getList("grade", Document.class);
            int qtdMaterias = (grade != null) ? grade.size() : 0;

            lista.add("ID: " + id + " | Curso: " + nome + " (" + turno + ") - Disciplinas: " + qtdMaterias);
        }
        return lista;
    }
    
    public static LinkedHashSet<String> listarDisciplinasDoCurso(int idCurso, ConexaoMongoDB conn) {
        LinkedHashSet<String> listaFormatada = new LinkedHashSet<>();
        MongoCollection<Document> collection = conn.getDatabase().getCollection("cursos");

        Document cursoDoc = collection.find(Filters.eq("id_sql", idCurso)).first();

        if (cursoDoc != null) {
            List<Document> grade = cursoDoc.getList("grade", Document.class);

            if (grade != null && !grade.isEmpty()) {
                for (Document disc : grade) {
                    int id = disc.getInteger("id_disc_sql");
                    String nome = disc.getString("nome");

                    listaFormatada.add("ID: " + id + " - " + nome);
                }
            }
        }
        return listaFormatada;
    }
}
