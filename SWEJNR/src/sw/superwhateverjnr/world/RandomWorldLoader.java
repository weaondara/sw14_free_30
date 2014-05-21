package sw.superwhateverjnr.world;

import java.util.Random;

import sw.superwhateverjnr.random.RandomWorldGenerator;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import lombok.Getter;
import lombok.Setter;

public class RandomWorldLoader extends WorldLoader
{
	private RandomWorldGenerator rwg;
	
	RandomWorldLoader()
	{
		Random r = new Random();
		int maxx = r.nextInt();
		int maxy = r.nextInt();
		int limit = getLimit();
		if (maxx > limit)
		{
			maxx = limit;
		}
		if (maxy > limit)
		{
			maxy = limit;
		}
		int minx = r.nextInt(maxx);
		int miny= r.nextInt(maxy);
		rwg = new RandomWorldGenerator(minx, maxx, miny, maxy);
	}
	
	public World loadWorld(String name) throws Exception
	{
		
		World w = rwg.newWorld(name);
		
		return w;
	}
	
}