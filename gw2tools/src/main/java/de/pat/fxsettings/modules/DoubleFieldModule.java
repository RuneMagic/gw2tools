package de.pat.fxsettings.modules;


import de.pat.fxsettings.MaxValue;
import de.pat.fxsettings.MinValue;
import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import de.pat.util.javafx.components.NumberTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class DoubleFieldModule extends AbstractFXSettingsModule<Number>
{

    private NumberTextField numberField;
    private MinValue minValue;
    private MaxValue maxValue;

    public DoubleFieldModule(String fieldName, Property<Number> setting, MinValue minValue, MaxValue maxValue)
    {
        super(fieldName, setting);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void buildNode(GridPane content)
    {

        String ttText = "";

        double min = Double.MIN_VALUE;
        if (minValue != null)
        {
            min = minValue.getMinValue();
            if (min != Double.MIN_VALUE) ttText += "Minimum value: " + min;
        }

        double max = Double.MAX_VALUE;
        if (maxValue != null)
        {
            max = maxValue.getMaxValue();
            if (max != Double.MIN_VALUE) ttText += "\nMaximum value: " + max;
        }

        numberField = new NumberTextField(Double.class, min, max);

        if (!Objects.equals(ttText, ""))
        {
            Tooltip.install(numberField, new Tooltip(ttText));
        }

        numberField.setMaxWidth(Double.MAX_VALUE);
        numberField.setText(String.valueOf(setting.getValue()));
        setting.addListener((e, oldValue, newVal) -> {
            numberField.setText(String.valueOf(newVal));
        });
        Label fieldLabel = new Label(getFieldName());
        content.getChildren().addAll(fieldLabel, numberField);
        GridPane.setConstraints(fieldLabel, 0, 0);
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
