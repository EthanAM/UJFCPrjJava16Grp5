package ihm;

import data.ObjetGit;
import data.RepositoryModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ListPane extends AnchorPane {

	private RepositoryModel repository;
	private SelectionModel<ObjetGit> selectionModel;
	private TextField fieldRecherche = new TextField();
	private TableView<ObjetGit> objectTable = new TableView<>();
	private ObservableList<ObjetGit> objectList = FXCollections.observableArrayList();

	public ListPane(RepositoryModel repository) {
		this.repository = repository;
		this.objectList.addAll(this.repository.getAllObjects());
		initBarreRecherche();
		initObjectTable();

	}

	// cree la barre de recherche
	private void initBarreRecherche() {
		Label rechercheLabel = new Label("Rechercher : ");
		this.getChildren().addAll(rechercheLabel, fieldRecherche);
		AnchorPane.setTopAnchor(rechercheLabel, 10d);
		AnchorPane.setLeftAnchor(rechercheLabel, 5d);
		AnchorPane.setTopAnchor(fieldRecherche, 5d);
		AnchorPane.setLeftAnchor(fieldRecherche, 100d);
	}

	// Cree la TableView Affichant les Objets
	private void initObjectTable() {
		FilteredList<ObjetGit> filteredList = new FilteredList<>(this.objectList, p -> true);
		SortedList<ObjetGit> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(objectTable.comparatorProperty());
		objectTable.setItems(sortedList);
		this.selectionModel = objectTable.getSelectionModel();

		fieldRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(o -> o.getNom().startsWith(newValue));
		});
		TableColumn<ObjetGit, String> colonneDossier = new TableColumn<>("Dossier");
		TableColumn<ObjetGit, String> colonneNom = new TableColumn<>("Nom");
		TableColumn<ObjetGit, String> colonneType = new TableColumn<>("Type");

		colonneDossier.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDossier()));
		colonneNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		colonneType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
		objectTable.getColumns().add(colonneDossier);
		objectTable.getColumns().add(colonneNom);
		objectTable.getColumns().add(colonneType);

		this.getChildren().add(objectTable);
		AnchorPane.setTopAnchor(objectTable, 40d);
		AnchorPane.setLeftAnchor(objectTable, 5d);
		AnchorPane.setRightAnchor(objectTable, 5d);
		AnchorPane.setBottomAnchor(objectTable, 5d);
	}

	public SelectionModel<ObjetGit> getSelectionModel() {
		return this.selectionModel;
	}
}
