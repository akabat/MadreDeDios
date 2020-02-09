package game.map;

public class Plain extends Field {
    
    public Plain() {
    }
    
    public Plain(int gemsNb) {
        super(gemsNb);
    }
    
    @Override
    public boolean provideResource() {
        Resource resource = null;
        if( getGemsNb() > 0 ) {
            setGemsNb( getGemsNb() -1 );
            resource = Resource.Tresor;
            getPlayer().enjoy(resource);
            return true;
        }
        return false;
    }

}
