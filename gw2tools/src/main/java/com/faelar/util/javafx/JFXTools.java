package com.faelar.util.javafx;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public final class JFXTools
{
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

	private JFXTools() {}

	public static void showAndWait(Stage owner, Scene scene)
	{
		showAndWait(owner, scene, null);
	}

	public static void showAndWait(Scene scene)
	{
		showAndWait(null, scene, null);
	}

	public static void showAndWait(Scene scene, String title)
	{
		showAndWait(null, scene, title);
	}

	public static void showAndWait(Stage owner, Scene scene, String title)
	{
		Stage stage=new Stage();
		if (title!=null) stage.setTitle(title);
		if (owner!=null) stage.initOwner(owner);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public static void show(Stage owner, Scene scene)
	{
		showAndWait(owner, scene, null);
	}

	public static void show(Scene scene)
	{
		showAndWait(null, scene, null);
	}

	public static void show(Scene scene, String title)
	{
		showAndWait(null, scene, title);
	}

	public static void show(Stage owner, Scene scene, String title)
	{
		Stage stage=new Stage();
		if (title!=null) stage.setTitle(title);
		if (owner!=null) stage.initOwner(owner);
		stage.setScene(scene);
		stage.show();
	}

	@SuppressWarnings("unchecked")
	public static <T> void enableInternalDragAndDrop(TableView<T> tableView)//TODO multi-item drag and drop
	{
		Callback<TableView<T>, TableRow<T>> oldFactory=tableView.getRowFactory();
		tableView.setRowFactory(tv -> {
			TableRow<T> row = oldFactory!=null?oldFactory.call(tv):new TableRow<T>();
			row.setOnDragDetected(event -> {
				if (!row.isEmpty()) {
					Integer index = row.getIndex();
					Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
					db.setDragView(row.snapshot(null, null));
					ClipboardContent cc = new ClipboardContent();
					cc.put(SERIALIZED_MIME_TYPE, index);
					db.setContent(cc);
					event.consume();
				}
			});
			//TODO show drop target location
			row.setOnDragOver(event -> {
				Dragboard db = event.getDragboard();
				Object gestureSource=event.getGestureSource();
				if (!(gestureSource instanceof TableRow)) return;
				if (!tableView.equals(((TableRow<T>)gestureSource).getTableView())) return;
				if (db.hasContent(SERIALIZED_MIME_TYPE))
				{
					if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue())
					{
						event.acceptTransferModes(TransferMode.MOVE);
						event.consume();
					}

				}
			});

			row.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				Object gestureSource=event.getGestureSource();
				if (!(gestureSource instanceof TableRow)) return;
				if (!tableView.equals(((TableRow<T>)gestureSource).getTableView())) return;
				if (db.hasContent(SERIALIZED_MIME_TYPE))
				{
					int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
					T draggedItem = tableView.getItems().remove(draggedIndex);

					int dropIndex;

					if (row.isEmpty()) dropIndex = tableView.getItems().size();
					else dropIndex = row.getIndex();

					tableView.getItems().add(dropIndex, draggedItem);

					event.setDropCompleted(true);
					tableView.getSelectionModel().select(dropIndex);
					event.consume();
				}
			});

			return row ;
		});
	}

	public static Button createButton(Node graphic, EventHandler<ActionEvent> action)
	{
		return createButton(null, graphic, action, null);
	}

	public static Button createButton(Node graphic, EventHandler<ActionEvent> action, ObservableValue<? extends Boolean> disableCondition)
	{
		return createButton(null, graphic, action, disableCondition);
	}

	public static Button createButton(String text, EventHandler<ActionEvent> action)
	{
		return createButton(text, null, action, null);
	}

	public static Button createButton(String text, Node graphic, EventHandler<ActionEvent> action)
	{
		return createButton(text, graphic, action, null);
	}

	public static Button createButton(String text, EventHandler<ActionEvent> action, ObservableValue<? extends Boolean> disableCondition)
	{
		return createButton(text, null, action, disableCondition);
	}

	public static Button createButton(String text, Node graphic, EventHandler<ActionEvent> action, ObservableValue<? extends Boolean> disableCondition)
	{
		Button ret=new Button();
		if (text!=null) ret.setText(text);
		if (action!=null) ret.setOnAction(action);
		if (graphic!=null) ret.setGraphic(graphic);
		if (disableCondition!=null) ret.disableProperty().bind(disableCondition);
		return ret;
	}

	public static Node withLabel(String label, Node node)
	{
		HBox ret=new HBox();
		ret.getChildren().addAll(new Label(label), node);
		return ret;
	}
}
