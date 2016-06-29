package data;

import org.eclipse.jgit.lib.ObjectLoader;

public class Commit extends ObjetGit {

	public Commit(String nom, String chemin, String dossier, ObjectLoader loader) {
		super(nom, chemin, dossier);
		this.type = TypeObjet.Commit;
		this.donnees = new String(loader.getCachedBytes());
	}

}
