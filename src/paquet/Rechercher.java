package paquet;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import data.ObjetGit;
import data.RepositoryModel;

public class Rechercher extends Tableur {
	RepositoryModel repository;
	List<ObjetGit> objetListe = new ArrayList<>();
	Predicate<ObjetGit> filtre = o -> true;

	public Rechercher(String directoryPath) throws IOException {
		super(new DefaultTableModel());
		this.rechercheFichier(directoryPath);
	}

	//Cree un nouveau RepositoryModel et recupere la liste des objets git trouves
	private void rechercheFichier(String directoryPath) throws IOException {
		this.repository = new RepositoryModel(directoryPath);
		objetListe.addAll(this.repository.getAllObjects());
		this.update();

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//On recupere les donnees de l'objet selectionne
				String nom = (String) getValueAt(getSelectedRow(), 1);
				ObjetGit objet = repository.getObject(nom);
				//// FENETRE POP UP/////
				if(objet != null){
					JOptionPane.showMessageDialog(null, objet.getDonnees(), "<<<Information>>>", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
	}
	
	//Mets a jour la liste des objets affiches en tenant compte de predicat filtre
	private void update(){
		tableur.setRowCount(0);
		for (ObjetGit o : this.objetListe){
			if (filtre.test(o)){
				Vector<String> rowData = new Vector<>();
				rowData.add(o.getDossier());
				rowData.add(o.getNom());
				rowData.add(o.getChemin());
				rowData.add(o.getType().toString());
				tableur.addRow(rowData);
			}
		}
	}

	//setter pour le filtre
	public void setFilter(Predicate<ObjetGit> p){
		this.filtre = p;
		this.update();
	}
}
