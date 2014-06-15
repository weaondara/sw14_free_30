package sw.superwhateverjnr.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.render.GLRenderer;
import sw.superwhateverjnr.render.GLTex;
import sw.superwhateverjnr.render.RenderThread;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.texture.entity.CreeperTexture;
import sw.superwhateverjnr.texture.entity.PlayerTexture;
import sw.superwhateverjnr.texture.entity.SkeletonTexture;
import sw.superwhateverjnr.texture.entity.ZombieTexture;
import sw.superwhateverjnr.util.IdAndSubId;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class GLGameView extends GLSurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, GLSurfaceView.Renderer
{
	private RenderThread rt;
	private GLRenderer renderer;
	
	@Getter
	private boolean paused;
	
	@Getter
	private int fps=0;
	private int frames=0;
	private long fpsmeasurelast=0;
	
	private List<GLTex> bindable;
	private Map<Object, GLTex> textures;
	
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
		bindable=new ArrayList<GLTex>();
		textures=new HashMap<Object, GLTex>();
		
		
		rt=new RenderThread(true);
		renderer=(GLRenderer) rt.getRenderer();
		
		loadTextures();
		
		getHolder().addCallback(this);
		setFocusable(true);
		this.setOnTouchListener(this);
		
		this.setRenderer(this);
	}
	
	private boolean bla=false;
	
	public void setPaused(boolean paused)
	{
		this.paused = paused;
		
		if(paused)
		{
			this.onPause();
		}
		else
		{
			if(!bla)
			{
				
				bla=true;
			}
			this.onResume();
		}
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
	@Override
	public void onDrawFrame(GL10 gl)
	{
		renderer.nextFrame(gl);
		
		long now=System.currentTimeMillis();
		if(now-fpsmeasurelast>=1000)
		{
			fpsmeasurelast+=1000;
			fps=frames;
			frames=0;
//			System.out.println(fps);
		}
		frames++;
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
        renderer.setDwidth(width);
        renderer.setDheight(height);
        
		gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrthof(0, 0, width, height, -1F, 1);
        gl.glLoadIdentity();
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		fpsmeasurelast=System.currentTimeMillis();
		
		//bind texures
		textures.clear();
		bindTextures(gl);
		
		renderer.setTextures(textures);
	}
	
	
	private void loadTextures()
	{
		for(Entry<IdAndSubId, Texture> e:TextureMap.getBlocks().entrySet())
		{
			GLTex gltex=new GLTex(e.getKey(), e.getValue().getOrgimage());
			bindable.add(gltex);
		}
		
		GLTex gltex;
		
		//player
		PlayerTexture pt = (PlayerTexture) TextureMap.getTexture(EntityType.PLAYER);
		gltex=new GLTex(GLRenderer.PLAYER_HEAD_RIGHT, pt.getHeadRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_HEAD_LEFT, pt.getHeadLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.PLAYER_BODY_RIGHT, pt.getBodyRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_BODY_LEFT, pt.getBodyLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.PLAYER_RIGHT_ARM_RIGHT, pt.getRightArmRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_RIGHT_ARM_LEFT, pt.getRightArmLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_LEFT_ARM_RIGHT, pt.getLeftArmRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_LEFT_ARM_LEFT, pt.getLeftArmLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.PLAYER_RIGHT_LEG_RIGHT, pt.getRightLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_RIGHT_LEG_LEFT, pt.getRightLegLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_LEFT_LEG_RIGHT, pt.getLeftLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.PLAYER_LEFT_LEG_LEFT, pt.getLeftLegLeft());
		bindable.add(gltex);
		
		//zombie
		ZombieTexture zt = (ZombieTexture) TextureMap.getTexture(EntityType.ZOMBIE);
		gltex=new GLTex(GLRenderer.ZOMBIE_HEAD_RIGHT, zt.getHeadRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_HEAD_LEFT, zt.getHeadLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.ZOMBIE_BODY_RIGHT, zt.getBodyRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_BODY_LEFT, zt.getBodyLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.ZOMBIE_RIGHT_ARM_RIGHT, zt.getRightArmRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_RIGHT_ARM_LEFT, zt.getRightArmLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_LEFT_ARM_RIGHT, zt.getLeftArmRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_LEFT_ARM_LEFT, zt.getLeftArmLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.ZOMBIE_RIGHT_LEG_RIGHT, zt.getRightLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_RIGHT_LEG_LEFT, zt.getRightLegLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_LEFT_LEG_RIGHT, zt.getLeftLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.ZOMBIE_LEFT_LEG_LEFT, zt.getLeftLegLeft());
		bindable.add(gltex);
		
		//skeleton
		SkeletonTexture st = (SkeletonTexture) TextureMap.getTexture(EntityType.SKELETON);
		gltex=new GLTex(GLRenderer.SKELETON_HEAD_RIGHT, st.getHeadRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_HEAD_LEFT, st.getHeadLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.SKELETON_BODY_RIGHT, st.getBodyRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_BODY_LEFT, st.getBodyLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.SKELETON_RIGHT_ARM_RIGHT, st.getRightArmRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_RIGHT_ARM_LEFT, st.getRightArmLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_LEFT_ARM_RIGHT, st.getLeftArmRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_LEFT_ARM_LEFT, st.getLeftArmLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.SKELETON_RIGHT_LEG_RIGHT, st.getRightLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_RIGHT_LEG_LEFT, st.getRightLegLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_LEFT_LEG_RIGHT, st.getLeftLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.SKELETON_LEFT_LEG_LEFT, st.getLeftLegLeft());
		bindable.add(gltex);
		
		//skeleton
		CreeperTexture ct = (CreeperTexture) TextureMap.getTexture(EntityType.CREEPER);
		gltex=new GLTex(GLRenderer.CREEPER_HEAD_RIGHT, ct.getHeadRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.CREEPER_HEAD_LEFT, ct.getHeadLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.CREEPER_BODY_RIGHT, ct.getBodyRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.CREEPER_BODY_LEFT, ct.getBodyLeft());
		bindable.add(gltex);
		
		gltex=new GLTex(GLRenderer.CREEPER_RIGHT_LEG_RIGHT, ct.getRightLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.CREEPER_RIGHT_LEG_LEFT, ct.getRightLegLeft());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.CREEPER_LEFT_LEG_RIGHT, ct.getLeftLegRight());
		bindable.add(gltex);
		gltex=new GLTex(GLRenderer.CREEPER_LEFT_LEG_LEFT, ct.getLeftLegLeft());
		bindable.add(gltex);
		
		
		
	}
	
	private void bindTextures(GL10 gl)
	{
		textures.clear();
		for(GLTex e:bindable)
		{
			e.upload(gl);
			textures.put(e.getRef(), e);
		}
	}
	public void drawNextFrame(){}
}
