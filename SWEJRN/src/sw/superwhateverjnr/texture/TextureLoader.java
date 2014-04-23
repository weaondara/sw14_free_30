package sw.superwhateverjnr.texture;

import sw.superwhateverjnr.util.IdAndSubId;

public interface TextureLoader
{
	public Texture loadTexture(IdAndSubId ref) throws Exception;
}
