package de.pat.fxsettings.modules;


import de.pat.fxsettings.moduletypes.AbstractFXSettingsModule;
import de.pat.fxsettings.sheet.FXSubmitResponse;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class TextAreaModule extends AbstractFXSettingsModule<String>
{

    private TextArea textField;

    public TextAreaModule(String fieldName, Property<String> setting)
    {
        super(fieldName, setting);
    }

    @Override
    public void buildNode(GridPane content)
    {
        textField = new TextArea(setting.getValue());
        textField.setMinHeight(100);
        textField.setMaxWidth(Double.MAX_VALUE);
        setting.addListener((e, oldValue, newVal) -> {
            textField.setText(newVal);
        });
        Label fieldLabel = new Label(getFieldName());
        content.getChildren().addAll(fieldLabel, textField);
        GridPane.setConstraints(fieldLabel, 0, 0);
        GridPane.setConstraints(textField, 1, 0);

        hasChanges = Bindings.createBooleanBinding(() -> !Objects.equals(textField.textProperty().get(), setting.getValue()), textField.textProperty(), setting);
    }

    @Override
    public FXSubmitResponse onSubmit()
    {
        setting.setValue(textField.getText());
        return new FXSubmitResponse(true);
    }

    @Override
    public void onCancel()
    {
        textField.setText(setting.getValue());
    }

}
