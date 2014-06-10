package sw.superwhateverjnr.activity;

import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.ui.CreditsView;
import lombok.Getter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class CreditsActivity extends Activity
{
	@Getter
    private static CreditsActivity instance;
	
	public CreditsActivity()
	{
		super();
		instance=this;
	}
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle b) 
    {
        super.onCreate(b);
        
        this.setContentView(new CreditsView(SWEJNR.getInstance().getApplicationContext()));
    }
    
    @Override
    protected void onStop() 
    {
        super.onStop();
    }
}
