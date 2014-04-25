package sw.superwhateverjnr.render;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
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
import android.graphics.Rect;

public class Renderer
{
	private World world;
	private Game game;
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paint;
	
	public Renderer(World world)
	{
		super();
		this.world = world;
		game=Game.getInstance();
		
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		bitmap = Bitmap.createBitmap(game.getDisplayWidth(), game.getDisplayHeight(), conf); 
		
		canvas = new Canvas(bitmap);
		paint = new Paint();
	}

	public Bitmap nextFrame()
	{
		prepare();
		
		drawBackground();
		
		drawWorld();
		
		if(SWEJNR.DEBUG)
		{
			drawWorldGrid();
		}
		
		drawEntities();
		
		drawPlayer();
		
		drawInfo();
		
		drawControls();
		
		return bitmap;
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
		
		
		if(min.getY()<0)
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
	
	private void drawBackground()
	{
		canvas.drawColor(((game.getSettings().getBackgroudColor()<<8)>>8)|0xFF);
	}
	private void drawWorld()
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
	private void drawWorldGrid()
	{
		paint.setColor(0xFFFF0000);
		paint.setStrokeWidth(0);
		for(int x=xstart;x<xend+1;x++)
		{
			canvas.drawLine(leftoffset+x*game.getTextureWidth(), 0, leftoffset+x*game.getTextureWidth(), game.getDisplayHeight(), paint);
		}
		for(int y=ystart;y<yend+1;y++)
		{
			canvas.drawLine(0, topoffset+y*game.getTextureHeight(), game.getDisplayWidth(), topoffset+y*game.getTextureHeight(), paint);
		}
	}
	private void drawEntities()
	{
		
	}
	private void drawPlayer()
	{
		
	}
	private void drawInfo()
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
	private void drawControls()
	{
		Settings set=game.getSettings();
		
		
		float radiusinner=set.getControlCircleRadiusInner();
		float radiusouter=set.getControlCircleRadiusOuter();
		
		float margin=set.getControlMargin();
		
		int ci=(set.getControlCircleOpacityInner()<<24)|set.getControlCircleColorInner();
		int co=(set.getControlCircleOpacityOuter()<<24)|set.getControlCircleColorOuter();
		int ca=(set.getControlArrowOpacity()<<24)|set.getControlArrowColor();
		
		float centertop=game.getDisplayHeight()-margin-radiusouter;
		float clx=margin+radiusouter;
		float crx=2*margin+3*radiusouter;
		float cjx=game.getDisplayWidth()-(margin+radiusouter);
		
		paint.setColor(co);
		canvas.drawCircle(clx, centertop, radiusouter, paint);
		canvas.drawCircle(crx, centertop, radiusouter, paint);
		canvas.drawCircle(cjx, centertop, radiusouter, paint);
		
		paint.setColor(ci);
		canvas.drawCircle(clx, centertop, radiusinner, paint);
		canvas.drawCircle(crx, centertop, radiusinner, paint);
		canvas.drawCircle(cjx, centertop, radiusinner, paint);
		
		float size=set.getControlArrowSize();
		float alx=clx-4*size;
		float arx=crx-4*size;
		float ajx=cjx-4*size;
		float ay=centertop-4*size;
		
		paint.setColor(ca);
		paint.setStyle(Style.FILL);
		
		Path path=new Path();
		
		makeLeftArrow(path,size,alx,ay);
		canvas.drawPath(path, paint);
		
		makeRightArrow(path,size,arx,ay);
		canvas.drawPath(path, paint);
		
		makeUpArrow(path,size,ajx,ay);
		canvas.drawPath(path, paint);
	}
	private void makeLeftArrow(Path path, float size, float xoffset, float yoffset)
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
	private void makeRightArrow(Path path, float size, float xoffset, float yoffset)
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
}
