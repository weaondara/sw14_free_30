package sw.superwhateverjnr.texture.entity;

import lombok.Getter;
import android.graphics.Bitmap;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.EntityTexture;

@Getter
public class SkeletonTexture extends EntityTexture
{
	private Bitmap headRight;
	private Bitmap headLeft;
	private Bitmap headFront;
	private Bitmap headBack;
	private Bitmap headTop;
	private Bitmap headBottom;

	private Bitmap bodyRight;
	private Bitmap bodyLeft;
	private Bitmap bodyFront;
	private Bitmap bodyBack;
	private Bitmap bodyTop;
	private Bitmap bodyBottom;

	private Bitmap rightArmRight;
	private Bitmap rightArmLeft;
	private Bitmap rightArmFront;
	private Bitmap rightArmBack;
	private Bitmap rightArmTop;
	private Bitmap rightArmBottom;

	private Bitmap leftArmRight;
	private Bitmap leftArmLeft;
	private Bitmap leftArmFront;
	private Bitmap leftArmBack;
	private Bitmap leftArmTop;
	private Bitmap leftArmBottom;

	private Bitmap rightLegRight;
	private Bitmap rightLegLeft;
	private Bitmap rightLegFront;
	private Bitmap rightLegBack;
	private Bitmap rightLegTop;
	private Bitmap rightLegBottom;

	private Bitmap leftLegRight;
	private Bitmap leftLegLeft;
	private Bitmap leftLegFront;
	private Bitmap leftLegBack;
	private Bitmap leftLegTop;
	private Bitmap leftLegBottom;

	public SkeletonTexture(int width, int height, Bitmap image)
	{
		super(EntityType.SKELETON, width, height, image);
		
		try
		{
			piecify();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void piecify()
	{
		int scale = width / 64;
		
		headRight=getSubBitmap(image, 0*scale, 8*scale, 8*scale, 8*scale);
		headLeft=getSubBitmap(image, 16*scale, 8*scale, 8*scale, 8*scale);
		
		bodyRight=getSubBitmap(image, 16*scale, 20*scale, 4*scale, 12*scale);
		bodyLeft=getSubBitmap(image, 28*scale, 20*scale, 4*scale, 12*scale);

		rightArmRight=getSubBitmap(image, 40*scale, 20*scale, 2*scale, 12*scale);
		rightArmLeft=getSubBitmap(image, 44*scale, 20*scale, 2*scale, 12*scale);

		leftArmRight=getSubBitmap(image, 44*scale, 20*scale, 2*scale, 12*scale);
		leftArmLeft=getSubBitmap(image, 40*scale, 20*scale, 2*scale, 12*scale);

		rightLegRight=getSubBitmap(image, 0*scale, 20*scale, 2*scale, 10*scale);
		rightLegLeft=getSubBitmap(image, 4*scale, 20*scale, 2*scale, 10*scale);

		leftLegRight=getSubBitmap(image, 4*scale, 20*scale, 2*scale, 10*scale);
		leftLegLeft=getSubBitmap(image, 0*scale, 20*scale, 2*scale, 10*scale);
	}
}
