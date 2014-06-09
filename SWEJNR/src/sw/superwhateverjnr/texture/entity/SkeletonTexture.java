package sw.superwhateverjnr.texture.entity;

import lombok.Getter;
import android.graphics.Bitmap;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.EntityTexture;
import sw.superwhateverjnr.texture.Texture;

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
	}
	
	@Override
	protected void piecify()
	{
		double pscale = width / 64;
		
		headRight=getSubBitmap(image, 0*pscale, 8*pscale, 8*pscale, 8*pscale);
		headLeft=getSubBitmap(image, 16*pscale, 8*pscale, 8*pscale, 8*pscale);
		
		bodyRight=getSubBitmap(image, 16*pscale, 20*pscale, 4*pscale, 12*pscale);
		bodyLeft=getSubBitmap(image, 28*pscale, 20*pscale, 4*pscale, 12*pscale);

		rightArmRight=getSubBitmap(image, 40*pscale, 18*pscale, 2*pscale, 12*pscale);
		rightArmLeft=getSubBitmap(image, 44*pscale, 18*pscale, 2*pscale, 12*pscale);

		leftArmRight=getSubBitmap(image, 44*pscale, 18*pscale, 2*pscale, 12*pscale);
		leftArmLeft=getSubBitmap(image, 40*pscale, 18*pscale, 2*pscale, 12*pscale);

		rightLegRight=getSubBitmap(image, 0*pscale, 18*pscale, 2*pscale, 12*pscale);
		rightLegLeft=getSubBitmap(image, 4*pscale, 18*pscale, 2*pscale, 12*pscale);

		leftLegRight=getSubBitmap(image, 4*pscale, 18*pscale, 2*pscale, 12*pscale);
		leftLegLeft=getSubBitmap(image, 0*pscale, 18*pscale, 2*pscale, 12*pscale);
	}
	
	@Override
	protected void resize()
	{
		Texture.doScale(this.getClass(), this);
	}
}
