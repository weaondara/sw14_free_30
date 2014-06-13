package sw.superwhateverjnr.render;

import java.util.HashMap;
import java.util.Map;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.entity.Creeper;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.Skeleton;
import sw.superwhateverjnr.entity.Zombie;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.PointF;

public abstract class RendererBase
{
	protected World world;
	protected Game game;

	protected Paint paint;

	public RendererBase()
	{
		super();
		game=Game.getInstance();
		
		paint = new Paint();
	}

	public void nextFrame(Canvas canvas)
	{
		world=Game.getInstance().getWorld();
		if(world==null)
		{
			return;
		}
		
		prepare();
		
		
		drawBackground(canvas);
		drawWorld(canvas);
		
		if(SWEJNR.DEBUG)
		{
			drawWorldGrid(canvas);
		}
		
		drawEntities(canvas);
		drawPlayer(canvas);
		
		drawInfo(canvas);
		drawControls(canvas);
	}
	
	protected Location min;
	
	protected int x1;
	protected int x2;
	protected int xstart;
	protected int xend;
	
	protected int y1;
	protected int y2;	
	protected int ystart;
	protected int yend;
	
	protected float leftoffset;
	protected float topoffset;
	
	protected void prepare()
	{
		min=game.getMinDisplayPoint();
		
		x1=(int) Math.floor(min.getX());
		y1=(int) Math.floor(min.getY());
		
		x2=(int) (x1+Math.ceil((double)game.getDisplayWidth()/game.getTextureSize()))+(min.getX()%1==0?0:1);
		y2=(int) (y1+Math.ceil((double)game.getDisplayHeight()/game.getTextureSize()))+(min.getY()%1==0?0:1);
		
		xstart=x1<0?0:x1;
		ystart=y1<0?0:y1;
		
		xend=x2>world.getWidth()?world.getWidth():x2;
		yend=y2>world.getHeight()?world.getHeight():y2;
		
		
		if(min.getY()<0)
		{
			leftoffset=(float) ((min.getX()%1)*game.getTextureSize());
		}
		else
		{
			leftoffset=(float) ((1-min.getX()%1)*game.getTextureSize());
		}
		if(leftoffset>0)
		{
			leftoffset-=game.getTextureSize();
		}
		
		
		if(min.getY()>0)
		{
			topoffset=(float) ((min.getY()%1)*game.getTextureSize());
		}
		else
		{
			topoffset=(float) ((1-min.getY()%1)*game.getTextureSize());
		}
		if(topoffset>0)
		{
			topoffset-=game.getTextureSize();
		}
	}
	
