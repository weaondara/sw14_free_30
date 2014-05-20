package sw.superwhateverjnr.world;

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
	
	private World newWorld(int width, int height, String name) throws Exception
	{
		Block blocks[][] = new Block[width][height];
		
		int spawnHeight = height/2;
		if (spawnHeight > 10)
		{
			spawnHeight = 0;
		}
		
		Location spawn = new Location(0.5, spawnHeight+1);
		
		World w=createWorld(name, width, height, spawn, blocks);
		
		pillar(blocks, w, 0, spawnHeight);
		
		int thisHeight = spawnHeight;
		int nextHeight;
		
		for (int fillWidth = 1; fillWidth < width;)
		{
			int nextConstruct = randomizer.nextInt(2);
			switch(nextConstruct)
			{
			case 0:
			{
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
			}
			case 1:
			{
				//Gaps
			}
			case 2:
			{
				//Steps
			}
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
		
		World w = newWorld(width, height, name);
		
		return w;
	}
}
