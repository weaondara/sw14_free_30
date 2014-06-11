package sw.superwhateverjnr;

import android.content.res.AssetFileDescriptor;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import sw.superwhateverjnr.activity.GameActivity;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.scheduling.Scheduler;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.texture.DummyTextureLoader;
import sw.superwhateverjnr.texture.PackedTextureLoader;
import sw.superwhateverjnr.texture.TextureLoader;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.ui.GameView;
import sw.superwhateverjnr.util.Arithmetics;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.DummyWorldLoader;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.RandomWorldLoader;
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
	
	private View oldview;
	private GameView gameView;
	
	@Setter
	private WorldLoader worldLoader;
	private World world;
	private MediaPlayer mp;
	
	private TextureLoader textureLoader;
	private Settings settings;
	private Scheduler scheduler;
	
	private boolean gameRunning = false;
	
	private boolean enabled = false;
	private String worldname;
	
	public Game()
	{
		instance=this;
		
		minDisplayPoint=new Location(0, 9);
		
		DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
		displayWidth=metrics.widthPixels;
		displayHeight=metrics.heightPixels;
		
		textureWidth=64;
		textureHeight=64;
		
		player=new Player(null);
		settings=new Settings();
		scheduler=new Scheduler();
		worldLoader=new DummyWorldLoader();
		textureLoader=new PackedTextureLoader();
		
		gameView=new GameView(GameActivity.getInstance());
		
//		init();
//		loadWorld("bla");
//		enable();
	}

	public void init()
	{
		registerSchedulerTasks();
	}
	
	public void enable()
	{
		if(enabled)
		{
			return;
		}
		
		GameActivity.getInstance().setContentView(gameView);
		gameRunning=true;
		paused=false;
		
        mp = new MediaPlayer();
        
        try
        {
            AssetFileDescriptor as = SWEJNR.getInstance().getAssets().openFd("music/"+world.getBgmfile());
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(as.getFileDescriptor(), as.getStartOffset(), as.getLength());
            mp.setLooping(true);
            mp.prepare();
            mp.start(); 
            as.close();
        }
        catch(IOException e)
        {
            if(world.getBgmfile()!=null)
            {
                e.printStackTrace();
            }
        }
		
		enabled=true;
	}
	public void disable()
	{
		if(!enabled)
		{
			return;
		}
		gameRunning=false;
		
		enabled=false;
	}
	
	private boolean paused;
	public void setPaused(boolean paused)
	{
		if(this.paused == paused)
		{
			return;
		}
		
		gameRunning=!paused;
		gameView.setPaused(paused);
		this.paused = paused;
	}
	
	public void loadWorld(String name)
	{
		try
		{
			world=worldLoader.loadWorld(name);
			worldname=name;
			player.teleport(world.getSpawnLocation());
		}
		catch(Exception e) {e.printStackTrace();}
	}

	
	
	private void registerSchedulerTasks()
	{
		final Runnable r10ms=new Runnable()
		{
			@SneakyThrows
			@Override
			public void run()
			{
				while(!gameRunning)
				{
					return;
				}
				try
				{
					player.tick();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					world.tick();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
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
		if(jump)
		{
			player.jump();
		}
	}
	
	public void updateView()
	{
		Rectangle viewRect=new Rectangle(
				displayWidth*0.3/textureWidth,
				displayHeight*0.35/textureHeight,
				displayWidth*0.5/textureWidth,
				displayHeight*0.55/textureHeight
				);
		
		Location l=player.getLocation();
		if(l==null)
		{
			return;
		}
		if(l.getX()<minDisplayPoint.getX()+viewRect.getMin().getX())
		{
			float x=(float) (l.getX()-viewRect.getMin().getX());
			if(x<0)
			{
				x=0;
			}
			minDisplayPoint.setX(x);
		}
		else if(l.getX()>minDisplayPoint.getX()+viewRect.getMax().getX())
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
			float y=(float) (l.getY()-(viewRect.getMin().getY()));
			if(y<0)
			{
				y=0;
			}
			minDisplayPoint.setY(y);
		}
		else if(l.getY()>minDisplayPoint.getY()+viewRect.getMax().getY())
		{
			float y=(float) (l.getY()-(viewRect.getMax().getY()));
			if(y>world.getHeight())
			{
				y=world.getHeight();
			}
			minDisplayPoint.setY(y);
		}
	}
}
