package sw.superwhateverjnr.activity;

import android.app.Activity;
import android.os.Bundle;
import lombok.Getter;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.ui.EndView;

public class EndActivity extends Activity
{
    @Getter
    private static EndActivity instance;
            
    public EndActivity()
    {
    	super();
    	instance=this;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EndView e = new EndView(SWEJNR.getInstance().getApplicationContext());
        e.setWon(this.getIntent().getExtras().getBoolean("won"));
        e.setPoints(this.getIntent().getExtras().getInt("points"));
        this.setContentView(e);
    }
    
}
