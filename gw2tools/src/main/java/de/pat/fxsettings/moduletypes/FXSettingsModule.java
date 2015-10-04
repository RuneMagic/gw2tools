package de.pat.fxsettings.moduletypes;

import de.pat.fxsettings.sheet.FXSubmitResponse;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;

public interface FXSettingsModule
{

    void buildFXNode();

    String getFieldName();

    FXSubmitResponse onSubmit();

    void onCancel();

    Node getContent();

    BooleanBinding hasChangesProperty();
}
