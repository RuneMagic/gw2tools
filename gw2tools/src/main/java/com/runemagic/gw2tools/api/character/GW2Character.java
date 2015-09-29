package com.runemagic.gw2tools.api.character;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.runemagic.gw2tools.api.APIKeyHolder;

public class GW2Character implements APIKeyHolder
{
	private StringProperty apiKey=new SimpleStringProperty();
	private StringProperty name=new SimpleStringProperty();
	private ObjectProperty<CharacterRace> race=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterGender> gender=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterProfession> profession=new SimpleObjectProperty<>();
	private IntegerProperty level=new SimpleIntegerProperty();
	private StringProperty guild=new SimpleStringProperty();//TODO guild
	private ObjectProperty<LocalDateTime> created=new SimpleObjectProperty<>();
	private LongProperty age=new SimpleLongProperty();
	private IntegerProperty deaths=new SimpleIntegerProperty();
	//TODO crafting
	private ObjectProperty<CharacterBuild> buildPVE=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterBuild> buildPVP=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterBuild> buildWVW=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterEquipment> equipment=new SimpleObjectProperty<>();
	//TODO inventory

	public GW2Character()
	{
		buildPVE.set(new CharacterBuild());
		buildPVP.set(new CharacterBuild());
		buildWVW.set(new CharacterBuild());
	}

	public String getAPIKey()
	{
		return apiKey.get();
	}

	public String getName()
	{
		return name.get();
	}

	public StringProperty nameProperty()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name.set(name);
	}

	public CharacterRace getRace()
	{
		return race.get();
	}

	public ObjectProperty<CharacterRace> raceProperty()
	{
		return race;
	}

	public void setRace(CharacterRace race)
	{
		this.race.set(race);
	}

	public CharacterGender getGender()
	{
		return gender.get();
	}

	public ObjectProperty<CharacterGender> genderProperty()
	{
		return gender;
	}

	public void setGender(CharacterGender gender)
	{
		this.gender.set(gender);
	}

	public CharacterProfession getProfession()
	{
		return profession.get();
	}

	public ObjectProperty<CharacterProfession> professionProperty()
	{
		return profession;
	}

	public void setProfession(CharacterProfession profession)
	{
		this.profession.set(profession);
	}

	public int getLevel()
	{
		return level.get();
	}

	public IntegerProperty levelProperty()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level.set(level);
	}

	public String getGuild()
	{
		return guild.get();
	}

	public StringProperty guildProperty()
	{
		return guild;
	}

	public void setGuild(String guild)
	{
		this.guild.set(guild);
	}

	public LocalDateTime getCreated()
	{
		return created.get();
	}

	public ObjectProperty<LocalDateTime> createdProperty()
	{
		return created;
	}

	public void setCreated(LocalDateTime created)
	{
		this.created.set(created);
	}

	public long getAge()
	{
		return age.get();
	}

	public LongProperty ageProperty()
	{
		return age;
	}

	public void setAge(long age)
	{
		this.age.set(age);
	}

	public int getDeaths()
	{
		return deaths.get();
	}

	public IntegerProperty deathsProperty()
	{
		return deaths;
	}

	public void setDeaths(int deaths)
	{
		this.deaths.set(deaths);
	}

	public CharacterBuild getBuildPVE()
	{
		return buildPVE.get();
	}

	public ReadOnlyObjectProperty<CharacterBuild> buildPVEProperty()
	{
		return buildPVE;
	}

	/*public void setBuildPVE(CharacterBuild buildPVE)
	{
		this.buildPVE.set(buildPVE);
	}*/

	public CharacterBuild getBuildPVP()
	{
		return buildPVP.get();
	}

	public ReadOnlyObjectProperty<CharacterBuild> buildPVPProperty()
	{
		return buildPVP;
	}

	/*public void setBuildPVP(CharacterBuild buildPVP)
	{
		this.buildPVP.set(buildPVP);
	}*/

	public CharacterBuild getBuildWVW()
	{
		return buildWVW.get();
	}

	public ReadOnlyObjectProperty<CharacterBuild> buildWVWProperty()
	{
		return buildWVW;
	}

	/*public void setBuildWVW(CharacterBuild buildWVW)
	{
		this.buildWVW.set(buildWVW);
	}*/

	public CharacterEquipment getEquipment()
	{
		return equipment.get();
	}

	public ObjectProperty<CharacterEquipment> equipmentProperty()
	{
		return equipment;
	}

	public void setEquipment(CharacterEquipment equipment)
	{
		this.equipment.set(equipment);
	}
}
