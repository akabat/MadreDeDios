package carbon.execrice.pratique;

import java.util.Objects;

public class Position {
    
    private final int latitude;
    private final int longitude;

    public Position(int longitute, int latitude) {
        this.latitude = latitude;
        this.longitude = longitute;
    }

    /**
     * @return the latitude
     */
    public int getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public int getLongitude() {
        return longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position other = (Position) obj;
        return latitude == other.latitude && longitude == other.longitude;
    }

    @Override
    public String toString() {
        return "Position [latitude=" + latitude + ", longitude=" + longitude + "]";
    }

}
