package sw.superwhateverjnr.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sw.superwhateverjnr.block.Material;
import static sw.superwhateverjnr.block.Material.isSolid;
import static sw.superwhateverjnr.block.Material.onSurface;

public class RandomMaterialGetter
{

    private Random randomizer;
    private static List<Material> surfaceMats;
    private static List<Material> fillings;

    static
    {
        surfaceMats = new ArrayList<Material>();
        fillings = new ArrayList<Material>();
        Material[] materials = Material.values();
        for (int i = 0; i < materials.length; i++)
        {
            if (materials[i] == null)
            {
                continue;
            }
            if (isSolid(materials[i]) && onSurface(materials[i]))
            {
                surfaceMats.add(materials[i]);
            }
            else if (onSurface(materials[i]))
            {
                fillings.add(materials[i]);
            }

        }
    }
    private Material lastMaterial;

    public RandomMaterialGetter(long seed)
    {
        randomizer = new Random(seed);
        lastMaterial = Material.GRASS;
    }

    public void setSeed(long seed)
    {
        randomizer.setSeed(seed);
    }

    public Material nextSurface()
    {
        boolean nm = randomizer.nextBoolean();
        boolean sure = randomizer.nextBoolean();

        if (nm && sure)
        {
            Material thisMaterial = surfaceMats.get(randomizer.nextInt(surfaceMats.size()));
            lastMaterial = thisMaterial;
        }
        return lastMaterial;
    }

    public Material nextFilling()
    {
        return fillings.get(randomizer.nextInt(fillings.size()));
    }
}
