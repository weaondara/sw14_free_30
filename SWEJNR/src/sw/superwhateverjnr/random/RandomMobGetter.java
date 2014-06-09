package sw.superwhateverjnr.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.entity.EntityFactory;
import sw.superwhateverjnr.entity.EntityType;
import static sw.superwhateverjnr.entity.EntityType.isMob;

public class RandomMobGetter
{

    private Random randomizer;
    private static List<EntityType> mobs;

    static
    {
        mobs = new ArrayList<EntityType>();
        EntityType[] entities = EntityType.values();
        for (int i = 0; i < entities.length; i++)
        {
            if (entities[i] == null)
            {
                continue;
            }
            if (isMob(entities[i]))
            {
                mobs.add(entities[i]);
            }
        }
    }

    public RandomMobGetter(long seed)
    {
        randomizer = new Random(seed);
    }

    public EntityType nextMob()
    {
        return mobs.get(randomizer.nextInt(mobs.size()));
    }

    public void setSeed(long seed)
    {
        randomizer.setSeed(seed);
    }
}
