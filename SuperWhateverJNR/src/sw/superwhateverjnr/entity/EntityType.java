package sw.superwhateverjnr.entity;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EntityType
{
	PLAYER(0);
	
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
}
