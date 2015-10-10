package com.runemagic.gw2tools.gui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import com.faelar.util.javafx.FontIcon;
import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.character.GW2Character;

public class CharacterListCell<T> extends ListCell<GW2Character>
{

	private Label lblName;
	private Label lblGuild;
	private Label lblAge;
	private Label lblGender;
	private Label lblDeaths;
	private ImageView imgProfession;

	@Override
	protected void updateItem(GW2Character character, boolean empty)
	{
		super.updateItem(character, empty);

		if (empty)
		{
			setGraphic(null);
		}
		else
		{
			Label lblName = new Label();
			Label lblGuild = new Label();
			Label lblAge = new Label();
			Label lblGender = new Label();
			Label lblDeaths = new Label();
			ImageView imgProffesion = new ImageView();

			lblAge.getStyleClass().add("font-icon");
			lblGender.getStyleClass().add("font-icon");
			lblDeaths.getStyleClass().add("font-icon");

			imgProffesion.setFitHeight(95);
			imgProffesion.setFitWidth(95);

			lblName.setFont(Font.font("System", FontWeight.BOLD, 29));
			lblGuild.setFont(Font.font("System", FontPosture.ITALIC, 17));
			lblGuild.setStyle("-fx-text-fill: grey;");

			//construction
			HBox contentStats = new HBox(15);
			contentStats.getChildren().addAll(lblGender, lblDeaths, lblAge);

			VBox contentLabels = new VBox();
			VBox.setMargin(lblGuild, new Insets(-10, 0, 0, 0));
			VBox.setMargin(contentStats, new Insets(7, 0, 0, 0));
			contentLabels.getChildren().addAll(lblName, lblGuild, contentStats);

			HBox content = new HBox();
			content.setStyle("-fx-border-color: grey; -fx-border-width: 3; -fx-background-color:  rgb(33, 33, 33);");
			content.setOpaqueInsets(new Insets(10));
			content.setFillHeight(true);
			content.getChildren().addAll(imgProffesion, contentLabels);

			//Bindings
			lblName.textProperty().bind(Bindings.concat(character.nameProperty(), " (", character.levelProperty(), ")"));
			lblGuild.textProperty().bind(Bindings.when(character.guildProperty().isNotNull()).then(Bindings.concat(Bindings.selectString(character.guildProperty(), "name"), " [", Bindings.selectString(character.guildProperty(), "tag"), "]")).otherwise(new SimpleStringProperty("Not representing")));
			lblAge.textProperty().bind(Bindings.concat(FontIcon.CLOCK_O.getCharAsString(), " ", character.formattedAgeProperty()));
			lblDeaths.textProperty().bind(Bindings.concat(FontIcon.HEARTBEAT.getCharAsString(), " ", character.deathsProperty()));
			lblGender.textProperty().bind(Bindings.createObjectBinding(() -> {

				if (character.genderProperty().getValue() == null) return null;

				return character.genderProperty().getValue().getIcon().getCharAsString();
			}, character.genderProperty()));

			ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> {
				if (character.professionProperty().getValue() == null) return null;
				return GW2Tools.inst().getAssets().getAsset("icon_" + character.getProfession().getName().toLowerCase() + "_big.png");
			}, character.professionProperty());
			imgProffesion.imageProperty().bind(imageBinding);


			setGraphic(content);
		}

	}

}
