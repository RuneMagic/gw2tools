package com.runemagic.gw2tools.gui.components;

import com.runemagic.gw2tools.gui.ViewContainer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class NavigationList extends ListView<ViewContainer>
{

    public NavigationList()
    {
        super();
        initComponent();
    }

    private void initComponent()
    {
        setCellFactory(new Callback<ListView<ViewContainer>, ListCell<ViewContainer>>()
        {

            @Override
            public ListCell<ViewContainer> call(ListView<ViewContainer> p)
            {
                return new ListCell<ViewContainer>()
                {
                    @Override
                    protected void updateItem(ViewContainer t, boolean bln)
                    {
                        super.updateItem(t, bln);
                        if (bln)
                        {
                            setText(null);
                            setGraphic(null);
                        } else
                        {
                            if (t != null)
                            {
                                VBox content = new VBox(5);
                                content.setPadding(new Insets(10, 0, 0, 0));
                                content.setAlignment(Pos.CENTER);
                                Label text = new Label(t.getName());
                                if (t.getImage() != null)
                                {
                                    content.getChildren().add(t.getImage());
                                }
                                content.getChildren().add(text);
                                setGraphic(content);
                            }
                        }
                    }

                };
            }
        });
    }



}
