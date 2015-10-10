package com.runemagic.gw2tools.gui.controller;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.print.attribute.URISyntax;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AboutController
{

	@FXML
	private ImageView imgRuneMagic;

	@FXML
	private ImageView imgPatosaur;

	@FXML
	private Hyperlink linkGitHub;

	@FXML
	private Hyperlink linkVersion;

	@FXML
	private ImageView imgCodered;

	@FXML
	private Hyperlink linkDiscord;

	@FXML
	private ImageView imgFaelar;

	@FXML
	private Label lblTitle;

	@FXML void onClickVersion(ActionEvent event)
	{
		try{
			Desktop.getDesktop().browse(new URI("http://github.com/RuneMagic/gw2tools/releases"));
		} catch (URISyntaxException | IOException ex) {
			//There's a problem with opening the link
		}
	}


	@FXML void onClickFaelar(MouseEvent event)
	{

	}

	@FXML void onMouseEnteredFaelar(MouseEvent event)
	{

	}

	@FXML void onMouseExitedFaelar(MouseEvent event)
	{

	}

	@FXML void onMouseClickedCodered(MouseEvent event)
	{

	}

	@FXML void onMouseEnteredCodered(MouseEvent event)
	{

	}

	@FXML void onMouseExitedCodered(MouseEvent event)
	{

	}

	@FXML void onClickPatosaur(MouseEvent event)
	{

	}

	@FXML void onMouseEnteredPatosaur(MouseEvent event)
	{

	}

	@FXML void onMouseExitedPatosaur(MouseEvent event)
	{

	}

}
