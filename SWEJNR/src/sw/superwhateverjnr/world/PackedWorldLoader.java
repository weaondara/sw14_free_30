package sw.superwhateverjnr.world;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.io.FileReader;

public class PackedWorldLoader extends WorldLoader
{

	Boolean notfurtherimplemented = true;
	
	@Override
	public World loadWorld(String name) throws Exception
	{
		String path = "world/" + name;
		InputStream is=SWEJNR.getInstance().getResources().getAssets().open(path);
		
		FileReader fr = new FileReader(is);
		
		String wname = fr.readString();
		int wwidth = fr.readInt();
		int wheight = fr.readInt();
		double cheight = fr.readDouble();
		double cwidth = fr.readDouble();
		Location cloc = new Location(cheight, cwidth);
		Block blocks[][] = new Block[wwidth][wheight];
		
		Constructor<Block> bc = Block.class.getDeclaredConstructor(Location.class, int.class, Byte.class, HashMap.class);
		bc.setAccessible(true);

		if(notfurtherimplemented)
		{
			return null;
		}
		
		for(int x = 0; x < wwidth; x++)
		{
			for(int y = 0; y <wheight; y++)
			{
				Location bloc = new Location(x,y);
				int bid = fr.readInt();
				Byte bsub = fr.readByte();
				int edatac = fr.readInt();
				HashMap edatam = new HashMap<String,Object>();
				for(int d = 0; d < edatac; d++)
				{
					int edatat = fr.readInt();
					String edatas = fr.readString();
					//value edatav = fr.readExtraData();
					//extradata edata = new ExtraData(edatat, edatas, edatav);
				}
				Block b = bc.newInstance(bloc, bid, bsub, edatam);
			}
		}
		
		
		Constructor<World> c = World.class.getDeclaredConstructor(String.class, int.class, int.class, Location.class, Block[][].class);
		c.setAccessible(true);
		World w = c.newInstance(wname, wwidth, wheight, cloc, blocks);
		return w;
		
		//file is string; relative to project/assets
		
		//use sw.superwhateverjnr.io.FileReader for file reading
		
		
		
		/* World format (binary)
		 * -------------------------------
		 * string name
		 * int width
		 * int height
		 * double spawnx
		 * double spawny
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
		 * */
		
	}

}
