package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import game.map.TravelerMap;
import game.player.MoveType;
import game.player.PeacefulTourist;
import game.player.Player;
import game.position.Coordinates;
import game.position.Orientation;

/**
 * La classe principale du jeu <b>"La carte au trésor"</b>.
 * Le jeu est composé du terrain de jeu (<code>playground</code> de classe {@link TravelerMap}) et de l'ensemble de joueurs (<code>players</code> de classe {@link Player}).
 * Les méthodes de cette classe permettent : 
 * <ul>
 *      <li>l'initialisation du jeu (lecture du scénario à partir d'un fichier) : {@link MadreDeDios#init(String)}</li>
 *      <li>l'execution du scénario : {@link MadreDeDios#play()},</li>
 *      <li>la sauvegarde du scénario dans le fichier de sortie : {@link MadreDeDios#persistResult(String)}.</li>
 * </ul> 
 * 
 * @author Andrzej Kabat 
 */
public class MadreDeDios {
    
    private TravelerMap playground = null;
    
    private List<Player> players = null;

    /**
     * La méthode main.
     * 
     *  @param args 
     *          doit contenir deux itéms : le chemin d'accès des fichiers d'entrée et de sortie.
     */
    public static void main( String[] args ) {
        String inputFile;
        String outputFile;
        if(args.length < 2) {
            Scanner scanner = new Scanner(System.in);
            System.out.format("%n%s", "Veuillez specifier le fichier de scenario a jouer : ");
            inputFile = scanner.next();
            System.out.format("%n%s", "le fichier de sortie : ");
            outputFile = scanner.next();
            scanner.close();
        } else {
            inputFile = args[0];
            outputFile = args[1];    
        }

        MadreDeDios game = new MadreDeDios();
        game.init(inputFile);
        game.play();
        game.persistResult(outputFile);
    }

    public MadreDeDios() {
        this.players = new ArrayList<>();
    }

    /**
     * Initialisation du jeu à partir d'un fichier contenant le scénario à jouer.
     * 
     *  @param inputFile
     *          le chemin d'accès au fichier d'entrée
     */
    public void init(String inputFile) {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String inputLine = reader.readLine();
            while( inputLine != null) {
                initStep( inputLine );
                inputLine = reader.readLine();
            } 
        } catch (FileNotFoundException e) {
            System.err.println("Fichier d'entré inexistant");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Fichier d'entré inaccessible");
            System.exit(1);
        }
    }

    /**
     * L'initialisation est effectuée étape par étape, à chaque ligne du fichier d'entrée.
     * Chaque ligne est analysée afin d'identifier la commande d'initialisation correspondante.
     * L'ensemble de commandes d'initialisation peuvent être trouvées dans l'enum {@link SetupCommand}.
     * 
     *  @param inputLine 
     *          ligne du fichier d'entrée
     */
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
            if(!players.contains(player)) {
                this.playground.setupPlayer(poz, player);
                players.add(player);
            }
            return;
        case LIGNE_VIDE: 
        default:
            return;
        }
    }

    /**
     * La méthode exécute le scénario. Les joueurs se déplacent en ordre suivant lequel il figurent dans le fichier d'entrée. 
     */
    public void play() {

        boolean anyPlayerStillCanMove;
        do {
            anyPlayerStillCanMove = false;
            for(Player player : players) {
                anyPlayerStillCanMove = player.move() || anyPlayerStillCanMove;
            }
            playground.removeEmptyFields();
            
        } while(anyPlayerStillCanMove);
    }

    /**
     * La méthode qui permet de sauvegarder le résultat d'exécution du scénario.
     * 
     *  @param outputFile
     *          le chemin d'accès au fichier de sortie
     */
    public void persistResult(String outputFile) {

        playground.writeToFile(outputFile);
    }

    /**
     * Méthode auxilaire, utilisée pour tester.
     */
    public TravelerMap getTravelerMap() {
        return playground;
    } 
}
