package sw.superwhateverjnr.block;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Material
{
	AIR(0, StandardBlock.class),
	STONE(1, StandardBlock.class),
	GRASS(2, StandardBlock.class),
	DIRT(3, StandardBlock.class),
	COBBLESTONE(4, StandardBlock.class),
	WOOD_LOG(5, StandardBlock.class),
	SAPLING(6, StandardBlock.class),
	BEDROCK(7, StandardBlock.class),
	WATER_FLOWING(8, LiquidBlock.class),
	WATER_STANDING(9, LiquidBlock.class),
	LAVA_FlOWING(10, LiquidBlock.class),
	LAVA_STANDING(11, LiquidBlock.class),
	SAND(12, StandardBlock.class),
	GRAVEL(13, StandardBlock.class);
	
	@Getter
	private int id;
	@Getter
	private Class<? extends Block> blockClazz;

	public static Material fromID(int id)
	{
		for(Material mat:values())
		{
			if(mat.id==id)
			{
				return mat;
			}
		}
		return null;
	}
	
	public static boolean isSolid(Material m)
	{
		switch(m)
		{
			case STONE:
			case GRASS:
			case DIRT:
			case COBBLESTONE:
			case WOOD_LOG:
			case BEDROCK:
			case SAND:
			case GRAVEL:
				return true;
			default:
				return false;
		}
	}
	public static boolean isLiquid(Material m)
	{
		switch(m)
		{
			case WATER_FLOWING:
			case WATER_STANDING:
			case LAVA_FlOWING:
			case LAVA_STANDING:
				return true;
			default:
				return false;
		}
	}
        public static boolean onSurface(Material m)
        {
                switch(m)
                {
                        case AIR:
                        case GRASS:
                        case SAND:
                        case GRAVEL:
                        case WATER_STANDING:
                        case LAVA_STANDING:
                              return true;
                        default:
                                return false;
                }
        }
        public static boolean translucent(Material m)
        {
                switch(m)
                {
                        case AIR:
                        case WATER_FLOWING:
                        case WATER_STANDING:
                                return false;
                        default:
                                return true;
                }
        }
        
        public boolean isSolid() 
        {
                return isSolid(this);
        }
        public boolean isLiquid() 
        {
                return isLiquid(this);
        }
        public boolean onSurface() 
        {
                return onSurface(this);
        }
        public boolean translucent()
        {
                return translucent(this);
        }
        
        public Material getSubtop()
	{
		switch(this)
                {
                        case GRASS:
                                return DIRT;
                        default:
                                return this;
                }
	}
	public Material getGround()
	{
		switch(this)
                {
                        default:
                                return STONE;
                }
	}
        public boolean deepUnderground()
        {
                switch(this)
                {
                        case BEDROCK:
                                return true;
                        default:
                                return false;
                }
        }
}
