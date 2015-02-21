package com.RSen.Commandr.builtincommands;

import android.content.Context;
import android.widget.Toast;

import com.RSen.Commandr.R;
import com.RSen.Commandr.core.MostWantedCommand;

import tv.piratemedia.lightcontroler.api.LightControllerAPI;
import tv.piratemedia.lightcontroler.api.LightControllerException;
import tv.piratemedia.lightcontroler.api.LightZone;

/**
 * @author Eliot Stocker
 * @version 1.0 Feb 21st 15
 */
public class LightsOnCommand extends MostWantedCommand {

    private static String TITLE;
    private static String DEFAULT_PHRASE;
    private Context context;

    public LightsOnCommand(Context ctx) {
        DEFAULT_PHRASE = ctx.getString(R.string.lights_on_pharse);
        TITLE = ctx.getString(R.string.lights_on_title);
        context = ctx;
    }

    @Override
    public void execute(final Context context, String predicate) {

        LightControllerAPI api;
        try {
             api = new LightControllerAPI(context);
        } catch (LightControllerException e) {
            if(e.getCode() == LightControllerException.TYPE_APPLICATION_OLD) {
                Toast.makeText(context, context.getString(R.string.lights_controller_old), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getString(R.string.lights_controller_not_installed), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        
        LightZone[] zones = api.listZones();
        Boolean found = false;
        LightZone zone = null;
        for(int i = 0; i < zones.length; i++) {
            if(zones[i].Name.toLowerCase().equals(predicate)) {
                found = true;
                zone = zones[i];
            }
        }
        
        if(!found) {
            Toast.makeText(context, String.format(context.getString(R.string.light_zone_not_found), predicate), Toast.LENGTH_SHORT).show();
            return;
        }
        
        api.lightsOn(zone);
    }

    @Override
    public boolean isAvailable(Context context) {
        return true;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getPredicateHint() {
        return context.getString(R.string.light_zone_hint);
    }

    @Override
    public String getDefaultPhrase() {
        return DEFAULT_PHRASE;
    }


    @Override
    public boolean isHandlingGoogleNowReset() {
        return true;
    }
}