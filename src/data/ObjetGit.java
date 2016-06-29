package data;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectLoader;

//structure pour stoke les donnees	dun objet 
public abstract class ObjetGit {
	protected String nom;
	protected String chemin;
	protected TypeObjet type;
	protected String dossier;
	protected String donnees;

	public ObjetGit(String nom, String chemin, String dossier) {
		this.nom = nom;
		this.chemin = chemin;
		this.dossier = dossier;
	}

	public String getChemin() {
		return chemin;
	}

	public String getDonnees() {
		return donnees;
	}

	public String getDossier() {
		return dossier;
	}

	public String getNom() {
		return nom;
	}

	public TypeObjet getType() {
		return type;
	}

	// Renvoie le bon objet git en fonction du loader passe en argument.
	public static ObjetGit create(String nom, String chemin, String dossier, ObjectLoader loader)
			throws TypeErrorException {
		switch (loader.getType()) {
		case Constants.OBJ_BLOB:
			return new Blob(nom, chemin, dossier, loader);
		case Constants.OBJ_COMMIT:
			return new Commit(nom, chemin, dossier, loader);
		case Constants.OBJ_TAG:
			return new Tag(nom, chemin, dossier, loader);
		case Constants.OBJ_TREE:
			return new Tree(nom, chemin, dossier, loader);
		default:
			throw new TypeErrorException();
		}
	}
}
