package sw.superwhateverjnr.render;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import sw.superwhateverjnr.Game;

public class RenderThread extends Thread
{
	@Getter
	@Setter
	private boolean running;
	@Getter
	private RendererBase renderer;
	private boolean stop;
	
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
		while(!stop)
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
	
	@SneakyThrows
	public void kill()
	{
		stop = true;
		while(isAlive())
		{
			Thread.sleep(1);
		}
	}
}
