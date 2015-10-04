package de.pat.fxsettings.modules;


import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Objects;

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
        Label fieldLabel = new Label(getFieldName());
        content.getChildren().addAll(fieldLabel, datePicker);
        GridPane.setConstraints(fieldLabel, 0, 0);
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
