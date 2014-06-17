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
	
	private final static double runningMin = 0.75;
	private final static double runningMax = 2.25;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	
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
		triggerRadius = radius;
	}
	@Override
	protected void die()
	{
		if(lastDamageCause!=null)
		{
			dropGunPowder();
		}
		super.die();
	}
	@Override
	public void tick()
	{
		super.tick();
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

    private void dropGunPowder()
    {
        int amount = new Random().nextInt(2);
        super.dropItem(location.add(new Location(0.5, 0)), Drop.DropType.GUNPOWDER, amount);
    }
}