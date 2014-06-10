package sw.superwhateverjnr.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class CreditsView extends BackgroundView
{
	public CreditsView(Context context)
    {
        super(context);
    }
    public CreditsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public CreditsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    
    private Paint paint = new Paint();
    @Override
    public void draw(Canvas c)
	{
    	super.draw(c);
    	
    	paint.setColor(0xFFFFFFFF);
    	paint.setStyle(Style.STROKE);
    	paint.setTextSize(c.getHeight()/25);
    	
    	c.drawText("This game is made by ", c.getWidth()/7, c.getHeight()*60/100, paint);
    	c.drawText("wea_ondara", c.getWidth()/5, c.getHeight()*75/100, paint);
    	c.drawText("Lord Yuuma", c.getWidth()*3/7, c.getHeight()*90/100, paint);
    	c.drawText("PiMathCLanguage", c.getWidth()*5/7, c.getHeight()*82/100, paint);
	}
}
