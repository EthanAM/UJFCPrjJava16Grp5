package data;

import org.eclipse.jgit.lib.ObjectLoader;

public class Tag extends ObjetGit {

	public Tag(String nom, String chemin, String dossier, ObjectLoader loader) {
		super(nom, chemin, dossier);
		this.type = TypeObjet.Tag;
		this.donnees = new String(loader.getCachedBytes());
	}

}
