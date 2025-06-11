package riddlewave;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IntroPanel {

    private static Timeline typing;  // Variabel global untuk menyimpan referensi Timeline
    private static AudioClip typingSound;  // Variabel global untuk menyimpan suara ketik

    private static final String INTRO_TEXT = "Halo! Namaku Wisanggeni.\nAku akan memandumu di game Riddle Wave.\nSiapkan dirimu untuk menjawab teka-teki dari berbagai Pulau\nDi Indonesia!";

    public static void show(Stage stage) {
        final AnchorPane root = new AnchorPane();

        // Background
        ImageView bgView = new ImageView(new Image("file:resources/assets/Bg/bgwc2.png"));
        bgView.setPreserveRatio(false);
        bgView.setFitWidth(1920);
        bgView.setFitHeight(1080);
        root.getChildren().add(bgView);

        // Karakter
        ImageView botView = new ImageView(new Image("file:resources/assets/Karakter/Wisanggeni.png"));
        botView.setPreserveRatio(true);
        botView.setFitHeight(450);
        AnchorPane.setLeftAnchor(botView, 50.0);
        AnchorPane.setBottomAnchor(botView, 50.0);

        // Dialog Bubble
        final Text introText = new Text(INTRO_TEXT); // diisi dulu agar bisa diakses di fungsi ketik
        introText.setFont(Font.font("Verdana", 22));
        introText.setStyle("-fx-fill: white;");
        VBox textBubble = new VBox(introText);
        textBubble.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 20px; -fx-background-radius: 15;");
        textBubble.setMaxWidth(600);
        AnchorPane.setLeftAnchor(textBubble, 305.0);
        AnchorPane.setBottomAnchor(textBubble, 280.0);

        // Tombol Next
        final Button lanjutBtn = new Button("Next");
        lanjutBtn.setFont(Font.font(18));
        lanjutBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-padding: 10px 20px;");
        lanjutBtn.setVisible(false);
        AnchorPane.setRightAnchor(lanjutBtn, 30.0);
        AnchorPane.setBottomAnchor(lanjutBtn, 30.0);

        // Tombol Skip
        final Button skipBtn = new Button("Skip");
        skipBtn.setFont(Font.font(14));
        skipBtn.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-text-fill: white; -fx-padding: 6px 15px;");
        AnchorPane.setTopAnchor(skipBtn, 20.0);
        AnchorPane.setRightAnchor(skipBtn, 20.0);

        // Skip → langsung tampilkan semua
        skipBtn.setOnAction(e -> {
          if (typing != null) {
                typing.stop();  // Hentikan efek ketik
          }   
          if (typingSound != null) {
                typingSound.stop();  // Hentikan suara ketik
          }
            introText.setText(INTRO_TEXT);
            lanjutBtn.setVisible(true);
            root.getChildren().remove(skipBtn);
        });

        // Lanjut → ke map selection
        lanjutBtn.setOnAction(e -> MapSelectionScene.show(stage));

        root.getChildren().addAll(botView, textBubble, lanjutBtn, skipBtn);

        // Scene
        Scene scene = new Scene(root, 1280, 720);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> bgView.setFitWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> bgView.setFitHeight(newVal.doubleValue()));
        stage.setTitle("Welcome - Riddle Wave");
        stage.setScene(scene);
        stage.setFullScreen(true);

        // Mulai efek ketik + suara
        playTypingEffectText(introText, () -> {
            root.getChildren().remove(skipBtn);
            lanjutBtn.setVisible(true);
        });
    }

    // Efek ketik + suara + callback saat selesai

  // Efek ketik + suara + callback saat selesai
    private static void playTypingEffectText(Text targetText, Runnable onFinishCallback) {
        String fullText = targetText.getText();
        targetText.setText("");  // Kosongkan dulu teks yang ada

        File soundFile = new File("resources/assets/audio/ketik.wav");  // Ganti dengan path yang sesuai
        String soundPath = soundFile.toURI().toString();

        typing = new Timeline();  // Menggunakan variabel global 'typing'
        final int[] i = {0};

        typing.getKeyFrames().add(new KeyFrame(Duration.millis(30), ev -> {
            if (i[0] < fullText.length()) {
                targetText.setText(fullText.substring(0, i[0] + 1));

                // Suara ketik per huruf
                AudioClip clickSound = new AudioClip(soundPath);
                clickSound.play();

                i[0]++;
            } else {
                typing.stop();  // Berhenti setelah selesai
                if (onFinishCallback != null) {
                    onFinishCallback.run();  // Panggil callback setelah selesai mengetik
                }
            }
        }));

        typing.setCycleCount(Timeline.INDEFINITE);
        typing.play();  // Mulai efek ketik
    }

}
