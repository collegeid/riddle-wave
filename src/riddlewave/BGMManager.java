package riddlewave;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class BGMManager {
    private static MediaPlayer bgmPlayer;

    public static void play(String filePath) {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        Media media = new Media(new File(filePath).toURI().toString());
        bgmPlayer = new MediaPlayer(media);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop terus
        bgmPlayer.setVolume(0.4); // volume default
        bgmPlayer.play();
    }

    public static void stop() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public static void setVolume(double volume) {
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(volume);
        }
    }
}
