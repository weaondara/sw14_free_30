package sw.superwhateverjnr.world;

import java.util.Random;

import sw.superwhateverjnr.random.RandomWorldGenerator;

public class RandomWorldLoader extends WorldLoader
{
	private RandomWorldGenerator rwg;
	
	public RandomWorldLoader()
	{
		Random r = new Random();
		int hlimit = getHlimit();
		int wlimit = getWlimit();
		int minimum = getMinimum();
		rwg = new RandomWorldGenerator(minimum, wlimit, minimum, hlimit);
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