package sw.superwhateverjnr.world;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityFactory;
import sw.superwhateverjnr.io.FileReader;

public class PackedWorldLoader extends WorldLoader
{
    private static final String bgmFileEnding = ".flac";
        
    @Override
    public World loadWorld(String name) throws Exception
    {
        String path = "world/" + name + ".world";
        InputStream is=SWEJNR.getInstance().getResources().getAssets().open(path);
        
        FileReader fr = new FileReader(is);
        
        String wname = fr.readString();
        
        double spawnx = fr.readDouble();
        double spawny = fr.readDouble();
        Location spawn = new Location(spawnx, spawny);
        
        double goalx = fr.readDouble();
        double goaly = fr.readDouble();
        Location goal = new Location(goalx, goaly);
        
        long time = fr.readLong();
        
        String wbgmfile = fr.readString();
        wbgmfile += bgmFileEnding;
        
        int width = fr.readInt();
        int height = fr.readInt();
    
        Block blocks[][] = new Block[width][height];
        List<Entity> entities = new ArrayList<Entity>();
        
        World w = createWorld(wname, width, height, spawn, goal, blocks, entities, time);
        w.setBgmfile(wbgmfile);
        
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                int id = fr.readInt();
                Byte subid = fr.readByte();
                int datacount = fr.readInt();
                Map<String,Object> data = new HashMap<String,Object>();
                for(int d = 0; d < datacount; d++)
                {
                    int datatype = fr.readInt();
                    String datakey = fr.readString();
                    Object datavalue = readExtraDataEntry(datatype, fr);
                    
                    data.put(datakey, datavalue);
                }
                Block b = BlockFactory.getInstance().create(id, subid, x, y, w, data);
                blocks[x][y]=b;
            }
        }
        
        //entities
        int entitycount=fr.readInt();
        for(int i = 0;i < entitycount; i++)
        {
            int id = fr.readInt();
            int type = fr.readInt();
            double x = fr.readDouble();
            double y = fr.readDouble();
            int datacount = fr.readInt();
            Map<String,Object> data = new HashMap<String,Object>();
            for(int d = 0; d < datacount; d++)
            {
                int datatype = fr.readInt();
                String datakey = fr.readString();
                Object datavalue = readExtraDataEntry(datatype, fr);
                
                data.put(datakey, datavalue);
            }
            
            EntityFactory.getInstance().create(type, id, x, y, w, data);
        }
        
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
         *        int entityid
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
    
    private Object readExtraDataEntry(int type, FileReader fr)
    {
        return null;
//        switch(type)
//        {
//            
//        }
    }

}
