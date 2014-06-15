package sw.superwhateverjnr.render;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Environment;

public class GLRenderer extends RendererBase
{
	public final static String HEAD = "head";
	public final static String BODY = "body";
	public final static String ARM = "arm";
	public final static String LEG = "leg";
	
	public final static String LEFT = "left";
	public final static String RIGHT = "right";
	
	
	
	
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
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        super.nextFrame(gl);
    }

    

    @Override
    protected void drawBackground()
    {
    	glr.position(0, 0, dwidth, dheight, dwidth, dheight);
        glr.color(gl, (game.getSettings().getBackgroundColor() & 0x00FFFFFF) | 0xFF000000);
        glr.draw(gl);
        glr.clearColor(gl);
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
//                switch(e.getType())
//                {
//                    case CREEPER:
//                        drawCreeper((Creeper) e);
//                        break;
//                    case ZOMBIE:
//                        drawZombie((Zombie) e);
//                        break;
//                    case SKELETON:
//                        drawSkeleton((Skeleton) e);
//                        break;
//                    default:
//                        break;
//                }
//                drawEntityBoxes(e);
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
        
        if(1!=2)
    	{
    		return;
    	}
        
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
    	
        drawFPS();
        drawTime();
    }
    
    private GLTex fpstex;
    private String oldfps;
    @SneakyThrows
    private void drawFPS()
    {
        String fps="FPS: "+Game.getInstance().getGlgameView().getFps();
        if(oldfps == null || !fps.equalsIgnoreCase(oldfps))
        {
        	Rect r=new Rect();
        	paint.getTextBounds(fps, 0, fps.length(), r);
        	
        	Bitmap bm=Bitmap.createBitmap(r.width()+10, r.height()+10, Bitmap.Config.ARGB_8888);
        	Canvas cv=new Canvas(bm);
            cv.drawText(fps, 0+5, r.height()+5, paint);
        	
            if(oldfps == null)
            {
            	fpstex=new GLTex(null, bm);
            }
            else
            {
            	fpstex.delete(gl);
            	fpstex.getBitmap().recycle();
            	fpstex.setBitmap(bm);
            }
            
            fpstex.upload(gl);
            oldfps=fps;
        }
        
        fpstex.position(5, 5, fpstex.getBitmap().getWidth(), fpstex.getBitmap().getHeight(), dwidth, dheight);
        fpstex.draw(gl);
    }
    
    private GLTex timetex;
    private String oldtime;
    @SneakyThrows
    private void drawTime()
    {
        String time="Time: "+(world.getTime()-world.getTimeElapsed())/100;
        if(oldtime == null || !time.equalsIgnoreCase(oldtime))
        {
        	Rect r=new Rect();
        	paint.getTextBounds(time, 0, time.length(), r);
        	
        	Bitmap bm=Bitmap.createBitmap(r.width()+10, r.height()+10, Bitmap.Config.ARGB_8888);
        	Canvas cv=new Canvas(bm);
            cv.drawText(time, 0+5, r.height()+5, paint);
        	
            if(oldtime == null)
            {
            	timetex=new GLTex(null, bm);
            }
            else
            {
            	timetex.delete(gl);
            	timetex.getBitmap().recycle();
            	timetex.setBitmap(bm);
            }
            
            timetex.upload(gl);
            oldtime=time;
        }
        
        timetex.position(dwidth-timetex.getBitmap().getWidth()-5, 5, timetex.getBitmap().getWidth(), timetex.getBitmap().getHeight(), dwidth, dheight);
        timetex.draw(gl);
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
            
//            Bitmap bm=cachedControlsBitmaps.get(key);
//            canvas.drawBitmap(bm, point.x, point.y, null);
            
            GLTex gltex=textures.get(key);
            if(gltex==null)
            {
            	continue;
            }
            
            int size=(int) Math.ceil(2*game.getSettings().getControlCircleRadiusOuter());
            
            gltex.position(point.x, point.y, size, size, dwidth, dheight);
            gltex.draw(gl);
        }
    }
    @Override
    protected void redrawControls()
    {
    	super.redrawControls();
    	
    	for(String key:cachedControlKeys)
    	{
    		GLTex gltex=textures.get(key);
    		if(gltex!=null)
    		{
    			gltex.delete(gl);
    			gltex.getBitmap().recycle();
    			gltex.setBitmap(cachedControlsBitmaps.get(key));
    		}
    		else
    		{
    			gltex = new GLTex(key, cachedControlsBitmaps.get(key));
    			textures.put(key, gltex);
    		}
    		
    		gltex.upload(gl);
    		
    		cachedControlsBitmaps.get(key).recycle();
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
