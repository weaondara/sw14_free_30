package sw.superwhateverjnr.texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.base.Preconditions;

import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.util.IdAndSubId;

public class PackedTextureLoader implements TextureLoader
{
	private static Map<IdAndSubId, String> idpathmap;
	static
	{
		idpathmap=new HashMap<>();
		idpathmap.put(new IdAndSubId(1, -1),"textures/blocks/stone.png");
		idpathmap.put(new IdAndSubId(2, -1),"textures/blocks/grass.png");
		idpathmap.put(new IdAndSubId(3, -1),"textures/blocks/dirt.png");
		idpathmap.put(new IdAndSubId(4, -1),"textures/blocks/cobblestone.png");
		idpathmap.put(new IdAndSubId(5, 0),"textures/blocks/planks0.png");
		idpathmap.put(new IdAndSubId(5, 1),"textures/blocks/planks1.png");
		idpathmap.put(new IdAndSubId(5, 2),"textures/blocks/planks2.png");
		idpathmap.put(new IdAndSubId(5, 3),"textures/blocks/planks3.png");
		idpathmap.put(new IdAndSubId(5, 4),"textures/blocks/planks4.png");
		idpathmap.put(new IdAndSubId(5, 5),"textures/blocks/planks5.png");
		idpathmap.put(new IdAndSubId(6, 0),"textures/blocks/sapling0.png");
		idpathmap.put(new IdAndSubId(6, 1),"textures/blocks/sapling1.png");
		idpathmap.put(new IdAndSubId(6, 2),"textures/blocks/sapling2.png");
		idpathmap.put(new IdAndSubId(6, 3),"textures/blocks/sapling3.png");
		idpathmap.put(new IdAndSubId(6, 4),"textures/blocks/sapling4.png");
		idpathmap.put(new IdAndSubId(6, 5),"textures/blocks/sapling5.png");
		idpathmap.put(new IdAndSubId(7, -1),"textures/blocks/bedrock.png");
		idpathmap.put(new IdAndSubId(8, -1),"textures/blocks/water_still.png");
		idpathmap.put(new IdAndSubId(9, -1),"textures/blocks/water_flow.png");
		idpathmap.put(new IdAndSubId(10, -1),"textures/blocks/lava_still.png");
		idpathmap.put(new IdAndSubId(11, -1),"textures/blocks/lava_flow.png");
		idpathmap.put(new IdAndSubId(12, -1),"textures/blocks/sand.png");
		idpathmap.put(new IdAndSubId(13, -1),"textures/blocks/gravel.png");

		idpathmap.put(new IdAndSubId(17, 0),"textures/blocks/log0.png");
		idpathmap.put(new IdAndSubId(17, 1),"textures/blocks/log1.png");
		idpathmap.put(new IdAndSubId(17, 2),"textures/blocks/log2.png");
		idpathmap.put(new IdAndSubId(17, 3),"textures/blocks/log3.png");
		idpathmap.put(new IdAndSubId(17, 4),"textures/blocks/log4.png");
		idpathmap.put(new IdAndSubId(17, 5),"textures/blocks/log5.png");
		idpathmap.put(new IdAndSubId(18, 0),"textures/blocks/leaves0.png");
		idpathmap.put(new IdAndSubId(18, 1),"textures/blocks/leaves1.png");
		idpathmap.put(new IdAndSubId(18, 2),"textures/blocks/leaves2.png");
		idpathmap.put(new IdAndSubId(18, 3),"textures/blocks/leaves3.png");
		idpathmap.put(new IdAndSubId(18, 4),"textures/blocks/leaves4.png");
		idpathmap.put(new IdAndSubId(18, 5),"textures/blocks/leaves5.png");
	}
	@Override
	public BlockTexture loadTexture(IdAndSubId ref) throws IOException
	{
		Preconditions.checkNotNull(ref);
		
		
		String file=idpathmap.get(ref);
		if(file==null)
		{
			file="dummy/textures/error.png";
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
		// TODO Auto-generated method stub
		return null;
	}
}
