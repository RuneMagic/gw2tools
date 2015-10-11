package de.pat.fxsettings.modules;


import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;

public class ToggleButtonModule extends AbstractFXSettingsModule<Boolean>
{

    private ToggleButton toggleButton;

    public ToggleButtonModule(String fieldName, Property<Boolean> setting)
    {
        super(fieldName, setting);
    }

    @Override
    public void buildNode(GridPane content)
    {
        toggleButton = new ToggleButton();
        toggleButton.setMaxWidth(Double.MAX_VALUE);
        toggleButton.setSelected(setting.getValue());
        setting.addListener((e, oldValue, newVal) -> {
            toggleButton.setSelected(newVal);
        });
        GridPane.setConstraints(toggleButton, 1, 0);
        content.getChildren().add(toggleButton);

        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(toggleButton.selectedProperty().get(), setting.getValue()), toggleButton.selectedProperty(), setting);
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(toggleButton.isSelected());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        toggleButton.setSelected(setting.getValue());
    }
}
