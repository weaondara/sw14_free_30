package sw.superwhateverjnr.texture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import android.graphics.Bitmap;

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties=true)
@ToString
public class Texture
{
	public static Bitmap getSubBitmap(Bitmap bm, int xoff, int yoff, int width, int height)
	{
		Bitmap ret=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(int x=0;x<width;x++)
        {
        	for(int y=0;y<height;y++)
            {
            	ret.setPixel(x, y, bm.getPixel(x+xoff, y+yoff));
            }
        }

		return ret;
	}
	
	protected Object reference;
	protected int width, height;
	protected Bitmap image;
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Texture other = (Texture) obj;
		if (width != other.width)
			return false;
		if (height != other.height)
			return false;
		
		if (reference == null || other.reference == null)
			return false;
		else if (!reference.equals(other.reference))
			return false;
		
		if (image == null || other.image == null)
			return false;
		else 
		{
			if(image.getWidth()!=other.image.getWidth() || image.getHeight()!=other.image.getHeight())
			{
				return false;
			}
			else 
			{
				for(int x=0;x<image.getWidth();x++)
				{
					for(int y=0;y<image.getHeight();y++)
					{
						if(image.getPixel(x, y)!=other.image.getPixel(x, y))
						{
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + width;
		return result;
	}
}
