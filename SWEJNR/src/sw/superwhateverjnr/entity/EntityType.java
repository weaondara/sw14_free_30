package sw.superwhateverjnr.entity;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EntityType
{
	PLAYER(0),
	CREEPER(50),
	SKELETON(51),
	SPIDER(52),
	ZOMBIE(54),
	DROPPED_ITEM(1);
	
	@Getter
	private int id;

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
