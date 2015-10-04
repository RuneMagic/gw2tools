package de.pat.fxsettings.modules;


import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Objects;

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
        datePicker.setValue(setting.getValue());
    }

}
