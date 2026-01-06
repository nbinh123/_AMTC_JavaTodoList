package app.view;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import app.model.TodoItem;
import app.service.TodoService;
import app.session.UserSession;
import app.utils.MaterialCheckBox;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TodoPanel extends BorderPane {

    private final TodoService service = new TodoService();
    private final VBox listBox = new VBox(12);
    private final Label statsLabel = new Label();
    private YearMonth currentYearMonth = YearMonth.now();
    private final Label monthYearLabel = new Label();
    
    private DatePicker datePicker;

    public TodoPanel() {

        //  BLOCK TRUY CẬP TRÁI PHÉP
        if (!UserSession.getInstance().isLoggedIn()) {
            throw new IllegalStateException("User chưa đăng nhập");
        }

        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom, #0F0F0F, #1A1A1A);");

        /* ===== HEADER ===== */
        Label title = new Label("Todo Hôm Nay");
        title.setStyle(
            "-fx-font-size: 32px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.5), 10, 0, 0, 0);"
        );

        statsLabel.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-text-fill: #888888; " +
            "-fx-padding: 5 0 0 0;"
        );

        Button logoutBtn = new Button("Đăng xuất");
        logoutBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #ff6b6b; " +
            "-fx-font-size: 13px; " +
            "-fx-cursor: hand;"
        );

        logoutBtn.setOnAction(e -> {
            UserSession.getInstance().logout();

            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(
                new Scene(
                    new app.view.AuthPanel(stage)
                )
            );
        });
        VBox headerText = new VBox(5, title, statsLabel);

        Region spacerHeader = new Region();
        HBox.setHgrow(spacerHeader, Priority.ALWAYS);

        HBox headerBox = new HBox(10, headerText, spacerHeader, logoutBtn);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        /* ===== MONTH/YEAR SELECTOR ===== */
        Label calendarTitle = new Label("Chọn ngày");
        calendarTitle.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #AAAAAA;"
        );

        monthYearLabel.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-padding: 10 15; " +
            "-fx-background-color: #252525; " +
            "-fx-background-radius: 15; " +
            "-fx-min-width: 180; " +
            "-fx-alignment: center;"
        );
        updateMonthYearLabel();
        
        datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle(
            "-fx-background-color: #252525; " +
            "-fx-background-radius: 15; " +
            "-fx-padding: 10 15; " +
            "-fx-font-size: 13px;"
        );
        datePicker.setPrefWidth(180);

        Button prevMonth = new Button("◀");
        prevMonth.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 10 15; " +
            "-fx-cursor: hand;"
        );

        prevMonth.setOnMouseEntered(e -> {
            prevMonth.setStyle(
                "-fx-background-color: #667eea; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 15; " +
                "-fx-cursor: hand;"
            );
        });

        prevMonth.setOnMouseExited(e -> {
            prevMonth.setStyle(
                "-fx-background-color: #2A2A2A; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 15; " +
                "-fx-cursor: hand;"
            );
        });

        Button nextMonth = new Button("▶");
        nextMonth.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 10 15; " +
            "-fx-cursor: hand;"
        );

        nextMonth.setOnMouseEntered(e -> {
            nextMonth.setStyle(
                "-fx-background-color: #667eea; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 15; " +
                "-fx-cursor: hand;"
            );
        });

        nextMonth.setOnMouseExited(e -> {
            nextMonth.setStyle(
                "-fx-background-color: #2A2A2A; " +
                "-fx-text-fill: #667eea; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 15; " +
                "-fx-cursor: hand;"
            );
        });

        prevMonth.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateMonthYearLabel();
            
            // Cập nhật DatePicker về ngày đầu tháng mới
            datePicker.setValue(currentYearMonth.atDay(1));
        });

        nextMonth.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateMonthYearLabel();
            
            // Cập nhật DatePicker về ngày đầu tháng mới
            datePicker.setValue(currentYearMonth.atDay(1));
        });

        // Khi chọn ngày từ DatePicker
        datePicker.setOnAction(e -> {
            LocalDate selected = datePicker.getValue();
            if (selected != null) {
                currentYearMonth = YearMonth.from(selected);
                updateMonthYearLabel();
                render();
                updateStats();
                
                FadeTransition ft = new FadeTransition(Duration.millis(300), listBox);
                ft.setFromValue(0.5);
                ft.setToValue(1.0);
                ft.play();
            }
        });

        HBox monthSelector = new HBox(10, prevMonth, monthYearLabel, nextMonth);
        monthSelector.setAlignment(Pos.CENTER_LEFT);
        
        HBox calendarRow = new HBox(20, monthSelector, datePicker);
        calendarRow.setAlignment(Pos.CENTER_LEFT);

        VBox calendarBox = new VBox(8, calendarTitle, calendarRow);
        calendarBox.setPadding(new Insets(20, 0, 10, 0));
        
        /* ===== INPUT AREA ===== */
        TextField input = new TextField();
        input.setPromptText("Nhập công việc mới...");
        input.setStyle(
            "-fx-background-color: #252525; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-prompt-text-fill: #666666; " +
            "-fx-background-radius: 15; " +
            "-fx-padding: 15 20; " +
            "-fx-font-size: 14px; " +
            "-fx-border-color: transparent; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);"
        );

        input.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                input.setStyle(
                    "-fx-background-color: #2A2A2A; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-prompt-text-fill: #666666; " +
                    "-fx-background-radius: 15; " +
                    "-fx-padding: 15 20; " +
                    "-fx-font-size: 14px; " +
                    "-fx-border-color: linear-gradient(to right, #667eea, #764ba2); " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 12, 0, 0, 0);"
                );
            } else {
                input.setStyle(
                    "-fx-background-color: #252525; " +
                    "-fx-text-fill: #FFFFFF; " +
                    "-fx-prompt-text-fill: #666666; " +
                    "-fx-background-radius: 15; " +
                    "-fx-padding: 15 20; " +
                    "-fx-font-size: 14px; " +
                    "-fx-border-color: transparent; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 15; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);"
                );
            }
        });

        Button add = new Button("+ Thêm");
        add.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 15; " +
            "-fx-padding: 15 30; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 10, 0, 0, 3);"
        );

        add.setOnMouseEntered(e -> {
            add.setStyle(
                "-fx-background-color: linear-gradient(to right, #7b91ff, #8a5bb8); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 15 30; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.6), 15, 0, 0, 4); " +
                "-fx-scale-x: 1.05; " +
                "-fx-scale-y: 1.05;"
            );
        });

        add.setOnMouseExited(e -> {
            add.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 15 30; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 10, 0, 0, 3);"
            );
        });

        HBox inputRow = new HBox(15, input, add);
        inputRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(input, Priority.ALWAYS);
        inputRow.setPadding(new Insets(10, 0, 0, 0));

        VBox top = new VBox(15, headerBox, calendarBox, inputRow);
        setTop(top);

        /* ===== LIST AREA ===== */
        listBox.setPadding(new Insets(20, 0, 0, 0));

        ScrollPane scroll = new ScrollPane(listBox);
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-background: transparent;"
        );
        scroll.setPadding(new Insets(10, 0, 0, 0));
        setCenter(scroll);

        /* ===== EVENTS ===== */
        add.setOnAction(e -> {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                LocalDate selectedDate = datePicker.getValue();
                if (selectedDate == null) {
                    selectedDate = LocalDate.now();
                }
                service.addTask(text, selectedDate);
                input.clear();
                render();
                updateStats();
                
                ScaleTransition st = new ScaleTransition(Duration.millis(100), add);
                st.setToX(0.95);
                st.setToY(0.95);
                st.setAutoReverse(true);
                st.setCycleCount(2);
                st.play();
            }
        });

        input.setOnAction(e -> add.fire());

        // ✅ Gọi updateStats() SAU KHI datePicker đã được khởi tạo
        updateStats();
        render();
    }

    private void updateMonthYearLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("vi-VN"));
        String monthYear = currentYearMonth.format(formatter);
        monthYear = monthYear.substring(0, 1).toUpperCase() + monthYear.substring(1);
        
        if (currentYearMonth.equals(YearMonth.now())) {
            monthYearLabel.setText("" + monthYear);
        } else {
            monthYearLabel.setText("" + monthYear);
        }
    }

    private void updateStats() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        var tasksForDate = service.getTasksByDate(selectedDate);
        int total = tasksForDate.size();
        long completed = tasksForDate.stream().filter(TodoItem::isCompleted).count();
        statsLabel.setText(String.format("%d công việc · %d hoàn thành", total, completed));
    }

    private void render() {
        listBox.getChildren().clear();

        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        
        var tasksForDate = service.getTasksByDate(selectedDate);

        for (TodoItem item : tasksForDate) {
            HBox row = row(item);
            listBox.getChildren().add(row);
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), row);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
            TranslateTransition tt = new TranslateTransition(Duration.millis(300), row);
            tt.setFromY(-20);
            tt.setToY(0);
            tt.play();
        }

        if (tasksForDate.isEmpty()) {
            Label empty = new Label("Chưa có công việc nào\nHãy thêm công việc cho ngày này!");
            empty.setStyle(
                "-fx-text-fill: #666666; " +
                "-fx-font-size: 16px; " +
                "-fx-text-alignment: center; " +
                "-fx-padding: 50 0 0 0;"
            );
            empty.setAlignment(Pos.CENTER);
            empty.setMaxWidth(Double.MAX_VALUE);
            listBox.getChildren().add(empty);
        }
    }

    private HBox row(TodoItem item) {
        // ✅ Gộp tên công việc và người tạo vào 1 dòng
        String displayText = item.getTitle() + " (" + item.getCreatedBy() + ")";
        MaterialCheckBox cb = new MaterialCheckBox(displayText);
        cb.setSelected(item.isCompleted());
        cb.setStyle("-fx-text-fill: " + (item.isCompleted() ? "#666666" : "#FFFFFF") + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button del = new Button("x");
        del.setPrefWidth(45);
        del.setMinWidth(45);
        del.setMaxWidth(45);
        del.setStyle(
            "-fx-background-color: #2A2A2A; " +
            "-fx-text-fill: #ff6b6b; " +
            "-fx-font-size: 16px; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 8 12; " +
            "-fx-cursor: hand; " +
            "-fx-border-color: transparent; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 10;"
        );

        del.setOnMouseEntered(e -> {
            del.setStyle(
                "-fx-background-color: #ff6b6b; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 8 12; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(255, 107, 107, 0.5), 10, 0, 0, 2);"
            );
            
            ScaleTransition st = new ScaleTransition(Duration.millis(100), del);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        del.setOnMouseExited(e -> {
            del.setStyle(
                "-fx-background-color: #2A2A2A; " +
                "-fx-text-fill: #ff6b6b; " +
                "-fx-font-size: 16px; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 8 12; " +
                "-fx-cursor: hand;"
            );
            
            ScaleTransition st = new ScaleTransition(Duration.millis(100), del);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        HBox row = new HBox(15, cb, spacer, del); // ✅ Đổi contentBox thành cb
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15, 20, 15, 20));
        row.setStyle(
            "-fx-background-color: " + (item.isCompleted() ? "#1E1E1E" : "#252525") + "; " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: " + (item.isCompleted() ? "#2A2A2A" : "#333333") + "; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);"
        );

        row.setOnMouseEntered(e -> {
            row.setStyle(
                "-fx-background-color: " + (item.isCompleted() ? "#232323" : "#2A2A2A") + "; " +
                "-fx-background-radius: 15; " +
                "-fx-border-color: #667eea; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.3), 12, 0, 0, 3);"
            );
        });

        row.setOnMouseExited(e -> {
            row.setStyle(
                "-fx-background-color: " + (item.isCompleted() ? "#1E1E1E" : "#252525") + "; " +
                "-fx-background-radius: 15; " +
                "-fx-border-color: " + (item.isCompleted() ? "#2A2A2A" : "#333333") + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);"
            );
        });

        cb.setOnAction(e -> {
            service.toggleCompleted(item);
            updateStats();
            
            String bgColor = item.isCompleted() ? "#1E1E1E" : "#252525";
            String borderColor = item.isCompleted() ? "#2A2A2A" : "#333333";
            
            row.setStyle(
                "-fx-background-color: " + bgColor + "; " +
                "-fx-background-radius: 15; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 8, 0, 0, 2);"
            );
            
            cb.setStyle("-fx-text-fill: " + (item.isCompleted() ? "#666666" : "#FFFFFF") + ";");
            
            ScaleTransition st = new ScaleTransition(Duration.millis(150), row);
            st.setToX(0.98);
            st.setToY(0.98);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.play();
        });

        del.setOnAction(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(300), row);
            tt.setToX(500);
            
            FadeTransition ft = new FadeTransition(Duration.millis(300), row);
            ft.setToValue(0);
            
            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.setOnFinished(ev -> {
                service.removeTask(item);
                render();
                updateStats();
            });
            pt.play();
        });

        return row;
    }
}