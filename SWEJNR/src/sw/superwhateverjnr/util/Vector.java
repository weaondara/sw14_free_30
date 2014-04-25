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
public class Vector
{
	private double x, y;
	public double length()
	{
		return Math.sqrt(x*x+y*y);
	}
}
