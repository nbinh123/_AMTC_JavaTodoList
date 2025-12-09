import app.service.TodoService;
import app.model.TodoItem;

public class CompassTest {
    public static void main(String[] args) {
        System.out.println("=== Creating test data for MongoDB Compass ===");
        
        TodoService service = new TodoService();
        
        // Clear old data
        for (TodoItem item : service.getAll()) {
            service.removeTask(item);
        }
        
        // Add test data v?i cc field khc nhau
        service.addTask("Meeting with team at 10AM");
        service.addTask("Review PR #45");
        service.addTask("Fix bug in login page");
        
        // Toggle m?t task
        var tasks = service.getAll();
        if (!tasks.isEmpty()) {
            service.toggleCompleted(tasks.get(0));
        }
        
        // Add task da completed ngay
        TodoItem doneTask = new TodoItem("Deploy to production", true);
        service.addTask("Deploy to production");
        service.toggleCompleted(tasks.get(tasks.size() - 1));
        
        System.out.println("\n Test data created!");
        System.out.println("Open MongoDB Compass and check:");
        System.out.println("1. Database: 'todoDB'");
        System.out.println("2. Collection: 'todos'");
        System.out.println("3. You should see 4 documents");
    }
}
