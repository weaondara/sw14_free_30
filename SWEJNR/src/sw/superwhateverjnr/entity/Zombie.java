package sw.superwhateverjnr.entity;

import java.util.List;
import java.util.Map;
import java.util.Random;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;

public class Zombie extends HostileEntity
{
	//-------------------------- animation ------------------------------
	protected static final double radiant = 2 * Math.PI / 180;
	protected static double angleRotationMoving=2.5;
	protected static double angleRotationStanding=0.5;

	protected static double maxArmAngleMoving=6;
	protected static double maxArmAngleStanding=6;
	
	protected static double maxLegAngleMoving=30;
	protected static double maxLegAngleStanding=0;
	
	
	public double getAngleRotationMoving()
	{
		return angleRotationMoving;
	}
	public double getAngleRotationStanding()
	{
		return angleRotationStanding;
	}
	public double getMaxArmAngleMoving()
	{
		return maxArmAngleMoving;
	}
	public double getMaxArmAngleStanding()
	{
		return maxArmAngleStanding;
	}
	public double getMaxLegAngleMoving()
	{
		return maxLegAngleMoving;
	}
	public double getMaxLegAngleStanding()
	{
		return maxLegAngleStanding;
	}
	
	//-------------------------- movement ------------------------------
	private final static double runningMin = 0.75;
	private final static double runningMax = 2.25;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	private final static double radius = 6.0;
	
	private Player player;
	private static boolean isindistance = false;
	
	public Zombie(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(id, EntityType.ZOMBIE, location, extraData);
		
		triggerRadius = 6;
	}

    private void dropFlesh()
    {
        int amount = new Random().nextInt(2);
        super.dropItem(location.add(new Location(0.5, 0)), Drop.DropType.ROTTEN_FLESH, amount);
    }
    
	@Override
	protected void die()
	{
        dropFlesh();
		super.die();
	}
	
	@Override
	public void tick()
	{
		super.tick();
	}
	
	private long ticks;
	protected void trigger()
	{
		//dump left right run
		if(ticks % 200 == 0)
		{
			setMovingright(false);
			setMovingleft(true);
			setLookingRight(false);
		}
		else if((ticks - 100) % 200 == 0)
		{
			setMovingright(true);
			setMovingleft(false);
			setLookingRight(true);
		}
		ticks++;
	}
	/*
	protected void randomJump()
	{
		if (isindistance)
		{
			//do some cranky stuff
		}
	}
	*/

    @Override
    protected void attack()
    {
    	Game g=Game.getInstance();
    	Player p=g.getPlayer();
		if(hitBox.translatedTo(location).intersects(p.getHitBox().translatedTo(p.getLocation())))
		{
			p.takeDamage(DamageCause.TOUCHED_BY_ZOMBIE, 0);
		}
    }

    @Override
    public double getRunningMin()
    {
        return runningMin;
    }

    @Override
    public double getRunningMax()
    {
        return runningMax;
    }

    @Override
    public double getJumpPower()
    {
        return jumpPower;
    }

    @Override
    public double getRunPower()
    {
        return runPower;
    }

    @Override
    public double getTriggerRadius()
    {
        return triggerRadius;
    }
}
