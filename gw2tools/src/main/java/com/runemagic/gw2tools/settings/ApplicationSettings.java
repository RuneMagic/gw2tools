package com.runemagic.gw2tools.settings;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ToolBar;

import de.pat.fxsettings.FXSettingsSerializerType;
import de.pat.fxsettings.moduletypes.FXModuleType;
import de.pat.fxsettings.sheet.AbstractFXSettingsSheet;
import de.pat.fxsettings.types.FXSetting;

public class ApplicationSettings extends AbstractFXSettingsSheet
{

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "GW2 API", displayName = "API Key", saveSystemChanges = false)
    public SimpleStringProperty apiKey;

    @FXSetting(type = Boolean.class, moduleType = FXModuleType.CHECK_BOX, category = "Advanced (don't change unless you know what you're doing)", displayName = "Don't use unofficial methods to validate the API key")
    public SimpleBooleanProperty noUnofficialValidation;

    @FXSetting(type = Boolean.class, moduleType = FXModuleType.CHECK_BOX, category = "Advanced (don't change unless you know what you're doing)", displayName = "Enable profiler (requres restart)")
    public SimpleBooleanProperty profilerEnabled;

    @FXSetting(type = Boolean.class, moduleType = FXModuleType.CHECK_BOX, category = "Advanced (don't change unless you know what you're doing)", displayName = "Disable API source optimizer (requres restart)")
    public SimpleBooleanProperty sourceOptimizerDisabled;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Raids", displayName = "Raid Spreadsheet ID")
    public SimpleStringProperty raidSpreadsheetID;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Raids", displayName = "Raid Names Range")
    public SimpleStringProperty raidNamesRange;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Raids", displayName = "Raid Class Skill Range")
    public SimpleStringProperty raidSkillRange;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Raids", displayName = "Raid Class Preferences Range")
    public SimpleStringProperty raidPreferencesRange;

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