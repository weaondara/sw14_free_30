package sw.superwhateverjnr;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import sw.superwhateverjnr.activity.EndActivity;
import sw.superwhateverjnr.activity.GameActivity;
import sw.superwhateverjnr.entity.Player;
import sw.superwhateverjnr.scheduling.Scheduler;
import sw.superwhateverjnr.settings.Settings;
import sw.superwhateverjnr.ui.GLGameView;
import sw.superwhateverjnr.ui.GameView;
import sw.superwhateverjnr.util.Arithmetics;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.DummyWorldLoader;
import sw.superwhateverjnr.world.Location;
import sw.superwhateverjnr.world.World;
import sw.superwhateverjnr.world.WorldLoader;

@Getter
@ToString
@EqualsAndHashCode
public class Game
{
    public static final int TICK_INTERVAL=10;
    
    @Getter
    private static Game instance;
    
    private Activity activity;
    
    private Player player;
    
    private Location minDisplayPoint;
    
    private int displayWidth;
    private int displayHeight;
    
    private int textureSize;
    
    private View oldview;
    private GameView gameView;
    private GLGameView glgameView;
    
    private String worldname;
    @Setter
    private WorldLoader worldLoader;
    private World world;
    private MediaPlayer mp;
    
    private Settings settings;
    private Scheduler scheduler;
    
    private boolean enabled = false;
    
    private boolean finished = false;
    private boolean won = false;
    private int points = 0;
    
    private boolean musicinitdone = false;
    
    public Game(Activity calling)
    {
        instance=this;
        
        activity=calling;
        
        minDisplayPoint=new Location(0, 9);
        
        DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
        displayWidth=metrics.widthPixels;
        displayHeight=metrics.heightPixels;
        
        textureSize=64;
        
        player=new Player(null);
        settings=new Settings();
        scheduler=new Scheduler();
        worldLoader=new DummyWorldLoader();
        
        settings.load();
        
        if(settings.useGL())
        {
            glgameView=new GLGameView(GameActivity.getInstance());
        }
        else
        {
            gameView=new GameView(GameActivity.getInstance());
        }
        
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(true);
    }

    public void init()
    {
        registerSchedulerTasks();
    }
    
    public void enable()
    {
        if(enabled)
        {
            return;
        }
        
        if(settings.useGL())
        {
            GameActivity.getInstance().setContentView(glgameView);
            glgameView.setPaused(false);
        }
        else
        {
            GameActivity.getInstance().setContentView(gameView);
            gameView.setPaused(false);
        }
        
        
        if(!musicinitdone)
        {
            try
            {
                AssetFileDescriptor as = SWEJNR.getInstance().getAssets().openFd("music/"+world.getBgmfile());
                mp.setDataSource(as.getFileDescriptor(), as.getStartOffset(), as.getLength());
                mp.prepare();
                as.close();
            }
            catch(IOException e)
            {
                if(world.getBgmfile()!=null)
                {
                    e.printStackTrace();
                }
            }
            musicinitdone=true;
        }
        
        mp.start(); 
        
        enabled=true;
    }
    
    public void disable()
    {
        if(!enabled)
        {
            return;
        }
        if(settings.useGL())
        {
            glgameView.setPaused(true);
        }
        else
        {
            gameView.setPaused(true);
        }
        mp.pause();
        
        enabled=false;
    }    
    
    public void end(boolean won)
    {
        this.won = won;
        finished = true;
    }
    
    public void close()
    {
        if(enabled)
        {
            disable();
        }
        try
        {
            mp.stop();
            mp.release();
        }
        catch(IllegalStateException e)
        {
        }
        if(!settings.useGL())
        {
            gameView.getRt().kill();
            gameView.close();
        }
        scheduler.close();
        
        instance=null;
    }
    
    public void loadWorld(String name)
    {
        try
        {
            world=worldLoader.loadWorld(name);
            worldname=name;
            player.teleport(world.getSpawnLocation());
        }
        catch(Exception e) {e.printStackTrace();}
    }

