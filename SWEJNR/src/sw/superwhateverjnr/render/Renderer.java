package sw.superwhateverjnr.render;

import java.util.List;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Creeper;
import sw.superwhateverjnr.entity.Drop;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.entity.Skeleton;
import sw.superwhateverjnr.entity.Zombie;
import sw.superwhateverjnr.texture.ItemTexture;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.texture.entity.CreeperTexture;
import sw.superwhateverjnr.texture.entity.PlayerTexture;
import sw.superwhateverjnr.texture.entity.SkeletonTexture;
import sw.superwhateverjnr.texture.entity.ZombieTexture;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.Location;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;

public class Renderer extends RendererBase
{
	public Renderer()
	{
		super();
	}

	@Override
	protected void drawBackground()
	{
		canvas.drawColor((game.getSettings().getBackgroundColor() & 0x00FFFFFF) | 0xFF000000);
	}
	@Override
	protected void drawWorld()
	{
		for(int x=xstart;x<xend;x++)
		{
			float left=leftoffset + (x-x1) * game.getTextureSize();
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
				
				float top=topoffset+(y2-1-y)*game.getTextureSize();
				canvas.drawBitmap(tex.getImage(), left, top, null);
			}
		}
	}
	@Override
	protected void drawWorldGrid()
	{
		paint.setColor(0xFFFF0000);
		paint.setStrokeWidth(0);
		
		for(int x=xstart;x<xend+1;x++)
		{
			float px=leftoffset+(x-x1)*game.getTextureSize();
			canvas.drawLine(px, 0, px, game.getDisplayHeight(), paint);
		}
		for(int y=ystart;y<yend+1;y++)
		{
			float py=topoffset+(y2-1-y)*game.getTextureSize();
			canvas.drawLine(0, py, game.getDisplayWidth(), py, paint);
		}
	}
	@Override
	protected void drawEntities()
	{
		List<Entity> list = game.getWorld().getEntities();
		for(int i = 0; i < list.size(); i++)
		{
			Entity e = list.get(i);
			Location l = e.getLocation();
			if(l.getX() > min.getX() - 1 && l.getX() < min.getX() + game.getDisplayWidth() / game.getTextureSize() + 1 && 
			   l.getY() > min.getY() - 1 && l.getY() < min.getY() + game.getDisplayHeight() / game.getTextureSize() + 1)
			{
				switch(e.getType())
				{
					case CREEPER:
						drawCreeper((Creeper) e);
						break;
					case ZOMBIE:
						drawZombie((Zombie) e);
						break;
					case SKELETON:
						drawSkeleton((Skeleton) e);
						break;
					case DROPPED_ITEM:
						drawItem((Drop) e);
						break;
					default:
						break;
				}
				drawEntityBoxes(e);
			}
		}
	}
	@Override
	protected void drawCreeper(Creeper c)
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

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureSize());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureSize());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureSize());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureSize());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		CreeperTexture pt=(CreeperTexture) TextureMap.getTexture(EntityType.CREEPER);
		pt.scale(game.getTextureSize()/64);
		
		//head
		float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
		float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
		float top=ytop;

		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//body height
		left=x-game.getTextureSize()*(bodywidth/blocksize)/2;
		right=x+game.getTextureSize()*(bodywidth/blocksize)/2;
		top=bottom;
		bottom+=(bodyheight/blocksize)*game.getTextureSize();
		
		
		//body
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
		
		
		//leg height
		left=x-game.getTextureSize()*(legwidth/blocksize)/2;
		right=x+game.getTextureSize()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureSize();
		
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
			matrix.postTranslate(right, top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//right back leg
			matrix.setRotate(angle, right-left, 0);
			matrix.postTranslate(left-(right-left), top);
			canvas.drawBitmap(pt.getRightLegLeft(), matrix, paint);
			
			//left front leg
			matrix.setRotate(angle, 0, 0);
			matrix.postTranslate(right, top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
			
			//left back leg
			matrix.setRotate(-angle, right-left, 0);
			matrix.postTranslate(left-(right-left), top);
			canvas.drawBitmap(pt.getLeftLegLeft(), matrix, paint);
		}
	}
	@Override
	protected void drawZombie(Zombie c)
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

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureSize());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureSize());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureSize());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureSize());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		ZombieTexture pt=(ZombieTexture) TextureMap.getTexture(EntityType.ZOMBIE);
		pt.scale(game.getTextureSize()/64);
		
		//head
		float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
		float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//body height
		left=x-game.getTextureSize()*(bodywidth/blocksize)/2;
		right=x+game.getTextureSize()*(bodywidth/blocksize)/2;
		top=bottom;
		bottom+=(bodyheight/blocksize)*game.getTextureSize();
		
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
		left=x-game.getTextureSize()*(legwidth/blocksize)/2;
		right=x+game.getTextureSize()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureSize();
		
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
	@Override
	protected void drawSkeleton(Skeleton c)
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

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureSize());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureSize());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureSize());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureSize());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		SkeletonTexture pt=(SkeletonTexture) TextureMap.getTexture(EntityType.SKELETON);
		pt.scale(game.getTextureSize()/64);
		
		//head
		float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
		float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//arm height
		left=x-game.getTextureSize()*(armwidth/blocksize)/2;
		right=x+game.getTextureSize()*(armwidth/blocksize)/2;
		top=bottom;
		bottom+=(armheight/blocksize)*game.getTextureSize();
		
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
		left=x-game.getTextureSize()*(bodywidth/blocksize)/2;
		right=x+game.getTextureSize()*(bodywidth/blocksize)/2;
