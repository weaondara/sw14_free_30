package sw.superwhateverjnr;

import lombok.Getter;
import android.app.Application;

public class SWEJNR extends Application
{
	@Getter
	private static SWEJNR instance;
	@Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
