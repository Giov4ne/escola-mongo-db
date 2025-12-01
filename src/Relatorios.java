
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;

public class Relatorios {

    
    public void boletimAlunos(ConexaoMongoDB conexao) {
        MongoCollection<Document> colAlunos = conexao.getDatabase().getCollection("alunos");

        System.out.println("\n--- BOLETIM ESCOLAR (MEDIAS) ---");

        for (Document aluno : colAlunos.find()) {
            String nome = aluno.getString("nome");
            List<Document> historico = aluno.getList("historico_academico", Document.class);

            if (historico != null && !historico.isEmpty()) {
                System.out.println("> Aluno: " + nome);
                
                for (Document materia : historico) {
                    String disc = materia.getString("disciplina");
                    List<Document> avaliacoes = materia.getList("avaliacoes", Document.class);
                    
                    double soma = 0;
                    int qtd = 0;
                    
                    if (avaliacoes != null) {
                        for (Document prova : avaliacoes) {
                            Number nota = prova.get("nota", Number.class);
                            soma += nota.doubleValue();
                            qtd++;
                        }
                    }
                    
                    double media = (qtd > 0) ? (soma / qtd) : 0.0;
                    String status = (media >= 7.0) ? "APROVADO" : "EM RECUPERACAO";
                    
                    System.out.printf("   - %s: Media %.1f [%s]\n", disc, media, status);
                }
            }
        }
    }

    public void disciplinasPorCurso(ConexaoMongoDB conexao) {
        MongoCollection<Document> collection = conexao.getDatabase().getCollection("cursos");

        System.out.println("\n--- Disciplinas por Curso ---");

        for (Document cursoDoc : collection.find().sort(Sorts.ascending("nome"))) {

            String nomeCurso = cursoDoc.getString("nome");

            List<Document> grade = cursoDoc.getList("grade", Document.class);

            if (grade != null && !grade.isEmpty()) {
                for (Document disc : grade) {

                    String nomeDisciplina = disc.getString("nome");
                    int cargaHoraria = disc.getInteger("carga_hr", 0);
                    boolean obrigatorio = disc.getBoolean("obrigatorio", false);

                    String textoObrigatoria = obrigatorio ? "Sim" : "Nao";

                    System.out.printf("Curso: %s, Disciplina: %s, Obrigatoria: %s, Duracao (h): %d\n",
                            nomeCurso,
                            nomeDisciplina,
                            textoObrigatoria,
                            cargaHoraria);
                }
            }
        }
    }

    public void quantidadeAlunosPorCurso(ConexaoMongoDB conexao) {
        MongoCollection<Document> colAlunos = conexao.getDatabase().getCollection("alunos");

        System.out.println("\n--- QUANTIDADE DE ALUNOS POR CURSO ---");

        var pipeline = Arrays.asList(
            Aggregates.match(Filters.exists("matricula", true)),
            Aggregates.group("$matricula.nome_curso", Accumulators.sum("total", 1))
        );

        for (Document doc : colAlunos.aggregate(pipeline)) {
            String curso = doc.getString("_id");
            int total = doc.getInteger("total");
            System.out.println("Curso: " + curso + " | Total de Alunos: " + total);
        }
    }
}