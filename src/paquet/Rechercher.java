package paquet;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
	public Rechercher() {
		super();
	}

	public void rechercheFichier(String directoryPath) throws IOException {
		File directory = new File(directoryPath);
		File[] fichier = directory.listFiles();
		//Instantiation d'un Repository qui servira à ouvrir les packs
		RepositoryBuilder builder = new RepositoryBuilder();
		builder.setGitDir(directory.getParentFile());
		Repository repository = builder.build();

		int aligneur = 0;

		for (int i = 0; i < fichier.length - 1; i++) {// -1 afin de ne pas prendre le dossier pack. On le traite à la fin.
			if (!fichier[i].getName().equals("info")){
				File[] interfichier = fichier[i].listFiles();// interfichier liste
				// les sous fichiers
				// de fichier
				tableur.setValueAt(fichier[i].getName(), aligneur, 0);

				for (int j = 0; j < interfichier.length; j++) {

					// String cleff=fichier[i].getName()+interfichier[j].getName();
					// Permet la concatenation du nom du dossier avec le(s)
					// fichier(s) qu'il contient(s)
					// jessai de faire une condition pour pouvoir prendre le pack et decompresser
					if(fichier[i].getName()=="pack"){
						System.out.println(fichier[i].getName());
						System.out.println("hola");

					}
					//System.out.println(fichier[i].getName());
					else{
						File file = new File(interfichier[j].getPath());

						tableur.setValueAt(interfichier[j].getName(), aligneur, 1);
						tableur.setValueAt(file, aligneur, 2);// permet de stocker le
						// chemin (utile pour la
						// partie information)

						// decompression et remplisage de la table (colone 2 et 3)
						/////////////////////////////////////////////////////////////////////
						FileInputStream fichier1 = new FileInputStream(file);

						InflaterInputStream decompresser = new InflaterInputStream(fichier1);

						ArrayList<Byte> LectureFichier = new ArrayList();
						int caract;

						try {
							while ((caract = decompresser.read()) != -1) {
								LectureFichier.add((byte) caract);
							}
						} catch (IOException e) {
							throw new IOException("fichier " + file.getName() + " : " + e.getMessage());
						}

						Byte[] coder = LectureFichier.toArray(new Byte[0]);
						StringBuilder content = new StringBuilder();

						int i1 = 0;
						char c;
						while (i1 < coder.length) {

							c = (char) coder[i1].byteValue();
							content.append(c);
							i1++;
						}

						String[] z = content.toString().split(" ");// permet de recuper
						// le premier mot
						// qui correspont au
						// type

						/////////////////////////////////////////////////////////////////
						tableur.setValueAt(z[0], aligneur, 3);
					}
					aligneur++;
				}
			}
			//Traitement du dossier pack
			for (File f : fichier[fichier.length - 1].listFiles()) {
				if(f.getName().endsWith(".pack")){
					//Pour chaque objet .pack on crée un PackFile qui servira à trouver les clefs de hash des objets git
					//présents dans le pack
					PackFile pack = new PackFile(f, 0);
					for(MutableEntry mutableEntry : pack){
						//On ajoute chaque objet git trouvé dans le tableau
						ObjectId id = repository.resolve(mutableEntry.toObjectId().getName());
						ObjectLoader loader = repository.open(id);
						switch (loader.getType()) {
						case Constants.OBJ_COMMIT:
							tableur.setValueAt("Commit", aligneur, 3);
							break;
						case Constants.OBJ_TREE:
							tableur.setValueAt("Tree", aligneur, 3);
							break;
						case Constants.OBJ_BLOB:
							tableur.setValueAt("Blob", aligneur, 3);
							break;
						}
						tableur.setValueAt(mutableEntry.toObjectId().getName(), aligneur, 1);
						tableur.setValueAt("pack", aligneur, 0);
						tableur.setValueAt(f.getAbsolutePath(), aligneur, 2);
						++ aligneur;
					}
				}
			}	
		}

		table.addMouseListener(new MouseAdapter() {

			// decompression egalement par rapport Ã  la colonne 2

			public void mouseClicked(MouseEvent event) {
				int ligne = event.getY() / 20;

				File file = (File) tableur.getValueAt(ligne, 2);

				FileInputStream fichier1 = null;
				try {
					fichier1 = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				InflaterInputStream decompresser = new InflaterInputStream(fichier1);

				ArrayList<Byte> LectureFichier = new ArrayList();
				int caract;

				try {
					while ((caract = decompresser.read()) != -1) {
						LectureFichier.add((byte) caract);
					}
				} catch (IOException e) {
					try {
						throw new IOException("fichier " + file.getName() + " : " + e.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				Byte[] coder = LectureFichier.toArray(new Byte[0]);
				StringBuilder content = new StringBuilder();

				int i1 = 0;
				char c;
				while (i1 < coder.length) {

					c = (char) coder[i1].byteValue();
					content.append(c);
					i1++;
				}

				//// FENETRE POP UP/////

				JOptionPane jop1;
				jop1 = new JOptionPane();
				jop1.showMessageDialog(null, content, "<<<Information>>>", JOptionPane.PLAIN_MESSAGE);

			}
		});

	}

}
