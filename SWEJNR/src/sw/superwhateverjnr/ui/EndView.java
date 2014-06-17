package sw.superwhateverjnr.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import lombok.Getter;
import lombok.Setter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.activity.EndActivity;

public class EndView extends BackgroundView implements View.OnTouchListener
{
	public EndView(Context context)
    {
        super(context);
        this.setOnTouchListener(this);
    }
    public EndView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setOnTouchListener(this);
    }
    public EndView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
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
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1)
	{
		EndActivity.getInstance().finish();
		return true;
	}
}
