package riddlewave;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class GameplayScene {

    private static final int MAX_LIVES = 3;
    private static final int TIME_PER_QUESTION = 30;

    private static int currentIndex;
    private static int lives;
    private static int timeLeft;

    private static Timeline timer;
    private static Label timerLabel;
    private static HBox lifeBox;
    private static Text dialogText;
    private static VBox questionBox;
    private static ImageView jumpscareImage;
    private static VBox dialogBubble;

    private static List<Question> questions;

    public static void show(Stage stage, String provinceName) {
        currentIndex = 0;
        lives = MAX_LIVES;

        AnchorPane root = new AnchorPane();

        // Background
        String bgPath = "file:resources/assets/Bg/" + provinceName.toLowerCase() + ".png";
        ImageView bg = new ImageView(new Image(bgPath));
        bg.setFitWidth(1920);
        bg.setFitHeight(1080);
        bg.setPreserveRatio(false);
        root.getChildren().add(bg);

        // Bot
        ImageView bot = new ImageView(new Image("file:resources/assets/Karakter/Wisanggeni.png"));
        bot.setFitHeight(350);
        bot.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(bot, 30.0);
        AnchorPane.setBottomAnchor(bot, 30.0);

        // Dialog bubble
        dialogText = new Text();
        dialogText.setFont(Font.font("Verdana", 20));
        dialogText.setStyle("-fx-fill: white;");
        dialogBubble = new VBox(dialogText);
        dialogBubble.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 20px; -fx-background-radius: 15;");
        dialogBubble.setMaxWidth(600);
        AnchorPane.setLeftAnchor(dialogBubble, 300.0);
        AnchorPane.setBottomAnchor(dialogBubble, 200.0);

        // Timer
        timerLabel = new Label();
        timerLabel.setFont(Font.font("Arial", 26));
        timerLabel.setStyle("-fx-text-fill: white;");
        AnchorPane.setTopAnchor(timerLabel, 20.0);
        AnchorPane.setRightAnchor(timerLabel, 40.0);

        // Nyawa
        lifeBox = new HBox(10);
        for (int i = 0; i < MAX_LIVES; i++) {
            ImageView heart = new ImageView(new Image("file:resources/assets/UI/heart.png"));
            heart.setFitWidth(30);
            heart.setPreserveRatio(true);
            lifeBox.getChildren().add(heart);
        }
        AnchorPane.setTopAnchor(lifeBox, 20.0);
        AnchorPane.setLeftAnchor(lifeBox, 40.0);

        // Question area
        questionBox = new VBox(20);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(20));
        AnchorPane.setBottomAnchor(questionBox, 100.0);
        AnchorPane.setLeftAnchor(questionBox, 350.0);
        AnchorPane.setRightAnchor(questionBox, 350.0);

        // Load questions and narration
        questions = QuestionManager.loadQuestions(provinceName);
        String narasi = QuestionManager.loadNarration(provinceName);
        playTypingEffect(narasi);

        // Tombol mulai
        Button mulaiBtn = new Button("Mulai");
        mulaiBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-padding: 10px 20px;");
        mulaiBtn.setFont(Font.font(18));
        AnchorPane.setBottomAnchor(mulaiBtn, 40.0);
        AnchorPane.setRightAnchor(mulaiBtn, 40.0);
        mulaiBtn.setOnAction(e -> {
          root.getChildren().remove(mulaiBtn);
          root.getChildren().remove(bot);
          root.getChildren().remove(dialogBubble);
          updateHearts(); // ✅ Tambahkan ini
          showNextQuestion(stage, provinceName);
        });


        // Jumpscare
        jumpscareImage = new ImageView(new Image("file:resources/assets/Karakter/Monster3.png"));
        jumpscareImage.setFitHeight(500);
        jumpscareImage.setPreserveRatio(true);
        jumpscareImage.setVisible(false);
        AnchorPane.setLeftAnchor(jumpscareImage, 390.0);
        AnchorPane.setTopAnchor(jumpscareImage, 90.0);


        // Add all
        root.getChildren().addAll(jumpscareImage, dialogBubble, bot, timerLabel, lifeBox, questionBox, mulaiBtn);
        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    private static void showNextQuestion(Stage stage, String provinceName) {
      if (currentIndex >= questions.size()) {
            dialogText.setText("Kamu berhasil! Provinsi selanjutnya terbuka.");

            // ✅ Tampilkan kembali bot dan dialog
            dialogBubble.setVisible(true);
            dialogBubble.toFront(); // Pastikan dialog muncul di atas
            lifeBox.toFront();
            timerLabel.toFront();
            questionBox.setVisible(false);

            ImageView bot = new ImageView(new Image("file:resources/assets/Karakter/Wisanggeni.png"));
            bot.setFitHeight(350);
            bot.setPreserveRatio(true);
            AnchorPane.setLeftAnchor(bot, 30.0);
            AnchorPane.setBottomAnchor(bot, 30.0);

            ((AnchorPane) dialogBubble.getParent()).getChildren().add(bot);

            ProvinceManager.unlockNext(provinceName);

            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(e -> MapSelectionScene.show(stage));
            delay.play();
            return;
        }


        Question q = questions.get(currentIndex);
        questionBox.getChildren().clear();

        Label qLabel = new Label(q.getText());
        qLabel.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.7);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 15px;" +
            "-fx-background-radius: 15;" +
            "-fx-font-size: 22px;"
        );
        qLabel.setWrapText(true);
        qLabel.setFont(Font.font("Arial", 24));
        questionBox.getChildren().add(qLabel);

        String[] opts = q.getOptions();
        for (int i = 0; i < opts.length; i++) {
            char optChar = (char) ('A' + i);
            String label = optChar + ". " + opts[i];
            Button optBtn = new Button(label);
            optBtn.setMaxWidth(Double.MAX_VALUE);
            optBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 12px 20px;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 1px;"
            );
            int finalI = i;
            optBtn.setOnMouseEntered(ev -> optBtn.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-text-fill: black; -fx-font-size: 18px; -fx-background-radius: 15; -fx-padding: 10px;"));
            optBtn.setOnMouseExited(ev -> optBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 15; -fx-padding: 10px;"));            optBtn.setOnAction(e -> {
                timer.stop();
                checkAnswer(stage, q, String.valueOf(optChar), provinceName);
            });
            questionBox.getChildren().add(optBtn);
        }

        resetTimer(stage, q, provinceName);
    }

    private static void checkAnswer(Stage stage, Question q, String chosen, String provinceName) {
        if (!q.getCorrectAnswer().equalsIgnoreCase(chosen)) {
            lives--;
            updateHearts();
            if (lives <= 0) {
                showJumpscare(stage, "Yahh... nyawamu habis. Game Over!", true, provinceName);
            } else {
                //showJumpscare(stage, "Jawaban salah! Nyawa berkurang.", false, provinceName);
            }
        } else {
            dialogBubble.setVisible(true);
            dialogText.setText("Bagus! Jawabanmu benar.");
            currentIndex++;
            showNextQuestion(stage, provinceName);
        }
    }

    private static void resetTimer(Stage stage, Question q, String provinceName) {
        timeLeft = TIME_PER_QUESTION;
        timerLabel.setText(timeLeft + " detik");
        if (timer != null) timer.stop();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText(timeLeft + " detik");
            if (timeLeft <= 0) {
                timer.stop();
                lives--;
                updateHearts();
                if (lives <= 0) {
                    showJumpscare(stage, "Yahh... nyawamu habis. Game Over!", true, provinceName);
                } else {
                    //showJumpscare(stage, "Waktu habis! Nyawa berkurang.", false, provinceName);
                }
            }
        }));
        timer.setCycleCount(TIME_PER_QUESTION);
        timer.play();
    }

