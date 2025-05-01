package riddlewave;

import javafx.application.Application;
import javafx.stage.Stage;

public class RiddleWave extends Application {
    @Override
    public void start(Stage stage) {
        Cutscene.show(stage); // panggil cutscene seperti ini
    }

    public static void main(String[] args) {
        launch(args);
    }
}
