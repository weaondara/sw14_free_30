package sw.superwhateverjnr.world;

import sw.superwhateverjnr.block.Block;

public class DummyWorldLoader extends WorldLoader
{
	@Override
	public World loadWorld(String name) throws Exception
	{
		int width=100;
		int height=40;
		Location spawn=new Location(2, 2);
		Block[][] data=new Block[width][height];
		World world=createWorld("dummy", width, height, spawn, data);
		
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				if(y<10)
				{
					//Block b=new Block();
				}
			}
		}
		
		return world;
	}
}
