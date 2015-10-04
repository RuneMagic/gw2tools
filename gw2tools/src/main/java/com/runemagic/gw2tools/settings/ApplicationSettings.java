package com.runemagic.gw2tools.settings;


import de.pat.fxsettings.FXSettingsSerializerType;
import de.pat.fxsettings.moduletypes.FXModuleType;
import de.pat.fxsettings.sheet.AbstractFXSettingsSheet;
import de.pat.fxsettings.types.FXSetting;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ToolBar;

public class ApplicationSettings extends AbstractFXSettingsSheet
{

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "GW2 API", displayName = "API Key", saveSystemChanges = false)
    public SimpleStringProperty apiKey;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "GW2 API", displayName = "test")
    public SimpleStringProperty test;


    public ApplicationSettings()
    {
        super("application_settings", FXSettingsSerializerType.FX_PREFERENCES);
    }

    @Override
    public void onLoad() throws Exception
    {
    }

    @Override
    public void onSave() throws Exception
    {
    }

    @Override
    public void initToolbar(String category, ToolBar toolBar)
    {

    }

}