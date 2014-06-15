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
    ZOMBIE(54, Zombie.class),
    DROPPED_ITEM(1, Drop.class);
    
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
        
    public static boolean isMob(EntityType e)
    {
        switch(e)
        {
            case CREEPER:
	        case SKELETON:
	        case ZOMBIE:
	            return true;
	        default:
	        	return false;
        }
    }
    
    public boolean isHostile()
    {
        switch(this)
        {
            case CREEPER:
            case SKELETON:
            case ZOMBIE:
                return true;
            default:
                return false;
        }
    }
}
