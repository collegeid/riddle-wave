package riddlewave;

import javafx.application.Application;
import javafx.stage.Stage;

public class RiddleWave extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Langsung panggil cutscene saat program dimulai
        IntroPanel.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
