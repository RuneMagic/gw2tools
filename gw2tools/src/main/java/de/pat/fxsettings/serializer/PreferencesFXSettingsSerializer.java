package de.pat.fxsettings.serializer;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.runemagic.gw2tools.reference.Reference;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.prefs.Preferences;

public class PreferencesFXSettingsSerializer implements FXSettingsSerializer
{

    private final Gson gson = new Gson();

    @Override
    public <T> T loadSetting(Class<T> valueType, String settingsID, String fieldName)
    {
        Preferences prefs = Preferences.userRoot().node(Reference.REGISTRY_SETTINGS_ROOT + settingsID);
        if (prefs.get(fieldName, null) == null) return null;

        if (valueType == Boolean.class) return valueType.cast(prefs.getBoolean(fieldName, false));
        if (valueType == Integer.class) return valueType.cast(prefs.getInt(fieldName, 0));
        if (valueType == Float.class) return valueType.cast(prefs.getFloat(fieldName, 0));
        if (valueType == Double.class) return valueType.cast(prefs.getDouble(fieldName, 0));
        if (valueType == Long.class) return valueType.cast(prefs.getLong(fieldName, 0));

        String val = prefs.get(fieldName, null);
        if (val != null)
        {
            return gson.fromJson(val, valueType);
        } else
        {
            return null;
        }
    }

    @Override
    public <T> Collection<T> loadValues(Class<T> valueType, String settingsID, String fieldName)
    {
        Preferences prefs = Preferences.userRoot().node(Reference.REGISTRY_SETTINGS_ROOT + settingsID);

        Object val = prefs.get(fieldName, null);
        if (val == null) return null;

        Type listType = new TypeToken<Collection<T>>()
        {
        }.getType();

        return gson.<List<T>>fromJson(prefs.get(fieldName, null), listType);
    }

    @Override
    public boolean saveSetting(String settingsID, String fieldName, Object value)
    {
        Preferences prefs = Preferences.userRoot().node(Reference.REGISTRY_SETTINGS_ROOT + settingsID);
        if (value instanceof Boolean) prefs.putBoolean(fieldName, (boolean) value);
        if (value instanceof Integer) prefs.putInt(fieldName, (Integer) value);
        if (value instanceof Float) prefs.putFloat(fieldName, (Float) value);
        if (value instanceof Double) prefs.putDouble(fieldName, (Double) value);
        if (value instanceof Long) prefs.putLong(fieldName, (Long) value);
        if (value instanceof String) prefs.put(fieldName, (String) value);

        prefs.put(fieldName, gson.toJson(value));
        return true;
    }

    @Override
    public boolean saveValues(String settingsID, String fieldName, Collection<?> values)
    {
        String vals = gson.toJson(values);
        Preferences prefs = Preferences.userRoot().node(Reference.REGISTRY_SETTINGS_ROOT + settingsID);
        prefs.put(fieldName, vals);
        return true;
    }

}
