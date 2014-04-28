package sw.superwhateverjnr.entity;

import com.google.common.base.Preconditions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.ToString;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.util.Vector;
import sw.superwhateverjnr.world.Location;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Entity
{
	private static int ENTITY_ID = 0;
	@Synchronized
	public static int getNewId()
	{
		return ENTITY_ID++;
	}
	
	protected int id;
	protected EntityType type;
	protected Location location;
	protected Rectangle hitBox;
	protected Rectangle renderBox;
	@Setter
	protected Vector velocity;
	@Setter
	protected double health;
	@Setter
	protected long lastJumpTime;
	@Setter
	protected long lastMoveTime;
	
	public Entity(EntityType type, Location location)
	{
		id=getNewId();
		this.type = type;
		this.location = location;
		hitBox=EntityInfoMap.getHitBox(type);
		renderBox=EntityInfoMap.getRenderBox(type);
		health=EntityInfoMap.getMaxHealth(type);
		velocity=new Vector(0, 0);
	}
	public void jump()
	{
		lastJumpTime=System.currentTimeMillis();
		//TODO
	}
	public void teleport(Location l)
	{
		Preconditions.checkElementIndex(l.getBlockX(), Game.getInstance().getWorld().getWidth());
		Preconditions.checkElementIndex(l.getBlockY(), Game.getInstance().getWorld().getHeight());
		location=l;
		//TODO update rendering; not sure if we need it
	}
}
