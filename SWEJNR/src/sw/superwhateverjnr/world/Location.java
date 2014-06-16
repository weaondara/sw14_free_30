package sw.superwhateverjnr.world;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sw.superwhateverjnr.Game;

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
	public Location add(Location l)
	{
		return new Location(this.x+l.x, this.y+l.y);
	}
	public Location substract(Location l)
	{
		return new Location(this.x-l.x, this.y-l.y);
	}
	public Location multiply(double d)
	{
		return new Location(this.x*d, this.y*d);
	}
	public double distance(Location l)
	{
		return Math.sqrt(Math.pow(x - l.x, 2)+Math.pow(y - l.y, 2));
	}
	
	public boolean isInsideWorld(World w)
	{
		return x >= 0 && y >= 0 && x < w.getWidth() && y < w.getHeight();
	}
    
    public boolean visibleFrom(Location viewPoint)
    {
        World w = Game.getInstance().getWorld();
        int iterations = 100;
        int thisx = this.getBlockX();
        int thisy = this.getBlockY();
        int lastx = thisx;
        int lasty = thisy;
        int viewx = viewPoint.getBlockX();
        int viewy = viewPoint.getBlockY();
        double dx = (viewx - thisx)/(double)iterations;
        double dy = (viewy - thisy)/(double)iterations;
        
        for(int i=0; i<iterations; i++)
        {
            thisx = thisx + (int)(i*dx);
            thisy = thisy + (int)(i*dy);
            if(thisx != lastx || thisy != lasty)
            {
                lastx = thisx;
                lasty = thisy;
                if(!w.getBlockAt(thisx, thisy).getType().translucent())
                {
                    return false;
                }
            }
        }
        return true;
    }
}
