package sw.superwhateverjnr;

import lombok.Getter;
import android.app.Application;

public class SWEJNR extends Application
{
	public final static boolean DEBUG = true;
	
	@Getter
	private static SWEJNR instance;
	@Override
    public void onCreate() 
	{
        instance = this;
        super.onCreate();
    }
}
