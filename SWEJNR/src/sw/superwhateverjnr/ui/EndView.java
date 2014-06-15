package sw.superwhateverjnr.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.Game;

public class EndView extends BackgroundView
{
	public EndView(Context context)
    {
        super(context);
    }
    public EndView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public EndView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    @Getter @Setter
    private boolean won=false;
    @Getter @Setter
    private int points=0;
    
    private Paint paint = new Paint();
    @Override
    public void draw(Canvas c)
	{
    	super.draw(c);
    	
    	paint.setColor(0xFFFFFFFF);
    	paint.setStyle(Style.STROKE);
    	paint.setTextSize(c.getHeight()/12);
        paint.setTextAlign(Align.CENTER);
        
        Paint scorePaint = new Paint();
    	scorePaint.setColor(0xFFFFFFFF);
    	scorePaint.setStyle(Style.STROKE);
    	scorePaint.setTextSize(c.getHeight()/18);
        scorePaint.setTextAlign(Align.CENTER);
        if(won)
        {
            c.drawText("You win :D", c.getWidth()/2, c.getHeight()*2/3, paint);            
        }
        else
        {
            c.drawText("You lose ._.", c.getWidth()/2, c.getHeight()*2/3, paint);
        }
        c.drawText("Your score is " + points, c.getWidth()/2, c.getHeight()*4/5, scorePaint);
	}
}
