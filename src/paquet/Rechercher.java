package paquet;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Rechercher extends Tableur {
	RepositoryModel repository;
//model repository
	public Rechercher() {
		super();
	}

	public void rechercheFichier(String directoryPath) throws IOException {
		this.repository = new RepositoryModel(directoryPath);
		for (ObjetGit o : this.repository.getAllObjects()){
			Vector<String> rowData = new Vector<>();
			rowData.add(o.dossier);
			rowData.add(o.nom);
			rowData.add(o.chemin);
			rowData.add(o.type);
			tableur.addRow(rowData);
		}

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//On recupere les donnees de l'objet selectionne
				String nom = (String) getValueAt(getSelectedRow(), 1);
				ObjetGit objet = repository.getObject(nom);
				//// FENETRE POP UP/////
				if(objet != null){
					JOptionPane.showMessageDialog(null, objet.donnees, "<<<Information>>>", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
	}
}
