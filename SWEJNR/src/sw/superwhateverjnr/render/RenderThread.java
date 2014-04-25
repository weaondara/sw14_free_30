package sw.superwhateverjnr.render;

import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.world.World;

public class RenderThread extends Thread
{
	@Getter
	@Setter
	private boolean running;
	@Getter
	private Renderer renderer;
	
	public RenderThread(World world)
	{
		renderer=new Renderer(world);
		running=false;
	}
	
	public void run()
	{
		while(true)
		{
			if(!running)
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
