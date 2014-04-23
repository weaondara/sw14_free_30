package sw.superwhateverjnr.texture;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.util.IdAndSubId;

public class DummyTextureLoader implements TextureLoader
{
	@Override
	public Texture loadTexture(IdAndSubId ref) throws Exception
	{
		InputStream is=SWEJNR.getInstance().getResources().getAssets().open("dummy/textures/blocks/stone.png");
		Bitmap bm=BitmapFactory.decodeStream(is);
		if(bm==null)
		{
			return null;
		}
		
		Texture tex=new Texture(bm.getWidth(),bm.getHeight(), bm);
		return tex;
	}
}
