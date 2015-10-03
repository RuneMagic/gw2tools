package com.runemagic.gw2tools.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.api.character.GW2Account;
import com.runemagic.gw2tools.api.character.GW2Character;

public class MainUIController
{
	@FXML private TextField txtAPIKey;
	@FXML private TableView tblCharacters;
	@FXML private ProgressBar pbUpdateProgress;

	@FXML private TableColumn<GW2Character, String> colCharName;
	@FXML private TableColumn<GW2Character, String> colCharProf;
	@FXML private TableColumn<GW2Character, String> colCharRace;
	@FXML private TableColumn<GW2Character, String> colCharLevel;
	@FXML private TableColumn<GW2Character, String> colCharGender;
	@FXML private TableColumn<GW2Character, String> colCharGuild;
	@FXML private TableColumn<GW2Character, String> colCharCreated;
	@FXML private TableColumn<GW2Character, String> colCharAge;
	@FXML private TableColumn<GW2Character, String> colCharDeaths;


	private ObjectProperty<GW2Account> acc=new SimpleObjectProperty<>();

	public void initialize()
	{
		initCharacterTableColumns();


		acc.addListener((obs, oldVal, newVal)->{
			pbUpdateProgress.progressProperty().unbind();
			pbUpdateProgress.progressProperty().bind(newVal.updateProgressProperty());

			tblCharacters.setItems(newVal.getCharacters());
		});
	}

	private void initCharacterTableColumns()
	{
		colCharName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colCharProf.setCellValueFactory(new PropertyValueFactory<>("profession"));
		colCharRace.setCellValueFactory(new PropertyValueFactory<>("race"));
		colCharLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
		colCharGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colCharGuild.setCellValueFactory(new PropertyValueFactory<>("guild"));
		colCharCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
		colCharAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		colCharDeaths.setCellValueFactory(new PropertyValueFactory<>("deaths"));
	}

	@FXML protected void applyAPIKey(ActionEvent event)
	{
		String key=txtAPIKey.getText();
		//TODO validation (esp. against injection)
		GW2Account acct=GW2API.inst().getAccount(key);
		acc.set(acct);
		acct.update();
	}

}
