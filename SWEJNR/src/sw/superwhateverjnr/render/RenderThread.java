package sw.superwhateverjnr.render;

import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.Game;

public class RenderThread extends Thread
{
	@Getter
	@Setter
	private boolean running;
	@Getter
	private RendererBase renderer;
	
	public RenderThread(boolean useGL)
	{
		if(useGL)
		{
			renderer=new GLRenderer();
		}
		else
		{
			renderer=new Renderer();
		}
		running=false;
	}
	
	public void run()
	{
		while(true)
		{
			if(!running || Game.getInstance().getWorld()==null)
			{
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e) {}
				continue;
			}
			
			Game.getInstance().getGameView().drawNextFrame();
		}
	}
}
