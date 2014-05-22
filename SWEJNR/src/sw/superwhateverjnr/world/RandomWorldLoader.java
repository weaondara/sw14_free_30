package sw.superwhateverjnr.world;

import java.util.Random;

import sw.superwhateverjnr.random.RandomWorldGenerator;

public class RandomWorldLoader extends WorldLoader
{
	private RandomWorldGenerator rwg;
	
	public RandomWorldLoader()
	{
		Random r = new Random();
		int minx=0, maxx=0, miny=0, maxy=0;
		int hlimit = getHlimit();
		int wlimit = getWlimit();
		int minimum = getMinimum();
		while(minx < minimum)
		{
			maxx = r.nextInt(hlimit);
			maxy = r.nextInt(wlimit);
			minx = r.nextInt(maxx);
			miny= r.nextInt(maxy);
		}
		rwg = new RandomWorldGenerator(minx, maxx, miny, maxy);
	}
	
	public World loadWorld(String name) throws Exception
	{
		
		World w = rwg.newWorld(name);
		
		return w;
	}
	
}