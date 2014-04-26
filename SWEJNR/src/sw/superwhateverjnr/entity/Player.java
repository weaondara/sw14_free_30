package sw.superwhateverjnr.entity;

import sw.superwhateverjnr.world.Location;

public class Player extends Entity
{
	private double moveArmswingDegrees;
	private double standArmswingDegrees;
	public Player(Location location)
	{
		super(EntityType.PLAYER, location);
		moveArmswingDegrees=0;
		standArmswingDegrees=0;
	}
	
	private boolean movingright;
	private boolean movingleft;
}
