package de.pat.fxsettings;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;


public class FXCombinedSheetsPane extends BorderPane
{

    private ListView<FXSettingsSheetPane> navList = new ListView<>();
    private ObservableList<FXSettingsSheetPane> sheets = FXCollections.observableArrayList();
    private ScrollPane scrollPane = new ScrollPane();

    public FXCombinedSheetsPane()
    {
        super();
        initComponent();
    }

    public void addSettingsSheet(FXSettingsSheetPane sheet)
    {
        sheets.add(sheet);
        if (sheets.size() > 0) navList.getSelectionModel().select(0);
    }

    public void addAllSettingsSheets(FXSettingsSheetPane... panes)
    {
        sheets.addAll(panes);
        if (sheets.size() > 0) navList.getSelectionModel().select(0);
    }

    public void removeSettingsSheet(FXSettingsSheetPane sheet)
    {
        sheets.remove(sheet);
        if (sheets.size() > 0) navList.getSelectionModel().select(0);
    }

    private void initComponent()
    {

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        setMargin(navList, new Insets(10, 5, 10, 10));
        setMargin(scrollPane, new Insets(10, 10, 10, 5));

        navList.setMaxWidth(150);
        navList.setItems(sheets);

        navList.getSelectionModel().selectedItemProperty().addListener((e, oldValue, newValue) -> {
            if (newValue != null)
            {
                scrollPane.setContent(newValue);
            }
        });
        navList.setCellFactory(new Callback<ListView<FXSettingsSheetPane>, ListCell<FXSettingsSheetPane>>()
        {

            @Override
            public ListCell<FXSettingsSheetPane> call(ListView<FXSettingsSheetPane> p)
            {
                return new ListCell<FXSettingsSheetPane>()
                {
                    @Override
                    protected void updateItem(FXSettingsSheetPane t, boolean bln)
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
                                    ImageView image = new ImageView(t.getImage());
                                    content.getChildren().add(image);
                                }
                                content.getChildren().add(text);
                                setGraphic(content);
                            }
                        }
                    }

                };
            }
        });
        setLeft(navList);
        setCenter(scrollPane);

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_RIGHT);


        HBox spacer = new HBox();
        spacer.setMinHeight(0);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text title = new Text("Settings");
        HBox.setMargin(title, new Insets(0, 0, 0, 5));
        title.setFill(Color.web("#C7C7C7"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 15));


        controls.getChildren().addAll(title, spacer);
        setTop(controls);
        setStyle("-fx-background-color: rgba(41, 41, 41, 1)");
    }

}
