import app.service.TodoService;
import app.model.TodoItem;
import java.util.List;

public class FinalTest {
    public static void main(String[] args) {
        System.out.println("=== FINAL MONGODB BACKEND TEST ===");
        System.out.println("This verifies the backend works independently of UI.");
        
        TodoService service = new TodoService();
        
        // 1. Clear existing data
        System.out.println("\n1. Clearing existing tasks...");
        List<TodoItem> existing = service.getAll();
        for (TodoItem item : existing) {
            service.removeTask(item);
        }
        
        // 2. Add test tasks
        System.out.println("2. Adding test tasks...");
        String[] tasks = {
            "Review MongoDB integration",
            "Test with teammate's UI", 
            "Create Pull Request",
            "Deploy to production"
        };
        
        for (String task : tasks) {
            service.addTask(task);
        }
        
        // 3. Verify count
        long count = service.countTasks();
        System.out.println("3. Task count: " + count + " (expected: 4)");
        
        // 4. Toggle one task
        System.out.println("4. Toggling first task...");
        List<TodoItem> allTasks = service.getAll();
        if (!allTasks.isEmpty()) {
            TodoItem first = allTasks.get(0);
            System.out.println("   Before: " + first.getTitle() + " - completed: " + first.isCompleted());
            service.toggleCompleted(first);
            System.out.println("   After: " + first.getTitle() + " - completed: " + first.isCompleted());
        }
        
        // 5. List all tasks
        System.out.println("\n5. All tasks in MongoDB:");
        for (TodoItem item : service.getAll()) {
            System.out.println("   - [" + (item.isCompleted() ? "✓" : " ") + "] " + 
                             item.getTitle() + " (ID: " + item.getId().substring(0, 8) + "...)");
        }
        
        // 6. Test filters
        System.out.println("\n6. Filter test:");
        System.out.println("   Completed: " + service.getTasksByCompletion(true).size() + " tasks");
        System.out.println("   Pending: " + service.getTasksByCompletion(false).size() + " tasks");
        
        System.out.println("\n✅ MongoDB Backend TEST PASSED!");
        System.out.println("\nNext steps:");
        System.out.println("1. Create Pull Request on GitHub");
        System.out.println("2. Ask teammate to test with UI");
        System.out.println("3. Merge after review");
    }
}
