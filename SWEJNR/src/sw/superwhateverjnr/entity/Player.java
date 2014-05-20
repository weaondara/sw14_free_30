package sw.superwhateverjnr.entity;

import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

@Getter
public class Player extends Entity
{
	private final static int armIdleDegree = 90;
	private final static int armMaxDegreeDelta = 45;
	private static int armMoveConstant = 10;
	
	private final static double runningMin = 1.5;
	private final static double runningMax = 4.5;
	private final static double runPower = 0.00015;
	private final static double jumpPower = 7.0;
	
	private int moveArmswingDegrees;
	private int standArmswingDegrees;
	
	@Setter
	private boolean armMovingRight;
	
	public Player(Location location)
	{
		super(EntityType.PLAYER, location, null);
		moveArmswingDegrees=armIdleDegree;
		standArmswingDegrees=armIdleDegree;
	}
	
	@Setter
	private boolean movingright;
	@Setter
	private boolean movingleft;
	@Setter
	private boolean jumping;
	
	@Override
	public void tick()
	{
		tickMove();
		swingArms();
	}
	
	private void swingArms()
	{
		if(armMovingRight)
		{
			moveArmswingDegrees += armMoveConstant;
			standArmswingDegrees -= armMoveConstant;
		}
		else
		{
			moveArmswingDegrees -= armMoveConstant;
			standArmswingDegrees += armMoveConstant;
		}
		
		if(moveArmswingDegrees > armIdleDegree + armMaxDegreeDelta)
		{
			armMovingRight = false;
		}
		
		else if(this.moveArmswingDegrees < armIdleDegree - armMaxDegreeDelta)
		{
			armMovingRight = true;
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
		
		if(isJumping() && isOnGround())
		{
			velocity.setY(jumpPower);

			setLastJumpTime(now);
		}
		else if(!isOnGround())
		{
			if(time<now)
			{
				
				double vy=velocity.getY();
				
				vy-= gravity*time*time;
				
				velocity.setY(vy);
			}
		}
		
		double vx=velocity.getX();
		if(isMovingleft() && !isMovingright())
		{
			if(vx>-runningMin)
			{
				vx=-runningMin;
			}
			else
			{
				vx*=(1+runPower*time*time*(runningMax+vx));
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
				vx*=(1+runPower*time*time*(runningMax-vx));
			}
		}
		else //x decelerate
		{
			double d=runPower*time*time*(Math.abs(vx)+runningMin);
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
		location.setX(x);
		
		//world check
		double y=location.getY();
		y+=velocity.getY()*multiplier;
		if(y<0)
		{
			y=0;
			velocity.setY(0);
		}
		if(y>=world().getHeight())
		{
			y=world().getHeight()-0.0000001;
			velocity.setY(0);
		}
		
		//block check
		Location l5=new Location(location.getX()+playerwidth/2-0.0000001,y);
		Location l6=new Location(location.getX()-playerwidth/2,y);
		Block b5=world().getBlockAt(l5);
		Block b6=world().getBlockAt(l6);
		if(b5.getType().isSolid() || b6.getType().isSolid())
		{
			if(velocity.getY()<0)
			{
				y=Math.ceil(y);
				velocity.setY(0);
			}
		}
		
		Location l7=new Location(location.getX()+playerwidth/2-0.0000001,y+bounds.getMax().getY());
		Location l8=new Location(location.getX()-playerwidth/2,y+bounds.getMax().getY());
		Block b7=world().getBlockAt(l7);
		Block b8=world().getBlockAt(l8);
		if(b7.getType().isSolid() || b8.getType().isSolid())
		{
			if(velocity.getY()>0)
			{
				y=Math.floor(y+bounds.getMax().getY())-bounds.getMax().getY();
				velocity.setY(0);
			}
		}
		location.setY(y);
		
		Game.getInstance().updateView();
	}
	
	public boolean isOnGround()
	{
		World w=Game.getInstance().getWorld();
		if(w==null)
		{
			return false;
		}
		Block b=w.getBlockAt(location.add(0, -1));
		if(!b.getType().isSolid())
		{
			return false;
		}
		
		return location.getBlockY()==location.getY();
	}
	private World world()
	{
		return Game.getInstance().getWorld();
	}
}
