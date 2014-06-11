package sw.superwhateverjnr.world;

import java.lang.reflect.Constructor;
import java.util.List;

import lombok.Getter;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.entity.Entity;

public abstract class WorldLoader
{
    @Getter
    private static final int hlimit = 256;
    @Getter
    private static final int wlimit = 1024;
    @Getter
    private static final int minimum = 30;
    
    public abstract World loadWorld(String name) throws Exception;
    public static World createWorld(String name, int width, int height, Location spawn, Location goal, Block[][] data, List<Entity> entities, long time) throws Exception
    {
        Constructor<World> ctor=World.class.getDeclaredConstructor(String.class, int.class, int.class, Location.class, Location.class, Block[][].class, List.class, long.class, long.class, String.class);
        ctor.setAccessible(true);
        World w=ctor.newInstance(name, width, height, spawn, goal, data, entities, time, 0, null);
        return w;
    }
}
