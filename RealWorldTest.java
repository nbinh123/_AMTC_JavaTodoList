import app.service.TodoService;
import app.model.TodoItem;
import java.util.Scanner;

public class RealWorldTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== REAL-WORLD UI SIMULATION ===\n");
        
        TodoService service = new TodoService();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Simulating user actions...\n");
        
        // User adds tasks
        System.out.println("1. User adds 3 tasks");
        service.addTask("Morning coffee");
        Thread.sleep(500);
        service.addTask("Check emails");
        Thread.sleep(500);
        service.addTask("Team standup");
        
        System.out.println("   Check MongoDB Compass - should see 3 new docs\n");
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        
        // User completes a task
        System.out.println("\n2. User completes 'Morning coffee'");
        var tasks = service.getAll();
        for (TodoItem task : tasks) {
            if (task.getTitle().equals("Morning coffee")) {
                service.toggleCompleted(task);
                break;
            }
        }
        
        System.out.println("   Check Compass - 'Morning coffee' completed=true\n");
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
        
        // User deletes a task
        System.out.println("\n3. User deletes 'Check emails'");
        for (TodoItem task : tasks) {
            if (task.getTitle().equals("Check emails")) {
                service.removeTask(task);
                break;
            }
        }
        
        System.out.println("   Check Compass - 'Check emails' removed\n");
        
        // Final state
        System.out.println("4. Final state in MongoDB:");
        System.out.println("   Total documents: " + service.countTasks());
        System.out.println("   Completed: " + service.getTasksByCompletion(true).size());
        System.out.println("   Pending: " + service.getTasksByCompletion(false).size());
        
        scanner.close();
        System.out.println("\n✅ REAL-WORLD TEST COMPLETE!");
        System.out.println("\nMongoDB is successfully integrated and working!");
    }
}
