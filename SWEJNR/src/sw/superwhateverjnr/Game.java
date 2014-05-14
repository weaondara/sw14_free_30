package sw.superwhateverjnr;

import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.scheduling.Scheduler;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.texture.DummyTextureLoader;
import sw.superwhateverjnr.texture.TextureLoader;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.ui.GameView;
import sw.superwhateverjnr.util.Arithmetics;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.util.Rectangle;
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
	
	private GameView gameView;
	private WorldLoader worldLoader;
	private World world;
	
	private TextureLoader textureLoader;
	private Settings settings;
	private Scheduler scheduler;
	
	public Game()
	{
		instance=this;
		
		player=new Player(null);
		minDisplayPoint=new Location(0, 9);
		
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
		catch(Exception e) {e.printStackTrace();}
		
		
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
		final Runnable r10ms=new Runnable()
		{
			@Override
			public void run()
			{
				player.tick();
			}
		};
		scheduler.registerRepeatingTask(r10ms, 1, 10);
	}

	
	public void handleGameTouchEvent(MotionEvent event)
	{
		boolean left=false;
		boolean right=false;
		boolean jump=false;
		
		int action = MotionEventCompat.getActionMasked(event);
		
		float dx, dy, distance;
		float radius=settings.getControlCircleRadiusOuter();
		
		if(action==MotionEvent.ACTION_POINTER_DOWN || 
			action==MotionEvent.ACTION_DOWN || 
			action==MotionEvent.ACTION_MOVE)
		{
			for(int i=0;i<event.getPointerCount();i++)
			{
				float x = MotionEventCompat.getX(event, i);
				float y = MotionEventCompat.getY(event, i);
				
				PointF leftc=Arithmetics.getControlLeftCenter();
				dx=Math.abs(leftc.x-x);
				dy=Math.abs(leftc.y-y);
				distance=(float) Math.sqrt(dx*dx+dy*dy);
				if(distance<=radius)
				{
					left=true;
				}
				
				PointF rightc=Arithmetics.getControlRightCenter();
				dx=Math.abs(rightc.x-x);
				dy=Math.abs(rightc.y-y);
				distance=(float) Math.sqrt(dx*dx+dy*dy);
				if(distance<=radius)
				{
					right=true;
				}
				
				PointF jumpc=Arithmetics.getControlJumpCenter();
				dx=Math.abs(jumpc.x-x);
				dy=Math.abs(jumpc.y-y);
				distance=(float) Math.sqrt(dx*dx+dy*dy);
				if(distance<=radius)
				{
					jump=true;
				}
			}
		}
		
		player.setMovingleft(left);
		player.setMovingright(right);
		player.setJumping(jump);
	}
	
	public void updateView()
	{
		Rectangle viewRect=new Rectangle(
				displayWidth*0.3/textureWidth,
				displayHeight*0.35/textureHeight,
				displayWidth*0.5/textureWidth,
				displayHeight*0.65/textureHeight
				);
		
		Location l=player.getLocation();
		
		if(l.getX()<minDisplayPoint.getX()+viewRect.getMin().getX())
		{
			float x=(float) (l.getX()-viewRect.getMin().getX());
			if(x<0)
			{
				x=0;
			}
			minDisplayPoint.setX(x);
		}
		if(l.getX()>minDisplayPoint.getX()+viewRect.getMax().getX())
		{
			float x=(float) (l.getX()-viewRect.getMax().getX());
			if(x>world.getWidth()-displayWidth/textureWidth)
			{
				x=world.getWidth()-displayWidth/textureWidth;
			}
			minDisplayPoint.setX(x);
		}
		
		
		if(l.getY()<minDisplayPoint.getY()+viewRect.getMin().getY())
		{
			float y=(float) (l.getY()-viewRect.getMin().getY());
			if(y<0)
			{
				y=0;
			}
			minDisplayPoint.setY(y);
		}
		if(l.getY()>minDisplayPoint.getY()+viewRect.getMax().getY())
		{
			float y=(float) (l.getY()-viewRect.getMax().getY());
			if(y>world.getHeight()-displayHeight/textureHeight)
			{
				y=world.getHeight()-displayHeight/textureHeight;
			}
			minDisplayPoint.setY(y);
		}
	}
}
