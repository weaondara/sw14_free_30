package sw.superwhateverjnr;

import lombok.SneakyThrows;
import android.app.Activity;
import sw.superwhateverjnr.activity.FullscreenActivity;
import sw.superwhateverjnr.ui.LoadingView;

public class LoadingScreen implements Runnable
{
	private Activity calling;
	private LoadingView loading;
	private String loadstatus;
	
	private Thread t;
	
	
	public LoadingScreen(Activity calling)
	{
		this.calling = calling;
		loading = new LoadingView(SWEJNR.getInstance().getApplicationContext());
		calling.setContentView(loading);
		
		t=new Thread(this);
		t.start();
	}

	@Override
	@SneakyThrows
	public void run()
	{
		while(!SWEJNR.getInstance().isLoadFinished())
		{
			loadstatus = SWEJNR.getInstance().getLoadStatus();
			
			if(loadstatus != null)
			{
				loading.post(new Runnable()
				{
					@Override
					public void run()
					{
						loading.status("Loading "+loadstatus+" ...");
						loading.invalidate();
					}
				});
			}
			
			Thread.sleep(10);
		}
		
		loading.post(new Runnable()
		{
			@Override
			public void run()
			{
				new MainMenu(calling);
			}
		});
	}
}
