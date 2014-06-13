package sw.superwhateverjnr.activity;

import sw.superwhateverjnr.LoadingScreen;
import lombok.Getter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class FullscreenActivity extends Activity 
{
    @Getter
    private static FullscreenActivity instance;
    public FullscreenActivity()
    {
    	super();
    	instance=this;
    }
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) 
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
        new LoadingScreen(this);
    }
}
