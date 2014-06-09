package sw.superwhateverjnr.entity;

import java.util.Map;
import java.util.Random;

import lombok.SneakyThrows;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

public class Creeper extends Entity
{
	private final static int MAXCOUNTERWALK = 3;
	
	private static Player player;
	private static Game game;
	private static BlockFactory blockfactory;
	
	private final static double runningMin = 0.75;
	private final static double runningMax = 2.25;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	private final static double radius = 5.0;
	private static double distance = 0.0;
	
	private static double playerprevx = 0.0;
	private static double playerprevy = 0.0;
	private static double monsterprevx = 0.0;
	private static double monsterprevy = 0.0;
	
	private static boolean isindistance = false;
	private static boolean islavainfront = false;
	private static boolean istoohighleft = false;
	private static boolean istoohighright = false;
	
//	private static boolean isplayerpositionchangednow = false;
//	private static boolean isplayerpositionchangedprev = false;
//	private static boolean iscreeperpassingplayernow = false;
//	private static boolean iscreeperpassingplayerprev = false;
	private static boolean ismonsterstayingstill = false;
	private static boolean ismonsterstayingstilprev = false;

	private static boolean isgoingright = false;
	private static boolean isgoinghorizontal = false;
	private static boolean isgoingup = false;
	private static boolean isgoingvertical = false;
	
	private static double[] randomtimewalk = {0.0, 0.0};
	private static int counterright = 0;
	private static int counterleft = 0;
	private static boolean israndomgoing = false;
	private static boolean israndomgoingright = false;
	
	private static double[] randomtimejump = {0.0, 0.0};
	private static boolean israndomjump = false;
	private static boolean israndomjumpcompleted = false;
	
	private static double triggertimer = 0.0;
	private static boolean istriggertimer = false;
	private static boolean triggerexplosion = false;

	private Random random = new Random();
	
	public Creeper(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(EntityType.CREEPER, location, extraData);
		player=Game.getInstance().getPlayer();
		game=Game.getInstance();
		blockfactory = BlockFactory.getInstance();
	}

	@Override
	protected void die()
	{
		
	}
	
