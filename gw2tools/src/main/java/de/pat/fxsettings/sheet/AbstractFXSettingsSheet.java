package de.pat.fxsettings.sheet;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.pat.fxsettings.FXSettingsSerializerType;
import de.pat.fxsettings.types.FXListSetting;
import de.pat.fxsettings.types.FXSetting;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

public abstract class AbstractFXSettingsSheet implements FXSettingsSheet
{

    private final String settingsID;
    private final FXSettingsSerializerType serializerType;
    private final Multimap<Class<? extends Annotation>, Field> fxFields = ArrayListMultimap.create();

    public AbstractFXSettingsSheet(String settingsID, FXSettingsSerializerType serializerType)
    {
        this.settingsID = settingsID;
        this.serializerType = serializerType;
        gatherFXFields();
    }

    @Override
    public String getSettingsID()
    {
        return settingsID;
    }

    @Override
    public FXSettingsSerializerType getSerializerType()
    {
        return serializerType;
    }

    @Override
    public Collection<Field> getFXSettingFields(Class<? extends Annotation> type)
    {
        return fxFields.get(type);
    }

    @Override
    public Collection<Field> getFXSettingFields()
    {
        return fxFields.values();
    }

    @Override
    public Field getFXSettingField(String fieldName)
    {
        for (Field field : fxFields.values())
        {
            if (field.getName().equals(fieldName)) return field;
        }
        return null;
    }

    private void gatherFXFields()
    {
        for (Field loop : this.getClass().getFields())
        {
            Class<? extends Annotation> ant = null;

            if (loop.getAnnotation(FXSetting.class) != null)
            {
                ant = FXSetting.class;
            } else if (loop.getAnnotation(FXListSetting.class) != null)
            {
                ant = FXListSetting.class;
            } else if (loop.getAnnotation(FXListSetting.class) != null)
            {
                ant = FXListSetting.class;
            }

            if (ant != null) fxFields.put(ant, loop);
        }
    }

}
