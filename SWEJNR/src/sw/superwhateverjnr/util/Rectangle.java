package sw.superwhateverjnr.util;

import sw.superwhateverjnr.world.Location;
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
public class Rectangle
{
	private Location min, max;
	public Rectangle(double minx, double miny, double maxx, double maxy)
	{
		this(new Location(minx, miny), new Location(maxx, maxy));
	}
}
