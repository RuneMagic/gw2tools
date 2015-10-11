package com.runemagic.gw2tools.gui.controller;

import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.APIKey;
import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.api.GW2APIException;

public class LoginController
{

    @FXML
    private Button btnSkip;

    @FXML
    private Button btnApply;

    @FXML
    private TextField txtAPIKey;

    @FXML
    private BorderPane overlayPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private StackPane contentPane;

    @FXML
    private CheckBox chkbxRememberKey;

    public void initialize()
    {
        txtAPIKey.textProperty().addListener((obs, ov, nv) -> {
            ObservableList<String> styleClass = txtAPIKey.getStyleClass();
            if (!APIKey.isValid(nv))
            {
                styleClass.removeAll("field-ok");
                if (!styleClass.contains("field-error")) styleClass.add("field-error");
            }
            else
            {
                styleClass.removeAll("field-error");
                if (!styleClass.contains("field-ok")) styleClass.add("field-ok");
            }
        });

        String apiKey = GW2Tools.inst().getAppSettings().apiKey.getValue();
        if(apiKey != null && apiKey.length() > 0)
        {
            txtAPIKey.setText(apiKey);
            chkbxRememberKey.setSelected(true);
        }

        GW2Tools.inst().getAssets().updateGW2Assets(this);
    }

    public void setSimpleOverlay(Parent content)
    {
        BorderPane con = new BorderPane();
        BorderPane background = new BorderPane(content);
        BorderPane.setMargin(background, new Insets(50));

        background.setStyle("-fx-background-color: rgba(41, 41, 41, 0);");
        contentPane.setEffect(new BoxBlur());

        con.setCenter(background);
        con.setPickOnBounds(true);
        overlayPane.setCenter(con);
    }

    public void hideOverlay()
    {
        overlayPane.setCenter(null);
        contentPane.setEffect(null);
    }

    @FXML
    void onSkip(ActionEvent event)
    {
        showApplication();
    }

    @FXML
    void onApply(ActionEvent event)
    {
        APIKey apiKey;
        try
        {
            apiKey = APIKey.of(txtAPIKey.getText());
        }
        catch (GW2APIException e)
        {
            //TODO feedback
            return;
        }

        GW2Tools.inst().getAppSettings().apiKey.setValue(apiKey.getKey());
        if(!chkbxRememberKey.isSelected())
        {
            GW2Tools.inst().getAppSettings().apiKey.setValue(null);
        }
        GW2Tools.inst().getSettingsManager().saveFXSettingsSheet(GW2Tools.inst().getAppSettings(), Arrays.asList("apiKey"));//TODO hacked saved fix!
        GW2Tools.inst().getAppSettings().apiKey.setValue(apiKey.getKey());
        GW2Tools.inst().setAccount(GW2API.inst().getAccount(apiKey));
        showApplication();
    }

    private void showApplication()
    {
        GW2Tools.inst().getApplicationPane().setCenter(GW2Tools.inst().getApp().getRootPane());
    }

}
