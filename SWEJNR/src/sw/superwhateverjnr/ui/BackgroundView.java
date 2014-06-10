package sw.superwhateverjnr.ui;

import sw.superwhateverjnr.texture.TextureMap;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BackgroundView extends View
{
	public BackgroundView(Context context)
    {
        super(context);
    }
    public BackgroundView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public BackgroundView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    
    private Paint paint = new Paint();
    @Override
    public void draw(Canvas c)
	{
    	c.drawBitmap(TextureMap.getMenuTexture().getImage(), 0, 0, paint);
	}
}
