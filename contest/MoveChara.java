import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;
//移動時・衝突時効果音再生用
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class MoveChara {
    public static final int TYPE_DOWN = 0;
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_UP = 3;

    private final String[] directions = { "Down", "Left", "Right", "Up" };
    private final String[] animationNumbers = { "1", "2", "3" };
    private final String pngPathPre = "png/kenta";
    private final String pngPathSuf = ".png";

    private int posX;
    private int posY;

    private MapData mapData;

    private Image[][] charaImages;
    private ImageView[] charaImageViews;
    private ImageAnimation[] charaImageAnimations;

    private int charaDirection;

     // 効果音ファイルのパス
     private final String stepSoundFile = "sound/walk1cut.mp3";
     private final String wallHitSoundFile = "sound/wallbound.mp3";
     // 効果音再生用のMediaPlayer
    private MediaPlayer stepSoundPlayer;
    private MediaPlayer wallHitSoundPlayer;
    
    MoveChara(int startX, int startY, MapData mapData) {
        this.mapData = mapData;

        charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i = 0; i < 4; i++) {
            charaImages[i] = new Image[3];
            for (int j = 0; j < 3; j++) {
                charaImages[i][j] = new Image(
                        pngPathPre + directions[i] + animationNumbers[j] + pngPathSuf);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation(
                    charaImageViews[i], charaImages[i]);
        }

        posX = startX;
        posY = startY;

        setCharaDirection(TYPE_RIGHT); // start with right-direction

        // 移動効果音の初期化
        // 効果音の初期化
        try {
            Media stepSound = new Media(new File("sound/walk1cut.mp3").toURI().toString());
            stepSoundPlayer = new MediaPlayer(stepSound);

            Media wallHitSound = new Media(new File("sound/wallbound.mp3").toURI().toString());
            wallHitSoundPlayer = new MediaPlayer(wallHitSound);
        } catch (Exception e) {
            System.err.println("効果音の読み込みに失敗しました: " + e.getMessage());
        }
    }

    // set the cat's direction
    public void setCharaDirection(int cd) {
        charaDirection = cd;
        for (int i = 0; i < 4; i++) {
            if (i == charaDirection) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    // check whether the cat can move on
    private boolean isMovable(int dx, int dy) {
        if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_WALL) {
            return false;
        } else if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_SPACE) {
            return true;
        }
        return false;
    }

    // move the cat
    public boolean move(int dx, int dy) {
        if (isMovable(dx, dy)) {
            posX += dx;
            posY += dy;
            System.out.println("chara[X,Y]:" + posX + "," + posY);

            // 歩行効果音を再生
            playSound(stepSoundFile);

            return true;
        } else {
            // 壁にぶつかった効果音を再生
            playSound(wallHitSoundFile);

            return false;
        }
    }

    // 効果音を再生するメソッド
    private void playSound(String soundFile) {
        try {
            Media sound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("効果音の再生に失敗しました: " + e.getMessage());
        }
    }

    // getter: direction of the cat
    public ImageView getCharaImageView() {
        return charaImageViews[charaDirection];
    }

    // getter: x-positon of the cat
    public int getPosX() {
        return posX;
    }

    // getter: y-positon of the cat
    public int getPosY() {
        return posY;
    }

    // Show the cat animation
    private class ImageAnimation extends AnimationTimer {

        private ImageView charaView = null;
        private Image[] charaImages = null;
        private int index = 0;

        private long duration = 500 * 1000000L; // 500[ms]
        private long startTime = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        public ImageAnimation(ImageView charaView, Image[] images) {
            this.charaView = charaView;
            this.charaImages = images;
            this.index = 0;
        }

        @Override
        public void handle(long now) {
            if (startTime == 0) {
                startTime = now;
            }

            preCount = count;
            count = (now - startTime) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if (index < 0 || 2 < index) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }
}
