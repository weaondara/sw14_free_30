package sw.superwhateverjnr.entity;

import java.util.Map;

import sw.superwhateverjnr.world.Location;

public class HostileEntity extends Entity
{
	public HostileEntity(EntityType type, Location location, Map<String, Object> extraData)
	{
		this(getNewId(), type, location, extraData);
	}
	public HostileEntity(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(id, type, location, extraData);
	}
}
