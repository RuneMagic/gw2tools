package com.faelar.util.javafx;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public final class Icons
{


	private static final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
	private static final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";

	private Icons()
	{

	}

	public static Button createIconButton(FontIcon icon, String text, EventHandler<ActionEvent> action)
	{
		return JFXTools.createButton(text, Icons.createIconLabel(icon), action);
	}

	public static Button createIconButton(FontIcon icon, String text, EventHandler<ActionEvent> action, ObservableValue<? extends Boolean> disableCondition)
	{
		return JFXTools.createButton(text, Icons.createIconLabel(icon), action, disableCondition);
	}

	public static Button createIconButton(FontIcon icon, EventHandler<ActionEvent> action)
	{
		return JFXTools.createButton(Icons.createIconLabel(icon), action);
	}

	public static Button createIconButton(FontIcon icon, EventHandler<ActionEvent> action, ObservableValue<? extends Boolean> disableCondition)
	{
		return JFXTools.createButton(Icons.createIconLabel(icon), action, disableCondition);
	}

	public static Button createIconButton(FontIcon icon)
	{
		return createIconButton(icon, "", 12);
	}

	public static Button createIconButton(FontIcon icon, String text)
	{
		return createIconButton(icon, text, 12);
	}

	public static Button createIconButton(FontIcon icon, int iconSize)
	{
		return createIconButton(icon, "", iconSize);
	}

	public static Button createIconButton(FontIcon icon, String text, int iconSize)
	{
		Button btn = new Button(text, createIconLabel(icon, iconSize));

		btn.setStyle(STYLE_NORMAL);
		btn.setOnMousePressed(event -> btn.setStyle(STYLE_PRESSED));
		btn.setOnMouseReleased(event -> btn.setStyle(STYLE_NORMAL));

		return btn;
	}

	public static Label createIconLabel(FontIcon icon)
	{
		return createIconLabel(icon, 12);
	}

	public static Label createIconLabel(FontIcon icon, int iconSize)
	{
		return createIconLabel(icon, "-fx-font-size: " + iconSize + "px;");
	}

	public static Label createIconLabel(FontIcon icon, String style)
	{
		Label lbl=new Label(icon.getCharAsString());
		lbl.getStyleClass().add("font-icon");
		lbl.setStyle(style);
		return lbl;
	}

}