//		top=bottom;
//		bottom+=(bodyheight/blocksize)*game.getTextureSize();
		
		//body
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(c.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
		
		//arm height
		left=x-game.getTextureSize()*(armwidth/blocksize)/2;
		right=x+game.getTextureSize()*(armwidth/blocksize)/2;
//		top=bottom;
//		bottom+=(armheight/blocksize)*game.getTextureSize();
		
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
		left=x-game.getTextureSize()*(legwidth/blocksize)/2;
		right=x+game.getTextureSize()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureSize();
		
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
	@Override
	protected void drawItem(Drop c)
	{
		Location l=c.getLocation();
		if(l==null)
		{
			return;
		}
		
		float itemwidth=12;
		float itemheight=12;
		
		float blocksize=16;

		blocksize*=1/(c.getHitBox().getMax().getY()-c.getHitBox().getMin().getY());
		
		
		paint.setStyle(Style.FILL);
		paint.setColor(0xFF000000);

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureSize());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureSize());

		float playerwidh=(float) (Math.abs(c.getHitBox().getMin().getX()-c.getHitBox().getMax().getX())*game.getTextureSize());
		float playerheight=(float) (Math.abs(c.getHitBox().getMin().getY()-c.getHitBox().getMax().getY())*game.getTextureSize());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		ItemTexture pt=(ItemTexture) TextureMap.getTexture(c.getDropType().getId());
		pt.scale(game.getTextureSize()/64*itemwidth/blocksize);
		
		//head
		float left=x-game.getTextureSize()*(itemwidth/blocksize)/2;
		float right=x+game.getTextureSize()*(itemwidth/blocksize)/2;
		float bottom=ytop+(itemheight/blocksize)*game.getTextureSize();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(pt.getImage(), matrix, paint);
	}
	@Override
	protected void drawPlayer()
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

		float x=(float) (leftoffset+(l.getX()-x1)*game.getTextureSize());
		float y=(float) (topoffset+(y2-l.getY())*game.getTextureSize());

		float playerwidh=(float) (Math.abs(p.getHitBox().getMin().getX()-p.getHitBox().getMax().getX())*game.getTextureSize());
		float playerheight=(float) (Math.abs(p.getHitBox().getMin().getY()-p.getHitBox().getMax().getY())*game.getTextureSize());
		
		float ytop=y-playerheight;
		
		Matrix matrix = new Matrix();
		
		PlayerTexture pt=(PlayerTexture) TextureMap.getTexture(EntityType.PLAYER);
		pt.scale(pt.getOrigWidth()/(16+1)*game.getTextureSize()/64);
		
		//head
		float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
		float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
		float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
		float top=ytop;
		
		matrix.setRotate(0, 0, 0);
		matrix.postTranslate(left, top);
		canvas.drawBitmap(p.isLookingRight() ? pt.getHeadRight() : pt.getHeadLeft(), matrix, paint);

		//body height
		left=x-game.getTextureSize()*(bodywidth/blocksize)/2;
		right=x+game.getTextureSize()*(bodywidth/blocksize)/2;
		top=bottom;
		bottom+=(bodyheight/blocksize)*game.getTextureSize();
		
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
		left=x-game.getTextureSize()*(legwidth/blocksize)/2;
		right=x+game.getTextureSize()*(legwidth/blocksize)/2;
		top=bottom;
		bottom+=(legheight/blocksize)*game.getTextureSize();
		
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
	
		
		drawEntityBoxes(p);
	}
	@Override
	protected void drawInfo()
	{
		int fontsize=30;
		int fontcolor=0xFF00FF00;
		
		paint.setColor(fontcolor);
		paint.setTextSize(fontsize);
		
		Rect r=new Rect();
		paint.getTextBounds("b", 0, 1, r);
		int textheight=r.height();
		
//		float textheight=paint.ascent()+paint.descent();
		
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("FPS: "+Game.getInstance().getGameView().getFps(), 10, 10+textheight, paint);
		
		paint.setTextAlign(Align.RIGHT);
		canvas.drawText("Time: "+(world.getTime()-world.getTimeElapsed())/100, canvas.getWidth() - 10, 10+textheight, paint);
	}
	@Override
	protected void drawControls0(boolean retry)
	{
		for(String key:cachedControlKeys)
		{
			PointF point=cachedControlsPostions.get(key);
			if(point==null)
			{
				redrawControls();
				if(retry)
				{
					drawControls0(false);
				}
				return;
			}
			
			Bitmap bm=cachedControlsBitmaps.get(key);
			canvas.drawBitmap(bm, point.x, point.y, null);
		}
	}

	@Override
	protected void drawEntityBoxes(Entity e)
	{
		//render & hitbox
		if(SWEJNR.DEBUG)
		{
			//render box
			float x=(float) (leftoffset+(e.getLocation().getX()-x1)*game.getTextureSize());
			float y=(float) (topoffset+(y2-e.getLocation().getY())*game.getTextureSize());
			
			paint.setStyle(Style.STROKE);
			paint.setColor(0xFFFFFF00);
			
			float playerwidh=(float) (Math.abs(e.getRenderBox().getMin().getX()-e.getRenderBox().getMax().getX())*game.getTextureSize());
	
			float left=x-playerwidh/2;
			float right=x+playerwidh/2;
			float bottom=y;
			float top=(float) (y-e.getRenderBox().getMax().getY()*game.getTextureSize());
			
			canvas.drawRect(left, top, right, bottom, paint);
			
			//hitbox
			paint.setColor(0xFF00FF00);
			
			playerwidh=(float) (Math.abs(e.getHitBox().getMin().getX()-e.getHitBox().getMax().getX())*game.getTextureSize());
			
			left=x-playerwidh/2;
			right=x+playerwidh/2;
			bottom=y;
			top=(float) (y-e.getHitBox().getMax().getY()*game.getTextureSize());
			
			canvas.drawRect(left, top, right, bottom, paint);
			
			//triggercenter
			Location tc=e.getTriggerCenter().add(e.getLocation());
			x=(float) (leftoffset+(tc.getX()-x1)*game.getTextureSize());
			y=(float) (topoffset+(y2-tc.getY())*game.getTextureSize());
			
			canvas.drawLine(x-13, y-13, x+13, y+13, paint);
			canvas.drawLine(x+13, y-13, x-13, y+13, paint);
			
			//triggerradius
			if(e.getTriggerRadius() > 0)
			{
				canvas.drawCircle(x, y, (float) e.getTriggerRadius()*game.getTextureSize(), paint);
			}
			
			//debug data
			paint.setTextAlign(Align.LEFT);
			
			x=(float) (leftoffset+(e.getLocation().getX()-x1)*game.getTextureSize());
			y=(float) (topoffset+(y2-e.getLocation().getY())*game.getTextureSize());

			float playerheight=(float) (Math.abs(e.getHitBox().getMin().getY()-e.getHitBox().getMax().getY())*game.getTextureSize());
			
			String debugdata=e.getDebugInfo();
			String[] list=debugdata.split("\n");
			y+=(paint.descent()+paint.ascent())*(list.length-1) - playerheight;
			
			for(String s:list)
			{
				canvas.drawText(s, x, y, paint);
				y-=paint.descent()+paint.ascent();
			}
		}
	}
}
