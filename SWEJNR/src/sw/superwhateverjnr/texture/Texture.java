package sw.superwhateverjnr.texture;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import android.graphics.Bitmap;

@ToString
public class Texture
{
    public static Bitmap getSubBitmap(Bitmap bm, double xoff, double yoff, double width, double height)
    {
        return getSubBitmap(bm, (int)Math.round(xoff), (int)Math.round(yoff), (int)Math.round(width), (int)Math.round(height));
    }
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
    @SneakyThrows
    public static void doScale(Class<? extends Texture> clazz, Texture t)
    {
        for(Field f : clazz.getDeclaredFields())
        {
            if(f.getType() == Bitmap.class)
            {
                f.setAccessible(true);
                Bitmap b=(Bitmap) f.get(t);
                if(b==null)
                {
                    continue;
                }
                b = Bitmap.createScaledBitmap(b, (int)(b.getWidth()*t.getScale()), (int)(b.getHeight()*t.getScale()), false);
                f.set(t, b);
            }
        }
    }
    
    // ----------------------------- class ----------------------------------

    @Getter @Setter
    protected Object reference;
    protected int width, height;
    @Getter
    protected Bitmap orgimage;
    @Getter
    protected Bitmap image;
    @Getter
    protected double scale;
    
    public Texture(Object reference, int width, int height, Bitmap image)
    {
        super();
        this.reference = reference;
        this.width = width;
        this.height = height;
        this.orgimage = image;
        this.image = image;
        scale = 1;
    }

    public void scale(double scale)
    {
        image = Bitmap.createScaledBitmap(orgimage, (int)(width*scale), (int)(height*scale), false);
        this.scale = scale;
    }
    
    public double getOrigWidth()
    {
        return width;
    }
    public double getOrigHeight()
    {
        return height;
    }
    
    public double getWidth()
    {
        return width * scale;
    }
    public double getHeight()
    {
        return height * scale;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        
        Texture other = (Texture) obj;
        if (width != other.width)
        {
            return false;
        }
        if (height != other.height)
        {
            return false;
        }
        
        if (reference == null || other.reference == null)
        {
            return false;
        }
        else if (!reference.equals(other.reference))
        {
            return false;
        }
        
        if (image == null || other.image == null)
        {
            return false;
        }
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
        
        if (orgimage == null || other.orgimage == null)
        {
            return false;
        }
        else 
        {
            if(orgimage.getWidth()!=other.orgimage.getWidth() || orgimage.getHeight()!=other.orgimage.getHeight())
            {
                return false;
            }
            else 
            {
                for(int x=0;x<orgimage.getWidth();x++)
                {
                    for(int y=0;y<orgimage.getHeight();y++)
                    {
                        if(orgimage.getPixel(x, y)!=other.orgimage.getPixel(x, y))
                        {
                            return false;
                        }
                    }
                }
            }
        }
        
        if(scale != other.scale)
        {
            return false;
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
        result = prime * result + ((orgimage == null) ? 0 : orgimage.hashCode());
        result = prime * result + width;
        result = (int) (prime * result + scale);
        return result;
    }
}
