import java.io.IOException;
import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class StageDB {

    static private Stage mainStage = null;
    static private Stage gameOverStage = null;
    static private Stage ClearStage = null;
    static private MediaPlayer mainSound = null;
    static private MediaPlayer gameOverSound = null;
    static private MediaPlayer ClearSound = null;
    static private Class mainClass;
    //この下でメインBGMを決める（BGMのファイル名を書く）
    static private final String mainSoundFileName = "sound/Take_the_sword.mp3"; // BGM by OtoLogic
    static private final String GameOverSoundFileName = "sound/sousou4.mp3";
    public static void setMainClass(Class mainClass) {
        StageDB.mainClass = mainClass;
    }

    public static MediaPlayer getMainSound() {
        if (mainSound == null) {
            try {
                Media m = new Media(new File(mainSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setCycleCount(MediaPlayer.INDEFINITE); // loop play
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.3); // volume from 0.0 to 1.0
                mainSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return mainSound;
    }

    public static MediaPlayer getGameOverSound() {
        if (gameOverSound == null) {
            try {
                Media m = new Media(new File(GameOverSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.5); // volume from 0.0 to 1.0
                gameOverSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return gameOverSound;
    }

    public static MediaPlayer getClearSound() {
        if (ClearSound == null) {
            try {
                // please write down the code for playing gameclear sound
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return ClearSound;
    }

    public static Stage getMainStage() {
        if (mainStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGame.fxml"));
                VBox root = loader.load();
                Scene scene = new Scene(root);
                mainStage = new Stage();
                mainStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return mainStage;
    }

    public static Stage getGameOverStage() {
        if (gameOverStage == null) {
            try {
                System.out.println("StageDB:getGameOverStage()");
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGameOver.fxml"));
                VBox root = loader.load();
                Scene scene = new Scene(root);
                gameOverStage = new Stage();
                gameOverStage.setScene(scene);

                 // ゲームオーバーステージが表示されるときにサウンドを再生
            gameOverStage.setOnShown(event -> {
                MediaPlayer gameOverSound = getGameOverSound();
                if (gameOverSound != null) {
                    gameOverSound.stop();
                    gameOverSound.play();
                }
            });
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return gameOverStage;
    }

    public static Stage getClearStage() {
        if (ClearStage == null) {
            try {
                System.out.println("StageDB:getClearStage()");
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapClear.fxml"));
                VBox root = loader.load();
                ClearController controller = loader.getController();
                Scene scene = new Scene(root);
                ClearStage = new Stage();
                controller.setStage(ClearStage);
                ClearStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
                System.err.println("Error loading MapClear.fxml: " + ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        return ClearStage;
    }
}
