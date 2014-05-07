package sw.superwhateverjnr.world;

public class PackedWorldLoader extends WorldLoader
{

	@Override
	public World loadWorld(String name)
	{
		//InputStream is=SWEJNR.getInstance().getResources().getAssets().open(file); //file is string; relative to project/assets
		
		//use sw.superwhateverjnr.io.FileReader for file reading
		
		
		
		/* World format
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
		
		return null;
	}

}
