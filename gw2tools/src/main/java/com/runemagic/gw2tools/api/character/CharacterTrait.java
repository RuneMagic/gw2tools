package com.runemagic.gw2tools.api.character;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APIFieldType;
import com.runemagic.gw2tools.api.GW2APISource;

public class CharacterTrait extends AbstractAPIObject
{
	private final static String API_RESOURCE_TRAITS="traits";

	private IntegerProperty id=new SimpleIntegerProperty();
	//TODO everything else

	@GW2APIField(name="name")
	private StringProperty name=new SimpleStringProperty("");

	//TODO trait icon
	//private StringProperty icon=new SimpleStringProperty();

	@GW2APIField(name="specialization", sourceType = GW2APIFieldType.NUMBER, factory = "getSpecialization")
	private ObjectProperty<SpecializationInfo> specialization=new SimpleObjectProperty<>();

	@GW2APIField(name="description")
	private StringProperty description =new SimpleStringProperty();

	@GW2APIField(name="tier")
	private IntegerProperty tier=new SimpleIntegerProperty();

	@GW2APIField(name="slot")
	private StringProperty slot=new SimpleStringProperty();
	private BooleanProperty major=new SimpleBooleanProperty();

	//TODO facts and skills

	public CharacterTrait(GW2APISource source, int id)
	{
		super(source);
		this.id.set(id);
		major.bind(slot.isEqualTo("Major"));
	}

	@Override
	protected void initResources()
	{
		addAPIv2Resource(() -> API_RESOURCE_TRAITS + "/" + id.get(), null);
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

	public ReadOnlyStringProperty nameProperty()
	{
		return name;
	}

	public SpecializationInfo getSpecialization()
	{
		return specialization.get();
	}

	public ReadOnlyObjectProperty<SpecializationInfo> specializationProperty()
	{
		return specialization;
	}

	public String getDescription()
	{
		return description.get();
	}

	public ReadOnlyStringProperty descriptionProperty()
	{
		return description;
	}

	public int getTier()
	{
		return tier.get();
	}

	public ReadOnlyIntegerProperty tierProperty()
	{
		return tier;
	}

	public boolean isMajor()
	{
		return major.get();
	}

	public ReadOnlyBooleanProperty majorProperty()
	{
		return major;
	}
}
