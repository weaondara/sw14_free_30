package sw.superwhateverjnr.texture;

import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.util.IdAndSubId;

public interface TextureLoader
{
	public BlockTexture loadTexture(IdAndSubId ref) throws Exception;
	public EntityTexture loadTexture(EntityType ref) throws Exception;
	public ItemTexture loadTexture(Integer ref) throws Exception;
}
