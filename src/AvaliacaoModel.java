import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.LinkedHashSet;
import java.util.List;

public class AvaliacaoModel {

    public static void create(int idAluno, int idTurma, Document docAvaliacao, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        var filtro = Filters.and(
            Filters.eq("id_sql", idAluno),
            Filters.eq("historico_academico.id_turma_sql", idTurma)
        );

        var update = Updates.push("historico_academico.$.avaliacoes", docAvaliacao);

        collection.updateOne(filtro, update);
    }

    public static void remove(int idAvaliacao, ConexaoMongoDB conn) {
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        var filtro = Filters.eq("historico_academico.avaliacoes.id_avaliacao_sql", idAvaliacao);

        var update = Updates.pull("historico_academico.$.avaliacoes", 
                                  new Document("id_avaliacao_sql", idAvaliacao));

        collection.updateOne(filtro, update);
    }

    public static LinkedHashSet<String> listAllWithDetails(ConexaoMongoDB conn) {
        LinkedHashSet<String> lista = new LinkedHashSet<>();
        MongoCollection<Document> collection = conn.getDatabase().getCollection("alunos");

        for (Document aluno : collection.find()) {
            List<Document> historico = aluno.getList("historico_academico", Document.class);
            
            if (historico != null) {
                for (Document turma : historico) {
                    List<Document> avaliacoes = turma.getList("avaliacoes", Document.class);
                    
                    if (avaliacoes != null) {
                        for (Document av : avaliacoes) {
                            String linha = String.format("ID Avaliacao: %d | Aluno: %s | Materia: %s | Prova: %s | Nota: %.1f",
                                    av.getInteger("id_avaliacao_sql"),
                                    aluno.getString("nome"),
                                    turma.getString("disciplina"),
                                    av.getString("descricao"),
                                    av.getDouble("nota")
                            );
                            lista.add(linha);
                        }
                    }
                }
            }
        }
        return lista;
    }
}