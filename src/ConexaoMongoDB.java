import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexaoMongoDB {
    private MongoClient cliente;
    private MongoDatabase database;
    
    private final String URI = "mongodb+srv://giovanewbudal_db_user:udesc@cluster0.hzgdrgm.mongodb.net/";
    private final String NOME_BANCO = "escola_db;";

    public ConexaoMongoDB() {
        try {
            this.cliente = MongoClients.create(URI);
            
            this.database = cliente.getDatabase(NOME_BANCO);
            
            System.out.println("Conexao com MongoDB realizada com sucesso!");
            
        } catch (MongoException ex) {
            Logger.getLogger(ConexaoMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Erro ao conectar no MongoDB: " + ex.getMessage());
            System.exit(1);
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoClient getCliente() {
        return cliente;
    }

    public void closeConnection() {
        if (this.cliente != null) {
            try {
                this.cliente.close();
                System.out.println("Conexao fechada.");
            } catch (Exception ex) {
                Logger.getLogger(ConexaoMongoDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}