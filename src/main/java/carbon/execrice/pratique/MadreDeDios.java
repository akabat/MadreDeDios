package carbon.execrice.pratique;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 
 */
public class MadreDeDios {
    private TravelerMap travelerMap = null;
    private Set<Traveler> travelers = null;
    
    public static void main( String[] args ) {
        if(args.length < 2) {
            throw new InputMismatchException();
        }
        MadreDeDios mdd = new MadreDeDios();
        mdd.input(args[0]);
        mdd.play();
        mdd.output(args[1]);
    }
    
    public MadreDeDios() {
        this.travelers = new HashSet<>();
    }

    public void input(String inputFile) {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String inputLine = reader.readLine();
            while( inputLine != null) {
                initStep( inputLine );
                inputLine = reader.readLine();
            } 
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void output(String outputFile) {
//        System.out.println( map.toString() );
        try {
            Files.write( Paths.get(outputFile), travelerMap.toString().getBytes() );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initStep(String inputLine) {
        Pattern ptrn = null;
        Matcher mtchr = null;
        SetupCommand cmd = null;

        for(SetupCommand sc : SetupCommand.values()) {
            ptrn = sc.getRegexPattern();
            mtchr = ptrn.matcher(inputLine);
            if(mtchr.find()) {
                cmd = sc;
                break;
            }
        }

        if(cmd == null) {
            throw new InputMismatchException("input line: " + inputLine);
        }

        switch(cmd) {
        case COMMENTAIRE: return;
        case CARTE:
            if(this.travelerMap != null) {
                throw new InputMismatchException();
            }
            this.travelerMap = new TravelerMap( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")) );
            return;
        case MONTAGNE: 
            this.travelerMap.setupMountain( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")) );
            return;
        case TRESOR:
            this.travelerMap.setupGems( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")), Integer.parseInt(mtchr.group("gemsNb")));
            return;
        case AVENTURIER:
            Position poz = new Position( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")) );
            List<Move> movements = Arrays.stream( mtchr.group("movements").split("") ).map( Move::valueOf ).collect( Collectors.toList() );
            Traveler t = new Traveler( mtchr.group("name"), poz, Orientation.valueOf(mtchr.group("orientation")),  movements);
            this.travelerMap.setupTraveler(poz, t);
            travelers.add(t);
            return;
        case LIGNE_VIDE: 
            return;
        default:
            throw new InputMismatchException();
        }
    }

    public void play() {

        boolean anyTravelerStillCanMove;
        do {
            anyTravelerStillCanMove = false;
            for(Traveler t : travelers) {
                anyTravelerStillCanMove = anyTravelerStillCanMove || t.move();
            }
        } while(anyTravelerStillCanMove);
    }
    
    public TravelerMap getTravelerMap() {
        return travelerMap;
    } 
}
