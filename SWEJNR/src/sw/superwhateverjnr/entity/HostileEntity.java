package sw.superwhateverjnr.entity;

import java.util.Map;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Material;
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
    protected boolean jumps;
    
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
        tickMove();
        attack();
        reevaluateMovement();
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
        evaluateDirection();
        stopIfTooHigh();
    }
       
    protected void evaluateDirection()
    {
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
    protected void reevaluateMovement()
    {
        if(movement == MovementType.STAY)
        {
            movement = MovementType.RANDOM;
        }
        jumps = false;
    }
    
    protected void stopIfTooHigh()
    {
        int viewDepth = Math.min(location.getBlockY(), 4);
        double addx = direction == Direction.LEFT ? -0.5 : 0.5;
        for(int i = 0; i < viewDepth; i++)
        {
            if(Game.getInstance().getWorld().getBlockAt(location.add(addx, -i)).getType().isSolid())
            {
                movement = MovementType.STAY;
                break;
            }
        }
    }
    
    protected void jumpIfWall()
    {
        double addx = direction == Direction.LEFT ? -0.5 : 0.5;
        jumps = Game.getInstance().getWorld().getBlockAt(location.add(addx, 0)).getType().isSolid() && 
                Game.getInstance().getWorld().getBlockAt(location.add(addx, 1)).getType() == Material.AIR;
    }
    
    protected void tickMove()
    {
        if(jumps)
        {
            jump();
        }
    }
    protected abstract void attack();
}
