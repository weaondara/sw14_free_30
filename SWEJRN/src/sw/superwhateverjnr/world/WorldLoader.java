package sw.superwhateverjnr.world;

import java.lang.reflect.Constructor;

import sw.superwhateverjnr.block.Block;

public abstract class WorldLoader
{
	public abstract World loadWorld(String name) throws Exception;
	protected World createWorld(String name, int width, int height, Location spawn, Block[][] data) throws Exception
	{
		Constructor<World> ctor=World.class.getDeclaredConstructor(String.class, int.class, int.class, Location.class, Block[][].class);
		ctor.setAccessible(true);
		World w=ctor.newInstance(name, width, height, spawn, data);
		return w;
	}
}
