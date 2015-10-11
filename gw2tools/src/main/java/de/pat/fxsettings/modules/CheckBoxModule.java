package de.pat.fxsettings.modules;


import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;

public class CheckBoxModule extends AbstractFXSettingsModule<Boolean>
{

    private CheckBox checkbox;

    public CheckBoxModule(String fieldName, Property<Boolean> setting)
    {
        super(fieldName, setting);
    }

    @Override
    public void buildNode(GridPane content)
    {
        checkbox = new CheckBox();
        checkbox.setMaxWidth(Double.MAX_VALUE);
        checkbox.setSelected(setting.getValue());
        setting.addListener((e, oldValue, newVal) -> {
            checkbox.setSelected(newVal);
        });

        GridPane.setConstraints(checkbox, 1, 0);
        content.getChildren().add(checkbox);
        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(checkbox.selectedProperty().get(), setting.getValue()), checkbox.selectedProperty(), setting);
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(checkbox.isSelected());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        checkbox.setSelected(setting.getValue());
    }

}
