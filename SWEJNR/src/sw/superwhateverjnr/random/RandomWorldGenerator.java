package sw.superwhateverjnr.random;

import java.util.Map;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import sw.superwhateverjnr.world.WorldLoader;

public class RandomWorldGenerator
{
	@AllArgsConstructor(suppressConstructorProperties=true)
	private enum Structure 
	{
		PILLAR(0),
		GAP(1),
		STEP(2);
		
		@Getter
		private int id;
		
		public static Structure fromId(int id)
		{
			for(Structure struct:values())
			{
				if(struct.id==id)
				{
					return struct;
				}
			}
			return null;
		}
	}
	
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
		Player ref = new Player(null);
		
		pillar(blocks, w, 0, spawnHeight);
		
		int thisHeight = spawnHeight;
		int nextHeight;
		int jw;
		
		for (int fillWidth = 1; fillWidth < width;)
		{
			Structure nextConstruct = Structure.fromId(randomizer.nextInt(Structure.values().length));
			switch(nextConstruct)
			{
				case PILLAR:
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
				case GAP:
					jw = randomizer.nextInt((int) ref.getJumpWidth((double) thisHeight));
					if(jw > width-fillWidth)
					{
						jw = width-fillWidth;
					}
					nextHeight = thisHeight + (int) ref.getJumpHeight((double) jw);
					gap(blocks, w, fillWidth, jw, nextHeight, Material.AIR);
					fillWidth += jw;
					break;
				case STEP:
					jw = randomizer.nextInt((int) ref.getJumpWidth((double) thisHeight));
					if(jw > width-fillWidth)
					{
						jw = width-fillWidth;
					}
					nextHeight = thisHeight + (int) ref.getJumpHeight((double) jw);
					if (nextHeight > thisHeight)
					{
						
//						boolean doubled = false;
//						if(nextHeight > thisHeight+2)
//						{
//							doubled = randomizer.nextBoolean();
//						}
//						if(doubled)
//						{
//							//TODO
//						}
//						else
						{
							step(blocks, w, fillWidth, jw, nextHeight, false);
							fillWidth += jw;
						}
					}
					if(nextHeight < thisHeight)
					{
//						boolean tripled = false;
//						if(nextHeight < thisHeight+2)
//						{
//							tripled = randomizer.nextBoolean();
//						}
//						if(tripled)
//						{
//							//TODO
//						}
//						else
						{
							step(blocks, w, fillWidth, jw, nextHeight, false);
							fillWidth += jw;
						}
					}
					
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
	
	private void pillar(Block blocks[][], World w, int offset, int height, Material m) throws Exception
	{
		for(int i = 0; i <= height; i++)
		{
			blocks[offset][i] = bf.create(m.getId(), (byte)0, offset, i, w, null);
		}
	}
	
	private void gap(Block blocks[][], World w, int offset, int width, int toHeight, Material filling) throws Exception
	{
		for (int i = 0; i <= toHeight; i++)
		{
			pillar(blocks, w, offset+width, toHeight, filling);
		}
		pillar(blocks, w, offset+width, toHeight);
	}
	
	private void step(Block blocks[][], World w, int offset, int width, int toHeight, boolean left) throws Exception
	{
		if(left)
		{
			offset -= width;
		}
		else
		{
			offset += width;
		}
		blocks[offset][toHeight] = bf.create(1, (byte)0, offset, toHeight, w, null);	
	}
}