package de.pat.fxsettings.modules;


import java.time.LocalDate;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;

public class DatePickerModule extends AbstractFXSettingsModule<LocalDate>
{

    private DatePicker datePicker;

    public DatePickerModule(String fieldName, Property<LocalDate> setting)
    {
        super(fieldName, setting);
    }

    @Override
    public void buildNode(GridPane content)
    {
        datePicker = new DatePicker(setting.getValue());
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
        datePicker.setValue(setting.getValue());
    }

}
