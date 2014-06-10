package sw.superwhateverjnr.activity;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.world.DummyWorldLoader;
import sw.superwhateverjnr.world.PackedWorldLoader;
import sw.superwhateverjnr.world.RandomWorldLoader;
import sw.superwhateverjnr.world.WorldLoader;
import lombok.Getter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity
{
	@Getter
    private static GameActivity instance;
	
	private Game game;
	public GameActivity()
	{
		super();
		instance=this;
	}
	
	public void init(String worldLoader, String world)
	{
		WorldLoader wl;
		if(worldLoader.equalsIgnoreCase("packed"))
		{
			wl=new PackedWorldLoader();
		}
		else if(worldLoader.equalsIgnoreCase("random"))
		{
			wl=new RandomWorldLoader();
		}
		else
		{
			wl=new DummyWorldLoader();
		}
		
		game = new Game();
		game.init();
		game.setWorldLoader(wl);
		game.loadWorld(world);
	}
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle b) 
    {
        super.onCreate(b);
        String worldloader=this.getIntent().getExtras().getString("worldloader");
        String worldname=this.getIntent().getExtras().getString("worldname");
        init(worldloader, worldname);
        
        game.enable();
    }
    
    @Override
    protected void onPause() 
    {
    	super.onPause();
    }
    @Override
    protected void onResume() 
    {
    	super.onResume();
    }
    
    @Override
    protected void onStop() 
    {
        super.onStop();
        game.disable();
    }
}
