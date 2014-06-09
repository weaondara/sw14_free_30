package sw.superwhateverjnr.texture;

import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.entity.PiecyTexture;
import android.graphics.Bitmap;

public class EntityTexture extends PiecyTexture
{
	public EntityTexture(EntityType reference, int width, int height, Bitmap image)
	{
		super(reference, width, height, image);
	}
}
