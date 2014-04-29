package sw.superwhateverjnr;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sw.superwhateverjnr.block.Block;
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
import sw.superwhateverjnr.util.Vector;
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
		final Runnable r10ms=new Runnable()
		{
			@Override
			public void run()
			{
				tickPlayer();
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
	
	private void tickPlayer()
	{
		Location l=player.getLocation();
		Vector v=player.getVelocity();
		if(l==null || world==null)
		{
			return;
		}
		Block b=world.getBlockAt(l);
		Rectangle bounds=player.getHitBox();
		long now=System.currentTimeMillis();
		long time=now-player.getLastMoveTime();
		
		if(player.isJumping() && player.isOnGround())
		{
			v.setY(7);

			player.setLastJumpTime(now);
		}
		else if(!player.isOnGround())
		{
			if(time<now)
			{
				double ya=0.002;
				
				double vy=v.getY();
				
				vy-=ya*time*time;
				
				v.setY(vy);
			}
		}
		
		
		double xmaxmove=4.5;
		double xminmove=1.5;
		double xa=0.00015;
		
		
		double vx=v.getX();
		if(player.isMovingleft() && !player.isMovingright())
		{
			if(vx>-xminmove)
			{
				vx=-xminmove;
			}
			else
			{
				vx*=(1+xa*time*time*(xmaxmove+vx));
			}
		}
		else if(player.isMovingright() && !player.isMovingleft())
		{
			if(vx<xminmove)
			{
				vx=xminmove;
			}
			else
			{
				vx*=(1+xa*time*time*(xmaxmove-vx));
			}
		}
		else //x decelerate
		{
			double d=xa*time*time*(Math.abs(vx)+xminmove);
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

		player.getVelocity().setX(vx);
		player.setLastMoveTime(now);

		
		
		
		
		float multiplier=0.01F;
		float playerwidth=(float) (Math.abs(bounds.getMin().getX()-bounds.getMax().getX()));
		
		//world check
		double x=l.getX();
		x+=v.getX()*multiplier;
		if(x<0)
		{
			x=0;
			v.setX(0);
		}
		if(x>=world.getWidth())
		{
			x=world.getWidth()-0.0000001;
			v.setX(0);
		}
		
		//block check
		Location l1=new Location(x-playerwidth/2,l.getY());
		Location l2=new Location(x-playerwidth/2,l.getY()+1);
		Block b1=world.getBlockAt(l1);
		Block b2=world.getBlockAt(l2);
		if(b1.getType().isSolid() || b2.getType().isSolid())
		{
			if(v.getX()<0)
			{
				x=Math.ceil(x-playerwidth/2)+playerwidth/2;
				v.setX(0);
			}
		}
		
		Location l3=new Location(x+playerwidth/2,l.getY());
		Location l4=new Location(x+playerwidth/2,l.getY()+1);
		Block b3=world.getBlockAt(l3);
		Block b4=world.getBlockAt(l4);
		if(b3.getType().isSolid() || b4.getType().isSolid())
		{
			if(v.getX()>0)
			{
				x=Math.floor(x+playerwidth/2)-playerwidth/2;
				v.setX(0);
			}
		}
		l.setX(x);
		
		//world check
		double y=l.getY();
		y+=v.getY()*multiplier;
		if(y<0)
		{
			y=0;
			v.setY(0);
		}
		if(y>=world.getHeight())
		{
			y=world.getHeight()-0.0000001;
			v.setY(0);
		}
		
		//block check
		Location l5=new Location(l.getX()+playerwidth/2-0.0000001,y);
		Location l6=new Location(l.getX()-playerwidth/2,y);
		Block b5=world.getBlockAt(l5);
		Block b6=world.getBlockAt(l6);
		if(b5.getType().isSolid() || b6.getType().isSolid())
		{
			if(v.getY()<0)
			{
				y=Math.ceil(y);
				v.setY(0);
			}
		}
		
		Location l7=new Location(l.getX()+playerwidth/2-0.0000001,y+bounds.getMax().getY());
		Location l8=new Location(l.getX()-playerwidth/2,y+bounds.getMax().getY());
		Block b7=world.getBlockAt(l7);
		Block b8=world.getBlockAt(l8);
		if(b7.getType().isSolid() || b8.getType().isSolid())
		{
			if(v.getY()>0)
			{
				y=Math.floor(y+bounds.getMax().getY())-bounds.getMax().getY();
				v.setY(0);
			}
		}
		l.setY(y);
		
		updateView();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	private void updateView()
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
