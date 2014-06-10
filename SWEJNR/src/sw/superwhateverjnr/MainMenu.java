package sw.superwhateverjnr;

import sw.superwhateverjnr.ui.MainMenuView;
import sw.superwhateverjnr.ui.MainMenuView.SelectedListener;
import sw.superwhateverjnr.world.DummyWorldLoader;
import sw.superwhateverjnr.world.RandomWorldLoader;
import android.annotation.SuppressLint;
//import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.Random;

public class MainMenu implements SelectedListener
{
	private MainMenuView mainMenuView;
	
	@SuppressLint("NewApi")
	public MainMenu()
	{
		FullscreenActivity instance = FullscreenActivity.getInstance();
		
		mainMenuView = new MainMenuView(SWEJNR.getInstance());
		mainMenuView.setSelectedListener(this);
		instance.setContentView(mainMenuView);
	}
	
	public void newGame()
	{
		Game g = new Game();
		g.init();
		g.setWorldLoader(new DummyWorldLoader());
		g.loadWorld("entitytest");
		g.enable();
	}
	
	public void continueGame()
	{
		Game g = new Game();
		g.init();
		g.setWorldLoader(new DummyWorldLoader());
		g.loadWorld("physicstest");
		g.enable();
	}
	
	public void startRandom()
	{
		Random r = new Random();
		Game g = new Game();
		g.init();
		g.setWorldLoader(new RandomWorldLoader());
		g.loadWorld(String.valueOf(r.nextInt()));
		g.enable();
	}

	@Override
	public void onSelected(String touched)
	{
		if(touched.equalsIgnoreCase(MainMenuView.NEW_GAME))
		{
			newGame();
		}
		else if(touched.equalsIgnoreCase(MainMenuView.CONTINUE_GAME))
		{
			continueGame();
		}
		else if(touched.equalsIgnoreCase(MainMenuView.RANDOM_GAME))
		{
			startRandom();
		}
		else if(touched.equalsIgnoreCase(MainMenuView.SETTINGS))
		{
			
		}
		else if(touched.equalsIgnoreCase(MainMenuView.CREDITS))
		{
			
		}
		else if(touched.equalsIgnoreCase(MainMenuView.QUIT_GAME))
		{
			FullscreenActivity.getInstance().finish();
		}
	}
}
