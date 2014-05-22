package sw.superwhateverjnr.entity;

import sw.superwhateverjnr.world.Location;

public class Skeleton extends Entity
{
	private final static double runningMin = 1.5;
	private final static double runningMax = 4.5;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	
	public Skeleton(Location location)
	{
		super(EntityType.SKELETON, location, null);
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
