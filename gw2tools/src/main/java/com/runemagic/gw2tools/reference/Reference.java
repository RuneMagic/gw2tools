package com.runemagic.gw2tools.reference;

import java.io.File;

public class Reference
{
    private Reference()
    {

    }


    public static final String DIR_APP = System.getenv("APPDATA") + File.separator +  "gw2tools";
    public static final String DIR_ASSETS = DIR_APP + File.separator +  "assets";
    public static final String DIR_ASSETS_CACHED = DIR_ASSETS + File.separator + "cache";

    /**
     * Registry paths
     */
    public static final String REGISTRY_ROOT = "gw2tools";
    public static final String REGISTRY_WINDOW_BOUNDS = REGISTRY_ROOT + "/window_bounds";
    public static final String REGISTRY_SETTINGS_ROOT = REGISTRY_ROOT + "/settings";

}
