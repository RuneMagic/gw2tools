package com.runemagic.gw2tools.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.account.GW2APIPermission;

public class AccountController
{
	@FXML private ListView<GW2APIPermission> lstPermissions;

	public void initialize()
	{
		lstPermissions.getItems().addAll(GW2APIPermission.values());

		GW2Tools.inst().accountProperty().addListener((obs, oldVal, newVal) -> {
			lstPermissions.setCellFactory(list -> {
				CheckBoxListCell<GW2APIPermission> cell = new CheckBoxListCell<>((perm) -> newVal.getAPIKeyInfo().permissionProperty(perm));
				cell.setDisable(true);//TODO find a non-quick&dirty solution
				return cell;
			});
		});
	}

}
