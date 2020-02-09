package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 
 */
public class MadreDeDiosTest {

    /**
     */
    @Test
    @Disabled
    public void givenTestScenarioInString_afterGameExecution_expectedStringEqualsResultingString() {
        
        Deque<String> testScenario = new LinkedList<>();
        testScenario.addAll(Arrays.asList( new String[] {"C - 3 - 4", "M - 1 - 0", "M - 2 - 1", "T - 0 - 3 - 2", "T - 1 - 3 - 3", "A - Lara - 1 - 1 - S - AADADAGGA"} ));
        
        StringBuilder referenceStringBuilder = new StringBuilder();
        referenceStringBuilder.append(String.format("C - 3 - 4%n"));
        referenceStringBuilder.append(String.format("M - 1 - 0%n"));
        referenceStringBuilder.append(String.format("M - 2 - 1%n"));
        referenceStringBuilder.append(String.format("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n"));
        referenceStringBuilder.append(String.format("T - 1 - 3 - 2%n"));
        referenceStringBuilder.append(String.format("# {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}%n"));
        referenceStringBuilder.append(String.format("A - Lara - 0 - 3 - S - 3%n"));
        String referenceString = referenceStringBuilder.toString();
        
        MadreDeDios game = new MadreDeDios();
        while(!testScenario.isEmpty()) {
            game.initStep(testScenario.removeFirst());
        }
        game.play();

        String resultingString = game.getTravelerMap().toString();

        assertEquals(referenceString, resultingString);
    }
    
    /**
     * Test scénario 1 : celui présenté dans la description d'objectif de l'exercice.
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
     * La méthode compare deux fichiers texte : file1 et file2. Si leurs contenu (comparé ligne par ligne) est identique, la méthode returne "true", sinon, "false". 
     */
    private boolean compareFiles(String file1, String file2) {
        
        try(BufferedReader reader1 = new BufferedReader(new FileReader(file1)); 
            BufferedReader reader2 = new BufferedReader(new FileReader(file2)) ) {
            
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
            System.err.format("Au moins un des fichiers d'entré n'existe pas : %1$s, %2$s%n", file1, file2);
            return false;
        } catch (IOException e) {
            System.err.format("Au moins un des fichiers d'entré est inaccessible : %1$s, %2$s%n", file1, file2);
            return false;
        }
        
        return true;
    }
    
}
