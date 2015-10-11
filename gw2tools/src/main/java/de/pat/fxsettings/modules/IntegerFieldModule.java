package de.pat.fxsettings.modules;


import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import de.pat.fxsettings.MaxValue;
import de.pat.fxsettings.MinValue;
import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import de.pat.util.javafx.components.NumberTextField;

public class IntegerFieldModule extends AbstractFXSettingsModule<Number>
{

    private NumberTextField numberField;
    private MinValue minValue;
    private MaxValue maxValue;

    public IntegerFieldModule(String fieldName, Property<Number> setting)
    {
        super(fieldName, setting);
    }

    public IntegerFieldModule(String fieldName, Property<Number> setting, MinValue minValue, MaxValue maxValue)
    {
        super(fieldName, setting);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void buildNode(GridPane content)
    {

        String ttText = "";

        int min = Integer.MIN_VALUE;
        if (minValue != null)
        {
            min = minValue.getMinValue().intValue();
            if (min != Integer.MIN_VALUE) ttText += "Minimum value: " + min;
        }

        int max = Integer.MAX_VALUE;
        if (maxValue != null)
        {
            max = maxValue.getMaxValue().intValue();
            if (max != Integer.MIN_VALUE) ttText += "\nMaximum value: " + max;
        }

        numberField = new NumberTextField(Integer.class, min, max);

        if (!Objects.equals(ttText, ""))
        {
            Tooltip.install(numberField, new Tooltip(ttText));
        }

        numberField.setMaxWidth(Double.MAX_VALUE);
        numberField.setText(String.valueOf(setting.getValue()));
        setting.addListener((e, oldValue, newVal) -> {
            numberField.setText(String.valueOf(newVal));
        });
        content.getChildren().add(numberField);
        GridPane.setConstraints(numberField, 1, 0);

        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(numberField.getValueProperty().get(), setting.getValue()), numberField.getValueProperty(), setting);
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(numberField.getValueProperty().get());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        numberField.setText(String.valueOf(setting.getValue()));
    }

}
