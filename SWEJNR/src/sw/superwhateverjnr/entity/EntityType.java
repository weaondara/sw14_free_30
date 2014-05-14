package sw.superwhateverjnr.entity;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EntityType
{
	PLAYER(0, Player.class),
	CREEPER(50, null),
	SKELETON(51, null),
	SPIDER(52, null),
	ZOMBIE(54, null),
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
