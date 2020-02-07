package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

/**
 * 
 */
public class MadreDeDiosTest {

    /**
     */
    @Test
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
    
}
