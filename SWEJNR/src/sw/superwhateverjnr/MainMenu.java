package sw.superwhateverjnr;

import sw.superwhateverjnr.activity.CreditsActivity;
import sw.superwhateverjnr.activity.FullscreenActivity;
import sw.superwhateverjnr.activity.GameActivity;
import sw.superwhateverjnr.ui.MainMenuView;
import sw.superwhateverjnr.ui.MainMenuView.SelectedListener;
import android.annotation.SuppressLint;
import android.content.Intent;

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
		Intent i=new Intent(FullscreenActivity.getInstance(), GameActivity.class);
		i.putExtra("worldloader", "dummy");
		i.putExtra("worldname", "entitytest");
		FullscreenActivity.getInstance().startActivity(i);
	}
	
	public void continueGame()
	{
		Intent i=new Intent(FullscreenActivity.getInstance(), GameActivity.class);
		i.putExtra("worldloader", "dummy");
		i.putExtra("worldname", "physicstest");
		FullscreenActivity.getInstance().startActivity(i);
	}
	
	public void startRandom()
	{
		Intent i=new Intent(FullscreenActivity.getInstance(), GameActivity.class);
		i.putExtra("worldloader", "random");
		i.putExtra("worldname", String.valueOf(new Random().nextInt()));
		FullscreenActivity.getInstance().startActivity(i);
	}
	public void showCredits()
	{
		Intent i=new Intent(FullscreenActivity.getInstance(), CreditsActivity.class);
		FullscreenActivity.getInstance().startActivity(i);
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
			showCredits();
		}
		else if(touched.equalsIgnoreCase(MainMenuView.QUIT_GAME))
		{
			FullscreenActivity.getInstance().finish();
		}
	}
}
