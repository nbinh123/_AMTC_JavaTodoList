import app.service.TodoService;

public class AddMoreData {
    public static void main(String[] args) {
        TodoService service = new TodoService();
        
        // Thêm với các trạng thái khác nhau
        String[][] tasks = {
            {"Buy groceries", "false"},
            {"Call dentist", "true"},
            {"Finish project report", "false"},
            {"Plan weekend trip", "true"}
        };
        
        System.out.println("Adding varied tasks...");
        for (String[] task : tasks) {
            service.addTask(task[0]);
            if (task[1].equals("true")) {
                // Toggle để thành completed
                var all = service.getAll();
                if (!all.isEmpty()) {
                    service.toggleCompleted(all.get(all.size() - 1));
                }
            }
        }
        
        System.out.println("✅ Added 4 more tasks with mixed completion states");
        System.out.println("Total tasks now: " + service.countTasks());
        System.out.println("\nRefresh MongoDB Compass to see all documents!");
    }
}
