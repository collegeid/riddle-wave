package riddlewave;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class BGMManager {
    private static MediaPlayer bgmPlayer;

    /**
     * Plays the BGM from the given file path.
     * If volume is provided, it will use that volume; otherwise, it will use the default volume.
     *
     * @param filePath the path to the BGM file
     * @param volume   the volume level (optional, default is 0.4)
     */
    public static void play(String filePath, Double volume) {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        Media media = new Media(new File(filePath).toURI().toString());
        bgmPlayer = new MediaPlayer(media);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop forever

        // Set the volume, if provided. Default is 0.4
        if (volume != null) {
            bgmPlayer.setVolume(volume);
        } else {
            bgmPlayer.setVolume(0.4); // default volume
        }

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
