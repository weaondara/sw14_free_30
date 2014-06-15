package sw.superwhateverjnr.render;

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
import sw.superwhateverjnr.texture.entity.SkeletonTexture;
import sw.superwhateverjnr.texture.entity.ZombieTexture;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.world.Location;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class GLRenderer extends RendererBase
{
    public final static String HEAD = "head";
    public final static String BODY = "body";
    public final static String ARM = "arm";
    public final static String LEG = "leg";
    
    public final static String LEFT = "left";
    public final static String RIGHT = "right";
    
    // ---------------------------------- player --------------------------------------
    public final static String PLAYER_HEAD_RIGHT=EntityType.PLAYER.name()+HEAD+RIGHT;
    public final static String PLAYER_HEAD_LEFT=EntityType.PLAYER.name()+HEAD+LEFT;
    
    public final static String PLAYER_BODY_RIGHT=EntityType.PLAYER.name()+BODY+RIGHT;
    public final static String PLAYER_BODY_LEFT=EntityType.PLAYER.name()+BODY+LEFT;
    
    public final static String PLAYER_RIGHT_ARM_RIGHT=EntityType.PLAYER.name()+RIGHT+ARM+RIGHT;
    public final static String PLAYER_RIGHT_ARM_LEFT=EntityType.PLAYER.name()+RIGHT+ARM+LEFT;
    public final static String PLAYER_LEFT_ARM_RIGHT=EntityType.PLAYER.name()+LEFT+ARM+RIGHT;
    public final static String PLAYER_LEFT_ARM_LEFT=EntityType.PLAYER.name()+LEFT+ARM+LEFT;
    
    public final static String PLAYER_RIGHT_LEG_RIGHT=EntityType.PLAYER.name()+RIGHT+LEG+RIGHT;
    public final static String PLAYER_RIGHT_LEG_LEFT=EntityType.PLAYER.name()+RIGHT+LEG+LEFT;
    public final static String PLAYER_LEFT_LEG_RIGHT=EntityType.PLAYER.name()+LEFT+LEG+RIGHT;
    public final static String PLAYER_LEFT_LEG_LEFT=EntityType.PLAYER.name()+LEFT+LEG+LEFT;
    
    // ---------------------------------- zombie --------------------------------------
    public final static String ZOMBIE_HEAD_RIGHT=EntityType.ZOMBIE.name()+HEAD+RIGHT;
    public final static String ZOMBIE_HEAD_LEFT=EntityType.ZOMBIE.name()+HEAD+LEFT;
    
    public final static String ZOMBIE_BODY_RIGHT=EntityType.ZOMBIE.name()+BODY+RIGHT;
    public final static String ZOMBIE_BODY_LEFT=EntityType.ZOMBIE.name()+BODY+LEFT;
    
    public final static String ZOMBIE_RIGHT_ARM_RIGHT=EntityType.ZOMBIE.name()+RIGHT+ARM+RIGHT;
    public final static String ZOMBIE_RIGHT_ARM_LEFT=EntityType.ZOMBIE.name()+RIGHT+ARM+LEFT;
    public final static String ZOMBIE_LEFT_ARM_RIGHT=EntityType.ZOMBIE.name()+LEFT+ARM+RIGHT;
    public final static String ZOMBIE_LEFT_ARM_LEFT=EntityType.ZOMBIE.name()+LEFT+ARM+LEFT;
    
    public final static String ZOMBIE_RIGHT_LEG_RIGHT=EntityType.ZOMBIE.name()+RIGHT+LEG+RIGHT;
    public final static String ZOMBIE_RIGHT_LEG_LEFT=EntityType.ZOMBIE.name()+RIGHT+LEG+LEFT;
    public final static String ZOMBIE_LEFT_LEG_RIGHT=EntityType.ZOMBIE.name()+LEFT+LEG+RIGHT;
    public final static String ZOMBIE_LEFT_LEG_LEFT=EntityType.ZOMBIE.name()+LEFT+LEG+LEFT;
    
    // ---------------------------------- skeleton --------------------------------------
    public final static String SKELETON_HEAD_RIGHT=EntityType.SKELETON.name()+HEAD+RIGHT;
    public final static String SKELETON_HEAD_LEFT=EntityType.SKELETON.name()+HEAD+LEFT;
    
    public final static String SKELETON_BODY_RIGHT=EntityType.SKELETON.name()+BODY+RIGHT;
    public final static String SKELETON_BODY_LEFT=EntityType.SKELETON.name()+BODY+LEFT;
    
    public final static String SKELETON_RIGHT_ARM_RIGHT=EntityType.SKELETON.name()+RIGHT+ARM+RIGHT;
    public final static String SKELETON_RIGHT_ARM_LEFT=EntityType.SKELETON.name()+RIGHT+ARM+LEFT;
    public final static String SKELETON_LEFT_ARM_RIGHT=EntityType.SKELETON.name()+LEFT+ARM+RIGHT;
    public final static String SKELETON_LEFT_ARM_LEFT=EntityType.SKELETON.name()+LEFT+ARM+LEFT;
    
    public final static String SKELETON_RIGHT_LEG_RIGHT=EntityType.SKELETON.name()+RIGHT+LEG+RIGHT;
    public final static String SKELETON_RIGHT_LEG_LEFT=EntityType.SKELETON.name()+RIGHT+LEG+LEFT;
    public final static String SKELETON_LEFT_LEG_RIGHT=EntityType.SKELETON.name()+LEFT+LEG+RIGHT;
    public final static String SKELETON_LEFT_LEG_LEFT=EntityType.SKELETON.name()+LEFT+LEG+LEFT;
    
    // ---------------------------------- creeper --------------------------------------
    public final static String CREEPER_HEAD_RIGHT=EntityType.CREEPER.name()+HEAD+RIGHT;
    public final static String CREEPER_HEAD_LEFT=EntityType.CREEPER.name()+HEAD+LEFT;
    
    public final static String CREEPER_BODY_RIGHT=EntityType.CREEPER.name()+BODY+RIGHT;
    public final static String CREEPER_BODY_LEFT=EntityType.CREEPER.name()+BODY+LEFT;
    
    public final static String CREEPER_RIGHT_LEG_RIGHT=EntityType.CREEPER.name()+RIGHT+LEG+RIGHT;
    public final static String CREEPER_RIGHT_LEG_LEFT=EntityType.CREEPER.name()+RIGHT+LEG+LEFT;
    public final static String CREEPER_LEFT_LEG_RIGHT=EntityType.CREEPER.name()+LEFT+LEG+RIGHT;
    public final static String CREEPER_LEFT_LEG_LEFT=EntityType.CREEPER.name()+LEFT+LEG+LEFT;
    
    
    
    
    
    
    
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
    	gl.glDisable(GL10.GL_BLEND);
    	
        glr.position(0, 0, dwidth, dheight, dwidth, dheight);
        glr.color(gl, (game.getSettings().getBackgroundColor() & 0x00FFFFFF) | 0xFF000000);
        glr.draw(gl);
        glr.clearColor(gl);
        
        gl.glEnable(GL10.GL_BLEND);
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
    	gl.glDisable(GL10.GL_BLEND);
    	
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
        
        gl.glEnable(GL10.GL_BLEND);
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
        
        
        //head
        float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
        float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
        float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
        float top=ytop;

        drawTex(c.isLookingRight() ? CREEPER_HEAD_RIGHT : CREEPER_HEAD_LEFT, left, top, right, bottom, 0, 0, 0);

        //body height
        left=x-game.getTextureSize()*(bodywidth/blocksize)/2;
        right=x+game.getTextureSize()*(bodywidth/blocksize)/2;
        top=bottom;
        bottom+=(bodyheight/blocksize)*game.getTextureSize();
        
        
        //body
        drawTex(c.isLookingRight() ? CREEPER_BODY_RIGHT : CREEPER_BODY_LEFT, left, top, right, bottom, 0, (right-left)/2, (right-left)/2);
        
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
            drawTex(CREEPER_LEFT_LEG_RIGHT, left, top, right, bottom, angle, right-left, 0, 0, 0);
            
            //left back leg
            drawTex(CREEPER_LEFT_LEG_RIGHT, left, top, right, bottom, -angle, 0, 0, right-left, 0);

            //right front leg
            drawTex(CREEPER_RIGHT_LEG_RIGHT, left, top, right, bottom, -angle, right-left, 0, 0, 0);
            
            //right back leg
            drawTex(CREEPER_RIGHT_LEG_RIGHT, left, top, right, bottom, angle, 0, 0, right-left, 0);
        }
        else
        {
            //right front leg
            drawTex(CREEPER_RIGHT_LEG_LEFT, left, top, right, bottom, -angle, right-left, 0, 0, 0);
            
            //right back leg
            drawTex(CREEPER_RIGHT_LEG_LEFT, left, top, right, bottom, angle, 0, 0, right-left, 0);
            
            //left front leg
            drawTex(CREEPER_LEFT_LEG_LEFT, left, top, right, bottom, angle, right-left, 0, 0, 0);
            
            //left back leg
            drawTex(CREEPER_LEFT_LEG_LEFT, left, top, right, bottom, -angle, 0, 0, right-left, 0);
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
        
        
        //head
        float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
        float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
        float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
        float top=ytop;
        
        drawTex(c.isLookingRight() ? ZOMBIE_HEAD_RIGHT : ZOMBIE_HEAD_LEFT, left, top, right, bottom, 0, 0, 0);
        
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
            drawTex(ZOMBIE_LEFT_ARM_RIGHT, left, top, right, bottom, +90-angle, (right-left)/2, (right-left)/2);
        }
        else
        {
            //right arm
            drawTex(ZOMBIE_RIGHT_ARM_LEFT, left, top, right, bottom, -90+angle, (right-left)/2, (right-left)/2);
        }
        
        //body
        drawTex(c.isLookingRight() ? ZOMBIE_BODY_RIGHT : ZOMBIE_BODY_LEFT, left, top, right, bottom, 0, (right-left)/2, (right-left)/2);
        
        //arm
        if(c.isLookingRight())
        {
            //right arm
            drawTex(ZOMBIE_RIGHT_ARM_RIGHT, left, top, right, bottom, +90+angle, (right-left)/2, (right-left)/2);
        }
        else
        {
            //left arm
            drawTex(ZOMBIE_LEFT_ARM_LEFT, left, top, right, bottom, -90-angle, (right-left)/2, (right-left)/2);
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
            drawTex(ZOMBIE_LEFT_LEG_RIGHT, left, top, right, bottom, angle, (right-left)/2, 0);
            
            //right leg
            drawTex(ZOMBIE_RIGHT_LEG_RIGHT, left, top, right, bottom, -angle, (right-left)/2, 0);
        }
        else
        {
            //right leg
            drawTex(ZOMBIE_RIGHT_LEG_LEFT, left, top, right, bottom, -angle, (right-left)/2, 0);
            
            //left leg
            drawTex(ZOMBIE_LEFT_LEG_LEFT, left, top, right, bottom, angle, (right-left)/2, 0);
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
        
        //head
        float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
        float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
        float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
        float top=ytop;
        
        drawTex(c.isLookingRight() ? SKELETON_HEAD_RIGHT : SKELETON_HEAD_LEFT, left, top, right, bottom, 0, 0, 0);

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
            drawTex(SKELETON_LEFT_ARM_RIGHT, left, top, right, bottom, 90-angle, (right-left)/2, (right-left)/2);
        }
        else
        {
            //right arm
            drawTex(SKELETON_RIGHT_ARM_LEFT, left, top, right, bottom, -90+angle, (right-left)/2, (right-left)/2);
        }
        
        //body height
        left=x-game.getTextureSize()*(bodywidth/blocksize)/2;
        right=x+game.getTextureSize()*(bodywidth/blocksize)/2;
