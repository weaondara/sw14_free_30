package sw.superwhateverjnr;

import sw.superwhateverjnr.world.DummyWorldLoader;
import sw.superwhateverjnr.world.RandomWorldLoader;
import android.annotation.SuppressLint;
//import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.Random;

public class MainMenu
{
	@SuppressLint("NewApi")
	MainMenu()
	{
		FullscreenActivity instance = FullscreenActivity.getInstance();
		LinearLayout l = new LinearLayout(instance);
		l.setLayoutDirection(LinearLayout.HORIZONTAL);
		
		Button newGameButton = new Button(instance);
		newGameButton.setText("New Game");
		newGameButton.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){newGame();}});
		l.addView(newGameButton);
		
		Button continueGameButton = new Button(instance);
		continueGameButton.setText("Continue");
		continueGameButton.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){continueGame();}});
		l.addView(continueGameButton);
		
		Button randomGameButton = new Button(instance);
		randomGameButton.setText("SWERWG");
		randomGameButton.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){randomGame();}});
		l.addView(randomGameButton);
		
		/*
		 Button credits = new Button(instance);
		 credits.setText("Credits");
		 credits.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){SWENJR.showCredits();}});
		 l.addView(credits);
		 */
		
		instance.setContentView(l);
	}
	
	void newGame()
	{
		Game g = new Game();
		g.init();
		g.setWorldLoader(new DummyWorldLoader());
		g.loadWorld("bla");
		g.enable();
	}
	
	void continueGame()
	{
		Game g = new Game();
		g.init();
		g.setWorldLoader(new DummyWorldLoader());
		g.loadWorld("physicstest");
		g.enable();
	}
	
	void startRandom()
	{
		Random r = new Random();
		Game g = new Game();
		g.init();
		g.setWorldLoader(new RandomWorldLoader());
		g.loadWorld(String.valueOf(r.nextInt()));
		g.enable();
	}
}
