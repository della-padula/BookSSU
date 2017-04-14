package com.terry.librarysearch.CustomView;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontCache {

    private static Map<String, Typeface> fontMap = new HashMap<String, Typeface>();
    private static FontCache instance;

    private FontCache() {

    }

    public synchronized static FontCache getInstance(Context context) {
        if ( instance == null ) {
            instance = new FontCache();
            FontCache.initCache(context);
        }
        return instance;
    }

    public static void initCache(Context context) {

    }

    public static Typeface getFont(Context context, String fontName){
        if (fontMap.containsKey(fontName)){
            return fontMap.get(fontName);
        }
        else {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), fontName);
            fontMap.put(fontName, tf);
            return tf;
        }
    }

}