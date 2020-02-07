package game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TravelerMap {
    private final int surfaceMax = 85182;
    private int width;
    private int height;
    private Map<Coordinates, Field> fieldsMap;
    
    public TravelerMap(int width, int height) {
        if(width * height > this.surfaceMax) {
            throw new InputMismatchException("Surface demandée superieure à 85182 km2: " + width * height);
        }
        
        this.width = width;
        this.height = height;
        
        fieldsMap = new HashMap<>();
    }
    
    public void setupMountain(int longitude, int latitude) {
        Coordinates position = new Coordinates(longitude, latitude);
        Field mountain = new Mountain();
        setField(position, mountain);
    }
    
    public void setupPlainWithGems(int longitude, int latitude, int gems) {
        Coordinates position = new Coordinates(longitude, latitude);
        Field plain = new Plain(gems);
        setField(position, plain);
    }
    
    public void setupPlayer(Coordinates position, Player player) {
        Field f = getField(position);
        if( player.checkReachabilityOf(f) ) {
            f.setPlayer(player);
            player.setMap(this);
        } else {
            throw new InputMismatchException("Tentative d'ajouter un aventurier sur un champ non disponible pour lui : " + player.toString());
        }
    }
    
    public void setField(Coordinates position, Field newField) {
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
    
    public void removeEmptyFields() {
        fieldsMap = fieldsMap.entrySet().stream().filter(e -> e.getValue().hasSpecialProperties()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public void writeToFile(String filePath) {
        
        removeEmptyFields();
        
        try( BufferedWriter bWriter = new BufferedWriter(new FileWriter(filePath, false)) ) {
            
            bWriter.write( String.format( "C - %1$d - %2$d%n", this.width, this.height) );
            
            List<Map.Entry<Coordinates, Field>> mountains = fieldsMap.entrySet().stream()
                .filter(e -> e.getValue() instanceof Mountain )
                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
                .collect(Collectors.toList());
            for(Map.Entry<Coordinates, Field> e : mountains) {
                bWriter.write( String.format("M - %1$d - %2$d%n", e.getKey().getLongitude(), e.getKey().getLatitude()) );
            }
            
            bWriter.write( String.format("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n") );
            List<Map.Entry<Coordinates, Field>> gemFields = fieldsMap.entrySet().stream()
                .filter(e -> e.getValue() instanceof Plain && ((Plain) e.getValue()).getGemsNb()>0 )
                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
                .collect(Collectors.toList());
            for(Map.Entry<Coordinates, Field> e : gemFields) {
                bWriter.write( String.format("T - %1$d - %2$d - %3$d%n", e.getKey().getLongitude(), e.getKey().getLatitude(), ((Plain) e.getValue()).getGemsNb()) );
            }
            
            bWriter.write( String.format("# {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}%n") );
            List<Map.Entry<Coordinates, Field>> travelers = fieldsMap.entrySet().stream()
                .filter(e -> e.getValue().getPlayer() != null )
                .sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
                .collect(Collectors.toList());
            for(Map.Entry<Coordinates, Field> e : travelers) {
                bWriter.write( ((Plain) e.getValue()).getPlayer().toString() );
            }
            
        } catch (IOException e) {
            System.out.println("Fichier de sortie inaccessible");
            System.exit(1);
        }
    }

    
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
