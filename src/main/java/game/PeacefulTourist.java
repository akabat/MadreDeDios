package game;

import java.util.List;

public class PeacefulTourist extends Player {
    
    public PeacefulTourist(String name, Coordinates position, Orientation orientation, List<MoveType> moves) {
        super(name, position, orientation, moves);
    }

    @Override
    protected Coordinates getNewPosition() {
        switch(getOrientation()) {
        case N:
            return new Coordinates(getPosition().getLongitude(), getPosition().getLatitude() -1); 
        case E:
            return new Coordinates(getPosition().getLongitude() +1, getPosition().getLatitude()); 
        case S:
            return new Coordinates(getPosition().getLongitude(), getPosition().getLatitude() +1); 
        case O:
            return new Coordinates(getPosition().getLongitude() -1, getPosition().getLatitude());
        default: 
            return null;
        }
    }

    @Override
    public boolean checkReachabilityOf(Field placeToDiscover) {
        if(placeToDiscover == null) return false;
        if(placeToDiscover instanceof Plain && !((Plain) placeToDiscover).isTaken() ) return true;
        return false;
    }
    
    @Override
    public void explore(Field placeToDiscover) {
        placeToDiscover.provideResource();
    }
    
    @Override
    public boolean hasSpecialTools() {
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("A - %1$s - %2$d - %3$d - %4$s - %5$d%n", getName(), getPosition().getLongitude(), getPosition().getLatitude(), getOrientation(), getNbGemsFound());
    }
    
}
