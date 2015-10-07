package com.runemagic.gw2tools.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CommerceController
{
	@FXML
	private TextField txtProcessedPrice;

	@FXML
	private CheckBox chkRawBuy;

	@FXML
	private ComboBox<?> cbOperation;

	@FXML
	private ComboBox<?> cbSelectMaterial;

	@FXML
	private TextField txtProcessingValue;

	@FXML
	private TextField txtResult;

	@FXML
	private TextField txtRawPrice;

	@FXML
	private CheckBox chkProcessedBuy;


	public void initialize()
	{

	}

	@FXML
	void calculateResult(ActionEvent event)
	{


	}
}
