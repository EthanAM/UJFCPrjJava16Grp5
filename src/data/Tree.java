package data;

import org.eclipse.jgit.lib.ObjectLoader;

public class Tree extends ObjetGit {

	public Tree(String nom, String chemin, String dossier, ObjectLoader loader) {
		super(nom, chemin, dossier);
		this.type = TypeObjet.Tree;
		this.traduitDonnees(loader.getCachedBytes());
	}

	// Rend les donnees du tree lisibles
	private void traduitDonnees(byte[] b) {
		int i = 0;
		StringBuilder donnees = new StringBuilder();
		while (i < b.length) {
			char c;
			// Le caractere precendent une clef de hash est : '\0'
			while ((c = (char) b[i]) != '\0') {
				donnees.append(c);
				++i;
			}
			donnees.append(" ");
			++i;

			// Traduit la clef de hash
			for (int j = 0; j < 20; j++) {
				String s = Integer.toHexString(b[i]);
				if (s.length() > 2) {
					s = s.substring(s.length() - 2);
				} else if (s.length() == 1) {
					s = "0" + s;
				}
				donnees.append(s);
				++i;
			}
			donnees.append("\n");
		}
		this.donnees = donnees.toString();
	}
}