//        top=bottom;
//        bottom+=(bodyheight/blocksize)*game.getTextureSize();
        
        //body
        drawTex(c.isLookingRight() ? SKELETON_BODY_RIGHT : SKELETON_BODY_LEFT, left, top, right, bottom, 0, (right-left)/2, (right-left)/2);
        
        //arm height
        left=x-game.getTextureSize()*(armwidth/blocksize)/2;
        right=x+game.getTextureSize()*(armwidth/blocksize)/2;
//        top=bottom;
//        bottom+=(armheight/blocksize)*game.getTextureSize();
        
        //arm
        if(c.isLookingRight())
        {
            //right arm
            drawTex(SKELETON_RIGHT_ARM_RIGHT, left, top, right, bottom, +90+angle, (right-left)/2, (right-left)/2);
        }
        else
        {
            //left arm
            drawTex(SKELETON_LEFT_ARM_LEFT, left, top, right, bottom, -90-angle, (right-left)/2, (right-left)/2);
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
            drawTex(SKELETON_LEFT_LEG_RIGHT, left, top, right, bottom, angle, (right-left)/2, 0);
            
            //right leg
            drawTex(SKELETON_RIGHT_LEG_RIGHT, left, top, right, bottom, -angle, (right-left)/2, 0);
        }
        else
        {
            //right leg
            drawTex(SKELETON_RIGHT_LEG_LEFT, left, top, right, bottom, -angle, (right-left)/2, 0);
            
            //left leg
            drawTex(SKELETON_LEFT_LEG_LEFT, left, top, right, bottom, angle, (right-left)/2, 0);
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
        
        //head
        float left=x-game.getTextureSize()*(headwidth/blocksize)/2;
        float right=x+game.getTextureSize()*(headwidth/blocksize)/2;
        float bottom=ytop+(headheight/blocksize)*game.getTextureSize();
        float top=ytop;
        
        drawTex(p.isLookingRight() ? PLAYER_HEAD_RIGHT : PLAYER_HEAD_LEFT, left, top, right, bottom, 0, 0, 0);

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
            drawTex(PLAYER_LEFT_ARM_RIGHT, left, top, right, bottom, angle, (right-left)/2, (right-left)/2);
        }
        else
        {
            //right arm
            drawTex(PLAYER_RIGHT_ARM_LEFT, left, top, right, bottom, -angle, (right-left)/2, (right-left)/2);
        }
        
        //body
        drawTex(p.isLookingRight() ? PLAYER_BODY_RIGHT : PLAYER_BODY_LEFT, left, top, right, bottom, 0, (right-left)/2, (right-left)/2);
        
        //arm
        if(p.isLookingRight())
        {
            //right arm
            drawTex(PLAYER_RIGHT_ARM_RIGHT, left, top, right, bottom, -angle, (right-left)/2, (right-left)/2);
        }
        else
        {
            //left arm
            drawTex(PLAYER_LEFT_ARM_LEFT, left, top, right, bottom, angle, (right-left)/2, (right-left)/2);
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
            drawTex(PLAYER_LEFT_LEG_RIGHT, left, top, right, bottom, angle, (right-left)/2, 0);
            
            //right leg
            drawTex(PLAYER_RIGHT_LEG_RIGHT, left, top, right, bottom, -angle, (right-left)/2, 0);
        }
        else
        {
            //right leg
            drawTex(PLAYER_RIGHT_LEG_LEFT, left, top, right, bottom, -angle, (right-left)/2, 0);
            
            //left leg
            drawTex(PLAYER_LEFT_LEG_LEFT, left, top, right, bottom, angle, (right-left)/2, 0);
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
        if(SWEJNR.DEBUG)
        {
        	gl.glDisable(GL10.GL_BLEND);
        	
            //render box
            float x=(float) (leftoffset+(e.getLocation().getX()-x1)*game.getTextureSize());
            float y=(float) (topoffset+(y2-e.getLocation().getY())*game.getTextureSize());
            
            float playerwidh=(float) (Math.abs(e.getRenderBox().getMin().getX()-e.getRenderBox().getMax().getX())*game.getTextureSize());
    
            float left=x-playerwidh/2;
            float right=x+playerwidh/2;
            float bottom=y;
            float top=(float) (y-e.getRenderBox().getMax().getY()*game.getTextureSize());
            
            drawRect(left, top, right, bottom, 0xFFFFFF00);
            
            //hitbox
            playerwidh=(float) (Math.abs(e.getHitBox().getMin().getX()-e.getHitBox().getMax().getX())*game.getTextureSize());
            
            left=x-playerwidh/2;
            right=x+playerwidh/2;
            bottom=y;
            top=(float) (y-e.getHitBox().getMax().getY()*game.getTextureSize());
            
            drawRect(left, top, right, bottom, 0xFF00FF00);
            
            //triggercenter
            Location tc=e.getTriggerCenter().add(e.getLocation());
            x=(float) (leftoffset+(tc.getX()-x1)*game.getTextureSize());
            y=(float) (topoffset+(y2-tc.getY())*game.getTextureSize());
            
            gll.color(gl, 0xFF00FF00);
            gll.position(x-13, y-13, 26, 26, dwidth, dheight);
            gll.draw(gl);
            gll.position(x+13, y-13, -26, 26, dwidth, dheight);
            gll.draw(gl);
            gll.clearColor(gl);
            
            //triggerradius
            if(e.getTriggerRadius() > 0)
            {
                drawCircle(x, y, (float) e.getTriggerRadius()*game.getTextureSize(), 0xFF00FF00);
            }
//            
//            //debug data
//            paint.setTextAlign(Align.LEFT);
//            
//            x=(float) (leftoffset+(e.getLocation().getX()-x1)*game.getTextureSize());
//            y=(float) (topoffset+(y2-e.getLocation().getY())*game.getTextureSize());
//
//            float playerheight=(float) (Math.abs(e.getHitBox().getMin().getY()-e.getHitBox().getMax().getY())*game.getTextureSize());
//            
//            String debugdata=e.getDebugInfo();
//            String[] list=debugdata.split("\n");
//            y+=(paint.descent()+paint.ascent())*(list.length-1) - playerheight;
//            
//            for(String s:list)
//            {
//                canvas.drawText(s, x, y, paint);
//                y-=paint.descent()+paint.ascent();
//            }
            
            gl.glEnable(GL10.GL_BLEND);
        }
    }
    
    private void drawRect(float left, float top, float right, float bottom, int color)
    {
        gll.color(gl, color);
        
        gll.position(left, top, 0, bottom-top, dwidth, dheight);
        gll.draw(gl);
        gll.position(left, top, right-left, 0, dwidth, dheight);
        gll.draw(gl);
        gll.position(right, top, 0, bottom-top, dwidth, dheight);
        gll.draw(gl);
        gll.position(left, bottom, right-left, 0, dwidth, dheight);
        gll.draw(gl);
        
        gll.clearColor(gl);
    }
    
    private void drawCircle(float x, float y, float radius, int color)
    {
//        gll.color(gl, color);
//        double angle=2*Math.PI/180/(360);
//        float xa=x+radius;
//        float ya=y;
//        
//        for(int i=0;i<360;i++)
//        {
//            float x1=(float) (xa+Math.sin(angle*i)*radius);
//            float y1=(float) (ya+Math.cos(angle*i)*radius);
//            float x2=(float) (xa+Math.sin(angle*(i+1))*radius);
//            float y2=(float) (ya+Math.cos(angle*(i+1))*radius);
//            
//            
//            gll.position(x1, y1, x2-x1, y2-y1, dwidth, dheight);
//            gll.draw(gl);
//        }
//        gll.clearColor(gl);
    }


    private void drawTex(String tex, float left, float top, float right, float bottom, float angle, float xcorr, float ycorr)
    {
    	drawTex(tex, left, top, right, bottom, angle, xcorr, ycorr, xcorr, ycorr);
    }
    
    private void drawTex(String tex, float left, float top, float right, float bottom, float angle, float txcorr, float tycorr, float pxcorr, float pycorr)
    {
        GLTex gltex=textures.get(tex);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gltex.position(dwidth/2-pxcorr, dheight/2-pycorr, right-left, bottom-top, dwidth, dheight);
        gltex.translate(gl, left+txcorr, top+tycorr, dwidth, dheight);
        gltex.rotate(gl, angle);
        gltex.draw(gl);
        gltex.clearRotate(gl);
        gltex.clearTranslate(gl, dwidth, dheight);
    }
}
