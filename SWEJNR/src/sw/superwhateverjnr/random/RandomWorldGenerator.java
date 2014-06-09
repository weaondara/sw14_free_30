package sw.superwhateverjnr.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityFactory;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import sw.superwhateverjnr.world.WorldLoader;

public class RandomWorldGenerator
{

    @AllArgsConstructor(suppressConstructorProperties = true)
    private enum Structure
    {

        PILLAR(0),
        GAP(1),
        STEP(2),
        PLATEAU(3);

        @Getter
        private int id;

        public static Structure fromId(int id)
        {
            for (Structure struct : values())
            {
                if (struct.id == id)
                {
                    return struct;
                }
            }
            return null;
        }
    }

    @Getter @Setter
    private int maxHeight;
    @Getter @Setter
    private int maxWidth;
    @Getter @Setter
    private int minHeight;
    @Getter @Setter
    private int minWidth;

    private int lastSpawn = 0;
    private int spawnDistance;

    private Random randomizer;
    private static BlockFactory bf;
    private static EntityFactory ef;

    static
    {
        bf = BlockFactory.getInstance();
        ef = EntityFactory.getInstance();
    }
    private RandomMaterialGetter rmg;
    private RandomMobGetter rmobg;

    public RandomWorldGenerator(int minx, int maxx, int miny, int maxy)
    {
        minWidth = minx;
        maxWidth = maxx;
        minHeight = miny;
        maxHeight = maxy;
        randomizer = new Random();
        rmg = new RandomMaterialGetter(0);
        rmobg = new RandomMobGetter(0);
    }

