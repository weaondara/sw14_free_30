package sw.superwhateverjnr.random;

import java.util.Random;

import sw.superwhateverjnr.block.Material;

public class RandomMaterialGetter
{
	private Random randomizer;
	private Material lastMaterial;
	
	RandomMaterialGetter(long seed)
	{
		randomizer = new Random(seed);
		lastMaterial = Material.GRASS;
	}
	
	void setSeed(long seed)
	{
		randomizer.setSeed(seed);
	}
	
	Material nextMaterial()
	{
		boolean nm = randomizer.nextBoolean();
		boolean sure = randomizer.nextBoolean();
		
		if(nm && sure)
		{
			Material thisMaterial = Material.GRASS; //Magic!
			lastMaterial = thisMaterial;
		}
		return lastMaterial;
	}
	
	static Material getSubtop(Material m)
	{
		if(m.getId() == Material.GRASS.getId())
		{
			return Material.DIRT;
		}
		return m;
	}
	
	static Material getGround(Material m)
	{
		return Material.STONE;
	}
}