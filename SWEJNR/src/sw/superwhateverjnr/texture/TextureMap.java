package sw.superwhateverjnr.texture;

import java.util.Map;
import java.util.HashMap;

import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.util.IdAndSubId;

public class TextureMap
{
	public final static IdAndSubId errorid=new IdAndSubId(-1,-1);
	
	private final static Map<IdAndSubId,Texture> blocks=new HashMap<>();
	private final static Map<EntityType,Texture> entities=new HashMap<>();
	private static boolean loadingErrorBlock=false;
	private static boolean loadingErrorEntity=false;
	public static boolean loadTexture(IdAndSubId ref, TextureLoader loader)
	{
		checkErrorTextureBlock(loader);
		try
		{
			Texture texture=loader.loadTexture(ref);
			if(texture==null)
			{
				return false;
			}
			
			blocks.put(ref, texture);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public static boolean loadTexture(EntityType ref, TextureLoader loader)
	{
		checkErrorTextureEntity(loader);
		try
		{
			Texture texture=loader.loadTexture(ref);
			if(texture==null)
			{
				return false;
			}
			
			entities.put(ref, texture);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public static Texture getTexture(IdAndSubId ref)
	{
		Texture tex=blocks.get(ref);
		if(tex==null && ref.getSubid()>-1)
		{
			tex=blocks.get(new IdAndSubId(ref.getId(),-1));
			if(tex==null)
			{
				tex=blocks.get(errorid);
			}
		}
		return tex;
	}
	public static Texture getTexture(EntityType ref)
	{
		Texture tex=entities.get(ref);
		if(tex==null && ref!=EntityType.UNKNOWN)
		{
			tex=entities.get(EntityType.UNKNOWN.getId());
		}
		return tex;
	}
	private static void checkErrorTextureBlock(TextureLoader loader)
	{
		if(loadingErrorBlock)
		{
			return;
		}
		loadingErrorBlock=true;
		loadTexture(errorid, loader);
		loadingErrorBlock=false;
	}
	private static void checkErrorTextureEntity(TextureLoader loader)
	{
		if(loadingErrorEntity)
		{
			return;
		}
		loadingErrorEntity=true;
		loadTexture(EntityType.UNKNOWN, loader);
		loadingErrorEntity=false;
	}
}
