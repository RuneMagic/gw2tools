package de.pat.fxsettings.modules;


import java.text.DecimalFormat;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import de.pat.fxsettings.MaxValue;
import de.pat.fxsettings.MinValue;
import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;

public class SliderModule extends AbstractFXSettingsModule<Number>
{

    private Slider slider;
    private Double minValue;
    private Double maxValue;

    private DecimalFormat displayFormat = new DecimalFormat("#.##");

    public SliderModule(String fieldName, Property<Number> setting)
    {
        super(fieldName, setting);
    }

    public SliderModule(String fieldName, Property<Number> setting, MinValue minValue, MaxValue maxValue)
    {
        super(fieldName, setting);
        this.minValue = minValue.getMinValue();
        this.maxValue = maxValue.getMaxValue();
    }

    public SliderModule(String fieldName, Property<Number> setting, MinValue minValue)
    {
        super(fieldName, setting);
        this.minValue = minValue.getMinValue();
    }

    public SliderModule(String fieldName, Property<Number> setting, MaxValue maxValue)
    {
        super(fieldName, setting);
        this.maxValue = maxValue.getMaxValue();
    }


    @Override
    public void buildNode(GridPane content)
    {
        slider = new Slider();

        slider.setShowTickLabels(true);
        slider.setMaxWidth(Double.MAX_VALUE);
        if (minValue != null) slider.setMin(minValue);
        if (maxValue != null) slider.setMax(maxValue);

        slider.setValue(getValue(setting));
        setting.addListener((e, oldValue, newVal) -> {
            slider.setValue(getValue(newVal));
        });

        HBox controls = new HBox(5);
        HBox.setHgrow(slider, Priority.ALWAYS);
        controls.setAlignment(Pos.TOP_LEFT);
        controls.setMaxWidth(Double.MAX_VALUE);

        Label valueLabel = new Label(displayFormat.format(slider.getValue()));
        valueLabel.setStyle("-fx-border-color: grey");
        valueLabel.setPrefWidth(75);
        valueLabel.setMaxWidth(75);
        slider.valueProperty().addListener((e, oldVal, newVal) -> {
            valueLabel.setText(displayFormat.format(slider.getValue()));
        });

        controls.getChildren().addAll(slider, valueLabel);

        content.getChildren().add(controls);

        GridPane.setConstraints(controls, 1, 0);

        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(slider.valueProperty().get(), setting.getValue()), slider.valueProperty(), setting);
    }

    private double getValue(Property<Number> prop)
    {
        if (prop.getValue() != null) return prop.getValue().doubleValue();
        return 0d;
    }

    private double getValue(Number number)
    {
        if (number != null) return number.doubleValue();
        return 0d;
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(slider.getValue());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        slider.setValue(getValue(setting));
    }

}
