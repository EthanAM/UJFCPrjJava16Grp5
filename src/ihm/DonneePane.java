package ihm;

import data.ObjetGit;
import data.RepositoryModel;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

//Panneau gérant l'affichage des données brutes.
public class DonneePane extends TextFlow {
	private SelectionModel<ObjetGit> selectionModel;
	private RepositoryModel repository;
	private EventHandler<MouseEvent> clickOnTreeHandler = e -> selectionModel
			.select(repository.getObject(((Text) e.getSource()).getText()));

	public DonneePane(SelectionModel<ObjetGit> selectionModel, RepositoryModel repository) {
		this.selectionModel = selectionModel;
		this.repository = repository;
		this.selectionModel.selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> updateText(newValue));
	}

	private void updateText(ObjetGit objet) {
		if (objet == null) {
			return;
		}
		this.getChildren().clear();
		switch (objet.getType()) {
		case Tree:
			// sépare chaque ligne
			for (String lig : objet.getDonnees().split("\n")) {
				// sépare chaque mot
				String[] s = lig.split(" ");
				// Affiche les premiers mots normalement
				for (int i = 0; i < s.length - 1; i++) {
					this.getChildren().add(new Text(s[i] + " "));
				}
				// Le dernier mot est la clef
				this.getChildren().add(this.createClickableText(s[s.length - 1]));
				// Impossible de mettre le retour à la ligne dans le Text
				// precedent si on veut l'utiliser pour retrouver l'objet dans
				// la table de hash
				this.getChildren().add(new Text("\n"));
			}
			break;

		case Commit:
			// sépare chaque ligne
			String[] lig = objet.getDonnees().split("\n");
			// Les deux premieres lignes pointent vers d'autres objets
			for (int i = 0; i <= 1; i++) {
				if (lig[i].startsWith("tree") || lig[i].startsWith("parent")) {
					String[] s = lig[i].split(" ");
					this.getChildren().add(new Text(s[0] + " "));
					this.getChildren().add(createClickableText(s[1]));
					this.getChildren().add(new Text("\n"));
				}
			}
			for (int i = 2; i < lig.length; i++) {
				this.getChildren().add(new Text(lig[i] + "\n"));
			}
			break;

		default:
			this.getChildren().add(new Text(objet.getDonnees()));
			break;
		}
	}

	// crée un Text qui pointe vers un autre objet git
	private Text createClickableText(String s) {
		Text t = new Text(s);
		t.setOnMouseClicked(clickOnTreeHandler);
		t.setOnMouseEntered(e -> this.getScene().setCursor(Cursor.HAND));
		t.setOnMouseExited(e -> this.getScene().setCursor(Cursor.DEFAULT));
		t.getStyleClass().add("clickable-text");
		return t;
	}
}
