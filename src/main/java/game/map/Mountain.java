package game.map;

public class Mountain extends Field {

    @Override
    public boolean provideResource() {
        
        if(!getPlayer().hasSpecialTools()) return false;
        else {
            // TODO : consider an extraction with a special tools for another type of brave player, capable to climb the mountains
        }
        return false;
    }

    @Override
    public boolean hasSpecialProperties() {
        return true;
    }

}
