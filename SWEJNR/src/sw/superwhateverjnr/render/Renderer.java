package sw.superwhateverjnr.render;

import java.util.HashMap;
import java.util.Map;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;

public class Renderer
{
	private World world;
	private Game game;

	private Paint paint;

	public Renderer(World world)
	{
		super();
		this.world = world;
		game=Game.getInstance();
		
		paint = new Paint();
	}

	public void nextFrame(Canvas canvas)
	{
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
	
	private Location min;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int xstart;
	private int ystart;
	private int xend;
	private int yend;
	private float leftoffset;
	private float topoffset;
	
	private void prepare()
	{
		min=game.getMinDisplayPoint();
		x1=(int) Math.floor(min.getX());
		y1=(int) Math.floor(min.getY());
		x2=(int) (x1+Math.ceil((double)game.getDisplayWidth()/game.getTextureWidth()))+(min.getX()%1==0?0:1);
		y2=(int) (y1+Math.ceil((double)game.getDisplayHeight()/game.getTextureHeight()))+(min.getY()%1==0?0:1);
		
		xstart=x1<0?0:x1;
		ystart=y1<0?0:y1;
		xend=x2>world.getWidth()?world.getWidth():x2;
		yend=y2>world.getHeight()?world.getHeight():y2;
		
		
		if(min.getY()<0)
		{
			leftoffset=(float) ((min.getX()%1)*game.getTextureWidth());
		}
		else
		{
			leftoffset=(float) ((1-min.getX()%1)*game.getTextureWidth());
		}
		if(leftoffset>0)
		{
			leftoffset-=game.getTextureWidth();
		}
		
		
		if(min.getY()>0)
		{
			topoffset=(float) ((min.getY()%1)*game.getTextureHeight());
		}
		else
		{
			topoffset=(float) ((1-min.getY()%1)*game.getTextureHeight());
		}
		if(topoffset>0)
		{
			topoffset-=game.getTextureHeight();
		}
	}
	
	private void drawBackground(Canvas canvas)
	{
		canvas.drawColor((game.getSettings().getBackgroudColor() & 0x00FFFFF) | 0xFF000000);
	}
	private void drawWorld(Canvas canvas)
	{
		for(int x=xstart;x<xend;x++)
		{
			float left=leftoffset + (x-x1) * game.getTextureWidth();
			for(int y=yend-1;y>ystart-1;y--)
			{
				Block b=world.getBlockAt(x, y);
				if(b.getType()==Material.AIR)
				{
					continue;
				}
				
				IdAndSubId ref=new IdAndSubId(b.getType().getId(),b.getSubid());
				Texture tex=TextureMap.getTexture(ref);
				if(tex==null)
				{
					System.out.println("fatal render");
					continue; //fatal error
				}
				
				float top=topoffset+(y2-1-y)*game.getTextureHeight();
				canvas.drawBitmap(tex.getImage(), left, top, null);
			}
		}
	}
	private void drawWorldGrid(Canvas canvas)
	{
		paint.setColor(0xFFFF0000);
		paint.setStrokeWidth(0);
		for(int x=xstart;x<xend+1;x++)
		{
			float px=leftoffset+(x-x1)*game.getTextureWidth();
			canvas.drawLine(px, 0, px, game.getDisplayHeight(), paint);
		}
		for(int y=ystart;y<yend+1;y++)
		{
			float py=topoffset+(y2-1-y)*game.getTextureHeight();
			canvas.drawLine(0, py, game.getDisplayWidth(), py, paint);
		}
	}
	private void drawEntities(Canvas canvas)
	{
		
	}
	private void drawPlayer(Canvas canvas)
	{
		Player p=game.getPlayer();
		Location l=p.getLocation();
		if(l==null)
		{
			return;
		}
		
		if(SWEJNR.DEBUG)
		{
			float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureWidth());
			float y=(float) (topoffset+(y2-l.getY())*game.getTextureHeight());
			
			paint.setStyle(Style.STROKE);
			
			
			paint.setColor(0xFFFFFF00);
			
			float playerwidh=(float) (Math.abs(p.getRenderBox().getMin().getX()-p.getRenderBox().getMax().getX())*game.getTextureWidth());
	
			float left=x-playerwidh/2;
			float right=x+playerwidh/2;
			float bottom=y;
			float top=(float) (y-p.getRenderBox().getMax().getY()*game.getTextureHeight());
			
			canvas.drawRect(left, top, right, bottom, paint);
			
			
		
			paint.setColor(0xFF00FF00);
			
			playerwidh=(float) (Math.abs(p.getHitBox().getMin().getX()-p.getHitBox().getMax().getX())*game.getTextureWidth());

			left=x-playerwidh/2;
			right=x+playerwidh/2;
			bottom=y;
			top=(float) (y-p.getHitBox().getMax().getY()*game.getTextureHeight());
			
			canvas.drawRect(left, top, right, bottom, paint);
		}
	}
	private void drawInfo(Canvas canvas)
	{
		int fontsize=30;
		int fontcolor=0xFF00FF00;
		
		paint.setColor(fontcolor);
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(fontsize);
		
		Rect r=new Rect();
		paint.getTextBounds("b", 0, 1, r);
		int textheight=r.height();
		
		canvas.drawText("FPS: "+Game.getInstance().getGameView().getFps(), 10, 10+textheight, paint);
	}
	private void drawControls(Canvas canvas)
	{
		drawControls0(canvas, true);
	}
	private void drawControls0(Canvas canvas, boolean retry)
	{
		for(String key:cachedControlKeys)
		{
			PointF point=cachedControlsPostions.get(key);
			if(point==null)
			{
				redrawControls();
				if(retry)
				{
					drawControls0(canvas, false);
				}
				return;
			}
			
			Bitmap bm=cachedControlsBitmaps.get(key);
			canvas.drawBitmap(bm, point.x, point.y, null);
		}
	}
	private void makeLeftArrow(Path path, float size, float xoffset, float yoffset)
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
	private void makeRightArrow(Path path, float size, float xoffset, float yoffset)
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
	private void makeUpArrow(Path path, float size, float xoffset, float yoffset)
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
	private void makeDownArrow(Path path, float size, float xoffset, float yoffset)
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

	
	private String[] cachedControlKeys=new String[]{"left", "right", "jump"};
	private Map<String,PointF> cachedControlsPostions=new HashMap<>();
	private Map<String,Bitmap> cachedControlsBitmaps=new HashMap<>();
	private void redrawControls()
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
}
