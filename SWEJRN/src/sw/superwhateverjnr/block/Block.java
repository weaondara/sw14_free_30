package sw.superwhateverjnr.block;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.AccessLevel;

import sw.superwhateverjnr.world.Location;

@Getter
@RequiredArgsConstructor(suppressConstructorProperties=true, access=AccessLevel.PROTECTED)
@AllArgsConstructor(suppressConstructorProperties=true, access=AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public abstract class Block
{
	@NonNull
	private Location location;
	@NonNull
	private Material type;
	
	private byte subid;
	private Map<String,Object> extraData;
	
	public Map<String,Object> getExtraData()
	{
		return extraData==null ? new HashMap<String,Object>() : extraData;
	}
}
