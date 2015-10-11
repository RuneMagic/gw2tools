package de.pat.fxsettings.modules;


import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;

public class ComboBoxModule<T> extends AbstractFXSettingsModule<T>
{

    private ComboBox<T> comboBox;

    public ComboBoxModule(String fieldName, Property<T> setting, ObservableList<T> values)
    {
        super(fieldName, setting, values);
    }

    @Override
    public void buildNode(GridPane content)
    {
        comboBox = new ComboBox<>(values);
        comboBox.getSelectionModel().select(setting.getValue());
        comboBox.setMaxWidth(Double.MAX_VALUE);
        setting.addListener((e, oldValue, newVal) -> {
            comboBox.getSelectionModel().select(newVal);
        });
        content.getChildren().add(comboBox);
        GridPane.setConstraints(comboBox, 1, 0);

        if(setting.getValue() != null && !values.contains(setting.getValue()))setting.setValue(null);

        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(comboBox.valueProperty().get(), setting.getValue()), comboBox.valueProperty(), setting);
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(comboBox.getSelectionModel().getSelectedItem());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        comboBox.getSelectionModel().select(setting.getValue());
    }

}
