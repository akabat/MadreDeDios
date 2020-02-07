package game;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class Player implements Comparable<Player> {
    
    protected abstract Coordinates getNewPosition();
    
    public abstract boolean checkReachabilityOf(Field field);
    
    public boolean move() {
        
        if(getMovesList().isEmpty()) {
            return false;
        }
        
        MoveType moveType = getMovesList().removeFirst();
        if(MoveType.A.equals(moveType)) {
            
            Coordinates oldPosition = getPosition();
            Coordinates newPosition = getNewPosition();
            Field placeToDiscover = getMap().getField(newPosition);
            
            if(checkReachabilityOf(placeToDiscover)) {
                
                placeToDiscover.setPlayer(this);
                setPosition(newPosition);
                getMap().getField(oldPosition).setPlayer(null);
                explore(placeToDiscover);
            }
        } else {
            
            setOrientation(getNewOrientation(moveType));
        }
        return true;
    }
    
    public abstract void explore(Field field);
    
    public abstract boolean hasSpecialTools();
    
    public void enjoy(Resource resource) {
        if(resource != null) {
            nbGemsFound++;
        }
    }

    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    
    private String name;
    
    private TravelerMap map;
    private Coordinates position;
    private Orientation orientation;
    private Deque<MoveType> movesList;
    
    private int nbGemsFound;
    
    public Player(String name, Coordinates position, Orientation orientation, List<MoveType> moves) {
        this.name = name;
        this.position = position;
        this.orientation = orientation;
        this.movesList = new LinkedList<>();
        this.movesList.addAll(moves);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Coordinates getPosition() {
        return position;
    }
    
    public void setPosition(Coordinates position) {
        this.position = position;
    }
    
    public Orientation getOrientation() {
        return orientation;
    }
    
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    
    public Deque<MoveType> getMovesList() {
        return movesList;
    }
    
    public void setMovesList(List<MoveType> moves) {
        this.movesList.clear();
        this.movesList.addAll(moves);
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
    
    protected Orientation getNewOrientation(MoveType move) {
        switch(orientation) {
        case N:
            return move.equals(MoveType.D)? Orientation.E : Orientation.O; 
        case E:
            return move.equals(MoveType.D)? Orientation.S : Orientation.N; 
        case S:
            return move.equals(MoveType.D)? Orientation.O : Orientation.E; 
        case O:
            return move.equals(MoveType.D)? Orientation.N : Orientation.S;
        default: 
            return null;
        }
    }
    
    @Override
    public int compareTo(Player otherPlayer) {
        int compPosition = position.compareTo(otherPlayer.getPosition());
        if(compPosition != 0) return compPosition;
        int compName = name.compareTo(otherPlayer.getName());
        if(compName != 0) return compName;
        int compNbGemsFound = Integer.valueOf(nbGemsFound).compareTo(otherPlayer.getNbGemsFound());
        return compNbGemsFound;
    }

}
