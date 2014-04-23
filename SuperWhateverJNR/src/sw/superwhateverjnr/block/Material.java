package sw.superwhateverjnr.block;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Material
{
	AIR(0, StandardBlock.class),
	STONE(1, StandardBlock.class);
	
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
	
	public boolean isSolid()
	{
		switch(this)
		{
			case STONE:
				return true;
			default:
				return false;
		}
	}
}
