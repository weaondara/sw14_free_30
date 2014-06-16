package sw.superwhateverjnr.entity;

import java.util.Map;
import java.util.Random;
import lombok.SneakyThrows;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.BlockFactory;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.util.MathHelper;
//import static sw.superwhateverjnr.util.MathHelper.isInTolerance;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;

public class Creeper extends HostileEntity
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
	private final static double triggerradiusexplosion = 2.0;
	private double distance = 0.0;
	
	private double playerprevx = 0.0;
	private double playerprevy = 0.0;
	private double monsterprevx = 0.0;
	private double monsterprevy = 0.0;
	
	private boolean isbehindblocks = false;
	private boolean isindistance = false;
	private boolean istriggeredonce = false;
	private boolean islavainfront = false;
	private boolean istoohighleft = false;
	private boolean istoohighright = false;
	
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
	
	private double[] triggerexplosiontime = {0.0, 2.0};
	private boolean istriggertimer = false;
	private boolean istriggerexplosion = false;
	private int lasttriggerinttime = 0;

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
		super.die();
	}
	@Override
	public void tick()
	{
		super.tick();
//		stopIfTooHigh();
//		trigger();
//		seePlayer();
//		
//		randomJump(false);
//		randomWalk(true);
//		jumpIfWall();
//		setLastPosition();
//		setLookingSide();
//		tickMove();
	}
	@SneakyThrows
	protected void trigger()
	{	
		double playerx = player.getLocation().getX();
		double playery = player.getLocation().getY();
		double monsterx = getLocation().getX();
		double monstery = getLocation().getY();
		//System.out.println("monsterx_playerx="+absoluteDifference(monsterx, playerx)+"  monster_prev="+absoluteDifference(monsterx, monsterprevx));
		if (MathHelper.isInTolerance(monsterx, playerx, 0.8) && MathHelper.isInTolerance(playerprevx, playerx, 0.01))
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
				if ((playerx >= monsterx) && (lookingRight || istriggeredonce))
				{
					istriggeredonce = true;
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
				else if ((playerx <= monsterx) && (!lookingRight || istriggeredonce))
				{
					istriggeredonce = true;
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
			isgoinghorizontal = false;
			istriggeredonce = false;
		}
	}
	@SneakyThrows
	protected void attack()
	{
		if (!istriggerexplosion && (location.distance(player.location) <= triggerradiusexplosion) && !isbehindblocks)
		{
			istriggertimer = true;
			long now=System.currentTimeMillis();
			triggerexplosiontime[0] += (double)(Game.TICK_INTERVAL) / 1000.0;
			if (lasttriggerinttime < (int)triggerexplosiontime[0])
			{
				lasttriggerinttime = (int)triggerexplosiontime[0];
				//System.out.println("Countdown = "+((int)triggerexplosiontime[1]-lasttriggerinttime));
			}
			if (triggerexplosiontime[0] > triggerexplosiontime[1])
			{
				istriggerexplosion = !istriggerexplosion;
				//System.out.println("BOOOOOOOOOOOOOOMMMMMMMMMMMMMMMM!!!!");
				game.getWorld().createExplosion(location, 4, 2);
				die();
			}
		}
		else
		{
			istriggertimer = false;
			triggerexplosiontime[0] = 0.0;
			lasttriggerinttime = 0;
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
				randomtimejump[1] = MathHelper.roundNumber(random.nextDouble() * 3 + 2.0, 3);
			}
			long now=System.currentTimeMillis();
			randomtimejump[0] += (double)(Game.TICK_INTERVAL) / 1000.0;
		}
		else
		{
			israndomjump = false;
			israndomjumpcompleted = false;
		}
	}
	protected void randomWalk(boolean dorandomwalk)
	{
		if (!isindistance && dorandomwalk || !istriggeredonce && (israndomgoingright && (player.location.getX() < location.getX()) || !israndomgoingright && (player.location.getX() > location.getX())))
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
				//setMovingright(false);
				//setMovingleft(false);
				randomtimewalk[0] = 0.0;
				randomtimewalk[1] = MathHelper.roundNumber(random.nextDouble() * 2 + 1.5, 3);
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
			randomtimewalk[0] += (double)(Game.TICK_INTERVAL) / 1000.0;
		}
		else
		{
			israndomgoing = false;
		}
    }
