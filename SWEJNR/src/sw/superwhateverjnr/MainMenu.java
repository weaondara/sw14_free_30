package sw.superwhateverjnr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import java.util.Random;
import sw.superwhateverjnr.activity.CreditsActivity;
import sw.superwhateverjnr.activity.GameActivity;
import sw.superwhateverjnr.activity.SettingsActivity;
import sw.superwhateverjnr.ui.MainMenuView;
import sw.superwhateverjnr.ui.MainMenuView.SelectedListener;

public class MainMenu implements SelectedListener
{
	private MainMenuView mainMenuView;
	
	private Activity calling;
	
	@SuppressLint("NewApi")
	public MainMenu(Activity calling)
	{
		this.calling = calling;
		
		mainMenuView = new MainMenuView(SWEJNR.getInstance());
		mainMenuView.setSelectedListener(this);
		calling.setContentView(mainMenuView);
	}
	
	public void newGame()
	{
		Intent i=new Intent(calling, GameActivity.class);
		i.putExtra("worldloader", "dummy");
		i.putExtra("worldname", "entitytest");
		calling.startActivity(i);
	}
	
	public void continueGame()
	{
		Intent i=new Intent(calling, GameActivity.class);
		i.putExtra("worldloader", "dummy");
		i.putExtra("worldname", "physicstest");
		calling.startActivity(i);
	}
	
	public void startRandom()
	{
		Intent i=new Intent(calling, GameActivity.class);
		i.putExtra("worldloader", "random");
		i.putExtra("worldname", String.valueOf(new Random().nextInt()));
		calling.startActivity(i);
	}
	public void showCredits()
	{
		Intent i=new Intent(calling, CreditsActivity.class);
		calling.startActivity(i);
	}
    public void showSettings()
	{
		Intent i=new Intent(calling, SettingsActivity.class);
		calling.startActivity(i);
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
			showSettings();
		}
		else if(touched.equalsIgnoreCase(MainMenuView.CREDITS))
		{
			showCredits();
		}
		else if(touched.equalsIgnoreCase(MainMenuView.QUIT_GAME))
		{
			calling.finish();
		}
	}
}