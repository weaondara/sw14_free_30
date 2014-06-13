package sw.superwhateverjnr.ui;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.render.RenderThread;
import sw.superwhateverjnr.render.RendererBase;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class GLGameView extends GLSurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, GLSurfaceView.Renderer
{
	private boolean allowdraw;
	
	private RenderThread rt;
	private RendererBase renderer;
	
	@Getter
	private boolean paused;
	
	@Getter
	private int fps=0;
	private int frames=0;
	private long fpsmeasurelast=0;
	
	public GLGameView(Context context)
	{
		super(context);
		setup();
	}
	public GLGameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setup();
	}
	private void setup()
	{
		getHolder().addCallback(this);
		setFocusable(true);
		rt=new RenderThread(true);
		renderer=rt.getRenderer();
		this.setOnTouchListener(this);
		
		
//		this.requestRender();
//		rt.start();
	}
	
	private boolean bla=false;
	
	public void setPaused(boolean paused)
	{
		this.paused = paused;
//		rt.setRunning(!paused);
		
		if(paused)
		{
			this.onPause();
		}
		else
		{
			if(!bla)
			{
				this.setRenderer(this);
				bla=true;
			}
			this.onResume();
		}
	}
	
	public void drawNextFrame()
	{
//		if(allowdraw)
//		{
//			Canvas c=null;
//			try
//			{
//				c=getHolder().lockCanvas();
//			}
//			catch(Exception e){}
//			if(c!=null)
//			{
//				synchronized (getHolder()) 
//				{
//					this.draw(c);
//				}
//			}
//			try
//			{
//				this.getHolder().unlockCanvasAndPost(c);
//			}
//			catch(Exception e){}
//			
//			
//			long now=System.currentTimeMillis();
//			if(now-fpsmeasurelast>=1000)
//			{
//				fpsmeasurelast+=1000;
//				fps=frames;
//				frames=0;
//			}
//			frames++;
//		}
		
	}
//	@Override
//	public void draw(Canvas c)
//	{
////		renderer.nextFrame(c);
//	}

//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
//	{
//	}
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) 
//	{
////		fpsmeasurelast=System.currentTimeMillis();
////		rt.setRunning(true);
////		allowdraw=true;
//	}
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) 
//	{
////		rt.setRunning(false);
////		allowdraw=false;
//	}

	
	public boolean onTouch(View v, MotionEvent event)
	{
		if(v!=this)
		{
			return false;
		}
		
		Game.getInstance().handleGameTouchEvent(event);
		return true;
	}
	@Override
	public void onDrawFrame(GL10 gl)
	{
		renderer.nextFrame(gl);
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		//won't occur => do nothing
		
		
		gl.glViewport(0, 0, width, height);     //Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix
        gl.glLoadIdentity();                    //Reset The Projection Matrix
 
        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
 
        gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
        gl.glLoadIdentity();
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		//bind texures
		
		//gl setup
//		gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
//	    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
//	    gl.glShadeModel(GL10.GL_FLAT);
//	    gl.glDisable(GL10.GL_DEPTH_TEST);
//	    gl.glEnable(GL10.GL_BLEND);
//	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA); 
//
//	    gl.glViewport(0, 0, 1920,  1080);
//	    gl.glMatrixMode(GL10.GL_PROJECTION);
//	    gl.glLoadIdentity();
//	    gl.glEnable(GL10.GL_BLEND);
//	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	    gl.glShadeModel(GL10.GL_FLAT);
//	    gl.glEnable(GL10.GL_TEXTURE_2D);
//
//	    GLU.gluOrtho2D(gl, 0, 1920, 1080, 0);
	}
}
