package sw.superwhateverjnr.entity;

public enum DamageCause
{
    LAVA,
    EXPLOSION,
    STOMPED_BY_PLAYER;
    
    public double getDamage(double distance)
    {
        switch(this)
        {
            case LAVA:
                return 0.05;
            case EXPLOSION:
                return 30/(1+(distance*distance));
            case STOMPED_BY_PLAYER:
                return 9999;
            default:
                return 0;
        }
    }
    
    public boolean knocksBack()
    {
        switch(this)
        {
            case LAVA:
            case STOMPED_BY_PLAYER:
                return false;
            default:
                return true;
        }
    }
}
