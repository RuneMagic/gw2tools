package de.pat.fxsettings;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class FXSettingsSheetPane extends BorderPane
{
    private String name;
    private Image image;


    public FXSettingsSheetPane(String name, Image image)
    {
        super();
        this.name = name;
        this.image = image;
        setPadding(new Insets(10));
    }

    public Image getImage()
    {
        return image;
    }

    public String getName()
    {
        return name;
    }
}
