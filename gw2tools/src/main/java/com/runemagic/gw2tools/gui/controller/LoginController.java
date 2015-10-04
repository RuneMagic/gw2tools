package com.runemagic.gw2tools.gui.controller;

import com.runemagic.gw2tools.GW2Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.Arrays;

public class LoginController
{

    @FXML
    private Button btnSkip;

    @FXML
    private Button btnApply;

    @FXML
    private TextField txtAPIKey;

    @FXML
    private CheckBox chkbxRememberKey;


    public void initialize()
    {

        String apiKey = GW2Tools.inst().getAppSettings().apiKey.getValue();
        if(apiKey != null && apiKey.length() > 0)
        {
            txtAPIKey.setText(apiKey);
            chkbxRememberKey.setSelected(true);
        }
    }

    @FXML
    void onSkip(ActionEvent event)
    {
        showApplication();
    }

    @FXML
    void onApply(ActionEvent event)
    {
        String apiKey = txtAPIKey.getText();
        if(apiKey == null || apiKey.length() == 0)//TODO proper validation
        {
            //TODO error
        }else
        {
            GW2Tools.inst().getAppSettings().apiKey.setValue(apiKey);
            if(!chkbxRememberKey.isSelected())
            {
                GW2Tools.inst().getAppSettings().apiKey.setValue(null);
            }
            GW2Tools.inst().getSettingsManager().saveFXSettingsSheet(GW2Tools.inst().getAppSettings(), Arrays.asList("apiKey"));//TODO hacked saved fix!
            GW2Tools.inst().getAppSettings().apiKey.setValue(apiKey);

            showApplication();
        }

    }

    private void showApplication()
    {
        GW2Tools.inst().getPrimaryStage().getScene().setRoot(GW2Tools.inst().getApp().getRootPane());
    }

}
