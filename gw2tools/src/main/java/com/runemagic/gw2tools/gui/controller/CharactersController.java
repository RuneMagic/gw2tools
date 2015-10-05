package com.runemagic.gw2tools.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.character.GW2Character;

public class CharactersController
{
	@FXML private TableView<GW2Character> tblCharacters;

	@FXML private TableColumn<GW2Character, String> colCharName;
	@FXML private TableColumn<GW2Character, String> colCharProf;
	@FXML private TableColumn<GW2Character, String> colCharRace;
	@FXML private TableColumn<GW2Character, String> colCharLevel;
	@FXML private TableColumn<GW2Character, String> colCharGender;
	@FXML private TableColumn<GW2Character, String> colCharGuild;
	@FXML private TableColumn<GW2Character, String> colCharCreated;
	@FXML private TableColumn<GW2Character, String> colCharAge;
	@FXML private TableColumn<GW2Character, String> colCharDeaths;

	public void initialize()
	{
		initCharacterTableColumns();

		GW2Tools.inst().accountProperty().addListener((obs, oldVal, newVal) -> {
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
		colCharAge.setCellValueFactory(new PropertyValueFactory<>("formattedAge"));
		colCharDeaths.setCellValueFactory(new PropertyValueFactory<>("deaths"));
	}
}
