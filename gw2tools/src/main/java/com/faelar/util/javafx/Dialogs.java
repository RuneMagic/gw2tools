package com.faelar.util.javafx;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;

public final class Dialogs
{

	private Dialogs() {}


	public static <T> boolean showDialog(Node content, String title, DialogController controller)
	{
		return showDialog(content, title, null, controller);
	}

	public static boolean showDialog(Node content, String title, String headerText, DialogController controller)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(null);
		alert.initStyle(StageStyle.UTILITY);
		alert.setGraphic(null);
		controller.setDialog(alert);
		alert.getDialogPane().setContent(content);
		Optional<ButtonType> result = alert.showAndWait();
		return (result.isPresent() && result.get()==ButtonType.OK);
	}

	public static boolean showDialog(Node content, String title)
	{
		return showDialog(content, title, (String) null);
	}

	public static boolean showDialog(Node content, String title, String headerText)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(null);
		alert.initStyle(StageStyle.UTILITY);
		alert.setGraphic(null);
		alert.getDialogPane().setContent(content);
		Optional<ButtonType> result = alert.showAndWait();
		return (result.isPresent() && result.get()==ButtonType.OK);
	}

	public static void showMessageDialog(String message, String title)
	{
		showMessageDialog(message, title, AlertType.INFORMATION);
	}

	public static void showMessageDialog(String message, String title, AlertType type)
	{
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void showExceptionDialog(Throwable ex, String message, String title)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.setContentText(null);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		alert.getDialogPane().setExpandableContent(textArea);

		alert.showAndWait();
	}

	public static void showExceptionDialog(Throwable ex)
	{
		showExceptionDialog(ex, ex.toString(), "Error");
	}
}