	protected abstract void drawBackground(Canvas canvas);
	protected abstract void drawWorld(Canvas canvas);
	protected abstract void drawWorldGrid(Canvas canvas);
	protected abstract void drawEntities(Canvas canvas);
	protected abstract void drawCreeper(Canvas canvas, Creeper c);
	protected abstract void drawZombie(Canvas canvas, Zombie c);
	protected abstract void drawSkeleton(Canvas canvas, Skeleton c);
	protected abstract void drawPlayer(Canvas canvas);
	protected abstract void drawInfo(Canvas canvas);
	protected void drawControls(Canvas canvas)
	{
		drawControls0(canvas, true);
	}
	protected abstract void drawControls0(Canvas canvas, boolean retry);
	protected void makeLeftArrow(Path path, float size, float xoffset, float yoffset)
	{
		path.rewind();
		path.moveTo(xoffset+8*size, yoffset+5*size);
		path.lineTo(xoffset+3*size, yoffset+5*size);
		path.lineTo(xoffset+3*size, yoffset+7*size);
		path.lineTo(xoffset+0*size, yoffset+4*size);
		path.lineTo(xoffset+3*size, yoffset+1*size);
		path.lineTo(xoffset+3*size, yoffset+3*size);
		path.lineTo(xoffset+8*size, yoffset+3*size);
		path.lineTo(xoffset+8*size, yoffset+6*size);
	}
	protected void makeRightArrow(Path path, float size, float xoffset, float yoffset)
	{
		path.rewind();
		path.moveTo(xoffset+0*size, yoffset+5*size);
		path.lineTo(xoffset+5*size, yoffset+5*size);
		path.lineTo(xoffset+5*size, yoffset+7*size);
		path.lineTo(xoffset+8*size, yoffset+4*size);
		path.lineTo(xoffset+5*size, yoffset+1*size);
		path.lineTo(xoffset+5*size, yoffset+3*size);
		path.lineTo(xoffset+0*size, yoffset+3*size);
		path.lineTo(xoffset+0*size, yoffset+6*size);
	}
	protected void makeUpArrow(Path path, float size, float xoffset, float yoffset)
	{
		path.rewind();
		path.moveTo(xoffset+5*size, yoffset+8*size);
		path.lineTo(xoffset+5*size, yoffset+3*size);
		path.lineTo(xoffset+7*size, yoffset+3*size);
		path.lineTo(xoffset+4*size, yoffset+0*size);
		path.lineTo(xoffset+1*size, yoffset+3*size);
		path.lineTo(xoffset+3*size, yoffset+3*size);
		path.lineTo(xoffset+3*size, yoffset+8*size);
		path.lineTo(xoffset+6*size, yoffset+8*size);
	}
	@SuppressWarnings("unused")
	protected void makeDownArrow(Path path, float size, float xoffset, float yoffset)
	{
		path.rewind();
		path.moveTo(xoffset+5*size, yoffset+0*size);
		path.lineTo(xoffset+5*size, yoffset+5*size);
		path.lineTo(xoffset+7*size, yoffset+5*size);
		path.lineTo(xoffset+4*size, yoffset+8*size);
		path.lineTo(xoffset+1*size, yoffset+5*size);
		path.lineTo(xoffset+3*size, yoffset+5*size);
		path.lineTo(xoffset+3*size, yoffset+0*size);
		path.lineTo(xoffset+6*size, yoffset+0*size);
	}

	
	protected String[] cachedControlKeys=new String[]{"left", "right", "jump"};
	protected Map<String,PointF> cachedControlsPostions=new HashMap<String, PointF>();
	protected Map<String,Bitmap> cachedControlsBitmaps=new HashMap<String, Bitmap>();
	protected void redrawControls()
	{
		Settings set=game.getSettings();
		
		float radiusinner=set.getControlCircleRadiusInner();
		float radiusouter=set.getControlCircleRadiusOuter();
		
		float margin=set.getControlMargin();
		
		int ci=(set.getControlCircleOpacityInner()<<24)|set.getControlCircleColorInner();
		int co=(set.getControlCircleOpacityOuter()<<24)|set.getControlCircleColorOuter();
		int ca=(set.getControlArrowOpacity()<<24)|set.getControlArrowColor();
		
		float arrowsize=set.getControlArrowSize();

		paint.setStyle(Style.FILL);
		
		for(String key:cachedControlKeys)
		{
			PointF point=cachedControlsPostions.get(key);
			if(point==null)
			{
				Bitmap.Config conf = Bitmap.Config.ARGB_8888;
				Bitmap bm = Bitmap.createBitmap((int) Math.ceil(2*radiusouter), (int) Math.ceil(2*radiusouter), conf); 
				cachedControlsBitmaps.put(key, bm);
				
				Canvas canvas = new Canvas(bm);
				
				paint.setColor(co);
				canvas.drawCircle(radiusouter, radiusouter, radiusouter, paint);
				
				paint.setColor(ci);
				canvas.drawCircle(radiusouter, radiusouter, radiusinner, paint);
				
				float axy=radiusouter-4*arrowsize;
				
				paint.setColor(ca);
				
				Path path=new Path();
				
				if(key.equalsIgnoreCase("left"))
				{
					cachedControlsPostions.put(key, new PointF(margin,game.getDisplayHeight()-(margin+2*radiusouter)));
					makeLeftArrow(path,arrowsize,axy,axy);
				}
				else if(key.equalsIgnoreCase("right"))
				{
					cachedControlsPostions.put(key, new PointF(2*margin+2*radiusouter,game.getDisplayHeight()-(margin+2*radiusouter)));
					makeRightArrow(path,arrowsize,axy,axy);
				}
				else if(key.equalsIgnoreCase("jump"))
				{
					cachedControlsPostions.put(key, new PointF(game.getDisplayWidth()-(margin+2*radiusouter),game.getDisplayHeight()-(margin+2*radiusouter)));
					makeUpArrow(path,arrowsize,axy,axy);
				}
				
				canvas.drawPath(path, paint);
			}
		}
	}
	
	protected abstract void drawEntityBoxes(Canvas canvas, Entity e);
}
