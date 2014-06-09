package sw.superwhateverjnr.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sw.superwhateverjnr.block.Material;

public class RandomMaterialGetter
{
	private Random randomizer;
	private Material lastMaterial;
        private List<Material> surfaceMats;
        private List<Material> fillings;
	
	RandomMaterialGetter(long seed)
	{
		randomizer = new Random(seed);
		lastMaterial = Material.GRASS;
                surfaceMats = new ArrayList<Material>();
                fillings = new ArrayList<Material>();
                
                Material m = Material.AIR;
                for(int id = 0;;id++)
                {
                        m = Material.fromID(id);
                        if(m == null)
                        {
                                break;
                        }
                        if(m.onSurface() && m.isSolid()) //preventing Liquids on surface
                        {
                                surfaceMats.add(m);
                        }
                        else if (!m.isSolid())
                        {
                                fillings.add(m);
                        }
                }
	}
	
	void setSeed(long seed)
	{
		randomizer.setSeed(seed);
	}
	
	Material nextSurface()
	{
		boolean nm = randomizer.nextBoolean();
		boolean sure = randomizer.nextBoolean();
		
		if(nm && sure)
		{
			Material thisMaterial = surfaceMats.get(randomizer.nextInt(surfaceMats.size()));
			lastMaterial = thisMaterial;
		}
		return lastMaterial;
	}
        
        Material nextFilling()
        {
                return fillings.get(randomizer.nextInt(fillings.size()));
        }
	
}