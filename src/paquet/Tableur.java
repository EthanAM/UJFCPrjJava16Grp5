package paquet;


import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Tableur extends JTable {
	DefaultTableModel tableur;

	public Tableur(DefaultTableModel model) {
		tableur = model;
		tableur.addColumn("DOSSIER");
		tableur.addColumn("FICHIER");
		tableur.addColumn("CLEF/CHEMIN");
		tableur.addColumn("TYPE");
		this.setModel(tableur);
		this.setPreferredScrollableViewportSize(new Dimension(840, 500));
		this.setRowHeight(20);
		this.setAutoCreateRowSorter(true);
	}

}
