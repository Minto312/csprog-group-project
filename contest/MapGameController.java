import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MapGameController implements Initializable {
    public MapData mapData;
    public MapData First;
    public MapData Second;
    public int floor;
    public boolean StairCheck;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;
    public ImageView[] maskImageViews;
    private static final int VISION_RADIUS = 2; 

    // Show Goal
    private final String GOAL_IMAGE = "png/GOAL.png";
    private ImageView goalImageView;
    //Show Stair
    private final String STAIR_UP_IMAGE = "png/Stair_up.png";
    private ImageView stairupImageView;
    private final String STAIR_DOWN_IMAGE = "png/Stair_down.png";
    private ImageView stairdownImageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21, 15);
        Second = new MapData(21, 15);
        floor = 0;
        StairCheck = true;
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        maskImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                int index = y * mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x, y);

                Image maskImage = new Image("png/BLACK_MASK.png");
                maskImageViews[index] = new ImageView(maskImage);
                maskImageViews[index].setVisible(true);
            }
        }

        setGoal();
        drawMap(chara, mapData);
    }

    // Draw the map
    public void drawMap(MoveChara c, MapData m) {
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
    
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                StackPane cell = new StackPane();
                int index = y * mapData.getWidth() + x;
    
                if (x == cx && y == cy) {
                    cell.getChildren().add(c.getCharaImageView());
                } else if (x == Gx && y == Gy) {
                    // ゴール座標に☆(画像)を表示
                    if (goalImageView == null) {
                        goalImageView = new ImageView(new Image(GOAL_IMAGE));
                    }
                    cell.getChildren().add(goalImageView);
                } else if (x == 1 && y == 13 && floor == 0) {
                    // １階(1,13)に上り階段の(画像)を表示
                    if (stairupImageView == null) {
                        stairupImageView = new ImageView(new Image(STAIR_UP_IMAGE));
                    }
                    mapGrid.add(stairupImageView, x, y);
                } else if (x == 1 && y == 13 && floor == 1) {
                    // ２階(1,13)に下り階段の(画像)を表示
                    if (stairdownImageView == null) {
                        stairdownImageView = new ImageView(new Image(STAIR_DOWN_IMAGE));
                    }
                    mapGrid.add(stairdownImageView, x, y);
                } else {
                    mapData.setImageViews();
                    mapImageViews[index] = mapData.getImageView(x, y);
                    cell.getChildren().add(mapImageViews[index]);
                }

                // 視界のマスクを配置
                if (Math.abs(cx - x) <= VISION_RADIUS && Math.abs(cy - y) <= VISION_RADIUS) {
                    // キャラクター周囲のマスクを外す
                    maskImageViews[index].setVisible(false);
                } 
                cell.getChildren().add(maskImageViews[index]);
                mapGrid.add(cell, x, y);
            }
        }
    }
    

    // set Ramdom Goal
    private int Gx, Gy;

    public void initialize() {
        // 通路マスの端をリスト化し、ゴール座標をランダムに設定
        setGoal();
    }
    
    private void setGoal() {
        List<int[]> edgeSpaces = new ArrayList<>();
        Random random = new Random();
    
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                if (mapData.getMap(x, y) != MapData.TYPE_WALL && isEdge(x, y)) {
                    // ゴール候補に追加する条件に x>7 と y>7 を追加
                    if (x>7 && y>7) {
                        edgeSpaces.add(new int[] { x, y });
                    }
                }
            }
        }
    
        // リストからランダムに選択
        if (!edgeSpaces.isEmpty()) {
            int[] goal = edgeSpaces.get(random.nextInt(edgeSpaces.size()));
            Gx = goal[0];
            Gy = goal[1];
            System.out.println("Goal set at: (" + Gx + ", " + Gy + ")");
        } else {
            System.err.println("No valid edge spaces found for goal.");
        }
    }    

    private boolean isEdge(int x, int y) {
        // マップの範囲外を除外
        if (x < 0 || x >= mapData.getWidth() || y < 0 || y >= mapData.getHeight()) {
            return false;
        }
    
        // 通路マスでないものを除外
        // if (mapData.getMap(x, y) != MapData.TYPE_SPACE) {
        //     return false;
        // }
    
        // 上下左右の隣接マスをカウント
        int adjacentSpaces = 0;
        if (y > 0 && mapData.getMap(x, y - 1) != MapData.TYPE_WALL) { // 上
            adjacentSpaces++;
        }
        if (y < mapData.getHeight() - 1 && mapData.getMap(x, y + 1) != MapData.TYPE_WALL) { // 下
            adjacentSpaces++;
        }
        if (x > 0 && mapData.getMap(x - 1, y) != MapData.TYPE_WALL) { // 左
            adjacentSpaces++;
        }
        if (x < mapData.getWidth() - 1 && mapData.getMap(x + 1, y) != MapData.TYPE_WALL) { // 右
            adjacentSpaces++;
        }
    
        // 隣接する通路マスが1つだけの場合
        return adjacentSpaces == 1;
    }    

    // Get Character's positions & Check Goal
    public void CheckPosition() {
        int Cx = chara.getPosX();
        int Cy = chara.getPosY();
        int map_type = mapData.getMap(Cx, Cy);

        if (Cx == Gx && Cy == Gy){
            getGoal();
        }
        
        StairCheck = true;
        if (Cx == 1 && Cy == 13) {
            saveMap(floor);
            floor = (floor + 1)%2 ;
            StairCheck = false;
            getStair(floor);
            chara = new MoveChara(1, 13, mapData);
            System.out.println("別の階に移動した!");
        }

        System.out.println("map_type:" + map_type);
        if (map_type == MapData.TYPE_COIN) {
            mapData.setMap(Cx, Cy, MapData.TYPE_SPACE);
            Item.Coin.taken();
        } else if (map_type == MapData.TYPE_FEATHER) {
            mapData.setMap(Cx, Cy, MapData.TYPE_SPACE);
            Item.Feather.taken(chara);
        }
        drawMap(chara, mapData);
    }

    public void saveMap(int floor) { 
        if(floor == 0) {
            First = mapData;
        } else {
            Second = mapData;
        }
    }

    public void getStair(int floor) {
        if (floor == 0) {
            mapData = First;
            drawMap(chara, mapData);
        } else {
            mapData = Second;
            drawMap(chara, mapData);
        }
    }
    public void getGoal() {
        Scene scene = mapGrid.getScene();
        if (scene != null) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEventFilter);
        }
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            try {
                StageDB.getMainStage().hide();
                StageDB.getMainSound().stop();
                StageDB.getClearStage().show();
                if (scene != null) {
                    scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyEventFilter);
                }
            } catch (Exception ex) {
                System.out.println("Error displaying clear stage: " + ex.getMessage());
            }
        });
        delay.play();
    }

    private final EventHandler<KeyEvent> keyEventFilter = event -> {
        // キー入力の無効化
        event.consume();
    };

    // Get users' key actions
    public void keyAction(KeyEvent event) {
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
        if (key == KeyCode.H) {
            leftButtonAction();
        } else if (key == KeyCode.J) {
            downButtonAction();
        } else if (key == KeyCode.K) {
            upButtonAction();
        } else if (key == KeyCode.L) {
            rightButtonAction();
        }
        CheckPosition();
    }

    // Operations for going the cat up
    public void upButtonAction() {
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        chara.move(0, -1);
        drawMap(chara, mapData);
    }

    // Operations for going the cat down
    public void downButtonAction() {
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        chara.move(0, 1);
        drawMap(chara, mapData);
    }

    // Operations for going the cat right
    public void leftButtonAction() {
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        chara.move(-1, 0);
        drawMap(chara, mapData);
    }

    // Operations for going the cat right
    public void rightButtonAction() {
        printAction("RIGHT");
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        chara.move(1, 0);
        drawMap(chara, mapData);
    }

    @FXML
    public void func1ButtonAction(ActionEvent event) {
        try {
            System.out.println("func1");
            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameOverStage().show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void func2ButtonAction(ActionEvent event) {
        initialize(null, null);
    }

    @FXML
    public void func3ButtonAction(ActionEvent event) {
        System.out.println("func3: Nothing to do");
    }

    @FXML
    public void func4ButtonAction(ActionEvent event) {
        try {
            System.out.println("func4");
            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getClearStage().show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

}
