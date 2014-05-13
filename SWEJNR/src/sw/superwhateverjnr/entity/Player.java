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
	private int moveArmswingDegrees;
	private int standArmswingDegrees;
	
	@Getter @Setter
	private int armMoveConstant = 10;
	
	private int armIdleDegree = 90;
	private int armMaxDegreeDelta = 45;
	
	@Getter @Setter
	private String armMovementDirection;
	
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
	
	private void swingArms()
	{
		if(this.getArmMovementDirection().equals("right"))
		{
			this.moveArmswingDegrees += armMoveConstant;
			this.standArmswingDegrees -= armMoveConstant;
		}
		
		if(this.getArmMovementDirection().equals("left"))
		{
			this.moveArmswingDegrees -= armMoveConstant;
			this.standArmswingDegrees += armMoveConstant;
		}
		
		if(this.moveArmswingDegrees > armIdleDegree + armMaxDegreeDelta)
		{
			this.setArmMovementDirection("left");
		}
		
		if(this.moveArmswingDegrees < armIdleDegree - armMaxDegreeDelta)
		{
			this.setArmMovementDirection("right");
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