private static void showJumpscare(Stage stage, String msg, boolean isGameOver, String provinceName) {
    questionBox.setVisible(false);
    jumpscareImage.setVisible(true);
    dialogBubble.setVisible(true);
    dialogText.setText(""); // Kosongkan dulu untuk typing effect
    playTypingEffect(msg);

    // Pusatkan jumpscare
    AnchorPane.setLeftAnchor(jumpscareImage, null);
    AnchorPane.setRightAnchor(jumpscareImage, null);
    AnchorPane.setTopAnchor(jumpscareImage, null);
    AnchorPane.setBottomAnchor(jumpscareImage, null);
    AnchorPane.setTopAnchor(jumpscareImage, 100.0);
    AnchorPane.setLeftAnchor(jumpscareImage, 400.0); // sesuaikan jika perlu

    if (isGameOver) {
        // Jika game over, jangan sembunyikan jumpscare
        questionBox.getChildren().clear();

        // Tambahkan tombol kembali ke map
        Button backBtn = new Button("Kembali ke Map");
        backBtn.setFont(Font.font(18));
        backBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-padding: 10px 20px;");
        backBtn.setOnAction(ev -> MapSelectionScene.show(stage));

        VBox gameOverBox = new VBox(20, dialogBubble, backBtn);
        gameOverBox.setAlignment(Pos.CENTER);
        AnchorPane.setBottomAnchor(gameOverBox, 100.0);
        AnchorPane.setLeftAnchor(gameOverBox, 500.0); // sesuaikan posisi

        questionBox.getChildren().add(gameOverBox);
        questionBox.setVisible(true);
    } else {
        // Kalau bukan game over, delay 5 detik lalu lanjut
        PauseTransition wait = new PauseTransition(Duration.seconds(5));
        wait.setOnFinished(e -> {
            jumpscareImage.setVisible(false);
            dialogBubble.setVisible(false);
            questionBox.setVisible(true);
            currentIndex++;
            showNextQuestion(stage, provinceName);
        });
        wait.play();
    }
}


    private static void updateHearts() {
        lifeBox.getChildren().clear();
        for (int i = 0; i < lives; i++) {
            ImageView heart = new ImageView(new Image("file:resources/assets/Nyawa/heart.png"));
            heart.setFitWidth(30);
            heart.setPreserveRatio(true);
            lifeBox.getChildren().add(heart);
        }
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
}