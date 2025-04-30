package riddlewave;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IntroPanel {

    private static final String INTRO_TEXT = "Halo! Namaku Wisanggeni.\nAku akan memandumu di game Riddle Wave.\nSiapkan dirimu untuk menjawab teka-teki dari berbagai provinsi\n Di Indonesia!";

    public static void show(Stage stage) {
        AnchorPane root = new AnchorPane();

        // Gunakan ImageView sebagai Background agar responsif
        ImageView bgView = new ImageView(new Image("file:resources/assets/Bg/bgwc2.png"));
        bgView.setPreserveRatio(false); // agar tidak ada space kosong
        bgView.setFitWidth(1920); // default resolusi
        bgView.setFitHeight(1080);
        root.getChildren().add(bgView); // tambahkan paling bawah

        // Karakter Arimbi
        ImageView botView = new ImageView(new Image("file:resources/assets/Karakter/Wisanggeni.png"));
        botView.setPreserveRatio(true);
        botView.setFitHeight(450);
        AnchorPane.setLeftAnchor(botView, 50.0);
        AnchorPane.setBottomAnchor(botView, 50.0);

        // Bubble dialog
        Text introText = new Text();
        introText.setFont(Font.font("Verdana", 22));
        introText.setStyle("-fx-fill: white;");

        VBox textBubble = new VBox(introText);
        textBubble.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 20px; -fx-background-radius: 15;");
        textBubble.setMaxWidth(600);
        AnchorPane.setLeftAnchor(textBubble, 305.0);
        AnchorPane.setBottomAnchor(textBubble, 280.0);

        // Tombol Lanjut
        Button lanjutBtn = new Button("Next");
        lanjutBtn.setFont(Font.font(18));
        lanjutBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-padding: 10px 20px;");
        lanjutBtn.setVisible(false);
        AnchorPane.setRightAnchor(lanjutBtn, 30.0);
        AnchorPane.setBottomAnchor(lanjutBtn, 30.0);

        lanjutBtn.setOnAction(e -> {
            System.out.println("Lanjut ke scene berikutnya...");
            // Misal new MainMenu().start(stage);
                MapSelectionScene.show(stage);

        });

        // Tambahkan semua elemen di atas background
        root.getChildren().addAll(botView, textBubble, lanjutBtn);

        Scene scene = new Scene(root, 1280, 720);

        // Responsif: atur ulang ukuran bg saat layar resize
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            bgView.setFitWidth(newVal.doubleValue());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            bgView.setFitHeight(newVal.doubleValue());
        });

        stage.setScene(scene);
        stage.setFullScreen(true);

        // Efek ketik teks
        Timeline typingTimeline = new Timeline();
        final int[] index = {0};
        typingTimeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(40), event -> {
                if (index[0] < INTRO_TEXT.length()) {
                    introText.setText(INTRO_TEXT.substring(0, index[0] + 1));
                    index[0]++;
                } else {
                    typingTimeline.stop();
                    lanjutBtn.setVisible(true);
                }
            })
        );
        typingTimeline.setCycleCount(Timeline.INDEFINITE);
        typingTimeline.play();
    }
}
