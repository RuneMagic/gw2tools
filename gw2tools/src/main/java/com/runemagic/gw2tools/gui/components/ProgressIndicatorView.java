package com.runemagic.gw2tools.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ProgressIndicatorView extends HBox
{

	private ProgressIndicator progress = new ProgressIndicator();
	private Label lblTitle = new Label();
	private Label lblText = new Label();

	public ProgressIndicatorView()
	{
		initComponent();
	}

	public ProgressIndicatorView(String title, String text)
	{
		initComponent();
		lblTitle.setText(title);
		lblText.setText(text);
	}

	private void initComponent()
	{
		setSpacing(15);
		setPrefSize(550, 130);
		setMaxSize(550, 130);
		setStyle("-fx-border-color: grey; -fx-border-width: 3; -fx-background-color: rgba(58, 58, 58, 0.9);");

		lblTitle.setFont(Font.font("System", FontWeight.BOLD, 29));
		lblText.setFont(Font.font("System", FontPosture.ITALIC, 17));
		lblText.setStyle("-fx-text-fill: grey;");


		progress.setPrefSize(115,115);

		VBox content = new VBox();
		content.setAlignment(Pos.TOP_LEFT);
		content.getChildren().addAll(lblTitle, lblText);

		getChildren().addAll(new HBox(progress), content);
	}

	public ProgressIndicator getProgress()
	{
		return progress;
	}

	public Label getLblTitle()
	{
		return lblTitle;
	}

	public Label getLblText()
	{
		return lblText;
	}
}