    @SneakyThrows
    public World newWorld(long seed)
    {
        randomizer.setSeed(seed);
        rmg.setSeed(seed);
        rmobg.setSeed(seed);

        String name = String.valueOf(seed);

        int width = 0, height = 0;
        while (width < minWidth || height < minHeight)
        {
            width = randomizer.nextInt(maxWidth);
            height = randomizer.nextInt(maxHeight);
        }
        Block blocks[][] = new Block[width][height];

        int spawnHeight = height / 2;
        if (spawnHeight > 10)
        {
            spawnHeight = 0;
        }
        spawnDistance = randomizer.nextInt(5) + 5;

        Location spawn = new Location(0.5, spawnHeight + 1);

        World w = WorldLoader.createWorld(name, width, height, spawn, blocks, new ArrayList<Entity>());
        String[] music = SWEJNR.getInstance().getAssets().list("music");
        if (music.length != 0)
        {
            w.setBgmfile(music[randomizer.nextInt(music.length)]);
        }
        Player ref = new Player(null);

        pillar(blocks, w, 0, spawnHeight);

        int thisHeight = spawnHeight;
        int nextHeight;
        int jw;
        int jh = (int) ref.getJumpMaxHeight();
        int mwidth = width - 1;
        for (int fillWidth = 1; fillWidth < width;)
        {
            Structure nextConstruct = Structure.fromId(randomizer.nextInt(Structure.values().length));
            switch (nextConstruct)
            {
                case PILLAR:
                    nextHeight = randomizer.nextInt(thisHeight + jh);
                    while (nextHeight < thisHeight - 3)
                    {
                        nextHeight = randomizer.nextInt(thisHeight + jh);
                    }
                    if (nextHeight > maxHeight - 2)
                    {
                        nextHeight = maxHeight - 2;
                    }
                    pillar(blocks, w, fillWidth, nextHeight);
                    thisHeight = nextHeight;
                    fillWidth++;
                    break;
                case GAP:
                    jw = 0;
                    while (!(jw > 0))
                    {
                        jw = randomizer.nextInt((int) ref.getJumpWidth((double) thisHeight));
                    }
                    if (jw > mwidth - fillWidth)
                    {
                        jw = mwidth - fillWidth;
                    }
                    nextHeight = thisHeight + (int) ref.getJumpHeight((double) jw);
                    if (nextHeight > maxHeight - 1)
                    {
                        nextHeight = maxHeight - 1;
                    }
                    gap(blocks, w, fillWidth, jw, thisHeight, nextHeight, rmg.nextFilling());
                    fillWidth += jw + 1;
                    break;
                case STEP:
                    jw = randomizer.nextInt((int) ref.getJumpWidth((double) thisHeight));
                    if (jw > mwidth - fillWidth)
                    {
                        jw = mwidth - fillWidth;
                    }
                    nextHeight = thisHeight + (int) ref.getJumpHeight((double) jw);
                    if (nextHeight > maxHeight - 1)
                    {
                        nextHeight = maxHeight - 1;
                    }
                    step(blocks, w, fillWidth, jw, nextHeight, false);
                    fillWidth += jw + 1;
                    break;
                case PLATEAU:
                    int maxPlatWidth = Math.max(5, width / 10);
                    int platWidth = randomizer.nextInt(maxPlatWidth);
                    if (platWidth > mwidth - fillWidth)
                    {
                        platWidth = mwidth - fillWidth;
                    }
                    nextHeight = randomizer.nextInt(thisHeight + jh);
                    if (nextHeight > maxHeight - 2)
                    {
                        nextHeight = maxHeight - 2;
                    }
                    plateau(blocks, w, fillWidth, platWidth, nextHeight, rmg.nextSurface());
                    fillWidth += platWidth + 1;
                    thisHeight = nextHeight;
            }
        }

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (blocks[x][y] == null)
                {
                    blocks[x][y] = bf.create(0, (byte) 0, x, y, w, null);
                }
            }
        }

        return w;
    }

    @SneakyThrows
    public World newWorld(String name)
    {
        long seed;
        try
        {
            seed = Long.parseLong(name);
        }
        catch (Exception e)
        {
            seed = (long) name.hashCode();
        }
        World w = newWorld(seed);
        w.setName(name);
        return w;
    }

    @SneakyThrows
    private void pillar(Block blocks[][], World w, int offset, int height)
    {
        Material top = rmg.nextSurface();
        pillar(blocks, w, offset, height, top);
    }

    @SneakyThrows
    private void pillar(Block blocks[][], World w, int offset, int height, Material top)
    {
        Material subtop = top.getSubtop();
        Material ground = top.getGround();

        int i = 0;
        while (i <= height - 4)
        {
            blocks[offset][i] = bf.create(ground.getId(), (byte) 0, offset, i, w, null);
            i++;
        }

        while (i <= height - 1)
        {
            blocks[offset][i] = bf.create(subtop.getId(), (byte) 0, offset, i, w, null);
            i++;
        }
        blocks[offset][i] = bf.create(top.getId(), (byte) 0, offset, i, w, null);
        if (top.isSolid())
        {
            if (randomizer.nextBoolean() && lastSpawn > spawnDistance && height < maxHeight + 2 && height > 0)
            {
                try
                {
                    w.getEntities().add(ef.create(rmobg.nextMob().getId(), Entity.getNewId(), offset + 0.5, height + 1, w, null));
                    lastSpawn = 0;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                lastSpawn++;
            }
        }
    }

    @SneakyThrows
    private void gap(Block blocks[][], World w, int offset, int width, int fromHeight, int toHeight, Material filling)
    {
        //Dammmit Yukari!
        int fillDepth = Math.min(fromHeight, toHeight);
        try
        {
            if (!blocks[offset - 1][0].getType().isSolid())
            {
                filling = blocks[offset - 1][0].getType();
            }
        }
        catch (NullPointerException e)
        {
            filling = Material.AIR;
        }
        for (int i = 0; i < width; i++)
        {
            pillar(blocks, w, offset + i, fillDepth, filling);
        }
        pillar(blocks, w, offset + width, toHeight);
    }

    @SneakyThrows
    private void step(Block blocks[][], World w, int offset, int width, int toHeight, boolean left)
    {
        Material m = rmg.nextSurface();
        if (left)
        {
            offset -= width;
        }
        else
        {
            offset += width;
        }
        blocks[offset][toHeight] = bf.create(m.getId(), (byte) 0, offset, toHeight, w, null);
    }

    @SneakyThrows
    private void plateau(Block blocks[][], World w, int offset, int width, int height, Material top)
    {
        for (int i = 0; !(i > width); i++)
        {
            pillar(blocks, w, offset + i, height, top);
        }
    }
}
