package de.pat.fxsettings.modules;


import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;

public class ColorPickerModule extends AbstractFXSettingsModule<Color>
{

    private ColorPicker datePicker;

    public ColorPickerModule(String fieldName, Property<Color> setting)
    {
        super(fieldName, setting);
    }

    @Override
    public void buildNode(GridPane content)
    {

        datePicker = new ColorPicker(getColor(setting));
        datePicker.setMinHeight(25);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        setting.addListener((e, oldValue, newVal) -> {
            datePicker.setValue(newVal);
        });
        content.getChildren().add(datePicker);
        GridPane.setConstraints(datePicker, 1, 0);

        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(datePicker.valueProperty().get(), setting.getValue()), datePicker.valueProperty(), setting);
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(datePicker.getValue());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        datePicker.setValue(getColor(setting));
    }

    private Color getColor(Property<Color> color)
    {
        if (color.getValue() == null) return null;
        return Color.web(color.getValue().toString());
    }

}
