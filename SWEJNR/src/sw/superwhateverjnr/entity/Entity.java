package sw.superwhateverjnr.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import lombok.ToString;
import sw.superwhateverjnr.util.Rectangle;
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
	protected double health;
	protected long lastJumpTime;
	
	public Entity(EntityType type, Location location)
	{
		id=getNewId();
		this.type = type;
		this.location = location;
		hitBox=EntityInfoMap.getHitBox(type);
		renderBox=EntityInfoMap.getRenderBox(type);
		health=EntityInfoMap.getMaxHealth(type);
	}
	public void jump()
	{
		lastJumpTime=System.currentTimeMillis();
		//TODO
	}
	public void teleport(Location l)
	{
		location=l;
		//TODO check if outside the world; if true then throw invalid arg exception
		//TODO update rendering; not sure if we need it
	}
}
