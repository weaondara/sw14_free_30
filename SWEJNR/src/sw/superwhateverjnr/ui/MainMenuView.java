package sw.superwhateverjnr.ui;

import java.util.ArrayList;
import java.util.List;

import sw.superwhateverjnr.SWEJNR;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


public class MainMenuView extends BackgroundView implements View.OnTouchListener
{
	public final static String NEW_GAME = "new";
	public final static String CONTINUE_GAME = "continue";
	public final static String RANDOM_GAME = "random";
	public final static String SETTINGS = "settings";
	public final static String CREDITS = "credits";
	public final static String QUIT_GAME = "quit";
	
    private final static float BUTTON_WIDTH = 20;
    private final static float BUTTON_HEIGHT = 10;
    private final static float BUTTON_MARGIN = 1;
    
    private final static int TEXT_COLOR = 0xFFFFFFFF;
    private final static int PRESSED_TEXT_COLOR = 0xFF00FF7F;
    
    
    private List<MainMenuButton> buttons;
    @Getter @Setter
    private SelectedListener selectedListener;
    
    public MainMenuView(Context context)
    {
        super(context);
        setup();
    }
    public MainMenuView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setup();
    }
    public MainMenuView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setup();
    }
    @SneakyThrows
    private void setup()
    {
        //register things
        setFocusable(true);
        setOnTouchListener(this);
        
        //buttons
        DisplayMetrics metrics = SWEJNR.getInstance().getResources().getDisplayMetrics();
        int displayWidth=metrics.widthPixels;
        int displayHeight=metrics.heightPixels;
        
        int xleft=(int) (displayWidth/7);
        int width=(int) (displayWidth*BUTTON_WIDTH/100);
        int xright=(int) displayWidth-xleft-width;
        float height= displayHeight*BUTTON_HEIGHT/100;
        float ymargin = displayHeight*BUTTON_MARGIN/100;
        
        float currentheight=displayHeight*55/100;
        
        buttons=new ArrayList<MainMenuButton>();
        buttons.add(new MainMenuButton(NEW_GAME,"New Game",xleft, (int) currentheight, width, (int) height, Align.LEFT, SWEJNR.DEBUG));
        
        currentheight+=height+ymargin;
        buttons.add(new MainMenuButton(CONTINUE_GAME,"Continue Game",xleft, (int) currentheight, width, (int) height, Align.LEFT, SWEJNR.DEBUG));
        
        currentheight+=height+ymargin;
        buttons.add(new MainMenuButton(RANDOM_GAME,"Random Game",xleft, (int) currentheight, width, (int) height, Align.LEFT, true));
        
        
        currentheight=displayHeight*(55+(BUTTON_HEIGHT+BUTTON_MARGIN)/2)/100;
        buttons.add(new MainMenuButton(SETTINGS,"Settings",xright, (int) currentheight, width, (int) height, Align.RIGHT, true));

        currentheight+=height+ymargin;
        buttons.add(new MainMenuButton(CREDITS,"Credits",xright, (int) currentheight, width, (int) height, Align.RIGHT, true));

        currentheight+=height+ymargin;
        buttons.add(new MainMenuButton(QUIT_GAME,"Quit Game",xright, (int) currentheight, width, (int) height, Align.RIGHT, true));
    }
    
    private Paint paint=new Paint();
    @Override
	public void draw(Canvas c)
	{
    	super.draw(c);
    	
    	paint.setStyle(Style.STROKE);
    	paint.setTextSize(c.getHeight()/25);
    	int textheight=(int) (paint.descent() + paint.ascent());
    	
    	for(MainMenuButton b:buttons)
    	{
    		if(!b.isVisible())
    		{
    			continue;
    		}
    		
    		paint.setColor(b.isPressed() ? PRESSED_TEXT_COLOR : TEXT_COLOR);
    		
    		if(SWEJNR.DEBUG)
    		{
	    		paint.setStrokeWidth(0);
	    		c.drawRect(b.getX(),b.getY(),b.getX()+b.getWidth(),b.getY()+b.getHeight(), paint);
    		}
    		
    		paint.setTextAlign(b.getAlign());
    		if(b.getAlign()==Align.LEFT)
    		{
    			c.drawText(b.getText(), b.getX() + b.getWidth()/20, b.getY() + (b.getHeight()-textheight)/2, paint);
    		}
    		else if(b.getAlign()==Align.RIGHT)
    		{
    			c.drawText(b.getText(), b.getX() + b.getWidth()*19/20, b.getY() + (b.getHeight()-textheight)/2, paint);
    		}
    	}
	}
    
    private MainMenuButton touchdown;
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(v != this)
        {
            return false;
        }
        
        int action = MotionEventCompat.getActionMasked(event);
        if(event.getPointerCount() != 1 || (action!=MotionEvent.ACTION_DOWN && action!=MotionEvent.ACTION_UP))
        {
        	return false;
        }
        
    	float x = MotionEventCompat.getX(event, 0);
		float y = MotionEventCompat.getY(event, 0);
		
		
		boolean done = false;
		for(MainMenuButton b:buttons)
		{
	    	if(b.isVisible() && b.isInside(x, y))
	    	{
	    		if(action==MotionEvent.ACTION_DOWN)
	    		{
	    			touchdown=b;
		    		b.setPressed(true);
		    		done=true;
	    		}
	    		else if(action==MotionEvent.ACTION_UP)
	    		{
	    			if(b == touchdown)
			    	{
			    		touchdown=null;
			    		b.setPressed(false);
			    		if(selectedListener != null)
			    		{
			    			selectedListener.onSelected(b.getId());
			    		}
			    		done=true;
			    	}
	    		}
	    		else
		    	{
		    		b.setPressed(false);
		    	}
	    	}
	    	else
	    	{
	    		b.setPressed(false);
	    	}
		}
		this.invalidate();
		
        return done;
    }
    
    
    
    
    @Getter
    @ToString
    @RequiredArgsConstructor(suppressConstructorProperties=true)
    public class MainMenuButton
    {
    	@NonNull
        private String id;
    	@NonNull
        private String text;
    	@NonNull
        private int x, y, width, height;
    	@NonNull
        private Align align;
    	@NonNull
        private boolean visible;
    	@Setter
        private boolean pressed;
        
        public RectF getRectangle()
        {
        	return new RectF(x,y,x+width,x+height);
        }
        public RectF getRectangleAt(int x, int y)
        {
        	return new RectF(this.x+x,this.y+y,width,height);
        }
        
        public boolean isInside(float x, float y)
        {
        	return x >= this.x && y >= this.y && x <= this.x+this.width && y <= this.y+this.height;
        }
    }
    
    public interface SelectedListener
    {
    	public void onSelected(String touched);
    }
}
