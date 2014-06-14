package sw.superwhateverjnr.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import java.nio.ByteBuffer;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.R;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
    public void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        updateSummaries(getPreferenceScreen());
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        updateSummary(findPreference(key));
        Game g = Game.getInstance();
        if(g == null)
        {
            return;
        }
        if(key.equals("prefOuterButtonSize"))
        {
            g.getSettings().setControlCircleRadiusOuter(Integer.valueOf(sharedPreferences.getString(key, "-1")));
        }
        if(key.equals("prefInnerButtonSize"))
        {
            g.getSettings().setControlCircleRadiusInner(Integer.valueOf(sharedPreferences.getString(key, "-1")));
        }
        if(key.equals("prefArrowSize"))
        {
            g.getSettings().setControlArrowSize(Integer.valueOf(sharedPreferences.getString(key, "-1")));
        }
        if(key.equals("prefOuterColour"))
        {
            g.getSettings().setControlCircleColorOuter(Integer.decode(sharedPreferences.getString(key, "-1")));
        }
        if(key.equals("prefInnerColour"))
        {
            g.getSettings().setControlCircleColorInner(Integer.decode(sharedPreferences.getString(key, "-1")));
        }
        if(key.equals("prefArrowColour"))
        {
            g.getSettings().setControlArrowColor(Integer.decode(sharedPreferences.getString(key, "-1")));
        }
        if(key.equals("prefOuterOpacity"))
        {
            g.getSettings().setControlCircleOpacityOuter(ByteBuffer.allocate(4).putInt(Integer.decode(sharedPreferences.getString(key, "-1"))).array()[3]);
        }
        if(key.equals("prefInnerOpacity"))
        {
            g.getSettings().setControlCircleOpacityInner(ByteBuffer.allocate(4).putInt(Integer.decode(sharedPreferences.getString(key, "-1"))).array()[3]);
        }
        if(key.equals("prefArrowOpacity"))
        {
            g.getSettings().setControlArrowOpacity(ByteBuffer.allocate(4).putInt(Integer.decode(sharedPreferences.getString(key, "-1"))).array()[3]);
        }
        if(key.equals("prefBackgroundColour"))
        {
            g.getSettings().setBackgroundColor(Integer.decode(sharedPreferences.getString(key, "-1")));
        }
    }
    
    private void updateSummary(Preference p)
    {
        if(p instanceof ListPreference)
        {
            ListPreference lp = (ListPreference) p;
            p.setSummary(lp.getEntry());
        }
        else if (p instanceof EditTextPreference) 
        {
            EditTextPreference ep = (EditTextPreference) p;
            p.setSummary(ep.getText());
        }
    }
    
    private void updateSummaries(Preference p)
    {
        if(p instanceof PreferenceGroup)
        {
            PreferenceGroup pg = (PreferenceGroup) p;
            for(int i = 0; i < pg.getPreferenceCount(); i++)
            {
                updateSummaries(pg.getPreference(i));
            }
        }
        else
        {
            updateSummary(p);
        }
    }
}