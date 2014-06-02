package sw.superwhateverjnr.entity;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EntityType
{
	UNKNOWN(-1, null),
	
	PLAYER(0, Player.class),
	CREEPER(50, Creeper.class),
	SKELETON(51, Skeleton.class),
	SPIDER(52, Spider.class),
	ZOMBIE(54, Zombie.class),
	DROPPED_ITEM(1, null);
	
	private int id;
	private Class<? extends Entity> entityClazz;

	public static EntityType fromID(int id)
	{
		for(EntityType type:values())
		{
			if(type.id==id)
			{
				return type;
			}
		}
		return null;
	}
	
	public boolean isHostile()
	{
		switch(this)
		{
			case CREEPER:
			case SKELETON:
			case SPIDER:
			case ZOMBIE:
				return true;
			default:
				return false;
		}
	}
}
