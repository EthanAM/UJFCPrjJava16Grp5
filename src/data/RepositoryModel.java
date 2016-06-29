package data;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.internal.storage.file.PackIndex.MutableEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

//Representation du repository, contient tous les objets trouves dans le dossier .git/object
public class RepositoryModel {
	private Repository repository;
	private Hashtable<String, ObjetGit> objectTable;

	// Construit un RepositoryModel a partir du dossier .git
	public RepositoryModel(String path) throws IOException {
		File directory = new File(path);
		RepositoryBuilder builder = new RepositoryBuilder();
		builder.setGitDir(directory.getParentFile());
		builder.setMustExist(true);
		this.repository = builder.build();
		objectTable = new Hashtable<>();

		for (File f : directory.listFiles()) {
			if (f.getName().equals("pack")) {
				traiterPack(f);
			} else if (!f.getName().equals("info")) {
				traiterDossier(f);
			}
		}
	}

	// Retourne l'objet correspondant au nom
	public ObjetGit getObject(String name) {
		return this.objectTable.get(name);
	}

	// Retourne tous les objets
	public Collection<ObjetGit> getAllObjects() {
		return objectTable.values();
	}

	// Traite les dossiers n'étants pas des packs
	private void traiterDossier(File fichier) {
		for (File f : fichier.listFiles()) {
			traiterObjet(f);
		}
	}

	// Traite les objets n'étants pas dans des packs
	private void traiterObjet(File fichier) {
		String nom = fichier.getParentFile().getName() + fichier.getName();
		String chemin = fichier.getAbsolutePath();
		String dossier = fichier.getParentFile().getName();
		ObjectLoader loader;
		try {
			loader = repository.open(repository.resolve(nom));
			objectTable.put(nom, ObjetGit.create(nom, chemin, dossier, loader));
		} catch (RevisionSyntaxException | TypeErrorException | IOException e) {
			System.err.println("Erreur lors de l'ouverture de l'objet " + nom);
		}
	}

	// Ajoute les objets presents dans des packs.
	private void traiterPack(File fichier) {
		for (File f : fichier.listFiles()) {
			if (f.getName().endsWith(".pack")) {
				// Pour chaque objet pack on cree un PackFile qui servira a
				// trouver les clefs de hash des objets git
				// presents dans le pack
				PackFile pack = new PackFile(f, 0);
				String dossier = f.getName().substring(0, f.getName().length() - 5);
				for (MutableEntry mutableEntry : pack) {
					// On ajoute chaque objet git trouve dans la Hashtable
					ObjectId id;
					try {
						id = repository.resolve(mutableEntry.toObjectId().getName());
						ObjectLoader loader = repository.open(id);
						String nom = mutableEntry.toObjectId().getName();
						String chemin = f.getAbsolutePath();
						objectTable.put(nom, ObjetGit.create(nom, chemin, dossier, loader));
					} catch (Exception e) {
						System.err.println(
								"Erreur lors de l'importation de l'objet : " + mutableEntry.toObjectId().getName());
					}
				}
			}
		}
	}
}
