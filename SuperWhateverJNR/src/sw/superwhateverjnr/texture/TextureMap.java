package sw.superwhateverjnr.texture;

import java.util.Map;
import java.util.HashMap;

import sw.superwhateverjnr.util.IdAndSubId;

public class TextureMap
{
	private final static Map<IdAndSubId,Texture> map=new HashMap<>();
	public static void loadTexture(IdAndSubId ref, TextureLoader loader)
	{
		Texture texture=loader.loadTexture(ref);
		map.put(ref, texture);
	}
	public static Texture getTexture(IdAndSubId ref)
	{
		return map.get(ref);
	}
}
