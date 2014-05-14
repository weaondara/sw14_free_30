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
	private static int armIdleDegree = 90;
	private static int armMaxDegreeDelta = 45;
	private static int armMoveConstant = 10;
	
	
	private int moveArmswingDegrees;
	private int standArmswingDegrees;
	
	@Setter
	private boolean armMovingRight;
	
	public Player(Location location)
	{
		super(EntityType.PLAYER, location);
		moveArmswingDegrees=armIdleDegree;
		standArmswingDegrees=armIdleDegree;
	}
	
	@Setter
	private boolean movingright;
	@Setter
	private boolean movingleft;
	@Setter
	private boolean jumping;
	
	public void swingArms()
	{
		if(armMovingRight)
		{
			moveArmswingDegrees += armMoveConstant;
			standArmswingDegrees -= armMoveConstant;
		}
		else
		{
			moveArmswingDegrees -= armMoveConstant;
			standArmswingDegrees += armMoveConstant;
		}
		
		if(moveArmswingDegrees > armIdleDegree + armMaxDegreeDelta)
		{
			armMovingRight = false;
		}
		
		else if(this.moveArmswingDegrees < armIdleDegree - armMaxDegreeDelta)
		{
			armMovingRight = true;
		}
			
	}
	
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
