package sw.superwhateverjnr;

import java.io.InputStream;

import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import lombok.Getter;
import lombok.SneakyThrows;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class SWEJNR extends Application
{
	public final static boolean DEBUG = true;
	
	@Getter
	private static SWEJNR instance;
	@Override
	@SneakyThrows
    public void onCreate() 
	{
        instance = this;
        
        DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
        int displayWidth=metrics.widthPixels;
        int displayHeight=metrics.heightPixels;
        
        //background
        InputStream is=instance.getResources().getAssets().open("textures/swejnrtitle.png");
		Bitmap logo=BitmapFactory.decodeStream(is);
		
		int logowidth=logo.getHeight()*displayWidth/displayHeight;
		logo=Texture.getSubBitmap(logo, (logo.getWidth()-logowidth)/2, 0, logowidth, logo.getHeight());
		logo=Bitmap.createScaledBitmap(logo, displayWidth, displayHeight, true);
		
		TextureMap.setMenuTexture(new Texture(null, logo.getWidth(), logo.getHeight(), logo));
        
        super.onCreate();
    }
}
