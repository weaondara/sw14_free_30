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
	
	private Player player;
	private Game game;
	private BlockFactory blockfactory;
	
	private final static double runningMin = 0.75;
	private final static double runningMax = 2.25;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	private final static double radius = 6.0;
	private final static double triggerradiusexplosion = 3.0;
	private double distance = 0.0;
	
	private double playerprevx = 0.0;
	private double playerprevy = 0.0;
	private double monsterprevx = 0.0;
	private double monsterprevy = 0.0;
	
	private boolean isbehindblocks = false;
	private boolean isindistance = false;
	private boolean islavainfront = false;
	private boolean istoohighleft = false;
	private boolean istoohighright = false;
	
//	private static boolean isplayerpositionchangednow = false;
//	private static boolean isplayerpositionchangedprev = false;
//	private static boolean iscreeperpassingplayernow = false;
//	private static boolean iscreeperpassingplayerprev = false;
	private boolean ismonsterstayingstill = false;

	private boolean isgoingright = false;
	private boolean isgoinghorizontal = false;
	private boolean isgoingup = false;
	private boolean isgoingvertical = false;
	
	private double[] randomtimewalk = {0.0, 0.0};
	private int counterright = 0;
	private int counterleft = 0;
	private boolean israndomgoing = false;
	private boolean israndomgoingright = false;
	
	private double[] randomtimejump = {0.0, 0.0};
	private boolean israndomjump = false;
	private boolean israndomjumpcompleted = false;
	
	private double[] triggerexplosiontime = {0.0, 0.0};
	private boolean istriggertimer = false;
	private boolean istriggerexplosion = false;

	private Random random = new Random();
	
	public Creeper(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(EntityType.CREEPER, location, extraData);
		player=Game.getInstance().getPlayer();
		game=Game.getInstance();
		blockfactory = BlockFactory.getInstance();
		triggerRadius = radius;
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
		seePlayer();
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
		//System.out.println("monsterx_playerx="+absoluteDifference(monsterx, playerx)+"  monster_prev="+absoluteDifference(monsterx, monsterprevx));
		if (isInTolerance(monsterx, playerx, 0.8) && isInTolerance(playerprevx, playerx, 0.01))
		{
			ismonsterstayingstill = true;
		}
		else if (playerprevx != playerx)
		{
			ismonsterstayingstill = false;
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
	protected void seePlayer()
	{
		if (isindistance)
		{
			double playerx = player.getLocation().getX();
			double playery = player.getLocation().getY();
			double monsterx = getLocation().getX();
			double monstery = getLocation().getY();
			
			if ((playery >= 0) && (playerx >= 1) && (playerx <= game.getWorld().getWidth() - 1) && (monstery >= 0) && (monsterx >= 1) && (monsterx <= game.getWorld().getWidth() - 1))
			{
				boolean ismonsterlookingplayer1 = false;
				boolean ismonsterlookingplayer2 = false;
				double monsterlocationx = 0.0;
				double monsterlocationy = monstery + getEyeHeight(this);
				double playerlocationx1 = playerx;
				double playerlocationy1 = playery;
				double playerlocationx2 = playerx;
				double playerlocationy2 = playery + getEyeHeight(player);
				
				if (isgoingright && (monsterx < playerx))
				{
					monsterlocationx = monsterx + 0.3;
					ismonsterlookingplayer1 = !isInLineAnotherBlock(new Location(monsterlocationx, monsterlocationy), new Location(playerlocationx1, playerlocationy1), Material.AIR);
					ismonsterlookingplayer2 = !isInLineAnotherBlock(new Location(monsterlocationx, monsterlocationy), new Location(playerlocationx2, playerlocationy2), Material.AIR);
					//System.out.println("Monster going RIGHT, looking="+(ismonsterlookingplayer1||ismonsterlookingplayer2));
				}
				else if (!isgoingright && (monsterx > playerx))
				{
					monsterlocationx = monsterx - 0.3;
					ismonsterlookingplayer1 = !isInLineAnotherBlock(new Location(monsterlocationx, monsterlocationy), new Location(playerlocationx1, playerlocationy1), Material.AIR);
					ismonsterlookingplayer2 = !isInLineAnotherBlock(new Location(monsterlocationx, monsterlocationy), new Location(playerlocationx2, playerlocationy2), Material.AIR);
					//System.out.println("Monster going  LEFT, looking="+(ismonsterlookingplayer1||ismonsterlookingplayer2));
				}
				//System.out.println(isInLineAnotherBlock(new Location(13,13.5), new Location(25,13.5), Material.AIR));
				//System.out.println("monsterxmin="+getHitBox().getMin().getX()+"  monsterx"+getLocation().getX()+"  monsterxmax"+getHitBox().getMax().getX());
				if (ismonsterlookingplayer1 || ismonsterlookingplayer2)
				{
					isbehindblocks = false;
				}
				else
				{
					isbehindblocks = true;
					isindistance = false;
					setMovingright(false);
					setMovingleft(false);
					isgoinghorizontal = false;
					//System.out.println("FALSEEE!!!!");
				}
			}
		}
	}
	@SneakyThrows
	protected void triggerExplosion()
	{
		if (!istriggerexplosion && isindistance)
		{
			game.getWorld().createExplosion(location, 4, 2);
			istriggerexplosion = !istriggerexplosion;
			System.out.println("BOOOOOOOOOOOOOOMMMMMMMMMMMMMMMM!!!!");
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
			double addx = 0.6;
			double addy = 0.0;
			
			//System.out.println("playerx="+roundNumber(playerx,3)+"   playery="+roundNumber(playery,3)+"   monsterx="+roundNumber(monsterx,3)+"   monstery="+roundNumber(monstery,3)+"   addx="+addx+"   addy="+addy);
			if ((monsterx >= 1) && (monsterx <= game.getWorld().getWidth() - 1) && (monstery >= 0))
			{
				Material materialx0y0 = game.getWorld().getBlockAt(new Location(monsterx, monstery + addy)).getType();
				Material materialxp1y0 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + addy)).getType();
				Material materialxp1yp1 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + 1.0)).getType();
				Material materialxm1y0 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + addy)).getType();
				Material materialxm1yp1 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + 1.0)).getType();
				
				boolean isgoingrighttrue = isgoingright && isgoinghorizontal;
				boolean isgoinglefttrue = !isgoingright && isgoinghorizontal;
				boolean israndomgoingrighttrue = israndomgoingright && israndomgoing;
				boolean israndomgoinglefttrue = !israndomgoingright && israndomgoing;
				boolean isplayerpropertiesrighttrue = !player.isOnGround() || (!istoohighright && (absoluteDifference(playerx, monsterx) > 0.5));
				boolean isplayerpropertieslefttrue  = !player.isOnGround() || ( !istoohighleft && (absoluteDifference(playerx, monsterx) > 0.5));
				boolean ismaterialxp1true = (materialxp1y0 != materialx0y0) && (materialxp1yp1 == materialx0y0);
				boolean ismaterialxm1true = (materialxm1y0 != materialx0y0) && (materialxm1yp1 == materialx0y0);
				
				boolean statement1 = ((isgoingrighttrue && isplayerpropertiesrighttrue) || israndomgoingrighttrue) && ismaterialxp1true;
				boolean statement2 = (( isgoinglefttrue &&  isplayerpropertieslefttrue) ||  israndomgoinglefttrue) && ismaterialxm1true;
				
				if (statement1 || statement2)
				{
					jump();
				}
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
		
		if ((monsterx >= 1) && (monsterx <= game.getWorld().getWidth() - 1) && (monstery >= 0))
		{
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
				boolean istemp = false;
				
				if (monstery - 1.0 >= 0)
				{
					Material materialxp1ym1 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 1.0)).getType();
					ismaterialxp1ym1air = (materialxp1ym1 == Material.AIR);
					istemp = ismaterialxp1ym1air;
				}
				if (monstery - 2.0 >= 0)
				{
					Material materialxp1ym2 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 2.0)).getType();
					ismaterialxp1ym2air = (materialxp1ym2 == Material.AIR);
					istemp = (istemp && ismaterialxp1ym2air);
				}
				if (monstery - 3.0 >= 0)
				{
					Material materialxp1ym3 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 3.0)).getType();
					ismaterialxp1ym3air = (materialxp1ym3 == Material.AIR);
					istemp = (istemp && ismaterialxp1ym3air);
				}
				if (monstery - 4.0 >= 0)
				{
					Material materialxp1ym4 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery - 4.0)).getType();
					ismaterialxp1ym4air = (materialxp1ym4 == Material.AIR);
					istemp = (istemp && ismaterialxp1ym4air);
				}
				if (istemp)
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
				boolean istemp = false;
				
				if (monstery - 1.0 >= 0)
				{
					Material materialxm1ym1 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 1.0)).getType();
					ismaterialxm1ym1air = (materialxm1ym1 == Material.AIR);
					istemp = ismaterialxm1ym1air;
				}
				if (monstery - 2.0 >= 0)
				{
					Material materialxm1ym2 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 2.0)).getType();
					ismaterialxm1ym2air = (materialxm1ym2 == Material.AIR);
					istemp = (istemp && ismaterialxm1ym2air);
				}
				if (monstery - 3.0 >= 0)
				{
					Material materialxm1ym3 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 3.0)).getType();
					ismaterialxm1ym3air = (materialxm1ym3 == Material.AIR);
					istemp = (istemp && ismaterialxm1ym3air);
				}
				if (monstery - 4.0 >= 0)
				{
					Material materialxm1ym4 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery - 4.0)).getType();
					ismaterialxm1ym4air = (materialxm1ym4 == Material.AIR);
					istemp = (istemp && ismaterialxm1ym4air);
				}
				if (istemp)
				{
					istoohighleft = true;
				}
				else
				{
					istoohighleft = false;
				}
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
	
	private double roundNumber(double number, int digits)
	{
		return (double)((int)(number * (double)Math.pow(10.0, (double)digits))) / (double)Math.pow(10.0, (double)digits);
	}
	private double absoluteDifference(double number1, double number2)
	{
		return Math.abs(Math.abs(number1) - Math.abs(number2));
	}
	private boolean isInTolerance(double number1, double number2, double delta)
	{
		return (Math.abs(number1 - number2) <= delta);
	}
	private double getEyeHeight(Entity entity)
	{
		return (entity.getHitBox().getMax().getY() - getHitBox().getMin().getY()) * 0.8;
	}
	private boolean isInLineAnotherBlock(Location point1, Location point2, Material material)
	{
		boolean isinlineanotherblock = false;
		int iterations = 1000;
		int loop = 0;
		double x1 = point1.getX();
		double y1 = point1.getY();
		double x2 = point2.getX();
		double y2 = point2.getY();
		double dx = (x2 - x1) / (double)iterations;
		double dy = (y2 - y1) / (double)iterations;
		int x = (int)x1;
		int y = (int)y1;
		while (loop <= iterations)
		{
			if ((x != (int)(x1 + dx * (double)loop)) || (y != (int)(y1 + dy * (double)loop)))
			{
				x = (int)(x1 + dx * (double)loop);
				y = (int)(y1 + dy * (double)loop);
				if (game.getWorld().getBlockAt(new Location(x, y)).getType() != material)
				{
					isinlineanotherblock = !isinlineanotherblock;
					break;
				}
			}
			loop++;
		}
		return isinlineanotherblock;
	}
}