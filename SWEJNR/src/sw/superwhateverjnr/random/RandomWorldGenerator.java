package sw.superwhateverjnr.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Entity;
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
	private RandomMaterialGetter rmg;
	
	public RandomWorldGenerator(int minx, int maxx, int miny, int maxy)
	{
		minWidth = minx;
		maxWidth = maxx;
		minHeight = miny;
		maxHeight = maxy;
		randomizer = new Random();
		rmg = new RandomMaterialGetter(randomizer.nextLong());
		bf = BlockFactory.getInstance();
	}
	
	public World newWorld(long seed) throws Exception
	{
		randomizer.setSeed(seed);
		rmg.setSeed(seed);
		String name="bla";
		
		int width = 0, height = 0;
		while (width < minWidth && height < minHeight)
		{
			width = randomizer.nextInt(maxWidth);
			height = randomizer.nextInt(maxHeight);
		}
		Block blocks[][] = new Block[width][height];
		List<Entity> entities = new ArrayList<>();
		
		int spawnHeight = height/2;
		if (spawnHeight > 10)
		{
			spawnHeight = 0;
		}
		
		Location spawn = new Location(0.5, spawnHeight+1);
		
		World w = WorldLoader.createWorld(name, width, height, spawn, blocks, entities);
		Player ref = new Player(null);
		
		pillar(blocks, w, 0, spawnHeight);
		
		int thisHeight = spawnHeight;
		int nextHeight;
		int jw;
		int mwidth = width - 1;
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
					if(jw > mwidth-fillWidth)
					{
						jw = mwidth-fillWidth;
					}
					nextHeight = thisHeight + (int) ref.getJumpHeight((double) jw);
					if(nextHeight > maxHeight-1)
					{
						nextHeight = maxHeight-1;
					}
					gap(blocks, w, fillWidth, jw, nextHeight, Material.AIR);
					fillWidth += jw;
					break;
				case STEP:
					jw = randomizer.nextInt((int) ref.getJumpWidth((double) thisHeight));
					if(jw > mwidth-fillWidth)
					{
						jw = mwidth-fillWidth;
					}
					nextHeight = thisHeight + (int) ref.getJumpHeight((double) jw);
					if (nextHeight > thisHeight)
					{
						if(nextHeight > maxHeight-1)
						{
							nextHeight = maxHeight-1;
						}						
						step(blocks, w, fillWidth, jw, nextHeight, false);
						fillWidth += jw;
					}
					if(nextHeight < thisHeight)
					{
						step(blocks, w, fillWidth, jw, nextHeight, false);
						fillWidth += jw;
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
	
	public World newWorld(String name) throws Exception
	{
		long seed = (long) name.hashCode();
		return newWorld(seed);
	}
	
	private void pillar(Block blocks[][], World w, int offset, int height) throws Exception
	{
		Material top= rmg.nextMaterial();
		Material subtop = rmg.getSubtop(top);
		Material ground = rmg.getGround(top);
		int i=0;
		while(i <=height-4)
		{
			blocks[offset][i] = bf.create(ground.getId(), (byte)0, offset, i, w, null);
			i++;
		}
		
		while(i <= height-1)
		{
			blocks[offset][i] = bf.create(subtop.getId(), (byte)0, offset, i, w, null);
			i++;
		}
		blocks[offset][i] = bf.create(1, (byte)0, offset, i, w, null);
	}
	
	private void pillar(Block blocks[][], World w, int offset, int height, Material top) throws Exception
	{
		Material subtop = rmg.getSubtop(top);
		Material ground = rmg.getGround(top);
		
		int i=0;
		while(i <=height-4)
		{
			blocks[offset][i] = bf.create(ground.getId(), (byte)0, offset, i, w, null);
			i++;
		}
		
		while(i <= height-1)
		{
			blocks[offset][i] = bf.create(subtop.getId(), (byte)0, offset, i, w, null);
			i++;
		}
		blocks[offset][i] = bf.create(1, (byte)0, offset, i, w, null);
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