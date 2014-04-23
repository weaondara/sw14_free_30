package sw.superwhateverjnr.util;

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
	private Point min, max;
	public Rectangle(double minx, double miny, double maxx, double maxy)
	{
		this(new Point(minx, miny), new Point(maxx, maxy));
	}
}
