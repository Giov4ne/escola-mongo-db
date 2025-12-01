
import java.util.Scanner;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.LinkedHashSet;

public class CursoController {
    public void cadastrarCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        System.out.println("Insira os seguintes dados para cadastrar um novo curso: ");
        
        System.out.print("Nome: ");
        String nome = input.nextLine();
        
        System.out.print("Tipo: ");
        String tipo = input.nextLine();
        
        System.out.print("Turno: ");
        String turno = input.nextLine();
        
        int novoId = getProximoIdCurso(conexao);
        
        Curso novoCurso = new Curso(novoId, nome, tipo, turno);
        
        CursoModel.create(novoCurso, conexao);
        System.out.println("Curso criado com sucesso!!");
    }
    
    public int getProximoIdCurso(ConexaoMongoDB conn) {
        MongoCollection collection = conn.getDatabase().getCollection("cursos");
        return (int) collection.countDocuments() + 1;
    }

    public void removerCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        
        listarCursos(conexao);
        
        System.out.println("Informe o id do curso a remover: ");
        int n = input.nextInt();
        input.nextLine();

        CursoModel.remove(n, conexao);  
        System.out.println("Curso removido com sucesso!!");
    }

    public void alterarCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        
        listarCursos(conexao);
        
        System.out.println("Informe o id do curso a alterar: ");
        int id = input.nextInt();
        input.nextLine();
        
        System.out.print("Nome: ");
        String nome = input.nextLine();
        
        System.out.print("Tipo: ");
        String tipo = input.nextLine();
        
        System.out.print("Turno: ");
        String turno = input.nextLine();
        
        Curso curso = new Curso(id, nome, tipo, turno);
        
        CursoModel.update(curso, conexao);
        System.out.println("Curso atualizado com sucesso!!");
    }
    
    public void adicionarDisciplinaAoCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);
        
        listarCursos(conexao);
        System.out.print("Informe o ID do Curso para adicionar a disciplina: ");
        int idCurso = Integer.parseInt(input.nextLine());
        
        System.out.println("\n--- NOVA DISCIPLINA ---");
        
        int idDisc = (int) (System.currentTimeMillis() % 10000); 
        
        System.out.print("Nome da Disciplina: ");
        String nome = input.nextLine();
        
        System.out.print("Ementa: ");
        String ementa = input.nextLine();
        
        System.out.print("Carga Horaria (numero): ");
        int carga = Integer.parseInt(input.nextLine());
        
        System.out.print("E obrigatorio? (s/n): ");
        boolean obrigatorio = input.nextLine().equalsIgnoreCase("s");

        Document docDisciplina = new Document("id_disc_sql", idDisc)
                .append("nome", nome)
                .append("ementa", ementa)
                .append("carga_hr", carga)
                .append("obrigatorio", obrigatorio);

        CursoModel.adicionarDisciplina(idCurso, docDisciplina, conexao);
        System.out.println("Disciplina adicionada a grade do curso com sucesso!");
    }

    public void removerDisciplinaDoCurso(ConexaoMongoDB conexao) {
        Scanner input = new Scanner(System.in);

        listarCursos(conexao);
        System.out.print("Informe o ID do Curso: ");
        int idCurso = Integer.parseInt(input.nextLine());

        System.out.println("\n--- DISCIPLINAS DESTE CURSO ---");
        LinkedHashSet<String> disciplinas = CursoModel.listarDisciplinasDoCurso(idCurso, conexao);

        if (disciplinas.isEmpty()) {
            System.out.println("Este curso nao possui disciplinas cadastradas.");
            System.out.println("Cancelando operacao...");
            return;
        }

        for (String d : disciplinas) {
            System.out.println(d);
        }

        System.out.print("Informe o ID da Disciplina (o numero no inicio da linha) para remover: ");
        int idDisc = Integer.parseInt(input.nextLine());

        CursoModel.removerDisciplina(idCurso, idDisc, conexao);
        System.out.println("Disciplina removida da grade com sucesso!");
    }

    public void listarCursos(ConexaoMongoDB conexao) {
        System.out.println("\n--- LISTA DE CURSOS ---");
        LinkedHashSet<String> cursos = CursoModel.listarNomesCursos(conexao);
        for (String c : cursos) {
            System.out.println(c);
        }
    }
}
