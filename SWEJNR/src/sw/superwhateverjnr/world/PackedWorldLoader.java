package sw.superwhateverjnr.world;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.io.FileReader;

public class PackedWorldLoader extends WorldLoader
{

	boolean notfurtherimplemented = true;
	
	@Override
	public World loadWorld(String name) throws Exception
	{
		String path = "world/" + name + ".world";
		InputStream is=SWEJNR.getInstance().getResources().getAssets().open(path);
		
		FileReader fr = new FileReader(is);
		
		String wname = fr.readString();
		int width = fr.readInt();
		int height = fr.readInt();
		double spawnx = fr.readDouble();
		double spawny = fr.readDouble();
		Location spawn = new Location(spawnx, spawny);
		Block blocks[][] = new Block[width][height];
		
		Constructor<World> c = World.class.getDeclaredConstructor(String.class, int.class, int.class, Location.class, Block[][].class);
		c.setAccessible(true);
		World w = c.newInstance(wname, width, height, spawn, blocks);

		if(notfurtherimplemented)
		{
			return w;
		}
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y <height; y++)
			{
				int id = fr.readInt();
				Byte subid = fr.readByte();
				int datacount = fr.readInt();
				HashMap<String,Object> data = new HashMap<>();
				for(int d = 0; d < datacount; d++)
				{
					int datatype = fr.readInt();
					String datakey = fr.readString();
					//value datavalue = fr.readExtraData();
					//extradata edata = new ExtraData(edatat, edatas, edatav);
				}
				Block b = BlockFactory.getInstance().create(id, subid, x, y, w, data);
				blocks[x][y]=b;
			}
		}
		
		//entities
		
		return w;
		
		//file is string; relative to project/assets
		
		//use sw.superwhateverjnr.io.FileReader for file reading
		
		
		
		/* World format (binary)
		 * -------------------------------
		 * string name
		 * double spawnx
		 * double spawny
		 * int width
		 * int height
		 * 
		 * loop
		 *     int blockid
		 *     byte subid
		 *     int count extradata 
		 *     loop
		 *         int type extradata
		 *         string dataname
		 *         data extradata
		 *     end loop
		 * end loop
		 * 
		 * int entitycount
		 * loop
		 * 	   int entityid
		 *     double posx
		 *     double poy
		 *     int count extradata 
		 *     loop
		 *         int type extradata
		 *         string dataname
		 *         data extradata
		 *     end loop
		 * end loop
		 * */
		
	}

}
