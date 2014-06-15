package sw.superwhateverjnr.ui;

import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.render.RenderThread;
import sw.superwhateverjnr.render.RendererBase;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
	private boolean allowdraw;
	@Getter
	private RenderThread rt;
	private RendererBase renderer;
	
	@Getter
	private boolean paused;
	
	@Getter
	private int fps=0;
	private int frames=0;
	private long fpsmeasurelast=0;
	
	public GameView(Context context)
	{
		super(context);
		setup();
	}
	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setup();
	}
	public GameView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		setup();
	}
	private void setup()
	{
		getHolder().addCallback(this);
		setFocusable(true);
		rt=new RenderThread(false);
		renderer=rt.getRenderer();
		this.setOnTouchListener(this);
		rt.start();
	}
	public void close()
	{
		getHolder().removeCallback(this);
		this.setOnTouchListener(null);
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
		rt.setRunning(paused);
	}
	
	public void drawNextFrame()
	{
		if(allowdraw)
		{
			Canvas c=null;
			try
			{
				c=getHolder().lockCanvas();
			}
			catch(Exception e){}
			if(c!=null)
			{
				synchronized (getHolder()) 
				{
					this.draw(c);
				}
			}
			try
			{
				this.getHolder().unlockCanvasAndPost(c);
			}
			catch(Exception e){}
			
			
			long now=System.currentTimeMillis();
			if(now-fpsmeasurelast>=1000)
			{
				fpsmeasurelast+=1000;
				fps=frames;
				frames=0;
			}
			frames++;
		}
		
	}
	@Override
	public void draw(Canvas c)
	{
		renderer.nextFrame(c);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		fpsmeasurelast=System.currentTimeMillis();
		rt.setRunning(true);
		allowdraw=true;
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		rt.setRunning(false);
		allowdraw=false;
	}

	
	public boolean onTouch(View v, MotionEvent event)
	{
		if(v!=this)
		{
			return false;
		}
		
		Game g = Game.getInstance();
		if(g == null)
		{
			return false;
		}
		g.handleGameTouchEvent(event);
		return true;
	}
}
