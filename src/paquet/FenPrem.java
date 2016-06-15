package paquet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

public class FenPrem extends JFrame {

	private JMenuBar menuBar = new JMenuBar();
	private JMenu JFichier = new JMenu("Fichier");

	private JMenuItem IOuvrir = new JMenuItem("Ouvrir");
	private JMenuItem IQuitter = new JMenuItem("Quitter");

	public FenPrem() {

		JFichier.add(IOuvrir);
		menuBar.add(JFichier);
		setJMenuBar(menuBar);

		this.JFichier.add(IQuitter);

		IOuvrir.addActionListener(new ActionListener() {

			// permet l'ouverture d'un fichier mais avec Windows car méthode
			// differente
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser dialogue = new JFileChooser(new File("."));
				// FIXME: voici comment sélectionner un répertoire.
				// c'était à votre portée.
				dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				File fichier = null;

				if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fichier = dialogue.getSelectedFile();
					Rechercher finder;
					try {
						File objDir = new File(new File(fichier, ".git"), "objects");
						if (objDir.exists() && objDir.isDirectory()) {
							finder = new Rechercher();
							finder.rechercheFichier(objDir.getAbsolutePath());
							setContentPane(new JScrollPane(finder));//
							pack();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		IQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});
		setSize(860, 560);
		setLocation(300, 100);
		setTitle("Projet Git");
		setContentPane(new AfficheImage("FondFenetre.png"));//changement des couleurs png
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
