package sw.superwhateverjnr.entity;

import java.util.Map;
import java.util.Random;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;

public abstract class HostileEntity extends Entity
{
    protected enum Direction
    {
        LEFT,
        RIGHT;
        
        public Direction opposite()
        {
            switch(this)
            {
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                default:
                    return null;
            }
        }
        public static Direction fromBool(Boolean right)
        {
            if(right)
            {
                return RIGHT;
            }
            else
            {
                return LEFT;
            }
        }
        public boolean toBool()
        {
            switch(this)
            {
                case LEFT:
                    return false;
                case RIGHT:
                    return true;
                default:
                    return false;
            }
        }
    }
    
    protected enum MovementType
    {
        RANDOM,
        STAY,
        FOLLOW;
    }
        
    protected int lastRandomWalkDuration = 0;
    protected Direction direction;
    protected MovementType movement;
    protected boolean seesPlayer = false;
    protected boolean jumps;
    
    public abstract double getRunningMin();
    public abstract double getRunningMax();
    public abstract double getJumpPower();
    public abstract double getRunPower();
    
	public HostileEntity(EntityType type, Location location, Map<String, Object> extraData)
	{
		this(getNewId(), type, location, extraData);
	}
	public HostileEntity(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(id, type, location, extraData);
        movement = MovementType.RANDOM;
        direction = Direction.LEFT;
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
        if(p.getLocation().distance(location) < triggerRadius)
        {
            seesPlayer = p.getHitBox().translatedTo(p.getLocation()).visibleFrom(location.add(0, getEyeHeight()));
        }
        else
        {
            seesPlayer = false;
        }
    }
    
    protected void calculateMovement()
    {
        if(seesPlayer)
        {
            movement = MovementType.FOLLOW;
        }
        else
        {
        	movement = MovementType.RANDOM;
        }
        stopIfTooHigh();
        jumpIfWall();
        evaluateDirection();
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
                if(lastRandomWalkDuration==0)
                {
                    Random r = new Random();
                    direction = Direction.fromBool(r.nextBoolean());
                    lastRandomWalkDuration = r.nextInt(80) + 20;
                }
                else
                {
                    lastRandomWalkDuration--;
                }
                break;
            case STAY:
                for(int i = 0; i < lastDirections.length; i++)
                {
                    lastDirections[i] = null;
                }
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
            if(!Game.getInstance().getWorld().getBlockAt(location.add(addx, -i)).getType().isSolid())
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
        if(location==null || world()==null)
		{
			return;
		}
		Rectangle bounds=getHitBox();
		long now=System.currentTimeMillis();
		long time=Game.TICK_INTERVAL;
		
		double vx=velocity.getX();
        
        
        if(movement != MovementType.STAY)
        {
            if(direction == Direction.LEFT)
            {
                if(vx>-getRunningMin())
                {
                    vx=-getRunningMin();
                }
                else
                {
                    vx*=(1+getRunPower()*time*(getRunningMax()+vx));
                }
            }
            else
            {
                if(vx<getRunningMin())
                {
                    vx=getRunningMin();
                }
                else
                {
                    vx*=(1+getRunPower()*time*(getRunningMax()-vx));
                }
            }
        }
		else
		{
			double d=getRunPower()*time*(Math.abs(vx)+getRunningMin());
			d*=3;
			if(d>1)
			{
				d=1;
			}
			else if(d<0)
			{
				d=0;
			}
			
			vx*=(1-d);
		}

		velocity.setX(vx);
		
		float multiplier=0.01F;
		float playerwidth=(float) (Math.abs(bounds.getMin().getX()-bounds.getMax().getX()));
		
		//world check
		double x=location.getX();
		x+=velocity.getX()*multiplier;
		if(x<0)
		{
			x=0;
			velocity.setX(0);
		}
		if(x>=world().getWidth())
		{
			x=world().getWidth()-0.0000001;
			velocity.setX(0);
		}
		
		//block check
		try
		{
			Location l1=new Location(x-playerwidth/2,location.getY());
			Location l2=new Location(x-playerwidth/2,location.getY()+1);
			Block b1=world().getBlockAt(l1);
			Block b2=world().getBlockAt(l2);
			if(b1.getType().isSolid() || b2.getType().isSolid())
			{
				if(velocity.getX()<0)
				{
					x=Math.ceil(x-playerwidth/2)+playerwidth/2;
					velocity.setX(0);
				}
			}
		}
		catch(Exception e){}
		
		try
		{
			Location l3=new Location(x+playerwidth/2,location.getY());
			Location l4=new Location(x+playerwidth/2,location.getY()+1);
			Block b3=world().getBlockAt(l3);
			Block b4=world().getBlockAt(l4);
			if(b3.getType().isSolid() || b4.getType().isSolid())
			{
				if(velocity.getX()>0)
				{
					x=Math.floor(x+playerwidth/2)-playerwidth/2;
					velocity.setX(0);
				}
			}
		}
		catch(Exception e){}
			
		location.setX(x);
        if(jumps)
        {
            jump();
        }
    }
    protected abstract void attack();
}
