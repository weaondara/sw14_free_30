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
    protected Direction direction;
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
        Player p = Game.getInstance().getPlayer();
        double distance =  p.getLocation().distance(location);
        if(distance < triggerDistance)
        {
            seesPlayer = p.getHitBox().translatedTo(p.getLocation()).visibleFrom(new Location(location.getX()+getEyeHeight(),location.getY()));
        }
    }
    
    protected void calculateMovement()
    {
        if(seesPlayer)
        {
            movement = MovementType.FOLLOW;
        }
        stopIfTooHigh();
        switch(movement)
        {
            case FOLLOW:
                if(Game.getInstance().getPlayer().getLocation().getX() < location.getX())
                {
                    direction = Direction.LEFT;
                }
                else
                {
                    direction = Direction.RIGHT;
                }
                break;
            case RANDOM:
                break;
            case STAY:
                break;
        }
    }
        
    protected void stopIfTooHigh()
    {
        int viewDepth = Math.min(location.getY(), 4);
        double addx;
        switch(direction)
        {
            case LEFT:
                addx = -0.5;
                break;
            case RIGHT:
                addx = 0.5;
                break;
        }        
        for(int i = 0; i < viewDepth; i++)
        {
            if(Game.getInstance().getWorld().getBlockAt(location.add(addx, -i)).getType().isSolid())
            {
                movement = MovementType.STAY;
                break;
            }
        }
    }
    
    protected void move()
    {
        return;
    }
    protected abstract void attack();
}
