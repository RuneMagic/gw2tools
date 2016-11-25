package com.runemagic.gw2tools.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.items.GW2Item;

public class InventoryController
{

	@FXML
	private TableView<GW2Item> tblInventory;

	@FXML
	private TableColumn<GW2Item, String> colStorage;

	@FXML
	private TableColumn<GW2Item, Integer> colCount;

	@FXML
	private TableColumn<GW2Item, String> colName;

	@FXML
	private TableColumn<GW2Item, ?> colIcon;


	public void initialize()
	{
		GW2Tools.inst().accountProperty().addListener((obs, ov, nv) -> {

		});
	}


}
