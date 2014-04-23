package sw.superwhateverjnr.world;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties=true)
@ToString
@EqualsAndHashCode
public class Location
{
	private double x, y;
	public int getBlockX()
	{
		return (int) x;
	}
	public int getBlockY()
	{
		return (int) y;
	}
}
