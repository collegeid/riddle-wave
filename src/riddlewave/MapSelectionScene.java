package riddlewave;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class MapSelectionScene {

    private static final String DIALOG_TEXT =
            "Ini adalah peta provinsi Indonesia.\n" +
            "Beberapa provinsi masih terkunci. Kamu bisa membukanya\n" +
            "dengan menyelesaikan provinsi sebelumnya terlebih dahulu.\n" +
            "Silakan pilih provinsi yang tersedia untuk mulai bermain!";

    private static Text dialogText; // ❗️Supaya bisa diakses dan diubah

    public static void show(Stage stage) {
        AnchorPane root = new AnchorPane();

        // Background
        ImageView bg = new ImageView(new Image("file:resources/assets/Bg/bg5.png"));
        bg.setPreserveRatio(false);
        bg.setFitWidth(1920);
        bg.setFitHeight(1080);
        root.getChildren().add(bg);

        // Karakter Wisanggeni
        ImageView bot = new ImageView(new Image("file:resources/assets/Karakter/Wisanggeni.png"));
        bot.setFitHeight(400);
        bot.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(bot, 30.0);
        AnchorPane.setBottomAnchor(bot, 30.0);

        // Dialog bubble
        dialogText = new Text();
        dialogText.setFont(Font.font("Verdana", 20));
        dialogText.setStyle("-fx-fill: white;");
        VBox dialogBubble = new VBox(dialogText);
        dialogBubble.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 20px; -fx-background-radius: 15;");
        dialogBubble.setMaxWidth(600);
        AnchorPane.setLeftAnchor(dialogBubble, 250.0);
        AnchorPane.setBottomAnchor(dialogBubble, 210.0);

        // Province Row
        HBox provinceRow = new HBox(40);
        provinceRow.setPadding(new Insets(20));
        provinceRow.setAlignment(Pos.CENTER_LEFT);

        ProvinceManager.getAllStatus().entrySet().stream()
                .sorted((a, b) -> Boolean.compare(a.getValue(), b.getValue()))
                .forEach(entry -> {
                    provinceRow.getChildren().add(createProvinceItem(entry.getKey(), entry.getValue()));
                });

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(provinceRow);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(260);

        // Blur Layer
        Region blurLayer = new Region();
        blurLayer.setPrefHeight(260);
        blurLayer.setStyle("-fx-background-color: rgba(0,0,0,0.35); -fx-background-radius: 20;");
        blurLayer.setEffect(new BoxBlur(20, 20, 3));

        // Stack Scroll
        StackPane scrollContainer = new StackPane();
        scrollContainer.getChildren().addAll(blurLayer, scrollPane);
        scrollContainer.setPadding(new Insets(10));

        AnchorPane.setTopAnchor(scrollContainer, 60.0);
        AnchorPane.setLeftAnchor(scrollContainer, 180.0);
        AnchorPane.setRightAnchor(scrollContainer, 80.0);

        // Tambahkan semua ke root
        root.getChildren().addAll(scrollContainer, bot, dialogBubble);

        // Scene setup
        Scene scene = new Scene(root, 1280, 720);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> bg.setFitWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> bg.setFitHeight(newVal.doubleValue()));
        stage.setScene(scene);
        stage.setFullScreen(true);

        // Efek ketik teks pertama
        playTypingEffect(DIALOG_TEXT);
    }

    // Komponen provinsi
    private static StackPane createProvinceItem(String provinceKey, boolean locked) {
        StackPane container = new StackPane();
        container.setAlignment(Pos.CENTER);

        ImageView provinceImage = new ImageView(new Image("file:resources/assets/Peta/" + provinceKey + ".png"));
        provinceImage.setFitWidth(200);
        provinceImage.setPreserveRatio(true);
        container.getChildren().add(provinceImage);

        if (locked) {
            ImageView lockIcon = new ImageView(new Image("file:resources/assets/Gembok/lock.png"));
            lockIcon.setFitWidth(48);
            lockIcon.setPreserveRatio(true);
            container.getChildren().add(lockIcon);
        }

        container.setOnMouseClicked(e -> {
            if (locked) {
                playTypingEffect("Hehh! Provinsi ini masih terkunci.\nSelesaikan provinsi sebelumnya untuk membukanya !!!.");
            } else {
                System.out.println("Provinsi dipilih: " + provinceKey);
                // TODO: lanjut ke scene gameplay
            }
        });

        return container;
    }

    // Efek ketik dinamis
    private static void playTypingEffect(String text) {
        Timeline typing = new Timeline();
        final int[] i = {0};
        typing.getKeyFrames().add(new KeyFrame(Duration.millis(30), ev -> {
            if (i[0] < text.length()) {
                dialogText.setText(text.substring(0, i[0] + 1));
                i[0]++;
            } else {
                typing.stop();
            }
        }));
        typing.setCycleCount(Timeline.INDEFINITE);
        typing.play();
    }
}
