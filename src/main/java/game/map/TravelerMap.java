package game.map;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import game.player.Player;
import game.position.Coordinates;

/**
 * La classe définit les dimensions du terrain de jeu, et l'emplacement des différents éléments du scénario.
 * 
 * 
 * @author Andrzej Kabat
 */
public class TravelerMap {
    private final int surfaceMax = 85182;
    private final int width;
    private final int height;
    private Map<Coordinates, Field> fieldsMap;
    
    public TravelerMap(int width, int height) {
        if(width * height > this.surfaceMax) {
            throw new InputMismatchException("Surface demandée superieure à 85182 km2: " + width * height);
        }
        
        this.width = width;
        this.height = height;
        
        fieldsMap = new HashMap<>();
    }
    
    /**
     * Initialise une montagne.
     * 
     * @param longitude
     *          coordonnée en direction horizontale
     * @param latitude
     *          coordonnée en direction verticale
     */
    public void setupMountain(int longitude, int latitude) {
        Coordinates position = new Coordinates(longitude, latitude);
        Field mountain = new Mountain();
        setField(position, mountain);
    }
    
    /**
     * Initialise une plaine avec un nombre défini des trésors.
     * 
     * @param longitude
     *          coordonnée en direction horizontale
     * @param latitude
     *          coordonnée en direction verticale
     * @param gems
     *          nombre de trésors cachés dans la plaine
     *          
     */
    public void setupPlainWithGems(int longitude, int latitude, int gems) {
        Coordinates position = new Coordinates(longitude, latitude);
        Field plain = new Plain(gems);
        setField(position, plain);
    }
    
    /**
     * Initialise l'avanturier.
     * 
     * @param   position
     *          coordonnées de l'avanturier
     * @param   player
     *          avanturier - lui même : la classe du joueur est derivée du {@link Player}
     */
    public void setupPlayer(Coordinates position, Player player) {
        Field f = getField(position);
        if( player.checkReachabilityOf(f) ) {
            f.setPlayer(player);
            player.setMap(this);
        } else {
            throw new InputMismatchException("Tentative d'ajouter un aventurier sur un champ non disponible pour lui : " + player.toString());
        }
    }
    
    /**
     * Méthode auxiliaire utilisée lors de l'initialisation du jeu.
     * Si lors d'initialisation il y a une tentative d'ajouter un champ en déhors du terrain de jeu, une exception est levée.
     * L'écrasement d'un champ déjà existant par un autre n'est pas possible à l'exception suivante : 
     * si une plaine aux trésors est ajoutée à l'endroit déjà occupée par une autre plaine aux trésors, le nobre de trésors s'accumule.
     * 
     * @param position
     *          coordonnées du champ à ajouter : {@link Coordinates}
     * @param   newField
     *          nouveau champ à ajouter : {@link Field}
     */
    private void setField(Coordinates position, Field newField) {
        if(position.getLatitude()<0 || position.getLatitude()>=height || position.getLongitude()<0 || position.getLongitude()>=width) {
            throw new IndexOutOfBoundsException("Position demandée en dehors de la carte : " + position.toString());
        }
        Field field = fieldsMap.get(position);
        if(field == null) {
            fieldsMap.put(position, newField);
        } else {
            if(field instanceof Plain && newField instanceof Plain) {
                Plain plain = (Plain) field;
                Plain newPlain = (Plain) newField;
                plain.setGemsNb( plain.getGemsNb() + newPlain.getGemsNb() );
            } else {
                throw new InputMismatchException("Tentative d'ajouter plusieurs champs au même endroit : " + position.toString());
            }
        }
    }
    
    /**
     * Méthode utilisée lors de placement des avanturier : pendant l'initialisation, ainsi que l'exécution du scénario.
     * Afin d'économiser la mémoire, la carte de jeu peut être débarassée de plaines vides.
     * Lorsqu'on demande un champ non existant, par défaut une plaine est créée. 
     * 
     * @param   position
     *          coordonnées du champ cherché : {@link Coordinates}
     *          
     * @return  {@link Field}
     *          le champs demané
     */
    public Field getField(Coordinates position) {
        if(position.getLatitude()<0 || position.getLatitude()>=height || position.getLongitude()<0 || position.getLongitude()>=width) {
            return null;
        }
        Field f = fieldsMap.get(position);
        
        if(f == null) {
            f = new Plain();
            fieldsMap.put(position, f);
        }
        return f;
    }
    
