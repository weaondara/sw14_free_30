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
    
    public Rectangle translatedTo(double x, double y)
    {
        return new Rectangle(min.getX() + x, min.getY() + y, max.getX() + x, max.getY() + y);
    }
    
    public Rectangle translatedTo(Location l)
    {
        return new Rectangle(min.add(l), max.add(l));
    }
    
    public boolean containsLocation(Location l)
    {
        if(l.getBlockX() < min.getBlockX() || l.getBlockX() > max.getBlockX() ||
           l.getBlockY() < min.getBlockY() || l.getBlockY() > max.getBlockY())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean intersects(Rectangle r)
    {
        if(r.getMax().getX() < min.getX() || r.getMin().getX() > min.getX() || r.getMax().getY() < min.getY() || r.getMin().getY() > max.getY())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean visibleFrom(Location viewPoint)
    {
        return min.visibleFrom(viewPoint) || max.visibleFrom(viewPoint);
    }
}
