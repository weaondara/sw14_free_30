package sw.superwhateverjnr.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class LoadingView extends BackgroundView
{
	public LoadingView(Context context)
    {
        super(context);
    }
    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    
    private String status;
	public void status(String status)
	{
		this.status = status;
	}
	
    
    private Paint paint = new Paint();
    @Override
    public void draw(Canvas c)
	{
    	super.draw(c);
    	
    	paint.setColor(0xFFFFFFFF);
    	paint.setStyle(Style.STROKE);
    	paint.setTextSize(c.getHeight()/25);
    	
    	if(status == null)
    	{
    		return;
    	}
    	
    	float width=paint.measureText(status);
    	float x=(c.getWidth()-width)/2;
    	
    	c.drawText(status, x, c.getHeight()*3/5, paint);
	}
}