    /**
     * 
     */
    public void removeEmptyFields() {
        fieldsMap = fieldsMap.entrySet().stream().filter(e -> e.getValue().hasSpecialProperties()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    /**
     * 
     */
    public void writeToFile(String filePath) {
        
        removeEmptyFields();
        
        try( BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false), StandardCharsets.UTF_8)) ) {
            
            bWriter.write( String.format("# {C comme Carte} - {Nb. de case en largeur} - {Nb. de case en hauteur}%n") );
            bWriter.write( String.format( "C - %1$d - %2$d%n", this.width, this.height) );
            
            List<Map.Entry<Coordinates, Field>> mountains = fieldsMap.entrySet().stream()
                .filter(e -> e.getValue() instanceof Mountain )
                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
                .collect(Collectors.toList());
            if(!mountains.isEmpty()) {
                bWriter.write( String.format("# {M comme Montagne} - {Axe horizontal} - {Axe vertical}%n") );
                for(Map.Entry<Coordinates, Field> e : mountains) {
                    bWriter.write( String.format("M - %1$d - %2$d%n", e.getKey().getLongitude(), e.getKey().getLatitude()) );
                }
            }
            
            List<Map.Entry<Coordinates, Field>> gemFields = fieldsMap.entrySet().stream()
                .filter(e -> e.getValue() instanceof Plain && ((Plain) e.getValue()).getGemsNb()>0 )
                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
                .collect(Collectors.toList());
            if(!gemFields.isEmpty()) {
                bWriter.write( String.format("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n") );
                for(Map.Entry<Coordinates, Field> e : gemFields) {
                    bWriter.write( String.format("T - %1$d - %2$d - %3$d%n", e.getKey().getLongitude(), e.getKey().getLatitude(), ((Plain) e.getValue()).getGemsNb()) );
                }
            }
            
            
            List<Map.Entry<Coordinates, Field>> travelers = fieldsMap.entrySet().stream()
                .filter(e -> e.getValue().getPlayer() != null )
                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
                .collect(Collectors.toList());
            if(!travelers.isEmpty()) {
                bWriter.write( String.format("# {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}%n") );
                for(Map.Entry<Coordinates, Field> e : travelers) {
                    bWriter.write( ((Plain) e.getValue()).getPlayer().toString() );
                }
            }
            
        } catch (IOException e) {
            System.err.println("Fichier de sortie inaccessible");
            System.exit(1);
        }
    }

    /**
     * Méthode auxiliaire, utilisée dans des premiers tests.
     */
    @Override
    public String toString() {
        
        removeEmptyFields();
        
        StringBuilder sBuilder = new StringBuilder();
        
        sBuilder.append(String.format( "C - %1$d - %2$d%n", this.width, this.height) );
        fieldsMap.entrySet().stream()
            .filter(e -> e.getValue() instanceof Mountain )
            .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
            .forEach( e -> sBuilder.append(String.format("M - %1$d - %2$d%n", e.getKey().getLongitude(), e.getKey().getLatitude())) );
        
        sBuilder.append(String.format("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n"));
        fieldsMap.entrySet().stream()
            .filter(e -> e.getValue() instanceof Plain && e.getValue().getGemsNb()>0 )
            .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
            .forEach( e -> sBuilder.append(String.format("T - %1$d - %2$d - %3$d%n", e.getKey().getLongitude(), e.getKey().getLatitude(), e.getValue().getGemsNb())) );
        
        sBuilder.append(String.format("# {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}%n"));
        fieldsMap.entrySet().stream()
            .filter(e -> e.getValue().getPlayer() != null )
            .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
            .forEach( e -> sBuilder.append(e.getValue().getPlayer().toString()) );
        
        return sBuilder.toString();
    }
    
}
