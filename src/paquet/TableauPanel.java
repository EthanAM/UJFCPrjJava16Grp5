package paquet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


//Panel Affichant le tableau et la barre de recherche
public class TableauPanel extends JPanel{
	Rechercher table;
	JTextField fieldRechercher;
	
	public TableauPanel(String directoryPath) throws IOException {
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		//Ajout du tableau dans la fenetre
		table = new Rechercher(directoryPath);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane,BorderLayout.CENTER);
		
		//Ajout du champ de recherche
		JPanel form = new JPanel();
		form.setPreferredSize(new Dimension(200, 30));
		
		form.setLayout(new BoxLayout(form,BoxLayout.X_AXIS));
		JLabel labelRechercher = new JLabel("Rechercher :");
		form.add(labelRechercher);
		fieldRechercher = new JTextField();
		fieldRechercher.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        filtrer();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        filtrer();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        filtrer();
                    }
                });
		form.add(fieldRechercher);
		add(form,BorderLayout.SOUTH);
		
	}
	
	//Modifie le filtre du tableau
	private void filtrer(){
		this.table.setFilter( o -> o.getNom().startsWith(fieldRechercher.getText()));//lamda :p
	}
}
