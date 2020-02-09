package game.map;

import game.player.Player;

/**
 * La classe abstraite représentant le champs sur la carte de jeu.
 * Elle est dérivée par {@link Mountain}, et {@link Plain}.
 * 
 * @author Andrzej Kabat
 */
public abstract class Field {
    
    public abstract boolean provideResource();
    
    public boolean hasSpecialProperties() {
        return isTaken() || getGemsNb() != 0;
    }
    
    public boolean isTaken() {
        return player != null;
    }
    
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------

    private int gemsNb;
    private Player player;
    
    public Field() {}
    
    public Field(int gemsNb) {
        this.gemsNb = gemsNb;
    }

    public int getGemsNb() {
        return gemsNb;
    }

    public void setGemsNb(int gemsNb) {
        this.gemsNb = gemsNb;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
}
