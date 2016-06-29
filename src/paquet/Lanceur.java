package paquet;

import ihm.FenPrem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Lanceur extends Application {

	private Stage primaryStage;
	private BorderPane mainWindow;
	static private Scene scene;

	public Lanceur() {

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Projet Git");
		this.primaryStage.show();

		initMainWindow();
	}

	public void initMainWindow() {
		mainWindow = new FenPrem();
		scene = new Scene(mainWindow, 1200, 600);
		scene.getStylesheets().add(FenPrem.class.getResource("css.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
