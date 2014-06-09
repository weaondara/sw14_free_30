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
public class Location implements Cloneable
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
	public Location clone()
	{
		return new Location(x, y);
	}
	public Location add(double x, double y)
	{
		return new Location(this.x+x, this.y+y);
	}
	public double distance(Location l)
	{
		return Math.sqrt(Math.pow(x - l.x, 2)+Math.pow(y - l.y, 2));
	}
}
