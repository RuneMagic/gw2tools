package de.pat.fxsettings.sheet;


import de.pat.fxsettings.FXSettingsSerializerType;
import de.pat.fxsettings.moduletypes.FXModuleType;
import de.pat.fxsettings.types.FXListSetting;
import de.pat.fxsettings.types.FXSetting;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class TestSettingSheet extends AbstractFXSettingsSheet
{

    @FXSetting(type = Boolean.class, moduleType = FXModuleType.CHECK_BOX, category = "Text")
    public SimpleBooleanProperty booleanTest;

    @FXSetting(type = Integer.class, moduleType = FXModuleType.INTEGER_FIELD, minValue = -100, maxValue = 100, category = "Numbers")
    public SimpleIntegerProperty intTest;

    @FXSetting(type = Integer.class, moduleType = FXModuleType.INTEGER_FIELD, minValue = -100, maxValue = 100, category = "Numbers", hasDefaultValue = true)
    public SimpleIntegerProperty defIntTest = new SimpleIntegerProperty(1337);

    @FXSetting(type = Double.class, moduleType = FXModuleType.SLIDER, maxValue = 500, category = "Numbers")
    public SimpleDoubleProperty sliderTest;

    @FXSetting(type = Double.class, moduleType = FXModuleType.DOUBLE_FIELD, minValue = 0, maxValue = 100.5, category = "Numbers")
    public SimpleDoubleProperty doubleTest;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_AREA, category = "Text")
    public SimpleStringProperty stringTest;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Text")
    public SimpleStringProperty stringTest1;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Text")
    public SimpleStringProperty stringTest2;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Text")
    public SimpleStringProperty stringTest3;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Text")
    public SimpleStringProperty stringTest4;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Text")
    public SimpleStringProperty stringTest5;

    @FXSetting(type = String.class, moduleType = FXModuleType.TEXT_FIELD, category = "Text")
    public SimpleStringProperty stringTest6;

    @FXSetting(type = String.class, moduleType = FXModuleType.COMBO_BOX, valuesField = "testList")
    public SimpleStringProperty test;

    @FXSetting(type = LocalDate.class, moduleType = FXModuleType.DATE_PICKER)
    public SimpleObjectProperty<LocalDate> dateTest;

    @FXSetting(type = Color.class, moduleType = FXModuleType.COLOR_PICKER)
    public SimpleObjectProperty<Color> colorTest;

    @FXListSetting(type = String.class, userEditable = false, category = "Text")
    public ObservableList<String> testList;

    @FXListSetting(type = Color.class, userEditable = false)
    public ObservableList<Color> colorList;


    public TestSettingSheet()
    {
        super("default", FXSettingsSerializerType.FX_PREFERENCES);
    }

    @Override
    public void onLoad() throws Exception
    {
        System.out.println("Loaded: " + test);
        System.out.println("Loaded: " + testList);
        System.out.println("Loaded: " + dateTest);
        System.out.println("Loaded: " + colorTest);
        System.out.println("Loaded: " + colorList);
        System.out.println("Loaded: " + sliderTest);
        System.out.println("Loaded: " + stringTest);
        System.out.println("Loaded: " + intTest);
    }

    @Override
    public void onSave() throws Exception
    {
        System.out.println("Saved: " + test);
        System.out.println("Saved: " + testList);
        System.out.println("Saved: " + dateTest);
        System.out.println("Saved: " + colorTest);
        System.out.println("Saved: " + colorList);
        System.out.println("Loaded: " + sliderTest);
        System.out.println("Loaded: " + stringTest);
        System.out.println("Loaded: " + intTest);
    }

    @Override
    public void initToolbar(String category, ToolBar toolBar)
    {

    }

}