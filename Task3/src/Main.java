
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(getMainPane());
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	Pane getMainPane() {
		BorderPane pane = new BorderPane();
		pane.setLeft(leftPane());
		return pane;
	}

	Pane leftPane() {
		VBox v = new VBox();

		v.setStyle("background:'red");

		Text name = new Text("Carl");
		v.getChildren().add(name);
		return v;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
