package sw.superwhateverjnr.texture.entity;

import android.graphics.Bitmap;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.EntityTexture;
import sw.superwhateverjnr.texture.Texture;

public class CreeperTexture extends EntityTexture
{
	public CreeperTexture(int width, int height, Bitmap image)
	{
		super(EntityType.CREEPER, width, height, image);
	}

}
