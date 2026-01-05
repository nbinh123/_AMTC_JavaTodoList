package controllers;

import models.Task;
import views.TaskView;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskController {
    private List<Task> taskList;
    private TaskView view;
    private Scanner scanner;

    public TaskController(TaskView view) {
        this.view = view;
        this.taskList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void runTaskProgram() {
        while (true) {
            System.out.println("\n=== MENU CONG VIEC ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them cong viec");
            System.out.println("3. Hoan thanh");
            System.out.println("4. Thoat");
            System.out.print("Chon: ");
            
            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": view.hienThiDanhSach(taskList); break;
                    case "2":
                        System.out.print("Nhap ten: ");
                        String name = scanner.nextLine();
                        taskList.add(new Task(taskList.size() + 1, name));
                        break;
                    case "3":
                        System.out.print("Nhap ID: ");
                        try {
                            int id = Integer.parseInt(scanner.nextLine());
                            for(Task t : taskList) if(t.getId() == id) t.hoanThanh();
                        } catch(Exception e) {}
                        break;
                    case "4": return;
                }
            }
        }
    }
}