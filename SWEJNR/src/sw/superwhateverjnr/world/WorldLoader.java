package sw.superwhateverjnr.world;

import java.lang.reflect.Constructor;

import lombok.Getter;

import sw.superwhateverjnr.block.Block;

public abstract class WorldLoader
{
	@Getter
	private static final int hlimit = 256;
	@Getter
	private static final int wlimit = 1024;
	@Getter
	private static final int minimum = 10;
	
	public abstract World loadWorld(String name) throws Exception;
	public static World createWorld(String name, int width, int height, Location spawn, Block[][] data) throws Exception
	{
		Constructor<World> ctor=World.class.getDeclaredConstructor(String.class, int.class, int.class, Location.class, Block[][].class);
		ctor.setAccessible(true);
		World w=ctor.newInstance(name, width, height, spawn, data);
		return w;
	}
}
