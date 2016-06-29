package ihm;

import java.io.File;
import java.io.IOException;

import data.ObjetGit;
import data.RepositoryModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

//Gère la fenetre principale
public class FenPrem extends BorderPane {
	ImageView fondFenetre = new ImageView("file:FondFenetre.png");
	RepositoryModel repository;
	SelectionModel<ObjetGit> selectionModel;

	public FenPrem() {
		initMenuBar();
		this.setCenter(fondFenetre);
	}

	// Crée la barre de menu
	private void initMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fichier = new Menu("Fichier");
		MenuItem ouvrir = new MenuItem("ouvrir");
		ouvrir.setOnAction(e -> ouvrir());
		fichier.getItems().add(ouvrir);
		menuBar.getMenus().add(fichier);
		this.setTop(menuBar);
	}

	// Action lorsqu'on clique sur ouvrir
	private void ouvrir() {
		DirectoryChooser chooser = new DirectoryChooser();
		File f = chooser.showDialog(this.getScene().getWindow());
		if (f == null) {
			return;
		}
		if (f.getName().equals(".git")) {
			f = new File(f, "objects");
		} else {
			f = new File(new File(f, ".git"), "objects");
		}
		if (!f.exists()) {
			Alert alert = new Alert(AlertType.ERROR,
					"Impossible de trouver le dossier .git/objects. Assurez-vous de sélectionner le dossier .git ou bien le dossier contenant .git");
			alert.showAndWait();
			return;
		}
		try {
			this.repository = new RepositoryModel(f.getAbsolutePath());
			ListPane listPane = new ListPane(this.repository);
			this.selectionModel = listPane.getSelectionModel();
			ScrollPane s = new ScrollPane(new DonneePane(this.selectionModel, this.repository));
			SplitPane split = new SplitPane(listPane, s);
			split.setDividerPosition(0, 0.65);
			this.setCenter(split);
		} catch (IOException e) {
			final Alert alert = new Alert(AlertType.ERROR, "Impossible d'ouvrir le Repository", ButtonType.OK);
			alert.showAndWait();
		}
	}
}