//	protected void jumpIfWall()
//	{
//		if (isgoinghorizontal || israndomgoing)
//		{
//			double playerx = player.getLocation().getX();
//			double playery = player.getLocation().getY();
//			double monsterx = getLocation().getX();
//			double monstery = getLocation().getY();
//			double addx = 0.6;
//			double addy = 0.0;
//			
//			//System.out.println("playerx="+roundNumber(playerx,3)+"   playery="+roundNumber(playery,3)+"   monsterx="+roundNumber(monsterx,3)+"   monstery="+roundNumber(monstery,3)+"   addx="+addx+"   addy="+addy);
//			if ((monsterx >= 1) && (monsterx <= game.getWorld().getWidth() - 1) && (monstery >= 0))
//			{
//				Material materialx0y0 = game.getWorld().getBlockAt(new Location(monsterx, monstery + addy)).getType();
//				Material materialxp1y0 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + addy)).getType();
//				Material materialxp1yp1 = game.getWorld().getBlockAt(new Location(monsterx + addx, monstery + 1.0)).getType();
//				Material materialxm1y0 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + addy)).getType();
//				Material materialxm1yp1 = game.getWorld().getBlockAt(new Location(monsterx - addx, monstery + 1.0)).getType();
//				
//				boolean isgoingrighttrue = isgoingright && isgoinghorizontal;
//				boolean isgoinglefttrue = !isgoingright && isgoinghorizontal;
//				boolean israndomgoingrighttrue = israndomgoingright && israndomgoing;
//				boolean israndomgoinglefttrue = !israndomgoingright && israndomgoing;
//				boolean isplayerpropertiesrighttrue = !player.isOnGround() || (!istoohighright && (MathHelper.absoluteDifference(playerx, monsterx) > 0.5));
//				boolean isplayerpropertieslefttrue  = !player.isOnGround() || ( !istoohighleft && (MathHelper.absoluteDifference(playerx, monsterx) > 0.5));
//				boolean ismaterialxp1true = (materialxp1y0 != materialx0y0) && (materialxp1yp1 == materialx0y0);
//				boolean ismaterialxm1true = (materialxm1y0 != materialx0y0) && (materialxm1yp1 == materialx0y0);
//				
//				boolean statement1 = ((isgoingrighttrue && isplayerpropertiesrighttrue) || israndomgoingrighttrue) && ismaterialxp1true;
//				boolean statement2 = (( isgoinglefttrue &&  isplayerpropertieslefttrue) ||  israndomgoinglefttrue) && ismaterialxm1true;
//				
//				if (statement1 || statement2)
//				{
//					jump();
//				}
//			}
//		}
//	}
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
	protected void setLookingSide()
	{
		if (israndomgoingright && israndomgoing || isgoingright && isgoinghorizontal)
		{
			lookingRight = true;
		}
		else if (!israndomgoingright && israndomgoing || !isgoingright && isgoinghorizontal)
		{
			lookingRight = false;
		}
	}
	public String getDebugInfo()
	{
		return super.getDebugInfo()+"\nisindistance="+isindistance+"\nisbehindblock"+isbehindblocks+"\ncountdown="+MathHelper.roundNumber(triggerexplosiontime[0], 3)+"\nisgoing="+isgoinghorizontal+"\nisgoingright="+isgoingright+"\nisradnomgoing="+israndomgoing+"\nisradnomgoingright="+israndomgoingright;
	}

	@Override
    public double getRunningMin()
    {
        return runningMin;
    }

    @Override
    public double getRunningMax()
    {
        return runningMax;
    }

    @Override
    public double getJumpPower()
    {
        return jumpPower;
    }

    @Override
    public double getRunPower()
    {
        return runPower;
    }

    @Override
    public double getTriggerRadius()
    {
        return triggerRadius;
    }
}