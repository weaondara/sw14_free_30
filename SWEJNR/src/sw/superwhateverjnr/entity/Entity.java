package sw.superwhateverjnr.entity;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.ToString;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.util.Vector;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

@Getter
@ToString
@EqualsAndHashCode
public abstract class Entity
{
	protected static final double gravity = 0.02;
	
	private final static double runningMin = 1.5;
	private final static double runningMax = 4.5;
	private final static double runPower = 0.0015;
	private static double jumpPower = 7.0;
	
	public double getRunningMin()
	{
		return runningMin;
	}
	public double getRunningMax()
	{
		return runningMax;
	}
	public double getRunPower()
	{
		return runPower;
	}
	public double getJumpPower()
	{
		return jumpPower;
	}
	
	
	
	private static int ENTITY_ID = 0;
	@Synchronized
	public static int getNewId()
	{
		return ENTITY_ID++;
	}
	
	
	//-------------------------- general ------------------------------
	protected int id;
	protected EntityType type;
	protected Location location;
	protected Map<String, Object> extraData;
	
	//-------------------------- properties ------------------------------
	protected Rectangle hitBox;
	protected Rectangle renderBox;
	
	//--------------------------  ------------------------------
	@Setter
	protected double health;
	
	//-------------------------- movement ------------------------------
	@Setter
	protected Vector velocity;
	
	@Setter
	protected long lastJumpTime;
	@Setter
	protected long lastMoveTime;
	
	@Setter
	protected boolean movingright;
	@Setter
	protected boolean movingleft;
	@Setter
	protected boolean jumping;
	
	public Entity(EntityType type, Location location, Map<String, Object> extraData)
	{
		this(getNewId(), type, location, extraData);
	}
	public Entity(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		this.id = id;
		this.type = type;
		this.location = location;
		this.extraData = extraData;
		hitBox = EntityInfoMap.getHitBox(type);
		renderBox = EntityInfoMap.getRenderBox(type);
		health = EntityInfoMap.getMaxHealth(type);
		velocity = new Vector(0, 0);
	}
	public void jump()
	{
		if(isOnGround())
		{
			velocity.setY(getJumpPower());
			lastJumpTime=System.currentTimeMillis();
		}
	}
	public void teleport(Location l)
	{
		Preconditions.checkElementIndex(l.getBlockX(), Game.getInstance().getWorld().getWidth());
		Preconditions.checkElementIndex(l.getBlockY(), Game.getInstance().getWorld().getHeight());
		location=l;
		//TODO update rendering; not sure if we need it
	}
	
	public Map<String,Object> getExtraData()
	{
		return extraData==null ? new HashMap<String,Object>() : extraData;
	}
	
	public void tick()
	{
		tickGravity();
	}
	
	public double getJumpMaxWidth()
	{
		double maxwidth = 0.0;
		double maxheight = 0.0;
		double v_y = getJumpPower();
		double dt = 0.01;
		double t_max = 0.0;
		while (v_y > 0)
		{
			maxheight += v_y * dt;
			v_y -= gravity * 10 * dt;
			t_max += dt;
		}
		maxwidth = getRunningMax() * t_max / 100.0 * 2;
		return maxwidth;
	}
	
	public double getJumpMaxHeight()
	{
		double maxheight = 0.0;
		double v_y = getJumpPower();
		double dt = 0.01;
		while (v_y > 0)
		{
			maxheight += v_y * dt;
			v_y -= gravity * 10 * dt;
		}
		return maxheight;
	}
	
	protected void tickGravity()
	{
		if(location==null || world()==null)
		{
			return;
		}
		long now=System.currentTimeMillis();
		long time=now-getLastMoveTime();
		
		if(!isOnGround())
		{
			if(time<now)
			{
				double vy=velocity.getY();
				
				vy-= gravity*time;
				
				velocity.setY(vy);
			}
		}
		
		
		Rectangle bounds=getHitBox();
		float multiplier=0.01F;
		float entitywidth=(float) (Math.abs(bounds.getMin().getX()-bounds.getMax().getX()));
		
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
		Location l5=new Location(location.getX()+entitywidth/2-0.0000001,y);
		Location l6=new Location(location.getX()-entitywidth/2,y);
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
		
		Location l7=new Location(location.getX()+entitywidth/2-0.0000001,y+bounds.getMax().getY());
		Location l8=new Location(location.getX()-entitywidth/2,y+bounds.getMax().getY());
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
	}
	
	
	
	
	
	
	
	public boolean isOnGround()
	{
		World w=Game.getInstance().getWorld();
		if(w==null)
		{
			return false;
		}
		
		Location left=location.add(-hitBox.getMax().getX()/2, -1);
		Location right=location.add(hitBox.getMax().getX()/2, -1);
		Block bleft=w.getBlockAt(left);
		Block bright=w.getBlockAt(right);
		if(!bleft.getType().isSolid() && !bright.getType().isSolid())
		{
			return false;
		}
		
		return left.getBlockY()==left.getY() ||
				right.getBlockY()==right.getY();
	}
	protected World world()
	{
		return Game.getInstance().getWorld();
	}
	
}
