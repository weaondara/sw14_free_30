package sw.superwhateverjnr.block;

import java.util.Map;

import sw.superwhateverjnr.world.Location;

public class LiquidBlock extends Block
{
	protected LiquidBlock(Location location, Material type)
	{
		super(location, type);
	}
	protected LiquidBlock(Location location, Material type, byte subid, Map<String, Object> extraData)
	{
		super(location, type, subid, extraData);
	}
}
