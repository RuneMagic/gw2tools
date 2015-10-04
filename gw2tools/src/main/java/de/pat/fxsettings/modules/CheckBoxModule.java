package de.pat.fxsettings.modules;


import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Objects;

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
        Label fieldLabel = new Label(getFieldName());
        GridPane.setConstraints(fieldLabel, 0, 0);
        GridPane.setConstraints(checkbox, 1, 0);
        content.getChildren().addAll(fieldLabel, checkbox);

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
