package sw.superwhateverjnr.render;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

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
		//background
		drawBackground();
		
		//world
		drawWorld();
		
		//entities
		drawEntities();
		
		//world
		drawPlayer();
		
		return bitmap;
	}
	
	private void drawBackground()
	{
		canvas.drawColor(0xFF275FFF);
	}
	private void drawWorld()
	{
		Location min=game.getMinDisplayPoint();
		int x1=(int) Math.floor(min.getX());
		int y1=(int) Math.floor(min.getY());
		int x2=(int) (x1+Math.ceil((double)game.getDisplayWidth()/game.getTextureWidth()))+(min.getX()%1==0?0:1);
		int y2=(int) (y1+Math.ceil((double)game.getDisplayHeight()/game.getTextureHeight()))+(min.getY()%1==0?0:1);
		
		int xstart=x1<0?0:x1;
		int ystart=y1<0?0:y1;
		int xend=x2>world.getWidth()?world.getWidth():x2;
		int yend=y2>world.getHeight()?world.getHeight():y2;
		
		
		float leftoffset;
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
		
		
		float topoffset;
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
				canvas.drawBitmap(tex.getImage(), left, top, paint);
			}
		}
	}
	private void drawEntities()
	{
		
	}
	private void drawPlayer()
	{
		
	}
}
