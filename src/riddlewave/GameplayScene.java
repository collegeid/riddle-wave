package riddlewave;

import javafx.animation.KeyFrame;
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

    private static int currentIndex = 0;
    private static int lives = MAX_LIVES;
    private static int timeLeft = TIME_PER_QUESTION;

    private static Timeline timer;
    private static Label timerLabel;
    private static HBox lifeBox;
    private static Text dialogText;
    private static VBox questionBox;
    private static ImageView jumpscareImg;

    private static List<Question> questions;

    public static void show(Stage stage, String provinceName) {
        AnchorPane root = new AnchorPane();

        // Background
        ImageView bg = new ImageView(new Image("file:resources/assets/Bg/bg_game_jawa.png"));
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

        // Dialog Bubble
        dialogText = new Text();
        dialogText.setFont(Font.font("Verdana", 20));
        dialogText.setStyle("-fx-fill: white;");
        VBox bubble = new VBox(dialogText);
        bubble.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 20px; -fx-background-radius: 15;");
        bubble.setMaxWidth(600);
        AnchorPane.setLeftAnchor(bubble, 300.0);
        AnchorPane.setBottomAnchor(bubble, 200.0);

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

        // Soal
        questionBox = new VBox(20);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPadding(new Insets(20));
        AnchorPane.setTopAnchor(questionBox, 250.0);
        AnchorPane.setLeftAnchor(questionBox, 350.0);
        AnchorPane.setRightAnchor(questionBox, 350.0);

        // Jumpscare (overlay hidden)
        jumpscareImg = new ImageView(new Image("file:resources/assets/Karakter/jumpscare.png"));
        jumpscareImg.setFitWidth(1280);
        jumpscareImg.setPreserveRatio(true);
        jumpscareImg.setVisible(false);
        root.getChildren().add(jumpscareImg);

        // Load Narasi dan Soal
        questions = QuestionManager.loadQuestions(provinceName);
        String narasi = QuestionManager.loadNarration(provinceName);
        playTypingEffect(narasi);

        // Tombol Mulai
        Button mulaiBtn = new Button("Mulai");
        mulaiBtn.setFont(Font.font(20));
        AnchorPane.setBottomAnchor(mulaiBtn, 40.0);
        AnchorPane.setRightAnchor(mulaiBtn, 40.0);

        mulaiBtn.setOnAction(e -> {
            root.getChildren().removeAll(bot, bubble, mulaiBtn);
            showNextQuestion(stage, provinceName);
        });

        root.getChildren().addAll(bot, bubble, timerLabel, lifeBox, questionBox, mulaiBtn);

        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    private static void showNextQuestion(Stage stage, String provinceName) {
        if (currentIndex >= questions.size()) {
            dialogText.setText("Kamu berhasil menyelesaikan provinsi ini!");
            ProvinceManager.unlock(provinceName);
            return;
        }

        Question q = questions.get(currentIndex);
        questionBox.getChildren().clear();

        Label qLabel = new Label(q.getText());
        qLabel.setWrapText(true);
        qLabel.setFont(Font.font("Arial", 22));
        questionBox.getChildren().add(qLabel);

        String[] opts = q.getOptions();
        for (int i = 0; i < opts.length; i++) {
            char optChar = (char) ('A' + i);
            String label = optChar + ". " + opts[i];
            Button optBtn = new Button(label);
            optBtn.setMaxWidth(Double.MAX_VALUE);
            int finalI = i;
            optBtn.setOnAction(e -> {
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
            dialogText.setText("Jawaban salah! Nyawamu berkurang.");
            showJumpscare();
            updateHearts();
        } else {
            dialogText.setText("Bagus! Jawabanmu benar.");
        }

        if (lives <= 0) {
            dialogText.setText("Yahh... nyawamu habis. Game Over!");
            questionBox.getChildren().clear();
            return;
        }

        currentIndex++;
        showNextQuestion(stage, provinceName);
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
                dialogText.setText("Waktu habis! Nyawa berkurang.");
                updateHearts();

                if (lives <= 0) {
                    dialogText.setText("Yahh... nyawamu habis. Game Over!");
                    questionBox.getChildren().clear();
                } else {
                    currentIndex++;
                    showNextQuestion(stage, provinceName);
                }
            }
        }));
        timer.setCycleCount(TIME_PER_QUESTION);
        timer.play();
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

    private static void playTypingEffect(String fullText) {
        Timeline typing = new Timeline();
        final int[] i = {0};
        typing.getKeyFrames().add(new KeyFrame(Duration.millis(35), ev -> {
            if (i[0] < fullText.length()) {
                dialogText.setText(fullText.substring(0, i[0] + 1));
                i[0]++;
            } else {
                typing.stop();
            }
        }));
        typing.setCycleCount(Timeline.INDEFINITE);
        typing.play();
    }

    private static void showJumpscare() {
        jumpscareImg.setVisible(true);
        Timeline hide = new Timeline(new KeyFrame(Duration.seconds(1), ev -> jumpscareImg.setVisible(false)));
        hide.setCycleCount(1);
        hide.play();
    }
}
