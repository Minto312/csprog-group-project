import java.io.IOException;
import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ClearController {

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
