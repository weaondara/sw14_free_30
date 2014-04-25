package sw.superwhateverjnr.ui;

import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.render.RenderThread;
import sw.superwhateverjnr.render.Renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback 
{
	private boolean allowdraw;
	
	private RenderThread rt;
	private Renderer renderer;
	
	private int frames=0;
	@Getter
	private int fps=0;
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

	public void drawNextFrame()
	{
		if(allowdraw)
		{
			Canvas c=getHolder().lockCanvas();
			if(c!=null)
			{
				synchronized (getHolder()) 
				{
					this.draw(c);
				}
			}
			this.getHolder().unlockCanvasAndPost(c);
			
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

	private void setup()
	{
		getHolder().addCallback(this);
		setFocusable(true);
		rt=new RenderThread(Game.getInstance().getWorld());
		renderer=rt.getRenderer();
		rt.start();
	}
}
