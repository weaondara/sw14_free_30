package sw.superwhateverjnr.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import lombok.Setter;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.block.Material;
import sw.superwhateverjnr.entity.Creeper;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.entity.Skeleton;
import sw.superwhateverjnr.entity.Zombie;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.texture.entity.CreeperTexture;
import sw.superwhateverjnr.texture.entity.PlayerTexture;
import sw.superwhateverjnr.texture.entity.SkeletonTexture;
import sw.superwhateverjnr.texture.entity.ZombieTexture;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.Location;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class GLRenderer extends RendererBase
{
    @Getter @Setter
    private float dwidth;
    @Getter @Setter
    private float dheight;
    @Getter @Setter
    private Map<Object, GLTex> textures;
    
    
    private GLLine gll;
    private GLRect glr;
    
    public GLRenderer()
    {
        super();
        
        gll = new GLLine();
        glr = new GLRect();
    }
    
    @Override
    public void nextFrame(GL10 gl)
    {
        this.gl = gl;
        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
//        gl.glTranslatef(0.0f, 0.0f, 0.00f);
        
        world=Game.getInstance().getWorld();
		if(world==null)
		{
			return;
		}
        
        prepare();
        
        drawBackground();
        
//        drawRect(0, 0, (int)dwidth/2, (int)dheight/2, 0xFF00FF00);
//        drawRect((int)dwidth/2, (int)dheight/2, (int)dwidth, (int)dheight, 0xFF00FF00);
//        
//        drawRect(0, 0, 64, 64, 0xFFFF0000);
//        drawRect((int)dwidth-64, 0, 64, 64, 0xFFFF0000);
//        drawRect(0, (int)dheight-64, 64, 64, 0xFFFF0000);
//        drawRect((int)dwidth-64, (int)dheight-64, 64, 64, 0xFFFF0000);
//        
//        drawRect(1000, 500, 64, 64, 0xFFFF0000);
        
//        gl.glTranslatef(0.0f, 0.0f, -0.01f);
        
//        gl.glTranslatef(0.0f, 0.0f, 0.01f);
        
        
//        Bitmap wood = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//        
//        new Canvas(wood).drawColor(0xFFFFFFFF);
//        drawBitmap(wood, 1000, 500, 64, 64, 0, 0, 0);
//        
//        new Canvas(wood).drawColor(0xFFFFFF00);
//        drawBitmap(wood, 1032, 532, 64, 64, 0, 0, 0);
//        
//        new Canvas(wood).drawColor(0xFFFF0000);
//        drawBitmap(wood, 984, 548, 64, 64, 0, 0, 0);
        
        drawWorld();
        drawWorldGrid();
    }

    

    @Override
    protected void drawBackground()
    {
        fill((game.getSettings().getBackgroundColor() & 0x00FFFFF) | 0xFF000000);
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
                GLTex gltex=textures.get(ref);
                if(gltex==null)
                {
                	ref=new IdAndSubId(b.getType().getId(),-1);
                	gltex=textures.get(ref);
                }
                
                if(gltex==null)
                {
                    System.out.println("Fatal Rendering Error: Texture not found!");
                    continue;
                }
                
                
                float top=topoffset+(y2-1-y)*game.getTextureSize();
                gltex.position(left, top, game.getTextureSize(), game.getTextureSize(), dwidth, dheight);
                gltex.draw(gl);
                
//                canvas.drawBitmap(tex.getImage(), left, top, null);
            }
        }
    }
    @Override
    protected void drawWorldGrid()
    {
    	gll.color(gl, 0xFFFF0000);
        
        for(int x=xstart;x<xend+1;x++)
        {
            float px=leftoffset+(x-x1)*game.getTextureSize();
            gll.position(px, 0, 0, dheight, dwidth, dheight);
            gll.draw(gl);
        }
        for(int y=ystart;y<yend+1;y++)
        {
            float py=topoffset+(y2-1-y)*game.getTextureSize();
            gll.position(0, py, dwidth, 0, dwidth, dheight);
            gll.draw(gl);
        }
        
        gll.clearColor(gl);
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
//        top=bottom;
//        bottom+=(bodyheight/blocksize)*game.getTextureSize();
        
        //body
        matrix.setRotate(0, 0, 0);
        matrix.postTranslate(left, top);
        canvas.drawBitmap(c.isLookingRight() ? pt.getBodyRight() : pt.getBodyLeft(), matrix, paint);
        
        //arm height
        left=x-game.getTextureSize()*(armwidth/blocksize)/2;
        right=x+game.getTextureSize()*(armwidth/blocksize)/2;
//        top=bottom;
//        bottom+=(armheight/blocksize)*game.getTextureSize();
        
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
        
//        float textheight=paint.ascent()+paint.descent();
        
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



    
    private void fill(int color)
    {
        glr.position(0, 0, dwidth, dheight, dwidth, dheight);
        glr.color(gl, color);
        glr.draw(gl);
        glr.clearColor(gl);
    }

    
//    private void drawRect(int x, int y, int width, int height, int color) 
//    {
//        float left=(x - dwidth / 2) / (dwidth / 2);
//        float right=left + width / (dwidth / 2);
//        float top=(dheight / 2 - y) / (dheight / 2);
//        float bottom=top - height / (dheight / 2);
//        
//        float vertices[] = {
//                left,  bottom, 0.0f,
//                left,  top,    0.0f,
//                right, bottom, 0.0f,
//                right, top,    0.0f
//        };
//        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
//        vertexByteBuffer.order(ByteOrder.nativeOrder());
//         
//        FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();
//        vertexBuffer.put(vertices);
//        vertexBuffer.position(0);
//        
//        
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        
//        gl.glColor4f(
//                (float)((color & 0x00FF0000) >> 16) / 256,
//                (float)((color & 0x0000FF00) >> 8 ) / 256,
//                (float)((color & 0x000000FF)      ) / 256,
//                (float)((color & 0xFF000000) >> 24) / 256);
//        
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//        
//        gl.glColor4f(1, 1, 1, 1);
//        
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//    }
    
//    private void drawBitmap(Bitmap bitmap, int x, int y, int width, int height, float ratation, float rotX, float rotY)
//    {
//        float left=(x - dwidth / 2) / (dwidth / 2);
//        float right=left + width / (dwidth / 2);
//        float top=(dheight / 2 - y) / (dheight / 2);
//        float bottom=top - height / (dheight / 2);
//        
//        float vertices[] = {
//                left,  bottom, 0.0f,
//                left,  top,    0.0f,
//                right, bottom, 0.0f,
//                right, top,    0.0f
//        };
//        
//        float texturevertices[] = {
//                0.0f, 1.0f,
//                0.0f, 0.0f,
//                1.0f, 1.0f,
//                1.0f, 0.0f
//        };
//        
//        short[] index = {
//                0, 1, 2,
//                2, 1, 3
//        };
//        
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4); 
//        byteBuffer.order(ByteOrder.nativeOrder());
//        FloatBuffer vertexBuffer = byteBuffer.asFloatBuffer();
//        vertexBuffer.put(vertices);
//        vertexBuffer.position(0);
//        
//        byteBuffer = ByteBuffer.allocateDirect(texturevertices.length * 4); 
//        byteBuffer.order(ByteOrder.nativeOrder());
//        FloatBuffer textureBuffer = byteBuffer.asFloatBuffer();
//        textureBuffer.put(texturevertices);
//        textureBuffer.position(0);
//        
//        byteBuffer = ByteBuffer.allocateDirect(index.length * 4); 
//        byteBuffer.order(ByteOrder.nativeOrder());
//        ShortBuffer indicesBuffer = byteBuffer.asShortBuffer();
//        indicesBuffer.put(index);
//        indicesBuffer.position(0);
//
//        
//        gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        
//        int texturemap[] = new int[1];
//        gl.glGenTextures(1, texturemap, 0);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturemap[0]);
//        
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
//        
//        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//        
//        //draw
//        gl.glFrontFace(GL10.GL_CW);
//        
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
//        
//        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
//        
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisable(GL10.GL_TEXTURE_2D);
//    }
}
