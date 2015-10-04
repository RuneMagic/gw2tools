package de.pat.fxsettings.moduletypes;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public abstract class AbstractFXSettingsModule<T> implements FXSettingsModule
{

    protected String fieldName;
    protected Property<T> setting;
    protected ObservableList<T> settings;
    protected ObservableList<T> values;
    protected BooleanBinding hasChanges;
    private GridPane content;

    public AbstractFXSettingsModule(String fieldName, Property<T> setting)
    {
        this.fieldName = fieldName;
        this.content = new GridPane();
        this.setting = setting;
        initContent();
    }

    public AbstractFXSettingsModule(String fieldName, Property<T> setting, ObservableList<T> values)
    {
        this.fieldName = fieldName;
        this.content = new GridPane();
        this.setting = setting;
        this.values = values;
        initContent();
    }

    public AbstractFXSettingsModule(String fieldName, ObservableList setting, ObservableList<T> values)
    {
        this.fieldName = fieldName;
        this.content = new GridPane();
        this.settings = setting;
        this.values = values;
        initContent();
    }

    private void initContent()
    {
        GridPane.setMargin(content, new Insets(10));
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);
        ColumnConstraints column1 = new ColumnConstraints(100, 150, 150);
        ColumnConstraints column2 = new ColumnConstraints(150, 150, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true);
        content.getColumnConstraints().addAll(column1, column2);
    }

    @Override
    public Node getContent()
    {
        return content;
    }

    @Override
    public String getFieldName()
    {
        return fieldName;
    }

    @Override
    public BooleanBinding hasChangesProperty()
    {
        return hasChanges;
    }

    public abstract void buildNode(GridPane content);

    @Override
    public void buildFXNode()
    {
        this.buildNode(content);
    }

}
