package de.pat.util.javafx;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.controlsfx.control.CheckListView;

public class DialogTools
{

	private static Stage stage;

	public static void initOwner(Stage primaryStage)
	{
		stage = primaryStage;
	}

	public static void createErrorDialog(String error)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(error);
		alert.initOwner(stage);
		alert.getDialogPane().setStyle("-fx-background-color: -inner-pane-color !IMPORTANT");
		alert.showAndWait();
	}

	public static Optional<ButtonType> createConfirmDialog(String info)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(info);
		alert.initOwner(stage);
		alert.getDialogPane().setStyle("-fx-background-color: -inner-pane-color !IMPORTANT");
		return alert.showAndWait();
	}

	public static void createInfoDialog(String info)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(info);
		alert.initOwner(stage);
		alert.getDialogPane().setStyle("-fx-background-color: -inner-pane-color !IMPORTANT");
		alert.showAndWait();
	}

	public static Optional<String> createTextInputDialog(String title, String info, String info2)
	{
		TextInputDialog alert = new TextInputDialog();
		alert.setTitle(title);
		alert.setHeaderText(info);
		alert.setContentText(info2);
		alert.initOwner(stage);
		alert.getDialogPane().setStyle("-fx-background-color: -inner-pane-color !IMPORTANT");
		return alert.showAndWait();
	}

	public static <T> ObservableList<T> createSelectionDialog(String title, String info, ObservableList<T> values)
	{
		CheckListView<T> view = new CheckListView<>();
		view.setItems(values);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(info);
		alert.getDialogPane().setContent(view);
		alert.getDialogPane().setPrefWidth(500);

		alert.initOwner(stage);
		alert.getDialogPane().setStyle("-fx-background-color: -inner-pane-color !IMPORTANT");

		Optional<ButtonType> result = alert.showAndWait();
		if (result != null && result.isPresent() && result.get().equals(ButtonType.OK))
		{
			return FXCollections.observableArrayList(view.getCheckModel().getCheckedItems());
		}
		return null;
	}

	public static void createExceptionDialog(Throwable ex, String title, String info, String info2)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(info);
		alert.setContentText(info2);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Error Log:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setOpacity(0.75f);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		StackPane contentPane = new StackPane();
		contentPane.getChildren().addAll(textArea);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		contentPane.setMaxWidth(Double.MAX_VALUE);
		contentPane.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane.setVgrow(contentPane, Priority.ALWAYS);
		GridPane.setHgrow(contentPane, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(contentPane, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.initOwner(stage);
		alert.getDialogPane().setStyle("-fx-background-color: -inner-pane-color !IMPORTANT");
		alert.showAndWait();
	}

}
