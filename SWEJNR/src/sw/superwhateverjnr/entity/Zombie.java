package sw.superwhateverjnr.entity;

import sw.superwhateverjnr.world.Location;

public class Zombie extends Entity
{
	private final static double runningMin = 1.5;
	private final static double runningMax = 4.5;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	
	public Zombie(Location location)
	{
		super(EntityType.ZOMBIE, location, null);
	}

	@Override
	protected void die()
	{
		
	}
	
	@Override
	public void tick()
	{
		super.tick();
	}
}
