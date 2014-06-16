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
        return this.intersect(r) != null;
    }
    
    public boolean visibleFrom(Location viewPoint)
    {
        return min.visibleFrom(viewPoint) || max.visibleFrom(viewPoint);
    }

	public Rectangle intersect(Rectangle r)
	{
		double x1 = Math.max(min.getX(), r.min.getX());
		double x2 = Math.min(max.getX(), r.max.getX());
		double y1 = Math.max(min.getY(), r.min.getY());
		double y2 = Math.min(max.getY(), r.max.getY());
		if(x1 < x2 && y1 < y2)
		{
			return new Rectangle(x1, y1, x2, y2);
		}
		
		return null;
	}

	public boolean noArea()
	{
		return width() < 0 && height() < 0;
	}
	
	public double width()
	{
		return max.getX()-min.getX();
	}
	
	public double height()
	{
		return max.getY()-min.getY();
	}
}
