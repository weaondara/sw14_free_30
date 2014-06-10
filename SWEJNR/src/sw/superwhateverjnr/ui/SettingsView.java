package sw.superwhateverjnr.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class SettingsView extends LinearLayout
{
	public SettingsView(Context context)
	{
		super(context);
		setup();
	}
	public SettingsView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setup();
	}
	public SettingsView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		setup();
	}
	private void setup()
	{
		Context c = getContext();
		CheckBox cb = new CheckBox(c);
		cb.setText("");
		
		NumberPicker nbcircleradius = new NumberPicker(c);
		nbcircleradius.setMinValue(20);
		nbcircleradius.setMaxValue(200);
	}
}
