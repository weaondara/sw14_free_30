package sw.superwhateverjnr.ui;

import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.render.RenderThread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback 
{
	private boolean allowdraw;
	
	private Bitmap nextFrame=null;
	private Paint paint=new Paint();
	private RenderThread rt;
	
	private int frames=0;
	@Getter
	private int fps=0;
//	private long fpsmeasurestart=0;
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

	public void nextFrame(Bitmap nextFrame)
	{
		long now=System.currentTimeMillis();
		if(now-fpsmeasurelast>=1000)
		{
			fpsmeasurelast+=1000;
			fps=frames;
			frames=0;
		}
		frames++;
		
		this.nextFrame=nextFrame;
		
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
		}
	}
	
	@Override
	public void draw(Canvas c)
	{
		if(nextFrame==null)
		{
			return;
		}
		paint.setStyle(Style.FILL);
		c.drawBitmap(nextFrame, 0, 0, null);
		nextFrame=null;
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
		rt.start();
	}
}