    public void addPoints(int points)
    {
        this.points += points;
    }
    
    private void registerSchedulerTasks()
    {
        final Runnable r10ms=new Runnable()
        {
            @SneakyThrows
            @Override
            public void run()
            {
                while(!enabled)
                {
                    return;
                }
                try
                {
                    player.tick();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    world.tick();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(finished)
                {
                    disable();
                    
                    if(won)
                    {
                    	points+=world.getTimeRemaining();
                    }
                    
                    Intent i = new Intent(activity, EndActivity.class);
                    i.putExtra("won", won);
                    i.putExtra("points", points);
                    activity.startActivity(i);
                    
                    close();
                    activity.finish();
                }
            }
        };
        scheduler.registerRepeatingTask(r10ms, 1, TICK_INTERVAL);
    }

    
    public void handleGameTouchEvent(MotionEvent event)
    {
        boolean left=false;
        boolean right=false;
        boolean jump=false;
        
        int action = MotionEventCompat.getActionMasked(event);
        
        float dx, dy, distance;
        float radius=settings.getControlCircleRadiusOuter();
        
        if(action==MotionEvent.ACTION_POINTER_DOWN || 
            action==MotionEvent.ACTION_DOWN || 
            action==MotionEvent.ACTION_MOVE)
        {
            for(int i=0;i<event.getPointerCount();i++)
            {
                float x = MotionEventCompat.getX(event, i);
                float y = MotionEventCompat.getY(event, i);
                
                PointF leftc=Arithmetics.getControlLeftCenter();
                dx=Math.abs(leftc.x-x);
                dy=Math.abs(leftc.y-y);
                distance=(float) Math.sqrt(dx*dx+dy*dy);
                if(distance<=radius)
                {
                    left=true;
                }
                
                PointF rightc=Arithmetics.getControlRightCenter();
                dx=Math.abs(rightc.x-x);
                dy=Math.abs(rightc.y-y);
                distance=(float) Math.sqrt(dx*dx+dy*dy);
                if(distance<=radius)
                {
                    right=true;
                }
                
                PointF jumpc=Arithmetics.getControlJumpCenter();
                dx=Math.abs(jumpc.x-x);
                dy=Math.abs(jumpc.y-y);
                distance=(float) Math.sqrt(dx*dx+dy*dy);
                if(distance<=radius)
                {
                    jump=true;
                }
            }
        }
        
        player.setMovingleft(left);
        player.setMovingright(right);
        if(jump)
        {
            player.jump();
        }
    }
    
    public void updateView()
    {
        Rectangle viewRect=new Rectangle(
                displayWidth*0.3/textureSize,
                displayHeight*0.35/textureSize,
                displayWidth*0.5/textureSize,
                displayHeight*0.55/textureSize
                );
        
        Location l=player.getLocation();
        if(l==null)
        {
            return;
        }
        if(l.getX()<minDisplayPoint.getX()+viewRect.getMin().getX())
        {
            float x=(float) (l.getX()-viewRect.getMin().getX());
            if(x<0)
            {
                x=0;
            }
            minDisplayPoint.setX(x);
        }
        else if(l.getX()>minDisplayPoint.getX()+viewRect.getMax().getX())
        {
            float x=(float) (l.getX()-viewRect.getMax().getX());
            if(x>world.getWidth()-displayWidth/textureSize)
            {
                x=world.getWidth()-displayWidth/textureSize;
            }
            minDisplayPoint.setX(x);
        }
        
        if(l.getY()<minDisplayPoint.getY()+viewRect.getMin().getY())
        {
            float y=(float) (l.getY()-(viewRect.getMin().getY()));
            if(y<0)
            {
                y=0;
            }
            minDisplayPoint.setY(y);
        }
        else if(l.getY()>minDisplayPoint.getY()+viewRect.getMax().getY())
        {
            float y=(float) (l.getY()-(viewRect.getMax().getY()));
            if(y>world.getHeight())
            {
                y=world.getHeight();
            }
            minDisplayPoint.setY(y);
        }
    }
}
