package sw.superwhateverjnr.world;

import java.util.List;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Entity;

@Getter
@AllArgsConstructor(suppressConstructorProperties=true, access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class World
{
    @Setter
    private String name;
    private int width, height;
    private Location spawnLocation;
    @Setter
    private Location goal;
    private Block[][] data;
    private List<Entity> entities;
    private long time;
    private long timeElapsed;
    @Setter
    private String bgmfile;
    
    public void setBlockAt(int x, int y, Block b)
    {
        data[x][y] = b;
    }
    public Block getBlockAt(Location loc)
    {
        return getBlockAt(loc.getBlockX(), loc.getBlockY());
    }
    public Block getBlockAt(int x, int y)
    {
        Preconditions.checkElementIndex(x, width, "x is outside the world");
        Preconditions.checkElementIndex(y, height, "y is outside the world");
        
        return data[x][y];
    }
    
    public void tick()
    {
    	timeElapsed++;
    	for(int i=0;i<entities.size();i++)
		{
			Entity e=entities.get(i);
			try
			{
				e.tick();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
    }
    
    @SneakyThrows
    public void createExplosion(Location location, float radius, int method)
    {
        BlockFactory bf = BlockFactory.getInstance();
        Game game=Game.getInstance();
        double x = location.getX();
        double y = location.getY();
        switch (method)
        {
	        case 1: //Rectangle 
	            x -= radius - 1;
	            y += radius + 1;
	            for(int i = 0; i < radius * 2 - 1; i++)
	            {
	                for(int j = 0; j < radius * 2 - 1; j++)
	                {
	                    if (((x + i) >= 0) && ((x + i) < getWidth()) &&
	                        ((y + i) >= 0) && ((y + i) < getHeight()))
	                    {
	                        game.getWorld().setBlockAt((int)(x + i), (int)(y + j), bf.create(Material.AIR.getId(), (byte)0, (int)(x + i), (int)(y + j), game.getWorld(), null));
	                    }
	                }
	            }
	            break;
	        case 2: //Circle 
	            for(float i = -radius; i <= radius; i++)
	            {
	                for(float j = -radius; j <= radius; j++)
	                {
	                	Location l = location.add(i, j);
	                    if (l.isInsideWorld(this) && location.distance(l)<=radius)
	                    {
	                        game.getWorld().setBlockAt((int)(x + i), (int)(y + j), 
	                        		bf.create(Material.AIR.getId(), (byte)0, (int)(x + i), (int)(y + j), game.getWorld(), null));
	                    }
	                }
	            }
	            break;
        }
    }
}