package sw.superwhateverjnr.ui;

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

//	private int i=0;
	public void nextFrame(Bitmap nextFrame)
	{
//		i++;
		//System.out.println("new frame!");	
		this.nextFrame=nextFrame;
		
//		FileOutputStream out;
//		try {
//			File f=new File(Environment.getExternalStorageDirectory().toString(),"/frames/frame"+i+".png");
//			f.getParentFile().mkdirs();
//			f.createNewFile();
//			
//			out = new FileOutputStream(f.getAbsolutePath());
//			nextFrame.compress(Bitmap.CompressFormat.PNG, 90, out);
//			out.close();
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
		
		if(allowdraw)
		{
			Canvas c=getHolder().lockCanvas();
//			System.out.println("c==null:"+(c==null));
			synchronized (getHolder()) 
			{
				this.draw(c);
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
//		System.out.println("drawing frame");
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
