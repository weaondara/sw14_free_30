package sw.superwhateverjnr.texture.entity;

import android.graphics.Bitmap;
import sw.superwhateverjnr.texture.Texture;

public class PiecyTexture extends Texture
{
	public PiecyTexture(Object reference, int width, int height, Bitmap image)
	{
		super(reference, width, height, image);
		try
		{
			piecify();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void scale(double scale)
	{
		if(this.scale == scale)
		{
			return;
		}
		this.scale = scale;
		piecify();
		resize();
	}
	
	protected void piecify()
	{
		
	}
	
	protected void resize()
	{
		
	}
}
