package sw.superwhateverjnr.ui;

import java.io.InputStream;

import sw.superwhateverjnr.SWEJNR;
import sw.superwhateverjnr.texture.Texture;
import lombok.SneakyThrows;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class CreditsView extends View
{
	private Bitmap logo;
	
	public CreditsView(Context context)
    {
        super(context);
        setup();
    }
    public CreditsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setup();
    }
    public CreditsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setup();
    }
    
    @SneakyThrows
    private void setup()
    {
    	DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
        int displayWidth=metrics.widthPixels;
        int displayHeight=metrics.heightPixels;
    	
    	//background
        InputStream is=SWEJNR.getInstance().getResources().getAssets().open("textures/swejnrtitle.png");
		logo=BitmapFactory.decodeStream(is);
		
		int logowidth=logo.getHeight()*displayWidth/displayHeight;
		logo=Texture.getSubBitmap(logo, (logo.getWidth()-logowidth)/2, 0, logowidth, logo.getHeight());
		logo=Bitmap.createScaledBitmap(logo, displayWidth, displayHeight, true);
    }
    
    private Paint paint = new Paint();
    @Override
    public void draw(Canvas c)
	{
    	c.drawBitmap(logo, 0, 0, paint);
    	
    	paint.setColor(0xFFFFFFFF);
    	paint.setStyle(Style.STROKE);
    	paint.setTextSize(c.getHeight()/25);
    	int textheight=(int) (paint.descent() + paint.ascent());
    	
    	
    	c.drawText("This game is made by ", c.getWidth()/7, c.getHeight()*60/100, paint);
    	c.drawText("wea_ondara", c.getWidth()/5, c.getHeight()*75/100, paint);
    	c.drawText("Lord Yuuma", c.getWidth()*3/7, c.getHeight()*90/100, paint);
    	c.drawText("PiMathCLanguage", c.getWidth()*5/7, c.getHeight()*82/100, paint);
        
	}
}
