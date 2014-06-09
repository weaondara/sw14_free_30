package sw.superwhateverjnr.texture.entity;

import android.graphics.Bitmap;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.EntityTexture;
import sw.superwhateverjnr.texture.Texture;


public class SkeletonTexture extends EntityTexture
{
	public SkeletonTexture(int width, int height, Bitmap image)
	{
		super(EntityType.SKELETON, width, height, image);
	}

}
