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
		while(minx < minimum || miny < minimum)
		{
			maxx = r.nextInt(wlimit);
			maxy = r.nextInt(hlimit);
			minx = r.nextInt(wlimit);
			miny= r.nextInt(hlimit);
		}
		rwg = new RandomWorldGenerator(minx, maxx, miny, maxy);
	}
	
	public World loadWorld(String name) throws Exception
	{
		World w = null;
		try
		{
			w = rwg.newWorld(name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			w = new DummyWorldLoader().loadWorld("dummy");
		}		
		
		return w;
	}
	
}