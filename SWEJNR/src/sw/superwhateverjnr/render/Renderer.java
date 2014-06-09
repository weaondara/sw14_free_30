package sw.superwhateverjnr.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Creeper;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.entity.Skeleton;
import sw.superwhateverjnr.entity.Spider;
import sw.superwhateverjnr.entity.Zombie;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.texture.entity.CreeperTexture;
import sw.superwhateverjnr.texture.entity.PlayerTexture;
import sw.superwhateverjnr.texture.entity.SkeletonTexture;
import sw.superwhateverjnr.texture.entity.ZombieTexture;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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

	public Renderer()
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
	
	private Location min;
	
	private int x1;
	private int x2;
	private int xstart;
	private int xend;
	
	private int y1;
	private int y2;	
	private int ystart;
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
					System.out.println("Fatal Rendering Error: Texture not found!");
					continue;
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
		List<Entity> list = game.getWorld().getEntities();
		for(int i = 0; i < list.size(); i++)
		{
			Entity e = list.get(i);
			Location l = e.getLocation();
			if(l.getX() > min.getX() - 1 && l.getX() < min.getX() + game.getDisplayWidth() / game.getTextureWidth() + 1 && 
			   l.getY() > min.getY() - 1 && l.getY() < min.getY() + game.getDisplayHeight() / game.getTextureHeight() + 1)
			{
				switch(e.getType())
				{
					case CREEPER:
						drawCreeper(canvas, (Creeper) e);
						break;
					case ZOMBIE:
						drawZombie(canvas, (Zombie) e);
						break;
					case SKELETON:
						drawSkeleton(canvas, (Skeleton) e);
						break;
					case SPIDER:
						drawSpider(canvas, (Spider) e);
						break;
					default:
						break;
				}
				drawEntityBoxes(canvas, e);
			}
		}
	}
	private void drawCreeper(Canvas canvas, Creeper c)
	{
		Location l=c.getLocation();
		if(l==null)
		{
			return;
		}
		
		float headwidth=8;
		float headheight=8;
		
		float bodywidth=4;
		float bodyheight=12;
		
		float legwidth=4;
		float legheight=6;
		
		float blocksize=12+1;

		blocksize*=2/(c.getHitBox().getMax().getY()-c.getHitBox().getMin().getY());
		
		
		paint.setStyle(Style.FILL);
		paint.setColor(0xFF000000);

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureWidth());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureHeight());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureWidth());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureWidth());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		CreeperTexture pt=(CreeperTexture) TextureMap.getTexture(EntityType.CREEPER);
		pt.scale(game.getTextureWidth()/64);
		
		//head
		float left=x-game.getTextureWidth()*(headwidth/blocksize)/2;
		float right=x+game.getTextureWidth()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureHeight();
		float top=ytop;

		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//body height
		left=x-game.getTextureWidth()*(bodywidth/blocksize)/2;
		right=x+game.getTextureWidth()*(bodywidth/blocksize)/2;
		top=bottom;
		bottom+=(bodyheight/blocksize)*game.getTextureHeight();
		
		
		//body
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
		
		
		//leg height
		left=x-game.getTextureWidth()*(legwidth/blocksize)/2;
		right=x+game.getTextureWidth()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureHeight();
		
		//legs
		float angle=c.getLegAngle();
		if(c.isLookingRight())
		{
			//left front leg
			matrix.setRotate(angle, 0, 0);
			matrix.postTranslate(right, top);
			canvas.drawBitmap(pt.getLeftLegRight(), matrix, paint);
			
			//left back leg
			matrix.setRotate(-angle, right-left, 0);
			matrix.postTranslate(left-(right-left), top);
			canvas.drawBitmap(pt.getLeftLegRight(), matrix, paint);

			//right front leg
			matrix.setRotate(-angle, 0, 0);
			matrix.postTranslate(right, top);
			canvas.drawBitmap(pt.getRightLegRight(), matrix, paint);
			
			//right back leg
			matrix.setRotate(angle, right-left, 0);
			matrix.postTranslate(left-(right-left), top);
			canvas.drawBitmap(pt.getRightLegRight(), matrix, paint);
		}
		else
		{
			//right front leg
			matrix.setRotate(-angle, 0, 0);
			matrix.postTranslate(left-(right-left), top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//right back leg
			matrix.setRotate(angle, right-left, 0);
			matrix.postTranslate(right, top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//left front leg
			matrix.setRotate(angle, 0, 0);
			matrix.postTranslate(left-(right-left), top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
			
			//left back leg
			matrix.setRotate(-angle, right-left, 0);
			matrix.postTranslate(right, top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
		}
	}
	private void drawZombie(Canvas canvas, Zombie c)
	{
		Location l=c.getLocation();
		if(l==null)
		{
			return;
		}
		
		float headwidth=8;
		float headheight=8;
		
		float bodywidth=4;
		float bodyheight=12;
		
		float legwidth=4;
		float legheight=12;
		
		float blocksize=16;

		blocksize*=2/(c.getHitBox().getMax().getY()-c.getHitBox().getMin().getY());
		
		
		paint.setStyle(Style.FILL);
		paint.setColor(0xFF000000);

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureWidth());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureHeight());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureWidth());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureWidth());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		ZombieTexture pt=(ZombieTexture) TextureMap.getTexture(EntityType.ZOMBIE);
		pt.scale(game.getTextureWidth()/64);
		
		//head
		float left=x-game.getTextureWidth()*(headwidth/blocksize)/2;
		float right=x+game.getTextureWidth()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureHeight();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//body height
		left=x-game.getTextureWidth()*(bodywidth/blocksize)/2;
		right=x+game.getTextureWidth()*(bodywidth/blocksize)/2;
		top=bottom;
		bottom+=(bodyheight/blocksize)*game.getTextureHeight();
		
		//arm
		float angle=c.getArmAngle();
		if(c.isLookingRight())
		{
			//left arm
			matrix.setRotate(-90+angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);			
			canvas.drawBitmap(pt.getLeftArmRight(), matrix, paint);
		}
		else
		{
			//right arm
			matrix.setRotate(90-angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightArmLeft(), matrix, paint);
		}
		
		//body
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
		
		//arm
		if(c.isLookingRight())
		{
			//right arm
			matrix.setRotate(-90-angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightArmRight(), matrix, paint);
		}
		else
		{
			//left arm
			matrix.setRotate(90+angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftArmLeft(), matrix, paint);
		}
		
		//leg height
		left=x-game.getTextureWidth()*(legwidth/blocksize)/2;
		right=x+game.getTextureWidth()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureHeight();
		
		//legs
		angle=c.getLegAngle();
		if(c.isLookingRight())
		{
			//left leg
			matrix.setRotate(angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftLegRight(), matrix, paint);
			
			//right leg
			matrix.setRotate(-angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightLegRight(), matrix, paint);
		}
		else
		{
			//right leg
			matrix.setRotate(-angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//left leg
			matrix.setRotate(angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
		}
	}
	private void drawSkeleton(Canvas canvas, Skeleton c)
	{
		Location l=c.getLocation();
		if(l==null)
		{
			return;
		}
		
		float headwidth=8;
		float headheight=8;
		
		float armwidth=2;
		float armheight=12;
		
		float bodywidth=4;
		float bodyheight=12;
		
		float legwidth=2;
		float legheight=12;
		
		float blocksize=16;

		blocksize*=2/(c.getHitBox().getMax().getY()-c.getHitBox().getMin().getY());
		
		
		paint.setStyle(Style.FILL);
		paint.setColor(0xFF000000);

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureWidth());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureHeight());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureWidth());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureWidth());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		SkeletonTexture pt=(SkeletonTexture) TextureMap.getTexture(EntityType.SKELETON);
		pt.scale(game.getTextureWidth()/64);
		
		//head
		float left=x-game.getTextureWidth()*(headwidth/blocksize)/2;
		float right=x+game.getTextureWidth()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureHeight();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//arm height
		left=x-game.getTextureWidth()*(armwidth/blocksize)/2;
		right=x+game.getTextureWidth()*(armwidth/blocksize)/2;
		top=bottom;
		bottom+=(armheight/blocksize)*game.getTextureHeight();
		
		//arm
		float angle=c.getArmAngle();
		if(c.isLookingRight())
		{
			//left arm
			matrix.setRotate(-90+angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);			
			canvas.drawBitmap(pt.getLeftArmRight(), matrix, paint);
		}
		else
		{
			//right arm
			matrix.setRotate(90-angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightArmLeft(), matrix, paint);
		}
		
		//body height
		left=x-game.getTextureWidth()*(bodywidth/blocksize)/2;
		right=x+game.getTextureWidth()*(bodywidth/blocksize)/2;
