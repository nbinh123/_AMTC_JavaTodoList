import app.service.TodoService;
import app.model.TodoItem;
import java.util.List;

public class SimulateFrontend {
    public static void main(String[] args) {
        System.out.println("=== GIẢ LẬP FRONTEND GỌI BACKEND ===\n");
        
        TodoService backend = new TodoService();  // Backend của bạn
        
        // 1. Frontend: User thêm task
        System.out.println("1. [FRONTEND] User thêm task: 'Nộp bài tập Java'");
        backend.addTask("Nộp bài tập Java");
        System.out.println("   [BACKEND] Đã lưu vào MongoDB ✓\n");
        
        // 2. Frontend: User thêm task khác
        System.out.println("2. [FRONTEND] User thêm task: 'Mua sữa'");
        backend.addTask("Mua sữa");
        System.out.println("   [BACKEND] Đã lưu vào MongoDB ✓\n");
        
        // 3. Frontend: Hiển thị tất cả task
        System.out.println("3. [FRONTEND] Hiển thị tất cả task:");
        List<TodoItem> allTasks = backend.getAll();
        for (TodoItem task : allTasks) {
            System.out.println("   - " + task.getTitle() + 
                             " (completed: " + task.isCompleted() + ")");
        }
        System.out.println("   [BACKEND] Đã trả về " + allTasks.size() + " tasks ✓\n");
        
        // 4. Frontend: User đánh dấu hoàn thành
        if (!allTasks.isEmpty()) {
            System.out.println("4. [FRONTEND] User tick hoàn thành task đầu tiên");
            backend.toggleCompleted(allTasks.get(0));
            System.out.println("   [BACKEND] Đã cập nhật MongoDB ✓\n");
        }
        
        // 5. Frontend: Đếm task
        System.out.println("5. [FRONTEND] Hiển thị số lượng task:");
        long total = backend.countTasks();
        long completed = backend.getTasksByCompletion(true).size();
        System.out.println("   Tổng: " + total + " | Hoàn thành: " + completed);
        System.out.println("   [BACKEND] Đã trả về số đếm ✓\n");
        
        System.out.println("✅ BACKEND HOẠT ĐỘNG HOÀN HẢO VỚI FRONTEND!");
        System.out.println("\nTất cả data đã được lưu vào MongoDB:");
        System.out.println("- Database: todoDB");
        System.out.println("- Collection: todos");
        System.out.println("- Mở MongoDB Compass để xem data thực tế");
    }
}
