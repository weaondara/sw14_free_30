package sw.superwhateverjnr.entity;

import java.util.Map;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;

public class Creeper extends Entity
{
	private static Player player;
	
	private static boolean isindistance = false;
	private static boolean isgoingright = false;
	private static boolean isgoinghorizontal = false;
	private static boolean isgoingup = false;
	private static boolean isgoingvertical = false;
	
	private final static double runningMin = 0.75;
	private final static double runningMax = 2.25;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	private final static double radius = 5.0;
	
	public Creeper(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(EntityType.CREEPER, location, extraData);
		player=Game.getInstance().getPlayer();
	}

	@Override
	protected void die()
	{
		
	}
	
	@Override
	public void tick()
	{
		super.tick();
		trigger();
		tickMove();
		randomJump(true);
		randomWalk(true);
		jumpIfWall();
		stopIfLava();
		swimIfWater();
	}
	
	protected void trigger()
	{	
		double centerxplayer = player.getLocation().getX();// + player border width / 2
		double centerxmonster = getLocation().getX();// + monster border width / 2
		double centeryplayer = player.getLocation().getY();// - player border height / 2
		double centerymonster = getLocation().getY();// - monster border height / 2
		double distance = Math.sqrt(Math.pow(centerxplayer - centerxmonster, 2.0) + Math.pow(centeryplayer - centerymonster, 2.0));
		if (distance < radius)
		{
			isindistance = true;
			if (centerxplayer > centerxmonster)
			{
				setMovingright(true);
				setMovingleft(false);
				isgoingright = true;
				isgoinghorizontal = false;
			}
			else if (centerxplayer < centerxmonster)
			{
				setMovingright(false);
				setMovingleft(true);
				isgoingright = false;
				isgoinghorizontal = false;
			}
			else
			{
				isgoinghorizontal = false;
			}
			
			if (centeryplayer > centerymonster)
			{
				isgoingup = true;
				isgoingvertical = true;
			}
			else if (centeryplayer < centerymonster)
			{
				isgoingup = false;
				isgoingvertical = true;
			}
			else
			{
				isgoingvertical = false;
			}
		}
		else
		{
			isindistance = false;
			setMovingright(false);
			setMovingleft(false);
		}
	}
	
	protected void randomJump(boolean dorandomjump)
	{
		if (isindistance && dorandomjump)
		{
			// jump randomized, if player detected
		}
	}
	
	protected void randomWalk(boolean dorandomwalk)
	{
		if (!isindistance && dorandomwalk)
		{
			// walk randomized, if player not detected
		}
	}
	
	protected void jumpIfWall()
	{
		// follow player anyway, but jump!!
	}
	
	protected void stopIfLava()
	{
		//stop instantly!!!
	}
	
	protected void swimIfWater()
	{
		if (isindistance)
		{
			//swim to catch player
		}
		else
		{
			//swim away from player
		}
	}
	
	private void tickMove()
	{
		if(location==null || world()==null)
		{
			return;
		}
		Rectangle bounds=getHitBox();
		long now=System.currentTimeMillis();
		long time=now-getLastMoveTime();
		
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
		setLastMoveTime(now);

		
		
		
		
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
		}
		catch(Exception e){}
		
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
		}
		catch(Exception e){}
			
		location.setX(x);
	}
}