package carbon.exercice.pratique;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import carbon.execrice.pratique.MadreDeDios;

/**
 * 
 */
public class MadreDeDiosTest {

    /**
     */
    @Test
    public void testApp() {
        Deque<String> testScenario = new LinkedList<>();
        testScenario.addAll(Arrays.asList( new String[] {"C - 3 - 4", "M - 1 - 0", "M - 2 - 1", "T - 0 - 3 - 2", "T - 1 - 3 - 3", "A - Lara - 1 - 1 - S - AADADAGGA"} ));
        
        MadreDeDios mdd = new MadreDeDios();
        while(!testScenario.isEmpty()) {
            mdd.initStep(testScenario.removeFirst());
        }
        mdd.play();

        String result = mdd.getTravelerMap().toString();
        
        StringBuilder refResultBuilder = new StringBuilder();
        refResultBuilder.append(String.format("C - 3 - 4%n"));
        refResultBuilder.append(String.format("M - 1 - 0%n"));
        refResultBuilder.append(String.format("M - 2 - 1%n"));
        refResultBuilder.append(String.format("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n"));
        refResultBuilder.append(String.format("T - 1 - 3 - 2%n"));
        refResultBuilder.append(String.format("# {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}%n"));
        refResultBuilder.append(String.format("A - Lara - 0 - 3 - S - 3%n"));
        String referenceResult = refResultBuilder.toString();

        assertEquals(result, referenceResult, result);
    }
}
