package sw.superwhateverjnr.entity;

import java.util.HashMap;
import java.util.Map;

import sw.superwhateverjnr.util.Rectangle;

public class EntityInfoMap
{
	private final static Map<EntityType, Rectangle> hitBoxes;
	private final static Map<EntityType, Rectangle> renderBoxes;
	private final static Map<EntityType, Double> healthMap;
	static
	{
		hitBoxes=new HashMap<EntityType, Rectangle>();
		renderBoxes=new HashMap<EntityType, Rectangle>();
		healthMap=new HashMap<EntityType, Double>();
		
		hitBoxes.put(EntityType.PLAYER, new Rectangle(0, 0, 0.469, 1.875));
		hitBoxes.put(EntityType.CREEPER, new Rectangle(0, 0, 0.75, 1.625));
		hitBoxes.put(EntityType.SKELETON, new Rectangle(0, 0, 0.875, 2));
		hitBoxes.put(EntityType.ZOMBIE, new Rectangle(0, 0, 0.891, 2));
        hitBoxes.put(EntityType.DROPPED_ITEM, new Rectangle(0,0,0.5,0.5));
		
		
		renderBoxes.put(EntityType.PLAYER, new Rectangle(0, 0, 1.5, 1.75));
		renderBoxes.put(EntityType.CREEPER, new Rectangle(0, 0, 1.5, 1.75));
		renderBoxes.put(EntityType.SKELETON, new Rectangle(0, 0, 1.5, 1.75));
		renderBoxes.put(EntityType.ZOMBIE, new Rectangle(0, 0, 1.5, 1.75));
        renderBoxes.put(EntityType.DROPPED_ITEM, new Rectangle(0,0,0.5,0.5));
		
		
		healthMap.put(EntityType.PLAYER, 20D);
		healthMap.put(EntityType.CREEPER, 20D);
		healthMap.put(EntityType.SKELETON, 20D);
		healthMap.put(EntityType.ZOMBIE, 20D);
        healthMap.put(EntityType.DROPPED_ITEM, 1D);
	}
	public static Rectangle getHitBox(EntityType type)
	{
		return hitBoxes.get(type);
	}
	public static Rectangle getRenderBox(EntityType type)
	{
		return renderBoxes.get(type);
	}
	public static Double getMaxHealth(EntityType type)
	{
		return healthMap.get(type);
	}
}
