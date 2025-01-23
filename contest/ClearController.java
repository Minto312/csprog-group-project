import java.io.IOException;
import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class ClearController {

	@FXML
	private Label coin_count;

	public void setStage(Stage stage) {
		stage.setOnShown(event -> {
			System.out.println("Item.Coin.getCount() = " + Item.Coin.getCount());
			coin_count.setText("コインを" + Item.Coin.getCount() + "枚ゲットしました！");
		});
	}

	@FXML
	void onClearAction(ActionEvent event) {
		try {
			StageDB.getClearStage().hide();
			StageDB.getMainSound().stop();
			StageDB.getMainStage().show();
			StageDB.getMainSound().play();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
