package sw.superwhateverjnr;

import android.annotation.SuppressLint;
//import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu
{
	@SuppressLint("NewApi")
	MainMenu()
	{
		FullscreenActivity instance = FullscreenActivity.getInstance();
		LinearLayout l = new LinearLayout(instance);
		l.setLayoutDirection(LinearLayout.HORIZONTAL);
		
		Button newGame = new Button(instance);
		newGame.setText("New Game");
		newGame.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){Game g = new Game(); /*g.newWorld(0)*/}});
		l.addView(newGame);
		
		Button continueGame = new Button(instance);
		continueGame.setText("Continue");
		continueGame.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){Game g = new Game(); /*g.loadWorld()*/}});
		l.addView(continueGame);
		
		Button randomGame = new Button(instance);
		randomGame.setText("SWERWG");
		randomGame.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){Game g = new Game(); /*g.randomWorld()*/}});
		l.addView(randomGame);
		
		/*
		 Button credits = new Button(instance);
		 credits.setText("Credits");
		 credits.setOnClickListener(new OnClickListener(){@Override public void onClick(View v){SWENJR.showCredits();}});
		 l.addView(credits);
		 */
		
		instance.setContentView(l);
	}
}
