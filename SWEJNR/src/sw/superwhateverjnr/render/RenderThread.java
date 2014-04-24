package sw.superwhateverjnr.render;

import lombok.Getter;
import lombok.Setter;
import android.graphics.Bitmap;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.world.World;

public class RenderThread extends Thread
{
	@Getter
	@Setter
	private boolean running;
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
			
			printFrame();
		}
	}


	private void printFrame()
	{
		Bitmap bm=renderer.nextFrame();
		Game.getInstance().getGameView().nextFrame(bm);
	}
}
