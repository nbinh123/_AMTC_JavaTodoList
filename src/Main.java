import controllers.LoginController;
import models.UserManager;
import views.LoginView;

public class Main {
    public static void main(String[] args) {
        UserManager data = new UserManager();
        
        LoginView view = new LoginView();
        
        LoginController controller = new LoginController(data, view);
        
        controller.showWindow();
    }
}