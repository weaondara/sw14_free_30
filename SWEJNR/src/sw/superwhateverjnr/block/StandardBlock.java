package sw.superwhateverjnr.block;

import java.util.Map;

import sw.superwhateverjnr.world.Location;

public class StandardBlock extends Block
{
	protected StandardBlock(Location location, Material type)
	{
		super(location, type);
	}
	protected StandardBlock(Location location, Material type, byte subid, Map<String, Object> extraData)
	{
		super(location, type, subid, extraData);
	}
}
