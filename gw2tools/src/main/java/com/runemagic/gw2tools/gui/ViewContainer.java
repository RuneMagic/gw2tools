package com.runemagic.gw2tools.gui;

import javafx.scene.Parent;
import javafx.scene.control.Label;

public class ViewContainer
{

    private String name;
    private Parent view;
    private Label image;

    public ViewContainer(String name, Parent view)
    {
        this.name = name;
        this.view = view;
    }

    public ViewContainer(String name, Parent view, Label image)
    {
        this.name = name;
        this.view = view;
        this.image = image;
    }

    public String getName()
    {
        return name;
    }


    public Parent getView()
    {
        return view;
    }


    public Label getImage()
    {
        return image;
    }
}
