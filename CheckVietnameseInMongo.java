import app.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class CheckVietnameseInMongo {
    public static void main(String[] args) {
        System.out.println("=== Kiểm tra tiếng Việt trong MongoDB ===");
        
        MongoCollection<Document> collection = MongoDBConnection.getInstance()
                .getDatabase()
                .getCollection("todos");
        
        // Tìm task có chữ "Nộp" (nếu có)
        Document query = new Document("title", new Document("$regex", ".*[Nn]ộp.*"));
        
        System.out.println("Searching for Vietnamese tasks...");
        for (Document doc : collection.find(query)) {
            System.out.println("Found in MongoDB:");
            System.out.println("  ID: " + doc.getString("_id"));
            System.out.println("  Title: " + doc.getString("title"));
            System.out.println("  (Raw bytes): " + bytesToString(doc.getString("title").getBytes()));
        }
        
        // Hiển thị tất cả
        System.out.println("\nAll tasks in MongoDB (raw):");
        for (Document doc : collection.find().limit(5)) {
            System.out.println("  - " + doc.getString("title"));
        }
    }
    
    private static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
