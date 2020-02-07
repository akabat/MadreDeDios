package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private TravelerMap playground = null;
    private Set<Player> players = null;
    
    /**
     *  
     */
    public static void main( String[] args ) {
        if(args.length < 2) {
            throw new InputMismatchException("Le jeu Madre de Dios prend deux arguments :\n"
                + "\t1) le fichier d'entrée contenant le scénario,\n"
                + "\t2) le fichier de sortie.");
        }
        
        MadreDeDios game = new MadreDeDios();
        game.init(args[0]);
        game.play();
        game.finalize(args[1]);
    }
    
    public MadreDeDios() {
        this.players = new HashSet<>();
    }

    public void init(String inputFile) {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String inputLine = reader.readLine();
            while( inputLine != null) {
                initStep( inputLine );
                inputLine = reader.readLine();
            } 
        } catch (FileNotFoundException e) {
            System.out.println("Fichier d'entré inexistant");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Fichier d'entré inaccessible");
            System.exit(1);
        }
    }
    
    public void finalize(String outputFile) {
        
        playground.writeToFile(outputFile);
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
            throw new InputMismatchException("Fichie d'entrée corrompu, commande non reconnue : " + inputLine);
        }

        switch(cmd) {
        case COMMENTAIRE: return;
        case CARTE:
            if(this.playground != null) {
                throw new InputMismatchException("Fichie d'entrée corrompu : commande de création carte doupliquée");
            }
            this.playground = new TravelerMap( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")) );
            return;
        case MONTAGNE:
            if(this.playground == null) {
                throw new InputMismatchException("Fichie d'entrée corrompu : ajout d'une montagne à la carte inexistante");
            }
            this.playground.setupMountain( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")) );
            return;
        case TRESOR:
            if(this.playground == null) {
                throw new InputMismatchException("Fichie d'entrée corrompu : ajout d'un tresor à la carte inexistante");
            }
            this.playground.setupPlainWithGems( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")), Integer.parseInt(mtchr.group("gemsNb")));
            return;
        case AVENTURIER:
            if(this.playground == null) {
                throw new InputMismatchException("Fichie d'entrée corrompu : ajout d'un aventurier à la carte inexistante");
            }
            Coordinates poz = new Coordinates( Integer.parseInt(mtchr.group("longitude")), Integer.parseInt(mtchr.group("latitude")) );
            List<MoveType> movements = Arrays.stream( mtchr.group("movements").split("") ).map( MoveType::valueOf ).collect( Collectors.toList() );
            Player player = new PeacefulTourist( mtchr.group("name"), poz, Orientation.valueOf(mtchr.group("orientation")),  movements);
            this.playground.setupPlayer(poz, player);
            players.add(player);
            return;
        case LIGNE_VIDE: 
        default:
            return;
        }
    }

    public void play() {

        boolean anyPlayerStillCanMove;
        do {
            anyPlayerStillCanMove = false;
            for(Player t : players) {
                anyPlayerStillCanMove = anyPlayerStillCanMove || t.move();
            }
        } while(anyPlayerStillCanMove);
    }
    
    public TravelerMap getTravelerMap() {
        return playground;
    } 
}
