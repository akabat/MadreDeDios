package game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * La classe propose quelque scénarii de test permettant de valider le bon fonctionnement du jeu "La carte au trésor".
 * 
 * @author Andrzej Kabat
 */
public class MadreDeDiosTest {

    /**
     * Test scénario 1 : celui, présenté dans la description d'objectif de l'exercice.
     */
    @Test
    public void givenTestScenario1FromInputFile_afterGameExecution_expectedReferenceOutputFileEqualsResultingOutputFile() {
        
        MadreDeDios game = new MadreDeDios();
        game.init("./testResources/test1Input.txt");
        game.play();
        game.persistResult("./testResources/test1Output.txt");

        assertTrue(compareFiles("./testResources/test1ReferenceOutput.txt", "./testResources/test1Output.txt"));
    }

    /**
     * Test scénario 2 : impossible de traverser les montagnes, sortir en déhors de la carte.
     */
    @Test
    public void givenTestScenario2FromInputFile_afterGameExecution_expectedReferenceOutputFileEqualsResultingOutputFile() {
        
        MadreDeDios game = new MadreDeDios();
        game.init("./testResources/test2Input.txt");
        game.play();
        game.persistResult("./testResources/test2Output.txt");

        assertTrue(compareFiles("./testResources/test2ReferenceOutput.txt", "./testResources/test2Output.txt"));
    }
    
    /**
     * Test scénario 3 : 
     * <ol>
     *  <li>les avanturiers bougent en ordre selon lequel ils ont été ajoutés,</li>
     *  <li>un champs occupé par un avanturier est innaccessible à un autre,</li>
     *  <li>à la sortie, les éléments de la carte sont groupés (Montagnes / Trésors / Avanturier), ensuite arrangés en ordre selon leurs positions.</li>
     * </ol>
     */
    @Test
    public void givenTestScenario3FromInputFile_afterGameExecution_expectedReferenceOutputFileEqualsResultingOutputFile() {
        
        MadreDeDios game = new MadreDeDios();
        game.init("./testResources/test3Input.txt");
        game.play();
        game.persistResult("./testResources/test3Output.txt");

        assertTrue(compareFiles("./testResources/test3ReferenceOutput.txt", "./testResources/test3Output.txt"));
    }
    
    /**
     * La méthode permet de comparer deux fichiers texte encodés en UTF-8. 
     * Si leurs contenu (comparé ligne par ligne) est identique, la méthode returne <code>true</code>, sinon <code>false</code>. 
     */
    private boolean compareFiles(String referenceFile, String examinedFile) {
        
        try(BufferedReader reader1 = new BufferedReader(new FileReader(referenceFile)); 
            BufferedReader reader2 = new BufferedReader(new FileReader(examinedFile)) ) {
            
            String str1 = reader1.readLine();
            String str2 = reader2.readLine();
            
            while(str1 != null && str2 != null) {
                if(!str1.equals(str2)) {
                    System.out.println(str1 + " " + str2);
                    return false;
                }
                str1 = reader1.readLine();
                str2 = reader2.readLine();
            }
            if(str1 != null || str2 != null) {
                System.out.println(str1 + " " + str2);
                return false;
            }
            
        } catch (FileNotFoundException e) {
            System.err.format("Au moins un des fichiers d'entré n'existe pas : %1$s, %2$s%n", referenceFile, examinedFile);
            return false;
        } catch (IOException e) {
            System.err.format("Au moins un des fichiers d'entré est inaccessible : %1$s, %2$s%n", referenceFile, examinedFile);
            return false;
        }
        
        return true;
    }
    
}
