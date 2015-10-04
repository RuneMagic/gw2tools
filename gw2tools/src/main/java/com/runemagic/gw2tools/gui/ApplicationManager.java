package com.runemagic.gw2tools.gui;


import com.faelar.util.javafx.FontIcon;
import com.faelar.util.javafx.Icons;
import com.runemagic.gw2tools.gui.components.NavigationList;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ApplicationManager
{

    private BorderPane contentPane;
    private BorderPane overlayPane;
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

        overlayPane = new BorderPane();
        overlayPane.setPickOnBounds(false);

        rootPane = new StackPane();

        navList = new NavigationList();
        navList.setPadding(globalInset);
        navList.setMaxWidth(125);

        toolBar = new HBox();
        BorderPane.setMargin(toolBar, new Insets(25, 0, 5, 0));
        toolBar.setPadding(globalInset);
    }

    public void setOverlay(String name, Parent content)
    {
        setOverlay(name, content, null, null);
    }

    public void setOverlay(String name, Parent content, Label icon)
    {
        setOverlay(name, content, icon, null);
    }

    public void setOverlay(String name, Parent content, Label icon, BooleanProperty showingProperty)
    {
        BorderPane con = new BorderPane();
        BorderPane background = new BorderPane(content);
        BorderPane.setMargin(background, new Insets(50));

        background.setStyle("-fx-background-color: rgba(41, 41, 41, 1); -fx-border-color: #575757;");

        contentPane.setEffect(new BoxBlur());

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_RIGHT);

        Button close = Icons.createIconButton(FontIcon.TIMES, 20);

        HBox spacer = new HBox();
        spacer.setMinHeight(0);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text title = new Text(name);
        HBox.setMargin(title, new Insets(0, 0, 0, 5));
        title.setFill(Color.web("#C7C7C7"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 15));

        close.setOnAction((e) -> {
            hideOverlay();
            if (showingProperty != null) showingProperty.set(false);
        });

        if(icon != null)
        {
            HBox.setMargin(icon, new Insets(0, 0, 0, 15));
            controls.getChildren().add(icon);
        }
        controls.getChildren().addAll(title, spacer, close);
        background.setTop(controls);
        con.setCenter(background);
        con.setPickOnBounds(true);
        overlayPane.setCenter(con);
        if(showingProperty != null)showingProperty.set(true);
    }

    public void hideOverlay()
    {
        overlayPane.setCenter(null);
        contentPane.setEffect(null);
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
        rootPane.getChildren().addAll(contentPane, overlayPane);
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
