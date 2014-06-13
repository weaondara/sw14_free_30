package sw.superwhateverjnr;

import java.io.InputStream;

import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.PackedTextureLoader;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureLoader;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.util.IdAndSubId;
import lombok.Getter;
import lombok.SneakyThrows;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class SWEJNR extends Application implements Runnable
{
	public final static boolean DEBUG = true;
	
	@Getter
	private static SWEJNR instance;
	
	private Thread t;
	
	@Getter
	private boolean loadFinished = false;
	@Getter
	private String loadStatus;
	private TextureLoader textureLoader;
	
	@Override
	@SneakyThrows
    public void onCreate() 
	{
        instance = this;
        
        super.onCreate();
        
        loadBackground();
        
        t = new Thread(this);
        t.start();
    }
	
	@SneakyThrows
	private void loadBackground()
	{
		DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
        int displayWidth=metrics.widthPixels;
        int displayHeight=metrics.heightPixels;
		
		//background
        InputStream is=getResources().getAssets().open("textures/swejnrtitle.png");
		Bitmap logo=BitmapFactory.decodeStream(is);
		
		int logowidth=logo.getHeight()*displayWidth/displayHeight;
		logo=Texture.getSubBitmap(logo, (logo.getWidth()-logowidth)/2, 0, logowidth, logo.getHeight());
		logo=Bitmap.createScaledBitmap(logo, displayWidth, displayHeight, true);
		
		TextureMap.setMenuTexture(new Texture(null, logo.getWidth(), logo.getHeight(), logo));
	}
	
	private void loadSettings()
	{
		
		
	}
	
	private void loadTextures()
	{
		textureLoader=new PackedTextureLoader();
		
		TextureMap.loadTexture(new IdAndSubId(1, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(2, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(3, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(4, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(5, 0),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(5, 1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(5, 2),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(5, 3),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(5, 4),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(5, 5),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(6, 0),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(6, 1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(6, 2),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(6, 3),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(6, 4),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(6, 5),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(7, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(8, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(9, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(10, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(11, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(12, -1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(13, -1),textureLoader);
		
		
		TextureMap.loadTexture(new IdAndSubId(17, 0),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(17, 1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(17, 2),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(17, 3),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(17, 4),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(17, 5),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(18, 0),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(18, 1),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(18, 2),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(18, 3),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(18, 4),textureLoader);
		TextureMap.loadTexture(new IdAndSubId(18, 5),textureLoader);
		
		TextureMap.loadTexture(EntityType.PLAYER,textureLoader);
		TextureMap.loadTexture(EntityType.CREEPER,textureLoader);
		TextureMap.loadTexture(EntityType.ZOMBIE,textureLoader);
		TextureMap.loadTexture(EntityType.SKELETON,textureLoader);
		TextureMap.loadTexture(EntityType.SPIDER,textureLoader);
	}

	@Override
	public void run()
	{
		loadStatus = "settings";
		loadSettings();
		
		loadStatus = "textures";
		loadTextures();
		
		
		loadFinished = true;
	}
}
