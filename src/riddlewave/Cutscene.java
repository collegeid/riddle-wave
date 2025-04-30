package riddlewave;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.io.File;

public class Cutscene extends Application {

    @Override
    public void start(Stage stage) {
        // Path video
        // Mulai background music
        BGMManager.play("resources/assets/Bgm/bgm.mp3");

        String path = "resources/assets/video/Opening.mp4";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // StackPane
        StackPane root = new StackPane();
        root.getChildren().add(mediaView);

        // Tombol Skip
        Button skipButton = new Button("Skip");
        skipButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white;");
        StackPane.setAlignment(skipButton, Pos.TOP_RIGHT);
        StackPane.setMargin(skipButton, new Insets(20));

        skipButton.setOnAction(e -> {
            mediaPlayer.stop();
            IntroPanel.show(stage); // Lanjut ke scene berikutnya
        });

        root.getChildren().add(skipButton);

        // Scene
        Scene scene = new Scene(root, 1280, 720);

        // ⬅️ Ini bagian penting: buat MediaView menyesuaikan ukuran layar
        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty());
        mediaView.setPreserveRatio(false); // paksa isi layar penuh

        stage.setTitle("Opening - Riddle Wave");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        // Setelah video selesai
        mediaPlayer.setOnEndOfMedia(() -> {
            IntroPanel.show(stage);
        });

        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
