package game;

import java.util.Objects;

public class Coordinates implements Comparable<Coordinates> {
    
    private final int longitude;
    private final int latitude;

    public Coordinates(int longitute, int latitude) {
        this.latitude = latitude;
        this.longitude = longitute;
    }

    public int getLatitude() {
        return latitude;
    }

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
        if (!(obj instanceof Coordinates)) {
            return false;
        }
        Coordinates other = (Coordinates) obj;
        return latitude == other.latitude && longitude == other.longitude;
    }

    @Override
    public String toString() {
        return "Position [latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    @Override
    public int compareTo(Coordinates o) {
        int cmpLong = Integer.valueOf(longitude).compareTo(o.getLongitude());
        return cmpLong != 0? cmpLong : Integer.valueOf(latitude).compareTo(o.getLatitude());
    }

}
