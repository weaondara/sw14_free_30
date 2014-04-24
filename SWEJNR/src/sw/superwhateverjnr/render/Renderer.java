package sw.superwhateverjnr.render;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.World;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

public class Renderer
{
	private World world;
	private Game game;
	
	public Renderer(World world)
	{
		super();
		this.world = world;
		game=Game.getInstance();
	}

	public Bitmap nextFrame()
	{
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bm = Bitmap.createBitmap(game.getDisplayWidth(), game.getDisplayHeight(), conf); 
		Canvas c = new Canvas(bm);
		Paint p = new Paint();
		
		//background
		drawBackground(c, p);
		
		//world
		drawWorld(c, p);
		
//		//entities
//		drawEntities(c, p);
//		
//		//world
//		drawPlayer(c, p);
		
		return bm;
	}
	
	private void drawBackground(Canvas c, Paint p)
	{
		p.setColor(0xFF4444FF);
		p.setStyle(Style.FILL);
		c.drawRect(0, 0, game.getDisplayWidth(), game.getDisplayHeight(), p);
	}
	private void drawWorld(Canvas c, Paint p)
	{
		PointF min=game.getMinDisplayPoint();
		float x1=min.x;
		float y1=min.y;
		float x2=x1+game.getDisplayWidth();
		float y2=y1+game.getDisplayHeight();
		
		int xmax=(int) (Math.ceil(x2)<world.getWidth()?Math.ceil(x2):world.getWidth());
		int ymax=(int) (Math.ceil(y2)<world.getHeight()?Math.ceil(y2):world.getHeight());
		
		for(int x=(int) Math.floor(x1);x<xmax;x++)
		{
			float left=min.x%game.getTextureWidth()+x*game.getTextureWidth();
			for(int y=(int) Math.floor(y1);y<ymax;y++)
			{
				Block b=world.getBlockAt(x, y);
				if(b.getType()==Material.AIR)
				{
					continue;
				}
//				System.out.println("x="+x+";y="+y);
				
				IdAndSubId ref=new IdAndSubId(b.getType().getId(),b.getSubid());
				Texture tex=TextureMap.getTexture(ref);
				if(tex==null)
				{
					System.out.println("fatal render");
					continue; //fatal error
				}
				
				float top=(game.getDisplayHeight()-min.y)%game.getTextureHeight()+y*game.getTextureWidth();
//				System.out.println("left="+left+";top="+top);
				c.drawBitmap(tex.getImage(), left, top, p);
			}
		}
	}
	private void drawEntities(Canvas c, Paint p)
	{
		
	}
	private void drawPlayer(Canvas c, Paint p)
	{
		
	}
}
