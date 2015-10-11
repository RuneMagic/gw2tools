package com.runemagic.gw2tools.api.character;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APIFieldType;
import com.runemagic.gw2tools.api.GW2APISource;

public class SpecializationInfo extends AbstractAPIObject
{
	private final static String API_RESOURCE_SPECIALIZATIONS="specializations";

	private IntegerProperty id=new SimpleIntegerProperty();

	@GW2APIField(name="name")
	private StringProperty name=new SimpleStringProperty("");

	@GW2APIField(name = "profession", sourceType = GW2APIFieldType.STRING, targetType = CharacterProfession.class, factory = "byName")
	private ObjectProperty<CharacterProfession> profession=new SimpleObjectProperty<>();

	@GW2APIField(name = "elite")
	private BooleanProperty elite=new SimpleBooleanProperty();

	//TODO spec icon and background

	@GW2APIField(name = "minor_traits", sourceType = GW2APIFieldType.ARRAY, itemType = GW2APIFieldType.NUMBER, factory = "getTrait")
	private ListProperty<CharacterTrait> minorTraits=new SimpleListProperty<>(FXCollections.observableArrayList());

	@GW2APIField(name = "major_traits", sourceType = GW2APIFieldType.ARRAY, itemType = GW2APIFieldType.NUMBER, factory = "getTrait")
	private ListProperty<CharacterTrait> majorTraits=new SimpleListProperty<>(FXCollections.observableArrayList());

	public SpecializationInfo(GW2APISource source, int id)
	{
		super(source);
		this.id.set(id);
	}

	@Override
	protected void initResources()
	{
		addAPIv2Resource(() -> API_RESOURCE_SPECIALIZATIONS + "/" + id.get(), null);
	}

	public int getId()
	{
		return id.get();
	}

	public ReadOnlyIntegerProperty idProperty()
	{
		return id;
	}

	public String getName()
	{
		return name.get();
	}

	public StringProperty nameProperty()
	{
		return name;
	}

	public CharacterProfession getProfession()
	{
		return profession.get();
	}

	public ObjectProperty<CharacterProfession> professionProperty()
	{
		return profession;
	}

	public boolean getElite()
	{
		return elite.get();
	}

	public BooleanProperty eliteProperty()
	{
		return elite;
	}

	public ObservableList<CharacterTrait> getMinorTraits()
	{
		return minorTraits.get();
	}

	public ListProperty<CharacterTrait> minorTraitsProperty()
	{
		return minorTraits;
	}

	public ObservableList<CharacterTrait> getMajorTraits()
	{
		return majorTraits.get();
	}

	public ListProperty<CharacterTrait> majorTraitsProperty()
	{
		return majorTraits;
	}
}
