package sw.superwhateverjnr.world;

import java.util.Random;

import sw.superwhateverjnr.random.RandomWorldGenerator;

public class RandomWorldLoader extends WorldLoader
{
	private RandomWorldGenerator rwg;
	
	public RandomWorldLoader()
	{
		Random r = new Random();
		hlimit = getHlimit();
		wlimit = getWlimit();
		int maxx = r.nextInt(hlimit);
		int maxy = r.nextInt(wlimit);
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