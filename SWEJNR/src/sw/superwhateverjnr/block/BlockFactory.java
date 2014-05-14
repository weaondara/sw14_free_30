package sw.superwhateverjnr.block;

import java.lang.reflect.Constructor;
import java.util.Map;

import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockFactory
{
	@Getter
	private final static BlockFactory instance;
	static 
	{
		instance=new BlockFactory();
	}
	
	public Block create(int id, byte subid, int x, int y, World w, Map<String,Object> extradata) throws Exception
	{
		Preconditions.checkElementIndex(id, 256);
		Preconditions.checkElementIndex(x, w.getWidth());
		Preconditions.checkElementIndex(y, w.getHeight());
		
		Material mat=Material.fromID(id);
		Preconditions.checkNotNull(mat, "invalid id");
		
		Constructor<? extends Block> ctor=mat.getBlockClazz().getDeclaredConstructor(Location.class, Material.class, byte.class, Map.class);
		ctor.setAccessible(true);
		Block block=ctor.newInstance(new Location(x, y), mat, subid, extradata);
		return block;
	}
}
