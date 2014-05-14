package sw.superwhateverjnr.entity;

import java.lang.reflect.Constructor;
import java.util.Map;

import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityFactory
{
	@Getter
	private final static EntityFactory instance;
	static 
	{
		instance=new EntityFactory();
	}
	
	public Entity create(int type, int id, double x, double y, World w, Map<String,Object> extradata) throws Exception
	{
		Preconditions.checkElementIndex(id, 256);
		Preconditions.checkElementIndex((int) x, w.getWidth());
		Preconditions.checkElementIndex((int) y, w.getHeight());
		
		EntityType et=EntityType.fromID(id);
		Preconditions.checkNotNull(et, "invalid id");
		
		Constructor<? extends Entity> ctor=et.getEntityClazz().getDeclaredConstructor(int.class, EntityType.class, Location.class, Map.class);
		ctor.setAccessible(true);
		Entity entity=ctor.newInstance(id, et, new Location(x, y), extradata);
		return entity;
	}
}
