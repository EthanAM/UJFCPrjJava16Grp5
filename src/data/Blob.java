package data;

import org.eclipse.jgit.lib.ObjectLoader;

public class Blob extends ObjetGit {
	public Blob(String nom, String chemin, String dossier, ObjectLoader loader) {
		super(nom, chemin, dossier);
		this.type = TypeObjet.Blob;
		this.donnees = new String(loader.getCachedBytes());
	}
}
