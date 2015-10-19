package com.runemagic.gw2tools.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.util.GW2APIProfiler;

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

	private DropShadow dropShadow = new DropShadow(20, Color.rgb(254, 207, 57));

	public void initialize()
	{
		dropShadow.setInput(new Glow());
	}

	@FXML void onClickVersion(ActionEvent event)
	{
		GW2Tools.inst().getHostServices().showDocument("http://github.com/RuneMagic/gw2tools/releases");
	}

	@FXML void onClickFaelar(MouseEvent event)
	{
		GW2APIProfiler prof=GW2API.inst().getProfiler();//TODO "official" way to print stats and debug
		if (event.isControlDown() && prof.isEnabled())
		{
			prof.printStatistics();
		}
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
		imgCodered.setEffect(dropShadow);
	}

	@FXML void onMouseExitedCodered(MouseEvent event)
	{
		imgCodered.setEffect(null);
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
