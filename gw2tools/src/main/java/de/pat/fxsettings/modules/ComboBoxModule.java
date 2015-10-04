package de.pat.fxsettings.modules;


import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Objects;

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
        Label fieldLabel = new Label(getFieldName());
        content.getChildren().addAll(fieldLabel, comboBox);
        GridPane.setConstraints(fieldLabel, 0, 0);
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
