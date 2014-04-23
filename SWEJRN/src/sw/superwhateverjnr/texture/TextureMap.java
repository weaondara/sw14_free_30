package sw.superwhateverjnr.texture;

import java.util.Map;
import java.util.HashMap;

import sw.superwhateverjnr.util.IdAndSubId;

public class TextureMap
{
	private final static Map<IdAndSubId,Texture> map=new HashMap<>();
	private final static IdAndSubId errorid=new IdAndSubId(-1,-1);
	private static boolean loadingError=false;
	public static boolean loadTexture(IdAndSubId ref, TextureLoader loader)
	{
		checkErrorTexture(loader);
		try
		{
			Texture texture=loader.loadTexture(ref);
			if(texture==null)
			{
				return false;
			}
			
			map.put(ref, texture);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public static Texture getTexture(IdAndSubId ref)
	{
		Texture tex=map.get(ref);
		if(tex==null && ref.getSubid()>-1)
		{
			tex=map.get(new IdAndSubId(ref.getId(),-1));
		}
		return tex;
	}
	private static void checkErrorTexture(TextureLoader loader)
	{
		if(loadingError)
		{
			return;
		}
		loadingError=true;
		loadTexture(errorid, loader);
		loadingError=false;
	}
}
