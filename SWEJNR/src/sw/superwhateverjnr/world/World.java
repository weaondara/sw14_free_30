package sw.superwhateverjnr.world;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
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
import sw.superwhateverjnr.entity.DamageCause;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.util.Rectangle;

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
    public List<Entity> getEntitiesAt(Location l)
    {
        List<Entity> ents = new ArrayList<Entity>();
        for(int i=0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            Rectangle r = e.getHitBox().translatedTo(e.getLocation());
            if(r.containsLocation(l))
            {
                ents.add(e);
            }
        }
        return ents;
        
    }
    public List<Entity> getEntitiesAt(int x, int y)
    {
        return getEntitiesAt(new Location(x,y));
    }
    public List<Entity> getAllEntitiesAt(Location l)
    {
        List<Entity> ents = getEntitiesAt(l);
        Entity player = Game.getInstance().getPlayer();
        if(player.getHitBox().translatedTo(player.getLocation()).containsLocation(l))
        {
            ents.add(player);
        }
        return ents;
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
        List<Entity> damageTaking = new ArrayList<Entity>();
        List<Double> distances = new ArrayList<Double>();
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
                        List<Entity> atThisBlock = getAllEntitiesAt(l);
                        for(int it = 0; it < atThisBlock.size(); it++)
                        {
                            if(!damageTaking.contains(atThisBlock.get(it)))
                            {
                                damageTaking.add(atThisBlock.get(it));
                                distances.add(atThisBlock.get(it).getLocation().distance(location));
                            }
                        }
                    }
                }
                break;
        }
        for(int i = 0; i < damageTaking.size(); i++)
        {
            damageTaking.get(i).takeDamage(DamageCause.EXPLOSION, distances.get(i));
        }
    }
    
    public long getTimeRemaining()
    {
    	return time-timeElapsed;
    }
}