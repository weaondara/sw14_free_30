package sw.superwhateverjnr.texture;

import sw.superwhateverjnr.util.IdAndSubId;
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
	private Object reference;
	private int width, height;
	private Bitmap image;
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
