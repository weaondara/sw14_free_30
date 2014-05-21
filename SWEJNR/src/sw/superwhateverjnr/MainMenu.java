package sw.superwhateverjnr;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu
{
	@SuppressLint("NewApi")
	MainMenu(Activity instance)
	{
		LinearLayout l = new LinearLayout(instance);
		l.setLayoutDirection(LinearLayout.HORIZONTAL);
		
		Button b = new Button(instance);
		b.setText("New Game");
		b.setOnClickListener(new OnClickListener(){	@Override public void onClick(View v){new Game();}});
		l.addView(b);
		
		instance.setContentView(l);
	}
}
