package sw.superwhateverjnr.entity;

import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

@Getter
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
	
	@Setter
	private boolean movingright;
	@Setter
	private boolean movingleft;
	@Setter
	private boolean jumping;
	
	public boolean isOnGround()
	{
		World w=Game.getInstance().getWorld();
		if(w==null)
		{
			return false;
		}
		Block b=w.getBlockAt(location.add(0, -1));
		if(!b.getType().isSolid())
		{
			return false;
		}
		
		return location.getBlockY()==location.getY();
	}
}
