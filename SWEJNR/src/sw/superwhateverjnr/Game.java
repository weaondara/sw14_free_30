package sw.superwhateverjnr;

import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.scheduling.Scheduler;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.texture.DummyTextureLoader;
import sw.superwhateverjnr.texture.TextureLoader;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.ui.GameView;
import sw.superwhateverjnr.util.Arithmetics;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.DummyWorldLoader;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import sw.superwhateverjnr.world.WorldLoader;

@Getter
@ToString
@EqualsAndHashCode
public class Game
{
	@Getter
	private static Game instance;
	
	private Player player;
	private Location minDisplayPoint;
	
	private int displayWidth;
	private int displayHeight;
	
	private int textureWidth;
	private int textureHeight;
	
	@Getter
	private GameView gameView;
	
	
	private WorldLoader worldLoader;
	private TextureLoader textureLoader;
	
	private World world;
	
	@Getter
	private Settings settings;
	
	@Getter
	private Scheduler scheduler;
	
	public Game()
	{
		instance=this;
		
		player=new Player(null);
		minDisplayPoint=new Location(0, 0);
		
		DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
		displayWidth=metrics.widthPixels;
		displayHeight=metrics.heightPixels;
		
		textureWidth=64;
		textureHeight=64;
		
		settings=new Settings();
		loadSettings();
		
		scheduler=new Scheduler();
		registerSchedulerTasks();
		
		worldLoader=new DummyWorldLoader();
		try
		{
			world=worldLoader.loadWorld("bla");
		}
		catch(Exception e) {}
		
		
		textureLoader=new DummyTextureLoader();
		setupTextures();
		
		player.teleport(world.getSpawnLocation());
		
		gameView=new GameView(SWEJNR.getInstance());
		FullscreenActivity.getInstance().setContentView(gameView);
	}

	

	private void setupTextures()
	{
		TextureMap.loadTexture(new IdAndSubId(1, -1), textureLoader);
	}
	
	private void loadSettings()
	{
		
	}
	
	private void registerSchedulerTasks()
	{
		Runnable r10ms=new Runnable()
		{
			@Override
			public void run()
			{
				tickPlayer();
			}
		};
		scheduler.registerRepeatingTask(r10ms, 10, 100);
	}

	
	public void handleGameTouchEvent(MotionEvent event)
	{
		boolean left=false;
		boolean right=false;
		boolean jump=false;
		
		int action = MotionEventCompat.getActionMasked(event);
		int index = MotionEventCompat.getActionIndex(event);

		float x = MotionEventCompat.getX(event, index);
		float y = MotionEventCompat.getY(event, index);
		
		
		if(action==MotionEvent.ACTION_POINTER_DOWN || 
				action==MotionEvent.ACTION_DOWN || 
				action==MotionEvent.ACTION_MOVE)
		{
			float dx, dy, distance;
			float radius=settings.getControlCircleRadiusOuter();
			
			PointF leftc=Arithmetics.getControlLeftCenter();
			dx=Math.abs(leftc.x-x);
			dy=Math.abs(leftc.y-y);
			distance=(float) Math.sqrt(dx*dx+dy*dy);
			if(distance<=radius)
			{
				System.out.println("is left; dist: "+distance+"; radius: "+radius);
				left=true;
			}
			
			PointF rightc=Arithmetics.getControlRightCenter();
			dx=Math.abs(rightc.x-x);
			dy=Math.abs(rightc.y-y);
			distance=(float) Math.sqrt(dx*dx+dy*dy);
			if(distance<=radius)
			{
				System.out.println("is right; dist: "+distance+"; radius: "+radius);
				right=true;
			}
			
			PointF jumpc=Arithmetics.getControlJumpCenter();
			dx=Math.abs(jumpc.x-x);
			dy=Math.abs(jumpc.y-y);
			distance=(float) Math.sqrt(dx*dx+dy*dy);
			if(distance<=radius)
			{
				System.out.println("is jump; dist: "+distance+"; radius: "+radius);
				jump=true;
			}
		}
		
		player.setMovingleft(left);
		player.setMovingright(right);
		player.setJumping(jump);
	}
	
	private void tickPlayer()
	{
		System.out.println("ticking player");
		
		Location l=player.getLocation();
		if(l==null || world==null)
		{
			System.out.println("returning");
			return;
		}
		Block b=world.getBlockAt(l);
		if(player.isJumping() && 
		   player.isOnGround())
		{
			//do jump
		}
		else if(player.isMovingleft()==player.isMovingright())
		{
			//decelerate
		}
		else if(player.isMovingleft())
		{
			
		}
		else if(player.isMovingright())
		{
			
		}
		else
		{
			throw new RuntimeException("something went wrong");
		}
		
		System.out.println("left: "+player.isMovingleft());
		System.out.println("right: "+player.isMovingright());
		System.out.println("jump: "+player.isJumping());
	}
}
