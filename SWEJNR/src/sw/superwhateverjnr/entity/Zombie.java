package sw.superwhateverjnr.entity;

import java.util.Map;
import sw.superwhateverjnr.Game;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;

public class Zombie extends Entity
{
	//-------------------------- animation ------------------------------
	protected static final double radiant = 2 * Math.PI / 180;
	protected static double angleRotationMoving=2.5;
	protected static double angleRotationStanding=0.5;

	protected static double maxArmAngleMoving=6;
	protected static double maxArmAngleStanding=6;
	
	protected static double maxLegAngleMoving=30;
	protected static double maxLegAngleStanding=0;
	
	
	public double getAngleRotationMoving()
	{
		return angleRotationMoving;
	}
	public double getAngleRotationStanding()
	{
		return angleRotationStanding;
	}
	public double getMaxArmAngleMoving()
	{
		return maxArmAngleMoving;
	}
	public double getMaxArmAngleStanding()
	{
		return maxArmAngleStanding;
	}
	public double getMaxLegAngleMoving()
	{
		return maxLegAngleMoving;
	}
	public double getMaxLegAngleStanding()
	{
		return maxLegAngleStanding;
	}
	
	//-------------------------- movement ------------------------------
	private final static double runningMin = 0.75;
	private final static double runningMax = 2.25;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	private final static double radius = 6.0;
	
	private Player player;
	private static boolean isindistance = false;
	
	public Zombie(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(id, EntityType.ZOMBIE, location, extraData);
		
		triggerRadius = 6;
	}

	@Override
	protected void die()
	{
		super.die();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		trigger();
		// randomJump();
		tickMove();
	}
	
	private long ticks;
	protected void trigger()
	{
		//dump left right run
		if(ticks % 200 == 0)
		{
			setMovingright(false);
			setMovingleft(true);
			setLookingRight(false);
		}
		else if((ticks - 100) % 200 == 0)
		{
			setMovingright(true);
			setMovingleft(false);
			setLookingRight(true);
		}
		ticks++;
	}
	/*
	protected void randomJump()
	{
		if (isindistance)
		{
			//do some cranky stuff
		}
	}
	*/
	private void tickMove()
	{
		if(location==null || world()==null)
		{
			return;
		}
		Rectangle bounds=getHitBox();
		long now=System.currentTimeMillis();
		long time=Game.TICK_INTERVAL;
		
		double vx=velocity.getX();
		if(isMovingleft() && !isMovingright())
		{
			if(vx>-runningMin)
			{
				vx=-runningMin;
			}
			else
			{
				vx*=(1+runPower*time*(runningMax+vx));
			}
		}
		else if(isMovingright() && !isMovingleft())
		{
			if(vx<runningMin)
			{
				vx=runningMin;
			}
			else
			{
				vx*=(1+runPower*time*(runningMax-vx));
			}
		}
		else //x decelerate
		{
			double d=runPower*time*(Math.abs(vx)+runningMin);
			d*=3;
			if(d>1)
			{
				d=1;
			}
			else if(d<0)
			{
				d=0;
			}
			
			vx*=(1-d);
		}

		getVelocity().setX(vx);
        
		float multiplier=0.01F;
		float playerwidth=(float) (Math.abs(bounds.getMin().getX()-bounds.getMax().getX()));
		
		//world check
		double x=location.getX();
        x+=velocity.getX()*multiplier;
		if(x<0)
		{
			x=0;
			velocity.setX(0);
		}
		if(x>=world().getWidth())
		{
			x=world().getWidth()-0.0000001;
			velocity.setX(0);
		}
		
		//block check
		try
		{
		Location l1=new Location(x-playerwidth/2,location.getY());
		Location l2=new Location(x-playerwidth/2,location.getY()+1);
		Block b1=world().getBlockAt(l1);
		Block b2=world().getBlockAt(l2);
		if(b1.getType().isSolid() || b2.getType().isSolid())
		{
			if(velocity.getX()<0)
			{
				x=Math.ceil(x-playerwidth/2)+playerwidth/2;
				velocity.setX(0);
			}
		}
		}catch(Exception e){}
		
		try
		{
		Location l3=new Location(x+playerwidth/2,location.getY());
		Location l4=new Location(x+playerwidth/2,location.getY()+1);
		Block b3=world().getBlockAt(l3);
		Block b4=world().getBlockAt(l4);
		if(b3.getType().isSolid() || b4.getType().isSolid())
		{
			if(velocity.getX()>0)
			{
				x=Math.floor(x+playerwidth/2)-playerwidth/2;
				velocity.setX(0);
			}
		}
	}catch(Exception e){}
		
		location.setX(x);
	}
}
