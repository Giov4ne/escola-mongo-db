import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashSet;

public class MatriculaModel {

    public static void matricular(int idAluno, int idCurso, String periodo, ConexaoMongoDB conn) {
        MongoCollection<Document> colAlunos = conn.getDatabase().getCollection("alunos");
        MongoCollection<Document> colCursos = conn.getDatabase().getCollection("cursos");

        Document docCurso = colCursos.find(Filters.eq("id_sql", idCurso)).first();
        
        if (docCurso == null) {
            System.out.println("Erro: Curso nao encontrado!");
            return;
        }
        
        String nomeCurso = docCurso.getString("nome");

        String numeroMatricula = LocalDate.now().getYear() + String.format("%04d", idAluno);
        
        Date dataAtual = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        Document docMatricula = new Document("numero", numeroMatricula)
                .append("id_curso_sql", idCurso)
                .append("nome_curso", nomeCurso)
                .append("periodo", periodo)
                .append("dt_matricula", dataAtual);

        colAlunos.updateOne(
            Filters.eq("id_sql", idAluno),
            Updates.set("matricula", docMatricula)
        );
        
        System.out.println("Aluno matriculado com sucesso! Numero: " + numeroMatricula);
    }

    public static void desmatricular(String numeroMatricula, ConexaoMongoDB conn) {
        MongoCollection<Document> colAlunos = conn.getDatabase().getCollection("alunos");

        colAlunos.updateOne(
            Filters.eq("matricula.numero", numeroMatricula),
            Updates.set("matricula", null)
        );
    }

    public static LinkedHashSet<String> listarMatriculasAtivas(ConexaoMongoDB conn) {
        System.out.println("Matriculas ativas:");
        LinkedHashSet<String> lista = new LinkedHashSet<>();
        MongoCollection<Document> colAlunos = conn.getDatabase().getCollection("alunos");

        for (Document doc : colAlunos.find(Filters.exists("matricula", true))) {
            Document mat = (Document) doc.get("matricula");
            
            if (mat != null) {
                String nomeAluno = doc.getString("nome");
                String numero = mat.getString("numero");
                String curso = mat.getString("nome_curso");
                
                lista.add("Matricula: " + numero + " | Aluno: " + nomeAluno + " | Curso: " + curso);
            }
        }
        return lista;
    }
}