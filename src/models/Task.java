package models;

public class Task {
    private int id;
    private String tenCongViec;
    private boolean daHoanThanh;

    public Task(int id, String tenCongViec) {
        this.id = id;
        this.tenCongViec = tenCongViec;
        this.daHoanThanh = false;
    }
    public int getId() { return id; }
    public String getTenCongViec() { return tenCongViec; }
    public boolean isDaHoanThanh() { return daHoanThanh; }
    public void hoanThanh() { this.daHoanThanh = true; }
}