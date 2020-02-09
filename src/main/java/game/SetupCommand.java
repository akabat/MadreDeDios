package game;

import java.util.regex.Pattern;

/**
 * L'enum regroupe tous les types de commandes possibles du fichier d'entrée.  
 * Chaque élément est associé au <code>Pattern</code> regex permettant de l'identifier.
 *  
 * @author Andrzej Kabat
 */
public enum SetupCommand {
    COMMENTAIRE("^#.*"),
    CARTE("^C - (?<longitude>\\d+) - (?<latitude>\\d+)\\s*$"),
    MONTAGNE("^M - (?<longitude>\\d+) - (?<latitude>\\d+)\\s*$"),
    TRESOR("^T - (?<longitude>\\d+) - (?<latitude>\\d+) - (?<gemsNb>\\d+)\\s*$"),
    AVENTURIER("^A - (?<name>\\w+) - (?<longitude>\\d+) - (?<latitude>\\d+) - (?<orientation>[NSEO]) - (?<movements>[AGD]+)\\s*$"),
    LIGNE_VIDE("^\\s*$");
    
    private Pattern regexPattern;
    
    private SetupCommand(String regex) {
        this.regexPattern = Pattern.compile(regex);
    }
    
    public Pattern getRegexPattern() {
        return regexPattern;
    }
    
}
