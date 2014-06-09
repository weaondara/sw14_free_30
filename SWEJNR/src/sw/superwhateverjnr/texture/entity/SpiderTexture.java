package sw.superwhateverjnr.texture.entity;

import android.graphics.Bitmap;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.EntityTexture;
import sw.superwhateverjnr.texture.Texture;

public class SpiderTexture extends EntityTexture
{
	public SpiderTexture(int width, int height, Bitmap image)
	{
		super(EntityType.SPIDER, width, height, image);
	}

}
