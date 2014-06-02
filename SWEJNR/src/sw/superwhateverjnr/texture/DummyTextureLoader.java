package sw.superwhateverjnr.texture;

import java.io.InputStream;

import com.google.common.base.Preconditions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.util.IdAndSubId;

public class DummyTextureLoader implements TextureLoader
{
	@Override
	public BlockTexture loadTexture(IdAndSubId ref) throws Exception
	{
		Preconditions.checkNotNull(ref);
		
		String file="dummy/textures/error.png";
		if(ref.equals(new IdAndSubId(1, -1)))
		{
			file="dummy/textures/blocks/stone.png";
		}
		
		InputStream is=SWEJNR.getInstance().getResources().getAssets().open(file);
		Bitmap bm=BitmapFactory.decodeStream(is);
		if(bm==null)
		{
			return null;
		}
		
		BlockTexture tex=new BlockTexture(ref, bm.getWidth(),bm.getHeight(), bm);
		return tex;
	}

	@Override
	public EntityTexture loadTexture(EntityType ref) throws Exception
	{
		return null;
	}
}
