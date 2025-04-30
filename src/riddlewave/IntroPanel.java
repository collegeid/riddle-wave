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

    private static final String INTRO_TEXT = "Halo! Namaku Wisanggeni.\nAku akan memandumu di game Riddle Wave.\nSiapkan dirimu untuk menjawab teka-teki dari berbagai Pulau\nDi Indonesia!";

    public static void show(Stage stage) {
        AnchorPane root = new AnchorPane();

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

        // Tombol Skip
        Button skipBtn = new Button("Skip");
        skipBtn.setFont(Font.font(14));
        skipBtn.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-text-fill: white; -fx-padding: 6px 15px;");
        AnchorPane.setTopAnchor(skipBtn, 20.0);
        AnchorPane.setRightAnchor(skipBtn, 20.0);

        // Timeline efek ketik
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
                    root.getChildren().remove(skipBtn);
                }
            })
        );
        typingTimeline.setCycleCount(Timeline.INDEFINITE);

        // Skip: langsung tampilkan semua teks
        skipBtn.setOnAction(e -> {
            typingTimeline.stop();
            introText.setText(INTRO_TEXT);
            lanjutBtn.setVisible(true);
            root.getChildren().remove(skipBtn);
        });

        lanjutBtn.setOnAction(e -> {
            MapSelectionScene.show(stage);
        });

        // Tambahkan semua
        root.getChildren().addAll(botView, textBubble, lanjutBtn, skipBtn);

        // Scene
        Scene scene = new Scene(root, 1280, 720);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> bgView.setFitWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> bgView.setFitHeight(newVal.doubleValue()));
        stage.setScene(scene);
        stage.setFullScreen(true);

        typingTimeline.play();
    }
}
