package com.runemagic.gw2tools.api.character;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runemagic.gw2tools.api.APIKeyHolder;
import com.runemagic.gw2tools.api.AbstractAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;

public class GW2Character extends AbstractAPIObject implements APIKeyHolder
{
	private final static String API_RESOURCE_CHARACTERS="characters";

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

	public GW2Character(GW2APISource source, String name)
	{
		super(source);
		buildPVE.set(new CharacterBuild());
		buildPVP.set(new CharacterBuild());
		buildWVW.set(new CharacterBuild());
	}

	@Override
	protected void updateImpl() throws GW2APIException
	{
		JSONObject json=new JSONObject(readAPIv2Resource(API_RESOURCE_CHARACTERS + "/" + name, this));
		name.set(json.getString("name"));
		race.set(CharacterRace.byName(json.getString("race")));//TODO exception handling
		gender.set(CharacterGender.byName(json.getString("gender")));
		profession.set(CharacterProfession.byName(json.getString("profession")));
		level.set(json.getInt("level"));
		guild.set(json.optString("guild", null));//TODO guild parsing
		created.set(LocalDateTime.parse(json.getString("created")));
		age.set(json.getLong("age"));
		deaths.set(json.getInt("deaths"));
		//TODO crafting
		JSONObject specs=json.optJSONObject("specializations");
		if (specs!=null)
		{
			updateBuild(getBuildPVE(), json.getJSONArray("pve"));
			updateBuild(getBuildPVP(), json.getJSONArray("pvp"));
			updateBuild(getBuildWVW(), json.getJSONArray("wvw"));
		}
	}

	private void updateBuild(CharacterBuild build, JSONArray json)
	{
		build.setSpec1(readSpecialization(json.getJSONObject(0)));
		build.setSpec2(readSpecialization(json.getJSONObject(1)));
		build.setSpec3(readSpecialization(json.getJSONObject(2)));
	}

	private CharacterSpecialization readSpecialization(JSONObject json)
	{
		CharacterSpecialization spec=new CharacterSpecialization(json.getInt("id"));
		JSONArray traitsArray=json.getJSONArray("traits");
		spec.setTrait1(readTrait(traitsArray.getInt(0)));
		spec.setTrait2(readTrait(traitsArray.getInt(1)));
		spec.setTrait3(readTrait(traitsArray.getInt(2)));
		return spec;
	}

	private CharacterTrait readTrait(int id)
	{
		return CharacterTrait.of(id);
	}

	public String getAPIKey()
	{
		return apiKey.get();
	}

	public String getName()
	{
		return name.get();
	}

	public ReadOnlyStringProperty nameProperty()
	{
		return name;
	}

	public CharacterRace getRace()
	{
		return race.get();
	}

	public ReadOnlyObjectProperty<CharacterRace> raceProperty()
	{
		return race;
	}

	public CharacterGender getGender()
	{
		return gender.get();
	}

	public ReadOnlyObjectProperty<CharacterGender> genderProperty()
	{
		return gender;
	}

	public CharacterProfession getProfession()
	{
		return profession.get();
	}

	public ReadOnlyObjectProperty<CharacterProfession> professionProperty()
	{
		return profession;
	}

	public int getLevel()
	{
		return level.get();
	}

	public ReadOnlyIntegerProperty levelProperty()
	{
		return level;
	}

	public String getGuild()
	{
		return guild.get();
	}

	public ReadOnlyStringProperty guildProperty()
	{
		return guild;
	}

	public LocalDateTime getCreated()
	{
		return created.get();
	}

	public ReadOnlyObjectProperty<LocalDateTime> createdProperty()
	{
		return created;
	}

	public long getAge()
	{
		return age.get();
	}

	public ReadOnlyLongProperty ageProperty()
	{
		return age;
	}

	public int getDeaths()
	{
		return deaths.get();
	}

	public ReadOnlyIntegerProperty deathsProperty()
	{
		return deaths;
	}

	public CharacterBuild getBuildPVE()
	{
		return buildPVE.get();
	}

	public ReadOnlyObjectProperty<CharacterBuild> buildPVEProperty()
	{
		return buildPVE;
	}

	public CharacterBuild getBuildPVP()
	{
		return buildPVP.get();
	}

	public ReadOnlyObjectProperty<CharacterBuild> buildPVPProperty()
	{
		return buildPVP;
	}

	public CharacterBuild getBuildWVW()
	{
		return buildWVW.get();
	}

	public ReadOnlyObjectProperty<CharacterBuild> buildWVWProperty()
	{
		return buildWVW;
	}

	public CharacterEquipment getEquipment()
	{
		return equipment.get();
	}

	public ReadOnlyObjectProperty<CharacterEquipment> equipmentProperty()
	{
		return equipment;
	}
}
