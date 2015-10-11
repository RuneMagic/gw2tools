package de.pat.fxsettings.sheet;


import de.pat.fxsettings.FXSettingsSerializerType;
import javafx.scene.control.ToolBar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

public interface FXSettingsSheet
{

    String getSettingsID();

    FXSettingsSerializerType getSerializerType();

    Collection<Field> getFXSettingFields(Class<? extends Annotation> type);

    Collection<Field> getFXSettingFields();

    Field getFXSettingField(String fieldName);

    void onLoad() throws Exception;

    void onSave() throws Exception;

    void initToolbar(String category, ToolBar toolBar);

}
