package de.pat.fxsettings.types;


import de.pat.fxsettings.moduletypes.FXModuleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FXListSetting
{
    Class<?> type();

    boolean userEditable() default true;

    FXModuleType moduleType() default FXModuleType.TEXT_FIELD;

    String category() default "General";

    String valuesField() default "not_set";

    boolean hasDefaultValue() default false;

    boolean saveSystemChanges() default true;

    String displayName() default "";

}
