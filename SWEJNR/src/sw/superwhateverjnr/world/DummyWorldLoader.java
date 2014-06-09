package sw.superwhateverjnr.world;

import java.util.ArrayList;
import java.util.List;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityFactory;
import sw.superwhateverjnr.entity.EntityType;

public class DummyWorldLoader extends WorldLoader
{
	@Override
	public World loadWorld(String name) throws Exception
	{
		if(name.equalsIgnoreCase("physicstest"))
		{
			return loadPhysicsTest();
		}
		else if (name.equalsIgnoreCase("jumptest"))
		{
			return loadJumpTest();
		}
		else if (name.equalsIgnoreCase("entitytest"))
		{
			return loadEntityTest();
		}
		else
		{
			return loadDummy();
		}
	}
	
	private World loadPhysicsTest() throws Exception
	{
		int width=39;
		int height=40;
		Location spawn=new Location(1.5, 10);
		Block[][] data=new Block[width][height];
		List<Entity> entities = new ArrayList<Entity>();
		World world=createWorld("physicstest", width, height, spawn, data, entities);
		
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
		
		data[2][10]=bf.create(1, (byte)0, 2, 10, world, null);
		
		data[4][10]=bf.create(1, (byte)0, 4, 10, world, null);
		
		data[7][10]=bf.create(1, (byte)0, 7, 10, world, null);
		data[8][10]=bf.create(1, (byte)0, 8, 10, world, null);
		data[8][11]=bf.create(1, (byte)0, 8, 11, world, null);
		
		data[11][10]=bf.create(1, (byte)0, 11, 10, world, null);
		
		data[13][10]=bf.create(1, (byte)0, 13, 10, world, null);
		data[14][10]=bf.create(1, (byte)0, 14, 10, world, null);
		data[14][11]=bf.create(1, (byte)0, 14, 11, world, null);
		data[15][10]=bf.create(1, (byte)0, 15, 10, world, null);
		data[15][11]=bf.create(1, (byte)0, 15, 11, world, null);
		data[15][12]=bf.create(1, (byte)0, 15, 12, world, null);
		
		data[18][12]=bf.create(1, (byte)0, 18, 12, world, null);
		data[19][12]=bf.create(1, (byte)0, 19, 12, world, null);
		data[20][12]=bf.create(1, (byte)0, 20, 12, world, null);
		data[21][12]=bf.create(1, (byte)0, 21, 12, world, null);
		data[22][12]=bf.create(1, (byte)0, 22, 12, world, null);

		data[21][10]=bf.create(1, (byte)0, 21, 10, world, null);
		
		data[24][10]=bf.create(1, (byte)0, 24, 10, world, null);
		data[25][10]=bf.create(1, (byte)0, 25, 10, world, null);
		data[25][11]=bf.create(1, (byte)0, 25, 11, world, null);
		data[26][10]=bf.create(1, (byte)0, 26, 10, world, null);
		data[26][11]=bf.create(1, (byte)0, 26, 11, world, null);
		data[26][12]=bf.create(1, (byte)0, 26, 12, world, null);

		data[29][12]=bf.create(1, (byte)0, 29, 12, world, null);
		data[30][12]=bf.create(1, (byte)0, 30, 12, world, null);
		data[31][12]=bf.create(1, (byte)0, 31, 12, world, null);
		data[32][12]=bf.create(1, (byte)0, 32, 12, world, null);

		data[33][10]=bf.create(1, (byte)0, 33, 10, world, null);
		data[34][10]=bf.create(1, (byte)0, 34, 10, world, null);
		data[34][11]=bf.create(1, (byte)0, 34, 11, world, null);
		data[35][10]=bf.create(1, (byte)0, 35, 10, world, null);
		data[35][11]=bf.create(1, (byte)0, 35, 11, world, null);
		data[35][12]=bf.create(1, (byte)0, 35, 12, world, null);
		data[36][10]=bf.create(1, (byte)0, 36, 10, world, null);
		data[36][11]=bf.create(1, (byte)0, 36, 11, world, null);
		data[36][12]=bf.create(1, (byte)0, 36, 12, world, null);
		data[37][10]=bf.create(1, (byte)0, 37, 10, world, null);
		data[37][11]=bf.create(1, (byte)0, 37, 11, world, null);
		data[37][12]=bf.create(1, (byte)0, 37, 12, world, null);
		
		return world;
	}
	private World loadJumpTest() throws Exception
	{
		int width=10;
		int height=40;
		Location spawn=new Location(1.5, 11);
		Block[][] data=new Block[width][height];
		List<Entity> entities = new ArrayList<Entity>();
		World world=createWorld("jumpTest", width, height, spawn, data, entities);
		
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
			data[1][10] = bf.create(1, (byte)0, 1, 10, world, null);
			data[3][12] = bf.create(1, (byte)0, 3, 12, world, null);
			data[6][14] = bf.create(1, (byte)0, 6, 14, world, null);
			data[3][16] = bf.create(1, (byte)0, 3, 16, world, null);
		}
		
		return world;
	}
	
	private World loadDummy() throws Exception
	{
		int width=100;
		int height=40;
		Location spawn=new Location(2, 10);
		Block[][] data=new Block[width][height];
		List<Entity> entities = new ArrayList<Entity>();
		World world=createWorld("dummy", width, height, spawn, data, entities);
		
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
	
	private World loadEntityTest() throws Exception
	{
		int width=100;
		int height=40;
		Location spawn=new Location(2, 10);
		Block[][] data=new Block[width][height];
		List<Entity> entities = new ArrayList<Entity>();
		World world=createWorld("entitytest", width, height, spawn, data, entities);
		world.setBgmfile("futsuu.flac");
		
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
		// hardcoded
		int x = 0;
		int y = 0;
		x = 10; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 11; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 12; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 11; y = 11; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 12; y = 11; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 12; y = 12; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		
		x = 14; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 15; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 16; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 17; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 14; y = 11; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 15; y = 11; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 16; y = 11; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 14; y = 12; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 15; y = 12; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 14; y = 13; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		
		x = 21; y = 10; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 22; y = 11; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 23; y = 12; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		x = 24; y = 13; data[x][y] = bf.create(1, (byte)0, x, y, world, null);
		
		
		EntityFactory ef=EntityFactory.getInstance();
		entities.add(ef.create(EntityType.CREEPER.getId(), Entity.getNewId(), 20, 10, world, null));
		entities.add(ef.create(EntityType.ZOMBIE.getId(), Entity.getNewId(), 5, 10, world, null));
		entities.add(ef.create(EntityType.SKELETON.getId(), Entity.getNewId(), 8, 10, world, null));
		//
		return world;
	}
}
