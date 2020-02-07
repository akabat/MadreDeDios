package game;

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
            resource = new Resource();
            getPlayer().enjoy(resource);
            return true;
        }
        return false;
    }

}
