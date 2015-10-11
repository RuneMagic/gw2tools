package de.pat.fxsettings.serializer;

import java.util.Collection;


public interface FXSettingsSerializer
{

    <T> T loadSetting(Class<T> valueType, String settingsID, String fieldName);

    <T> Collection<T> loadValues(Class<T> valueType, String settingsID, String fieldName);

    boolean saveSetting(String settingsID, String fieldName, Object value);

    boolean saveValues(String settingsID, String fieldName, Collection<?> values);

}