	@Override
	public void tick()
	{
		super.tick();
		stopIfLava();
		stopIfTooHigh();
		// swimIfWater();
		trigger();
		triggerExplosion();
		randomJump(false);
		randomWalk(true);
		jumpIfWall();
		setLastPosition();
		tickMove();
	}
	@SneakyThrows
	protected void trigger()
	{	
		double playerx = player.getLocation().getX();
		double playery = player.getLocation().getY();
		double monsterx = getLocation().getX();
		double monstery = getLocation().getY();
		System.out.println("monsterx_playerx="+absoluteDifference(monsterx, playerx)+"  monster_prev="+absoluteDifference(monsterx, monsterprevx));
		if (
		    (
			 ((monsterx > playerx) && (monsterprevx <= playerx) || (absoluteDifference(monsterx, monsterprevx) < 0.1)) ||
		     ((monsterx < playerx) && (monsterprevx >= playerx) || (absoluteDifference(monsterx, monsterprevx) < 0.1))
		    ) && (playerprevx == playerx))
		{
			ismonsterstayingstill = true;
		}
		else if (playerprevx != playerx)
		{
			ismonsterstayingstill = false;
		}
		if (ismonsterstayingstilprev != ismonsterstayingstill)
		{
			ismonsterstayingstilprev = ismonsterstayingstill;
			if (ismonsterstayingstill)
			{
				System.out.println("Monster is STILL!!!!");
			}
			else
			{
				System.out.println("Monster is GOING!!!!");
			}
		}
		distance = Math.sqrt(Math.pow(playerx - monsterx, 2.0) + Math.pow(playery - monstery, 2.0));
		if (distance < radius)
		{
			isindistance = true;
			if (!ismonsterstayingstill)
			{
				if (playerx >= monsterx)
				{
					if (!istoohighright)
					{
						setMovingright(true);
						isgoinghorizontal = true;
					}
					else
					{
						setMovingright(false);
						isgoinghorizontal = false;
					}
					setMovingleft(false);
					isgoingright = true;
				}
				else if (playerx <= monsterx)
				{
					if (!istoohighleft)
					{
						setMovingleft(true);
						isgoinghorizontal = true;
					}
					else
					{
						setMovingleft(false);
						isgoinghorizontal = false;
					}
					setMovingright(false);
					isgoingright = false;
				}
			}
			else
			{
				setMovingright(false);
				setMovingleft(false);
				isgoinghorizontal = false;
			}
			
			if (playery > monstery)
			{
				isgoingup = true;
				isgoingvertical = true;
			}
			else if (playery < monstery)
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
		}
	}
	@SneakyThrows
	protected void triggerExplosion()
	{
		if (!triggerexplosion && (distance < radius))
		{
			//world.createExplosion(new Location(11,10), 3, 1);
			game.getWorld().setBlockAt(9, 10, blockfactory.create(Material.AIR.getId(), (byte)0, 9, 10, game.getWorld(), null));
			triggerexplosion = !triggerexplosion;
			
			System.out.println("BOOMMM!!!!");
		}
	}
	protected void randomJump(boolean dorandomjump)
	{
		if (isindistance && dorandomjump)
		{
			if (isOnGround() && israndomjump)
			{
				jump();
				israndomjump = false;
			}
			else if (isOnGround() && !israndomjump)
			{
				israndomjumpcompleted = true;
			}
			if  ((randomtimejump[0] > randomtimejump[1]) && israndomjumpcompleted)
			{
				israndomjump = true;
				israndomjumpcompleted = false;
				randomtimejump[0] = 0.0;
				randomtimejump[1] = roundNumber(random.nextDouble() * 3 + 2.0, 3);
			}
			long now=System.currentTimeMillis();
			randomtimejump[0] += (double)(now - getLastMoveTime()) / 1000.0;
		}
		else
		{
			israndomjump = false;
			israndomjumpcompleted = false;
		}
	}
	protected void randomWalk(boolean dorandomwalk)
	{
		if (!isindistance && dorandomwalk)
		{
			if (randomtimewalk[0] < randomtimewalk[1])
			{
				if (israndomgoing)
				{
					if (israndomgoingright)
					{
						if (!istoohighright)
						{
							setMovingright(true);
						}
						else
						{
							setMovingright(false);
							israndomgoing = !israndomgoing;
						}
						setMovingleft(false);
					}
					else
					{
						if (!istoohighleft)
						{
							setMovingleft(true);
						}
						else
						{
							setMovingleft(false);
							israndomgoing = !israndomgoing;
						}
						setMovingright(false);
					}
				}
				else
				{
					setMovingright(false);
					setMovingleft(false);
				}
			}
			else
			{
				setMovingright(false);
				setMovingleft(false);
				randomtimewalk[0] = 0.0;
				randomtimewalk[1] = roundNumber(random.nextDouble() * 2 + 1.5, 3);
				israndomgoingright = random.nextBoolean();
				israndomgoing = !israndomgoing;
				if (israndomgoing && israndomgoingright)
				{
					counterright++;
					counterleft = 0;
				}
				else if (israndomgoing && !israndomgoingright)
				{
					counterright = 0;
					counterleft++;
				}
				if (counterright > MAXCOUNTERWALK)
				{
					counterright = 0;
					counterleft = 1;
					israndomgoingright = false;
				}
				if (counterleft > MAXCOUNTERWALK)
				{
					counterright = 1;
					counterleft = 0;
					israndomgoingright = true;
				}
			}
			long now=System.currentTimeMillis();
			randomtimewalk[0] += (double)(now - getLastMoveTime()) / 1000.0;
		}
		else
		{
			israndomgoing = false;
		}
	}
	protected void jumpIfWall()
	{
		if (isgoinghorizontal || israndomgoing)
		{
			double playerx = player.getLocation().getX();
			double playery = player.getLocation().getY();
			double monsterx = getLocation().getX();
			double monstery = getLocation().getY();
			double addx = 0.7;
			double addy = 0.0;
			
			Material materialx0y0 = game.getWorld().getBlockAt(new Location(monsterx, monstery + addy)).getType();
			Material materialxp1y0 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + addy)).getType();
			Material materialxp1yp1 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + 1.0)).getType();
			Material materialxm1y0 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + addy)).getType();
			Material materialxm1yp1 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + 1.0)).getType();
			
			boolean ismaterialxp1true = (materialxp1y0 != materialx0y0) && (materialxp1yp1 == materialx0y0);
			boolean ismaterialxm1true = (materialxm1y0 != materialx0y0) && (materialxm1yp1 == materialx0y0);
			boolean isgoingrighttrue = isgoingright && isgoinghorizontal;
			boolean israndomgoingrighttrue = israndomgoingright && israndomgoing;
			boolean isplayerpropertiesrighttrue = (!player.isOnGround() || (!istoohighright && (absoluteDifference(playerx, monsterx) > 0.5)));
			boolean isplayerpropertieslefttrue  = (!player.isOnGround() || ( !istoohighleft && (absoluteDifference(playerx, monsterx) > 0.5)));
			
			if (
			       ((( isgoingrighttrue && isplayerpropertiesrighttrue) ||  israndomgoingrighttrue) && ismaterialxp1true) ||
				   (((!isgoingrighttrue &&  isplayerpropertieslefttrue) || !israndomgoingrighttrue) && ismaterialxm1true)
			   )
			{
				jump();
			}
		}
	}
	protected void stopIfLava()
	{
		//stop instantly!!!
	}
	protected void stopIfTooHigh()
	{
		double monsterx = getLocation().getX();
		double monstery = getLocation().getY();
		double addx = 0.5;
		
		Material materialxp1y0 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + 0.0)).getType();
		Material materialxp1yp1 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + 1.0)).getType();
		Material materialxm1y0 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + 0.0)).getType();
		Material materialxm1yp1 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + 1.0)).getType();
		if ((materialxp1y0 == Material.AIR) && (materialxp1yp1 == Material.AIR))
		{
			boolean ismaterialxp1ym1air = false;
			boolean ismaterialxp1ym2air = false;
			boolean ismaterialxp1ym3air = false;
			boolean ismaterialxp1ym4air = false;
			Material materialxp1ym1 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 1.0)).getType();
			Material materialxp1ym2 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 2.0)).getType();
			Material materialxp1ym3 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 3.0)).getType();
			Material materialxp1ym4 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 4.0)).getType();
			ismaterialxp1ym1air = (materialxp1ym1 == Material.AIR);
			ismaterialxp1ym2air = (materialxp1ym2 == Material.AIR);
			ismaterialxp1ym3air = (materialxp1ym3 == Material.AIR);
			ismaterialxp1ym4air = (materialxp1ym4 == Material.AIR);
			if (ismaterialxp1ym1air && ismaterialxp1ym2air && ismaterialxp1ym3air  && ismaterialxp1ym4air)
			{
				istoohighright = true;
			}
			else
			{
				istoohighright = false;
			}
		}
		if ((materialxm1y0 == Material.AIR) && (materialxm1yp1 == Material.AIR))
		{
			boolean ismaterialxm1ym1air = false;
			boolean ismaterialxm1ym2air = false;
			boolean ismaterialxm1ym3air = false;
			boolean ismaterialxm1ym4air = false;
			Material materialxm1ym1 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 1.0)).getType();
			Material materialxm1ym2 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 2.0)).getType();
			Material materialxm1ym3 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 3.0)).getType();
			Material materialxm1ym4 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 4.0)).getType();
			ismaterialxm1ym1air = (materialxm1ym1 == Material.AIR);
			ismaterialxm1ym2air = (materialxm1ym2 == Material.AIR);
			ismaterialxm1ym3air = (materialxm1ym3 == Material.AIR);
			ismaterialxm1ym4air = (materialxm1ym4 == Material.AIR);
			if (ismaterialxm1ym1air && ismaterialxm1ym2air && ismaterialxm1ym3air  && ismaterialxm1ym4air)
			{
				istoohighleft = true;
			}
			else
			{
				istoohighleft = false;
			}
		}
	}
	protected void setLastPosition()
	{
		playerprevx = player.getLocation().getX();
		playerprevy = player.getLocation().getY();
		monsterprevx = getLocation().getX();
		monsterprevy = getLocation().getY();
	}
	/*protected void swimIfWater()
	{
		if (isindistance)
		{
			//swim to catch player
		}
		else
		{
			//swim away from player
		}
	}*/
	
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
	
	double roundNumber(double number, int digits)
	{
		return (double)((int)(number * (double)Math.pow(10.0, (double)digits))) / (double)Math.pow(10.0, (double)digits);
	}
	
	double absoluteDifference(double number1, double number2)
	{
		return Math.abs(Math.abs(number1) - Math.abs(number2));
	}
}