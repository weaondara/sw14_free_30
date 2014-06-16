package sw.superwhateverjnr.entity;

import java.util.Map;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;

public class Skeleton extends HostileEntity
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
	
	public Skeleton(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(id, EntityType.SKELETON, location, extraData);
		
		triggerRadius = 6;
	}
	
	@Override
	protected void die()
	{
		super.die();
	}

	@Override
	public void tick()
	{
		super.tick();
		// randomJump();
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
        return;
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
