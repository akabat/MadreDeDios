package carbon.execrice.pratique;

import java.util.Objects;

public class Field {
    private boolean mountain;
    private int gemsNb;
    private Traveler traveler;
    
    public Field() {
    }
    
    public boolean isAvailable() {
        return !mountain && (traveler == null);
    }
    
    public boolean isEmpty() {
        return isAvailable() && (gemsNb==0);
    }
    
    public Traveler getTraveler() {
        return traveler;
    }
    
    public void setTraveler(Traveler t) {
        this.traveler = t;
        if(traveler != null && gemsNb>0) {
            traveler.setNbGemsFound( traveler.getNbGemsFound() +1 );
            gemsNb--;
        }
    }

    public boolean isMountain() {
        return mountain;
    }

    public void setMountain(boolean mountain) {
        this.mountain = mountain;
    }

    public int getGemsNb() {
        return gemsNb;
    }

    public void setGemsNb(int gemsNb) {
        this.gemsNb = gemsNb;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gemsNb, mountain, traveler);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Field)) {
            return false;
        }
        Field other = (Field) obj;
        return gemsNb == other.gemsNb && mountain == other.mountain && Objects.equals(traveler, other.traveler);
    }
    
}
