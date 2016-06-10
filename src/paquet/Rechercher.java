package paquet;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.InflaterInputStream;

import javax.swing.JOptionPane;


import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.internal.storage.file.PackIndex.MutableEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;;

public class Rechercher extends Tableur {
	private Repository repository;
	private List<ObjetGit>listeObjet = new ArrayList<>();

	public Rechercher() {
		super();
	}

	public void rechercheFichier(String directoryPath) throws IOException {
		File directory = new File(directoryPath);
		File[] fichier = directory.listFiles();
		//Instantiation d'un Repository qui servira à ouvrir les packs
		RepositoryBuilder builder = new RepositoryBuilder();
		builder.setGitDir(directory.getParentFile());
		repository = builder.build();

		for (int i = 0; i < fichier.length ; i++) {
			if(fichier[i].getName().equals("pack")){
				traiterPack(fichier[i]);
			}else if (!fichier[i].getName().equals("info")){
				traiterDossier(fichier[i]);
			}
		}
		for (ObjetGit o : listeObjet){
			Vector<String> rowData = new Vector<>();
			rowData.add(o.dossier);
			rowData.add(o.nom);
			rowData.add(o.chemin);
			rowData.add(o.type);
			tableur.addRow(rowData);
		}

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//On recupere les donnees de l'objet selectionne
				String nom = (String) table.getValueAt(table.getSelectedRow(), 1);
				ObjetGit objet = null;
				for(ObjetGit o : listeObjet){
					if(o.nom.equals(nom)){
						objet = o;
					}
				}
				//// FENETRE POP UP/////
				if(objet != null){
					JOptionPane.showMessageDialog(null, objet.donnees, "<<<Information>>>", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
	}

	//Traite les dossiers n'étants pas des packs
	private void traiterDossier(File fichier){
		for(File f : fichier.listFiles()){
			traiterObjet(f);
		}
	}

	//Traite les objets n'étants pas dans des packs
	private void traiterObjet(File fichier) {
		String nom = fichier.getName();
		String chemin = fichier.getAbsolutePath();
		String dossier = fichier.getParentFile().getName();
		StringBuilder content = new StringBuilder();
		//decompression du fichier :
		try {
			FileInputStream in = new FileInputStream(fichier);
			InflaterInputStream decompresser = new InflaterInputStream(in);
			int caract;
			while((caract = decompresser.read()) != -1){
				content.append((char) caract);
			}
			decompresser.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String donnees = content.toString();
		String type = donnees.split(" ")[0];//permet de recuperer le premier mot qui correspond au type
		listeObjet.add( new ObjetGit(nom, chemin, type, dossier, donnees));
	}

	//Traite les fichiers pack contenus dans le dossier pack
	private void traiterPack(File fichier){
		for (File f : fichier.listFiles()) {
			if(f.getName().endsWith(".pack")){
				//Pour chaque objet .pack on cr�e un PackFile qui servira � trouver les clefs de hash des objets git
				//pr�sents dans le pack
				PackFile pack = new PackFile(f, 0);
				for(MutableEntry mutableEntry : pack){
					//On ajoute chaque objet git trouv� dans le tableau
					ObjectId id;
					try {
						id = repository.resolve(mutableEntry.toObjectId().getName());
						ObjectLoader loader = repository.open(id);
						String nom = mutableEntry.toObjectId().getName();
						String chemin = f.getAbsolutePath();
						String dossier = "pack";
						String type = "";
						switch (loader.getType()) {
						case Constants.OBJ_COMMIT:
							type = "Commit";
							break;
						case Constants.OBJ_TREE:
							type = "Tree";
							break;
						case Constants.OBJ_BLOB:
							type = "Blob";
							break;
						}
						String donnees = new String(loader.getCachedBytes());
						listeObjet.add(new ObjetGit(nom, chemin, type, dossier, donnees));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}	
	}
}
