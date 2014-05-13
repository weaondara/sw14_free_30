package sw.superwhateverjnr.world;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;

public class DummyWorldLoader extends WorldLoader
{
	@Override
	public World loadWorld(String name) throws Exception
	{
		int width=100;
		int height=40;
		Location spawn=new Location(2, 10);
		Block[][] data=new Block[width][height];
		World world=createWorld("dummy", width, height, spawn, data);
		
		BlockFactory bf=BlockFactory.getInstance();
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				if(y<10 || x==0 || x+1==width)
				{
					Block b=bf.create(1, (byte)0, x, y, world, null);
					data[x][y]=b;
				}
				else
				{
					Block b=bf.create(0, (byte)0, x, y, world, null);
					data[x][y]=b;
				}
			}
		}
		
		return world;
	}
}
