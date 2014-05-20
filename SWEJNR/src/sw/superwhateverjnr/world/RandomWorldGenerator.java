package sw.superwhateverjnr.world;

import java.lang.reflect.Constructor;
import java.util.Random;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import lombok.Getter;
import lombok.Setter;

public class RandomWorldGenerator extends WorldLoader
{
	@Getter @Setter
	private int maxHeight = 40;
	@Getter @Setter
	private int maxWidth = 100;
	@Getter @Setter
	private int minHeight = 10;
	@Getter @Setter
	private int minWidth = 40;
	
	private Random randomizer = new Random();
	BlockFactory bf=BlockFactory.getInstance();
	
	private void Pillar(Block blocks[][], World w, int offset, int height) throws Exception
	{
		for(int i = 0; i < height; i++)
		{
			blocks[offset][i] = bf.create(1, (byte)0, 0, i, w, null);
		}
	}
	private void Step(Block blocks[][], World w, int offset, int width, int toHeight) throws Exception
	{
		//TODO
	}
	private void Gap(Block blocks[][], World w, int offset, int width, int toHeight) throws Exception
	{
		//TODO
	}
	
	private World newWorld(int width, int height, String name) throws Exception
	{
		Constructor<World> ctor=World.class.getDeclaredConstructor(String.class, int.class, int.class, Location.class, Block[][].class);
		ctor.setAccessible(true);
		Block blocks[][] = new Block[width][height];
		
		int spawnheight = height/2;
		if (spawnheight > 10)
		{
			spawnheight = 0;
		}
		
		Location spawn = new Location(1.5, spawnheight);
		
		World w=ctor.newInstance(name, width, height, spawn, blocks);
		
		Pillar(blocks, w, 0, spawnheight);		
		
		for(int x = 0; x < width+1; x++)
		{
			for(int y = 0; y < height+1; y++)
			{
				if(blocks[x][y] == null)
				{
					blocks[x][y] = bf.create(0, (byte) 0, x, y, w, null);
				}
			}
		}
		
		return null;
	}
	
	public World loadWorld(String name) throws Exception
	{
		int width = 0, height = 0;
		while(width < minWidth)
		{
			width = randomizer.nextInt(maxWidth);
		}
		while(height < minHeight)
		{
			height = randomizer.nextInt(maxHeight);
		}
		
		newWorld(width, height, name);
		
		return null;
	}
}
