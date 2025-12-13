package app.database;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private MongoDBConnection() {
        try {
            System.out.println("Attempting to connect to MongoDB with POJO Codec...");
            
            // Cấu hình Codec cho POJO (Plain Old Java Objects)
            CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );
            
            MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017"))
                .codecRegistry(pojoCodecRegistry)
                .build();
            
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("todoDB");
            System.out.println("✓ Connected to MongoDB: todoDB (POJO Codec enabled)");
            
        } catch (Exception e) {
            System.err.println("✗ MongoDB Connection Error: " + e.getMessage());
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
    }
    
    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }
    
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public boolean isConnected() {
        return mongoClient != null;
    }
    
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
