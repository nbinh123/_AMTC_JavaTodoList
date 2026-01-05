package views;
import java.util.List;
import models.Task;

public class TaskView {
    public void hienThiDanhSach(List<Task> danhSachTask) {
        System.out.println("\n=== DANH SÁCH CÔNG VIỆC ===");
        for (Task t : danhSachTask) {
            String trangThai = t.isDaHoanThanh() ? "[X]" : "[ ]";
            System.out.println(t.getId() + ". " + trangThai + " " + t.getTenCongViec());
        }
        System.out.println("---------------------------");
    }
    
    public void thongBao(String msg) {
        System.out.println(">> " + msg);
    }
}