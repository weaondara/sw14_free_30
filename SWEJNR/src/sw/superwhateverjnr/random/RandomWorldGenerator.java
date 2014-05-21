package sw.superwhateverjnr.random;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import sw.superwhateverjnr.world.WorldLoader;

public class RandomWorldGenerator
{
	@Getter @Setter
	private int maxHeight;
	@Getter @Setter
	private int maxWidth;
	@Getter @Setter
	private int minHeight;
	@Getter @Setter
	private int minWidth;
	
	private Random randomizer;
	private BlockFactory bf;
	
	public RandomWorldGenerator(int minx, int maxx, int miny, int maxy)
	{
		minWidth = minx;
		maxWidth = maxx;
		minHeight = miny;
		maxHeight = maxy;
		randomizer = new Random();
		bf = BlockFactory.getInstance();
	}
	
	public World newWorld(String name) throws Exception
	{
		long seed = (long) name.hashCode();
		randomizer.setSeed(seed);
		
		int width = 0, height = 0;
		while (width < minWidth && height < minHeight)
		{
			width = randomizer.nextInt(maxWidth);
			height = randomizer.nextInt(maxHeight);
		}
		Block blocks[][] = new Block[width][height];
		
		int spawnHeight = height/2;
		if (spawnHeight > 10)
		{
			spawnHeight = 0;
		}
		
		Location spawn = new Location(0.5, spawnHeight+1);
		
		World w = WorldLoader.createWorld(name, width, height, spawn, blocks);
		
		pillar(blocks, w, 0, spawnHeight);
		
		int thisHeight = spawnHeight;
		int nextHeight;
		
		for (int fillWidth = 1; fillWidth < width;)
		{
			int nextConstruct = randomizer.nextInt(2);
			switch(nextConstruct)
			{
				case 0:
					nextHeight = randomizer.nextInt(thisHeight+2);//Assuming Jump Height 2.
					while (nextHeight < thisHeight - 3)
					{
						nextHeight = randomizer.nextInt(thisHeight+2);
					}
					if(nextHeight > maxHeight - 2)
					{
						nextHeight = maxHeight - 2;
					}
					pillar(blocks, w, fillWidth, nextHeight);
					thisHeight = nextHeight;
					fillWidth++;
					break;
				case 1:
					//Gaps
					break;
				case 2:
					//Steps
					break;
			}
		}
		
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				if(blocks[x][y] == null)
				{
					blocks[x][y] = bf.create(0, (byte) 0, x, y, w, null);
				}
			}
		}
		
		return w;
	}
	
	private void pillar(Block blocks[][], World w, int offset, int height) throws Exception
	{
		for(int i = 0; i <= height; i++)
		{
			blocks[offset][i] = bf.create(1, (byte)0, offset, i, w, null);
		}
	}
	private void step(Block blocks[][], World w, int offset, int width, int toHeight) throws Exception
	{
		//TODO
	}
	private void gap(Block blocks[][], World w, int offset, int width, int toHeight) throws Exception
	{
		//TODO
	}
}