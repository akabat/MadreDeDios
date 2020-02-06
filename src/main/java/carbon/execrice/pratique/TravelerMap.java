package carbon.execrice.pratique;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class TravelerMap {
    private final int surfaceMax = 85182;
    private int width;
    private int height;
    private Map<Position, Field> fieldsMap;
    
    public TravelerMap(int width, int height) {
        if(width * height > this.surfaceMax) {
            throw new InputMismatchException("Requested surface is bigger than 85182 km2: " + width * height);
        }
        
        this.width = width;
        this.height = height;
        
        fieldsMap = new HashMap<>();
    }
    
    public void setupMountain(int longitude, int latitude) {
        Position p = new Position(longitude, latitude);
        if(!isFieldAvailable(p)) {
            throw new InputMismatchException("");
        }
        getField(p).setMountain(true);
    }
    
    public void setupGems(int longitude, int lattitude, int gems) {
        Position p = new Position(longitude, lattitude);
        Field f = getField(p);
        if(f.isMountain()) {
            throw new InputMismatchException("");
        }
        f.setGemsNb(f.getGemsNb() + gems);
    }
    
    public void setupTraveler(Position p, Traveler t) {
        t.setMap(this);
        if(!isFieldAvailable(p)) {
            throw new InputMismatchException("");
        }
        t.setPosition(p);
        getField(p).setTraveler(t);
    }
    
    public boolean isFieldAvailable(Position p) {
        return getField(p).isAvailable();
    }
    
    public void updateTravelersPosition(Position oldPosition, Position newPosition, Traveler t) {
        getField(newPosition).setTraveler(t);
        Field old = getField(oldPosition);
        if(old.isEmpty()) {
            fieldsMap.remove(oldPosition);
        } else {
            old.setTraveler(null);
        }
    }
    
    private Field getField(Position p) {
        if(p.getLatitude()<0 || p.getLatitude()>=height || p.getLongitude()<0 || p.getLongitude()>=width) {
            throw new IndexOutOfBoundsException("Position outside map: " + p.toString());
        }
        Field f = fieldsMap.get(p);
        if(f == null) {
            f = new Field();
            fieldsMap.put(p, f);
        }
        return f;
    }
    
    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(String.format( "C - %1$d - %2$d%n", this.width, this.height) );
        fieldsMap.entrySet().stream().filter(e -> e.getValue().isMountain())
            .forEach( e -> sBuilder.append(String.format("M - %1$d - %2$d%n", e.getKey().getLongitude(), e.getKey().getLatitude())) );
        sBuilder.append(String.format("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}%n"));
        fieldsMap.entrySet().stream().filter(e -> e.getValue().getGemsNb()>0 )
            .forEach( e -> sBuilder.append(String.format("T - %1$d - %2$d - %3$d%n", e.getKey().getLongitude(), e.getKey().getLatitude(), e.getValue().getGemsNb())) );
        sBuilder.append(String.format("# {A comme Aventurier} - {Nom de l’aventurier} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. trésors ramassés}%n"));
        fieldsMap.entrySet().stream().filter(e -> e.getValue().getTraveler() != null )
            .forEach( e -> sBuilder.append(e.getValue().getTraveler().toString()) );
        return sBuilder.toString();
    }
    
}
