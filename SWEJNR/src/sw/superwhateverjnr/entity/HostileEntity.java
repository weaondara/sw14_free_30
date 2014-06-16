package sw.superwhateverjnr.entity;

import java.util.Map;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.world.Location;

public abstract class HostileEntity extends Entity
{

    protected enum Direction
    {
        LEFT,
        RIGHT;
    }
    
    protected enum MovementType
    {
        RANDOM,
        STAY,
        FOLLOW;
    }
        
    protected Direction[] lastDirections;
    protected MovementType movement;
    protected boolean seesPlayer = false;
    protected double triggerDistance;
    
	public HostileEntity(EntityType type, Location location, Map<String, Object> extraData)
	{
		this(getNewId(), type, location, extraData);
	}
	public HostileEntity(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(id, type, location, extraData);
        lastDirections = new Direction[3];
        movement = MovementType.STAY;
	}
    
    public void tick()
    {
        super.tick();
        seePlayer();
        calculateMovement();
        move();
        attack();
    }
    
    protected void seePlayer()
    {
        if(seesPlayer)
        {
            return;
        }
        else
        {
            Player p = Game.getInstance().getPlayer();
            double playerx = p.getLocation().getX();
            double playery = p.getLocation().getY();
            double x = location.getX();
            double y = location.getY();
            double distance =  Math.sqrt(Math.pow(playerx - x, 2.0) + Math.pow(playery - y, 2.0));
            if(distance < triggerDistance)
            {
                seesPlayer = p.getHitBox().translatedTo(p.getLocation()).visibleFrom(new Location(location.getX()+getEyeHeight(),location.getY()));
            }
        }
    }
    
    protected void calculateMovement()
    {
        stopIfLava();
    }
    
	protected void stopIfLava()
	{
		//stop instantly!!!
	}
    
    protected void move()
    {
        return;
    }
    protected abstract void attack();
}
