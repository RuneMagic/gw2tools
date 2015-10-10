package com.runemagic.gw2tools.reference;


public class Reference
{
    private Reference()
    {

    }


    public static final String DIR_APP = System.getenv("APPDATA") + "\\gw2tools";
    public static final String DIR_ASSETS = DIR_APP + "\\assets";

    /**
     * Registry paths
     */
    public static final String REGISTRY_ROOT = "gw2tools";
    public static final String REGISTRY_WINDOW_BOUNDS = REGISTRY_ROOT + "/window_bounds";
    public static final String REGISTRY_SETTINGS_ROOT = REGISTRY_ROOT + "/settings";

}
