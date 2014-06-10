package sw.superwhateverjnr;

import lombok.SneakyThrows;
import android.app.Activity;
import sw.superwhateverjnr.activity.FullscreenActivity;
import sw.superwhateverjnr.ui.LoadingView;

public class LoadingScreen
{
	private LoadingView loading;
	private SWEJNR swejnr;
	private String loadstatus;
	
	@SneakyThrows
	public LoadingScreen()
	{
		swejnr=SWEJNR.getInstance();
		loading = new LoadingView(swejnr.getApplicationContext());
		Activity a = FullscreenActivity.getInstance();
		a.setContentView(loading);
		
		while(!SWEJNR.getInstance().isLoadFinished())
		{
			loadstatus = SWEJNR.getInstance().getLoadStatus();
			
			if(loadstatus != null)
			{
				loading.status("Loading "+loadstatus+" ...");
				loading.invalidate();
			}
			
			Thread.sleep(10);
		}
		
		new MainMenu();
	}
}