//		top=bottom;
//		bottom+=(bodyheight/blocksize)*game.getTextureHeight();
		
		//body
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
		
		//arm height
		left=x-game.getTextureWidth()*(armwidth/blocksize)/2;
		right=x+game.getTextureWidth()*(armwidth/blocksize)/2;
//		top=bottom;
//		bottom+=(armheight/blocksize)*game.getTextureHeight();
		
		//arm
		if(c.isLookingRight())
		{
			//right arm
			matrix.setRotate(-90-angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightArmRight(), matrix, paint);
		}
		else
		{
			//left arm
			matrix.setRotate(90+angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftArmLeft(), matrix, paint);
		}
		
		//leg height
		left=x-game.getTextureWidth()*(legwidth/blocksize)/2;
		right=x+game.getTextureWidth()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureHeight();
		
		//legs
		angle=c.getLegAngle();
		if(c.isLookingRight())
		{
			//left leg
			matrix.setRotate(angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftLegRight(), matrix, paint);
			
			//right leg
			matrix.setRotate(-angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightLegRight(), matrix, paint);
		}
		else
		{
			//right leg
			matrix.setRotate(-angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//left leg
			matrix.setRotate(angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
		}
	}
	private void drawSpider(Canvas canvas, Spider c)
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
		
		float headwidth=8;
		float headheight=8;
		
		float bodywidth=4;
		float bodyheight=12;
		
		float legwidth=4;
		float legheight=12;
		
		float blocksize=16;

		blocksize*=2/(p.getHitBox().getMax().getY()-p.getHitBox().getMin().getY());
		
		paint.setStyle(Style.FILL);
		paint.setColor(0xFF000000);

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureWidth());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureHeight());

		float playerwidh=(float) (Math.abs(p.getHitBox().getMin().getX()-p.getHitBox().getMax().getX())*game.getTextureWidth());
		float playerheight=(float) (Math.abs(p.getHitBox().getMin().getY()-p.getHitBox().getMax().getY())*game.getTextureWidth());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		PlayerTexture pt=(PlayerTexture) TextureMap.getTexture(EntityType.PLAYER);
		pt.scale(pt.getOrigWidth()/(16+1)*game.getTextureWidth()/64);
		
		//head
		float left=x-game.getTextureWidth()*(headwidth/blocksize)/2;
		float right=x+game.getTextureWidth()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureHeight();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(p.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//body height
		left=x-game.getTextureWidth()*(bodywidth/blocksize)/2;
		right=x+game.getTextureWidth()*(bodywidth/blocksize)/2;
		top=bottom;
		bottom+=(bodyheight/blocksize)*game.getTextureHeight();
		
		//arm
		float angle=p.getArmAngle();
		if(p.isLookingRight())
		{
			//left arm
			matrix.setRotate(angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);			
			canvas.drawBitmap(pt.getLeftArmRight(), matrix, paint);
		}
		else
		{
			//right arm
			matrix.setRotate(-angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightArmLeft(), matrix, paint);
		}
		
		//body
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(p.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
		
		//arm
		if(p.isLookingRight())
		{
			//right arm
			matrix.setRotate(-angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightArmRight(), matrix, paint);
		}
		else
		{
			//left arm
			matrix.setRotate(angle, (right-left)/2, (right-left)/2);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftArmLeft(), matrix, paint);
		}
		
		//leg height
		left=x-game.getTextureWidth()*(legwidth/blocksize)/2;
		right=x+game.getTextureWidth()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureHeight();
		
		//legs
		angle=p.getLegAngle();
		if(p.isLookingRight())
		{
			//left leg
			matrix.setRotate(angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftLegRight(), matrix, paint);
			
			//right leg
			matrix.setRotate(-angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightLegRight(), matrix, paint);
		}
		else
		{
			//right leg
			matrix.setRotate(-angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//left leg
			matrix.setRotate(angle, (right-left)/2, 0);
			matrix.postTranslate(left, top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
		}
	
		
		drawEntityBoxes(canvas, p);
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
	private Map<String,PointF> cachedControlsPostions=new HashMap<String, PointF>();
	private Map<String,Bitmap> cachedControlsBitmaps=new HashMap<String, Bitmap>();
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
	
	private void drawEntityBoxes(Canvas canvas, Entity e)
	{
		//render & hitbox
		if(SWEJNR.DEBUG)
		{
			float x=(float) (leftoffset+(e.getLocation().getX()-x1)*game.getTextureWidth());
			float y=(float) (topoffset+(y2-e.getLocation().getY())*game.getTextureHeight());
			
			paint.setStyle(Style.STROKE);
			paint.setColor(0xFFFFFF00);
			
			float playerwidh=(float) (Math.abs(e.getRenderBox().getMin().getX()-e.getRenderBox().getMax().getX())*game.getTextureWidth());
	
			float left=x-playerwidh/2;
			float right=x+playerwidh/2;
			float bottom=y;
			float top=(float) (y-e.getRenderBox().getMax().getY()*game.getTextureHeight());
			
			canvas.drawRect(left, top, right, bottom, paint);
			
		
			paint.setColor(0xFF00FF00);
			
			playerwidh=(float) (Math.abs(e.getHitBox().getMin().getX()-e.getHitBox().getMax().getX())*game.getTextureWidth());

			left=x-playerwidh/2;
			right=x+playerwidh/2;
			bottom=y;
			top=(float) (y-e.getHitBox().getMax().getY()*game.getTextureHeight());
			
			canvas.drawRect(left, top, right, bottom, paint);
		}
	}
}
