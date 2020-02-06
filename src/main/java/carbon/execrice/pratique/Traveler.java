package carbon.execrice.pratique;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Traveler {
    private String name;
    
    private TravelerMap map;
    private Position position;
    private Orientation orientation;
    private Deque<Move> moves;
    
    private int nbGemsFound;
    
    public Traveler(String name, Position position, Orientation orientation, List<Move> moves) {
        this.name = name;
        this.position = position;
        this.orientation = orientation;
        this.moves = new LinkedList<>();
        this.moves.addAll(moves);
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Orientation getOrientation() {
        return orientation;
    }
    
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    
    public Deque<Move> getMoves() {
        return moves;
    }
    
    public void setMoves(List<Move> moves) {
        this.moves.clear();
        this.moves.addAll(moves);
    }
    
    public int getNbGemsFound() {
        return nbGemsFound;
    }
    
    public void setNbGemsFound(int nbGemsFound) {
        this.nbGemsFound = nbGemsFound;
    }
    
    public TravelerMap getMap() {
        return map;
    }
    
    public void setMap(TravelerMap map) {
        this.map = map;
    }
    
    public boolean move() {
        if(moves.isEmpty()) {
            return false;
        }
        Move move = moves.removeFirst();
        if(Move.A.equals(move)) {
            Position oldPosition = position;
            Position newPosition = getNewPosition();
            if(map.isFieldAvailable(newPosition)) {
                position = newPosition;
                map.updateTravelersPosition(oldPosition, newPosition, this);
            }
        } else {
            orientation = getNewOrientation(move);
        }
        return true;
    }
    
    private Position getNewPosition() {
        switch(orientation) {
        case N:
            return new Position(position.getLongitude(), position.getLatitude() -1); 
        case E:
            return new Position(position.getLongitude() +1, position.getLatitude()); 
        case S:
            return new Position(position.getLongitude(), position.getLatitude() +1); 
        case O:
            return new Position(position.getLongitude() -1, position.getLatitude());
        default: 
            return null;
        }
    }
    
    private Orientation getNewOrientation(Move move) {
        switch(orientation) {
        case N:
            return move.equals(Move.D)? Orientation.E : Orientation.O; 
        case E:
            return move.equals(Move.D)? Orientation.S : Orientation.N; 
        case S:
            return move.equals(Move.D)? Orientation.O : Orientation.E; 
        case O:
            return move.equals(Move.D)? Orientation.N : Orientation.S;
        default: 
            return null;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, moves, name, nbGemsFound, orientation, position);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Traveler)) {
            return false;
        }
        Traveler other = (Traveler) obj;
        return Objects.equals(map, other.map) && Objects.equals(moves, other.moves) && Objects.equals(name, other.name) && nbGemsFound == other.nbGemsFound && orientation == other.orientation && Objects.equals(position, other.position);
    }
    
    @Override
    public String toString() {
        return String.format("A - %1$s - %2$d - %3$d - %4$s - %5$d%n", name, position.getLongitude(), position.getLatitude(), orientation, nbGemsFound);
    }    
}
