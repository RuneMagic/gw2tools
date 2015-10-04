package com.runemagic.gw2tools.gui;


import com.runemagic.gw2tools.gui.components.NavigationList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ApplicationManager
{

    private BorderPane contentPane;
    private StackPane rootPane;
    private NavigationList navList;
    private HBox toolBar;

    public ApplicationManager()
    {
        initComponent();
        initLayout();
        initBehavior();
    }


    private void initComponent()
    {
        Insets globalInset = new Insets(5);

        contentPane = new BorderPane();
        contentPane.setPadding(globalInset);

        rootPane = new StackPane();

        navList = new NavigationList();
        navList.setPadding(globalInset);
        navList.setMaxWidth(125);

        toolBar = new HBox();
        BorderPane.setMargin(toolBar, new Insets(0,0,5,0));
//        toolBar.setStyle("-fx-border-color: grey");
        toolBar.setPadding(globalInset);
    }

    private void initLayout()
    {


        /**
         * Contentpane setup
         */
        contentPane.setLeft(navList);
        contentPane.setTop(toolBar);


        /**
         * RootPane setup
         */
        rootPane.getChildren().add(contentPane);
    }

    public void initBehavior()
    {
        navList.getSelectionModel().selectedItemProperty().addListener((e, oldValue, newValue) -> {
            if (newValue != null)
            {
                contentPane.setCenter(newValue.getView());
            }
        });
    }

    public void addView(String name, Parent view)
    {
        addView(name, view, null, false);
    }

    public void addView(String name, Parent view, boolean selected)
    {
        addView(name, view, null, selected);
    }

    public void addView(String name, Parent view, Label image, boolean selected)
    {
        ViewContainer cont = new ViewContainer(name, view, image);
        navList.getItems().add(cont);
        if(selected)
        {
            navList.getSelectionModel().select(cont);
        }
    }

    public StackPane getRootPane()
    {
        return rootPane;
    }

    public HBox getToolBar()
    {
        return toolBar;
    }
}
