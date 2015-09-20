package de.pat.util.javafx;


import com.runemagic.gw2tools.reference.Reference;
import javafx.beans.property.BooleanProperty;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class ViewBoundsSerializer
{

    private static final String WINDOW_POSITION_X = "Window_Position_X";
    private static final String WINDOW_POSITION_Y = "Window_Position_Y";
    private static final String WINDOW_WIDTH = "Window_Width";
    private static final String WINDOW_HEIGHT = "Window_Height";
    private static final String WINDOW_MAXIMISED = "Window_Maximimised";

    private static final double DEFAULT_X = 10;
    private static final double DEFAULT_Y = 10;
    private static final double DEFAULT_WIDTH = 800;
    private static final double DEFAULT_HEIGHT = 600;

    public void switchView(Stage stage, String node, boolean setSize)
    {
        init(stage, node, null, setSize);
    }

    public void switchView(Stage stage, String node, BooleanProperty maximiseProperty, boolean setSize)
    {
        init(stage, node, maximiseProperty, setSize);
    }

    private void init(Stage stage, String node, BooleanProperty maximiseProperty, boolean setSize)
    {
        try
        {

            stage.showingProperty().addListener((observable, oldValue, newValue) -> {

                if (!newValue && stage.isIconified()) return;

                Preferences preferences = Preferences.userRoot().node(Reference.REGISTRY_WINDOW_BOUNDS + node);
                boolean isMaximised = false;
                if (maximiseProperty == null)
                {
                    preferences.putBoolean(WINDOW_MAXIMISED, stage.isMaximized());
                    isMaximised = stage.isMaximized();
                } else
                {
                    preferences.putBoolean(WINDOW_MAXIMISED, maximiseProperty.get());
                    isMaximised = maximiseProperty.get();
                }

                if (!isMaximised)
                {
                    preferences.putDouble(WINDOW_POSITION_X, stage.getX());
                    preferences.putDouble(WINDOW_POSITION_Y, stage.getY());
                    preferences.putDouble(WINDOW_WIDTH, stage.getWidth());
                    preferences.putDouble(WINDOW_HEIGHT, stage.getHeight());
                    preferences.putDouble(WINDOW_POSITION_X, stage.getX());
                }

            });

            if (setSize)
            {
                Preferences pref = Preferences.userRoot().node(Reference.REGISTRY_WINDOW_BOUNDS + node);
                double x = pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
                double y = pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
                double width = pref.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH);
                double height = pref.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT);
                stage.setX(x);
                stage.setY(y);
                stage.setWidth(width);
                stage.setHeight(height);

                if (maximiseProperty == null) stage.setMaximized(pref.getBoolean(WINDOW_MAXIMISED, false));
                else
                {
                    stage.setOnShown((e) -> maximiseProperty.set(pref.getBoolean(WINDOW_MAXIMISED, false)));
                }

            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
