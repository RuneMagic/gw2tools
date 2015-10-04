package de.pat.fxsettings;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class FXSettingsSheetPane extends BorderPane
{
    private String name;
    private Label image;


    public FXSettingsSheetPane(String name, Label image)
    {
        super();
        this.name = name;
        this.image = image;
        setPadding(new Insets(10));
    }

    public Label getImage()
    {
        return image;
    }

    public String getName()
    {
        return name;
    }
}
