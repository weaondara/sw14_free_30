package sw.superwhateverjnr.texture.entity;

import lombok.Getter;
import android.graphics.Bitmap;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.texture.EntityTexture;

@Getter
public class PlayerTexture extends EntityTexture
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

	public PlayerTexture(int width, int height, Bitmap image)
	{
		super(EntityType.PLAYER, width, height, image);
		piecify();
	}
	private void piecify()
	{
		headRight=getSubBitmap(image, 0, 8, 8, 8);
		headLeft=getSubBitmap(image, 16, 8, 8, 8);
		
		bodyRight=getSubBitmap(image, 16, 20, 4, 12);
		bodyLeft=getSubBitmap(image, 28, 20, 4, 12);

		rightArmRight=getSubBitmap(image, 36, 20, 4, 12);
		rightArmLeft=getSubBitmap(image, 42, 20, 4, 12);

		leftArmRight=getSubBitmap(image, 42, 20, 4, 12);
		leftArmLeft=getSubBitmap(image, 36, 20, 4, 12);

		rightLegRight=getSubBitmap(image, 0, 20, 4, 12);
		rightLegLeft=getSubBitmap(image, 8, 20, 4, 12);

		leftLegRight=getSubBitmap(image, 8, 20, 4, 12);
		leftLegLeft=getSubBitmap(image, 0, 20, 4, 12);
	}
	private static Bitmap getSubBitmap(Bitmap bm, int x, int y, int width, int height)
	{
		Bitmap ret=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width * height];
        
		bm.getPixels(pixels, 0, width, 0, 0, height, height);
		ret.setPixels(pixels, 0, width, 0, 0, width, height);
		
		return ret;
	}
	
	
}
