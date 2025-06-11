package riddlewave;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class Cutscene  {

    public static void show(Stage stage) {
        BGMManager.play("resources/assets/Bgm/ingame.mp3", null);
        String path = "resources/assets/video/cutscene.mp4";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        StackPane root = new StackPane();
        root.getChildren().add(mediaView);

        // 🌟 Welcome Text
        Text welcomeText = new Text("Welcome to Riddle Wave");
        welcomeText.setFont(Font.font("Verdana", 60));
        welcomeText.setFill(Color.WHITE);
        welcomeText.setEffect(new DropShadow(20, Color.GOLD));
        welcomeText.setOpacity(0); // mulai transparan

        StackPane.setAlignment(welcomeText, Pos.TOP_CENTER);
        StackPane.setMargin(welcomeText, new Insets(80));
        root.getChildren().add(welcomeText);

        // ✨ Fade In (0 → 1), wait, then Fade Out (1 → 0)
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), welcomeText);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), welcomeText);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(2)); // tunggu 2 detik setelah muncul penuh

        fadeOut.setOnFinished(e -> root.getChildren().remove(welcomeText));

        SequentialTransition sequence = new SequentialTransition(fadeIn, fadeOut);
        sequence.play();

        // Tombol Skip
        Button skipButton = new Button("Skip");
        skipButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white;");
        StackPane.setAlignment(skipButton, Pos.TOP_RIGHT);
        StackPane.setMargin(skipButton, new Insets(20));
        skipButton.setOnAction(e -> {
            mediaPlayer.stop();
            IntroPanel.show(stage);
        });
        root.getChildren().add(skipButton);

        // Scene
        Scene scene = new Scene(root, 1280, 720);
        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty());
        mediaView.setPreserveRatio(false);

        stage.setTitle("Cut Scene - Riddle Wave");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        mediaPlayer.setOnEndOfMedia(() -> IntroPanel.show(stage));
        mediaPlayer.play();
    }

    
}
