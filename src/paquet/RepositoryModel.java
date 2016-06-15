package paquet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.zip.InflaterInputStream;

import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.internal.storage.file.PackIndex.MutableEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

//Representation du repository, contient tous les objets trouves dans le dossier .git/object
public class RepositoryModel {
	private Repository repository;
	private Hashtable<String,ObjetGit> objectTable;

	//Construit un RepositoryModel a partir du dossier .git
	public RepositoryModel(String path) {
		File directory = new File(path);
		RepositoryBuilder builder = new RepositoryBuilder();
		builder.setGitDir(directory.getParentFile());
		builder.setMustExist(true);
		try {
			this.repository = builder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		objectTable = new Hashtable<>();

		for(File f : directory.listFiles()){
			if(f.getName().equals("pack")){
				traiterPack(f);
			}else if (!f.getName().equals("info")){
				traiterDossier(f);
			}
		}
	}
	
	//Retourne l'objet correspondant au nom
	public ObjetGit getObject(String name){
		return this.objectTable.get(name);
	}
	
	//Retourne tous les objets
	public Collection<ObjetGit> getAllObjects(){
		return objectTable.values();
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
		objectTable.put(nom,  new ObjetGit(nom, chemin, type, dossier, donnees));
	}

	private void traiterPack(File fichier){
		for (File f : fichier.listFiles()) {
			if(f.getName().endsWith(".pack")){
				//Pour chaque objet .pack on cree un PackFile qui servira a trouver les clefs de hash des objets git
				//presents dans le pack
				PackFile pack = new PackFile(f, 0);
				String dossier = f.getName().substring(0, f.getName().length()-5);
				for(MutableEntry mutableEntry : pack){
					//On ajoute chaque objet git trouve dans la Hashtable
					ObjectId id;
					try {
						id = repository.resolve(mutableEntry.toObjectId().getName());
						ObjectLoader loader = repository.open(id);
						String nom = mutableEntry.toObjectId().getName();
						String chemin = f.getAbsolutePath();	
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
						objectTable.put(nom, new ObjetGit(nom, chemin, type, dossier, donnees));
					} catch (Exception e) {
						System.out.println("Erreur lors de l'importation de l'objet : " + mutableEntry.toObjectId().getName());
					}
				}
			}
		}	
	}
}
