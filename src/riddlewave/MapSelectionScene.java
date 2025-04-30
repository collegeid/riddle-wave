
// versi final MapSelectionScene.java dengan blur transparan, arrow kiri-kanan dan tanpa background putih

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
import javafx.scene.input.MouseEvent;
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

    private static Text dialogText;
    private static HBox provinceRow;

    public static void show(Stage stage) {
        AnchorPane root = new AnchorPane();

        // Background
        ImageView bg = new ImageView(new Image("file:resources/assets/Bg/bgMap.png"));
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
        provinceRow = new HBox(40);
        provinceRow.setPadding(new Insets(20));
        provinceRow.setAlignment(Pos.CENTER_LEFT);
        refreshProvinceRow();
        provinceRow.setStyle("-fx-background-color: transparent;");
        provinceRow.setBackground(Background.EMPTY);

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(provinceRow);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(false);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setBackground(Background.EMPTY);
        scrollPane.setPrefHeight(260);

        // Blur layer
        Region blurLayer = new Region();
        blurLayer.setPrefHeight(260);
        blurLayer.setStyle("-fx-background-color: rgba(0,0,0,0.35); -fx-background-radius: 20;");
        blurLayer.setEffect(new BoxBlur(20, 20, 3));
        blurLayer.setPickOnBounds(false);

        // Arrows
        ImageView leftArrow = new ImageView(new Image("file:resources/assets/UI/arrow_left.png"));
        leftArrow.setFitHeight(50);
        leftArrow.setPreserveRatio(true);
        leftArrow.setOnMouseClicked((MouseEvent e) -> {
            scrollPane.setHvalue(Math.max(0.0, scrollPane.getHvalue() - 0.2));
        });

        ImageView rightArrow = new ImageView(new Image("file:resources/assets/UI/arrow_right.png"));
        rightArrow.setFitHeight(50);
        rightArrow.setPreserveRatio(true);
        rightArrow.setOnMouseClicked((MouseEvent e) -> {
            scrollPane.setHvalue(Math.min(1.0, scrollPane.getHvalue() + 0.2));
        });

        // Scroll container
        StackPane scrollContainer = new StackPane();
        scrollContainer.getChildren().addAll(blurLayer, scrollPane, leftArrow, rightArrow);
        StackPane.setAlignment(leftArrow, Pos.CENTER_LEFT);
        StackPane.setMargin(leftArrow, new Insets(0, 0, 0, 5));
        StackPane.setAlignment(rightArrow, Pos.CENTER_RIGHT);
        StackPane.setMargin(rightArrow, new Insets(0, 5, 0, 0));
        scrollContainer.setPadding(new Insets(10));

        AnchorPane.setTopAnchor(scrollContainer, 60.0);
        AnchorPane.setLeftAnchor(scrollContainer, 180.0);
        AnchorPane.setRightAnchor(scrollContainer, 80.0);

        root.getChildren().addAll(scrollContainer, bot, dialogBubble);

        Scene scene = new Scene(root, 1280, 720);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> bg.setFitWidth(newVal.doubleValue()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> bg.setFitHeight(newVal.doubleValue()));
        stage.setScene(scene);
        stage.setFullScreen(true);

        playTypingEffect(DIALOG_TEXT);
    }

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
                playTypingEffect("Hehh! Pulau " + provinceKey + " ini masih terkunci.\nSelesaikan Pulau sebelumnya untuk membukanya !!!.");
            } else {
                System.out.println("Provinsi dipilih: " + provinceKey);
                riddlewave.GameplayScene.show(
                    (Stage) container.getScene().getWindow(),
                    provinceKey
                );
            }
        });

        return container;
    }

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

    private static void refreshProvinceRow() {
        provinceRow.getChildren().clear();
        Map<String, Boolean> updatedStatus = ProvinceManager.getAllStatus();
        updatedStatus.entrySet().stream()
            .sorted((a, b) -> Boolean.compare(a.getValue(), b.getValue()))
            .forEach(entry -> {
                provinceRow.getChildren().add(createProvinceItem(entry.getKey(), entry.getValue()));
            });
    }
}
