package com.runemagic.gw2tools.gui.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.account.GW2APIPermission;
import com.runemagic.gw2tools.api.account.Guild;
import com.runemagic.gw2tools.api.account.TokenInfo;

public class AccountController
{

	@FXML
	private ListView<GW2APIPermission> lstTokenPermissions;

	@FXML
	private TextField txtAccountWorld;

	@FXML
	private TextField txtTokenName;

	@FXML
	private TextField txtAccountName;

	@FXML
	private TextField txtAccountID;

	@FXML
	private TextField txtTokenID;

	@FXML
	private ListView<Guild> lstAccountGuilds;

	@FXML
	private TextField txtAccountCreated;

	public void initialize()
	{
		lstTokenPermissions.getItems().addAll(GW2APIPermission.values());
		lstAccountGuilds.setCellFactory((guild) -> new ListCell<Guild>(){
			@Override
			protected void updateItem(Guild item, boolean empty)
			{
				super.updateItem(item, empty);
				textProperty().unbind();
				if (item != null)
				{
					textProperty().bind(Bindings.concat(item.nameProperty(), " [", item.tagProperty(), "]"));
				}
			}

		});

		GW2Tools.inst().accountProperty().addListener((obs, ov, nv) -> {
			txtAccountWorld.textProperty().unbind();
			txtAccountName.textProperty().unbind();
			txtAccountID.textProperty().unbind();
			if (nv != null)
			{
				txtAccountWorld.textProperty().bind(Bindings.selectString(nv.worldProperty(), "name"));
				txtAccountName.textProperty().bind(nv.nameProperty());
				txtAccountID.textProperty().bind(nv.idProperty());
				txtAccountCreated.textProperty().bind(StringBinding.stringExpression(nv.createdProperty()));
				lstAccountGuilds.setItems(nv.getGuilds());

				TokenInfo token=nv.getAPIKeyInfo();
				txtTokenID.textProperty().bind(token.idProperty());
				txtTokenName.textProperty().bind(token.nameProperty());
				lstTokenPermissions.setCellFactory(list -> {
					CheckBoxListCell<GW2APIPermission> cell = new CheckBoxListCell<>((perm) -> nv.getAPIKeyInfo().permissionProperty(perm));
					cell.setDisable(true);//TODO find a non-quick&dirty solution
					cell.setOpacity(1);
					return cell;
				});
			}
			else
			{
				lstAccountGuilds.setItems(null);
				lstTokenPermissions.setCellFactory(null);
			}



		});
	}

}
