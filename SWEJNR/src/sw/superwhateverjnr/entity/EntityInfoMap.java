package sw.superwhateverjnr.entity;

import java.util.HashMap;
import java.util.Map;

import sw.superwhateverjnr.util.Rectangle;

public class EntityInfoMap
{
	private final static Map<EntityType, Rectangle> map;
	static
	{
		map=new HashMap<>();
		map.put(EntityType.PLAYER, new Rectangle(0, 0, 0.5, 1.75));
	}
	public static Rectangle getHitBox(EntityType type)
	{
		return map.get(type);
	}
}
