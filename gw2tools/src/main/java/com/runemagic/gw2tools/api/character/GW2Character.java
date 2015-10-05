package com.runemagic.gw2tools.api.character;

import java.time.Instant;

import javafx.beans.binding.Bindings;
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

import com.runemagic.gw2tools.api.APIKey;
import com.runemagic.gw2tools.api.AuthenticatedAPIObject;
import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.account.Guild;
import com.runemagic.gw2tools.util.StringTools;

public class GW2Character extends AuthenticatedAPIObject
{
	private final static String API_RESOURCE_CHARACTERS="characters";

	private StringProperty name=new SimpleStringProperty();
	private ObjectProperty<CharacterRace> race=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterGender> gender=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterProfession> profession=new SimpleObjectProperty<>();
	private IntegerProperty level=new SimpleIntegerProperty();
	private ObjectProperty<Guild> guild=new SimpleObjectProperty<>();
	private ObjectProperty<Instant> created=new SimpleObjectProperty<>();
	private LongProperty age=new SimpleLongProperty();
	private StringProperty formattedAge=new SimpleStringProperty();
	private IntegerProperty deaths=new SimpleIntegerProperty();
	//TODO crafting
	private ObjectProperty<CharacterBuild> buildPVE=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterBuild> buildPVP=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterBuild> buildWVW=new SimpleObjectProperty<>();
	private ObjectProperty<CharacterEquipment> equipment=new SimpleObjectProperty<>();
	//TODO inventory

	public GW2Character(GW2APISource source, String name, APIKey apiKey)
	{
		super(source, apiKey);
		this.name.set(name);
		buildPVE.set(new CharacterBuild());
		buildPVP.set(new CharacterBuild());
		buildWVW.set(new CharacterBuild());

		formattedAge.bind(Bindings.createStringBinding(()-> StringTools.formatSecondsLong(age.get()), age));
	}

	private String getURLEncodedName()
	{
		return name.get().replaceAll(" ", "%20");//TODO proper encoding
	}

	@Override
	protected void updateImpl() throws GW2APIException
	{
		JSONObject json=new JSONObject(readAPIv2Resource(API_RESOURCE_CHARACTERS + "/" + getURLEncodedName(), this));
		name.set(json.getString("name"));
		race.set(CharacterRace.byName(json.getString("race")));
		gender.set(CharacterGender.byName(json.getString("gender")));
		profession.set(CharacterProfession.byName(json.getString("profession")));
		level.set(json.getInt("level"));
		guild.set(GW2API.inst().getGuild(json.optString("guild", null)));
		created.set(Instant.parse(json.getString("created")));
		age.set(json.getLong("age"));
		deaths.set(json.getInt("deaths"));
		//TODO crafting
		JSONObject specs=json.optJSONObject("specializations");
		if (specs!=null)
		{
			updateBuild(getBuildPVE(), json.optJSONArray("pve"));
			updateBuild(getBuildPVP(), json.optJSONArray("pvp"));
			updateBuild(getBuildWVW(), json.optJSONArray("wvw"));
		}
	}

	private void updateBuild(CharacterBuild build, JSONArray json)
	{
		if (json!=null)
		{
			build.setSpec1(readSpecialization(json.getJSONObject(0)));
			build.setSpec2(readSpecialization(json.getJSONObject(1)));
			build.setSpec3(readSpecialization(json.getJSONObject(2)));
		}
		else
		{
			build.setSpec1(null);
			build.setSpec2(null);
			build.setSpec3(null);
		}
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

	public Guild getGuild()
	{
		return guild.get();
	}

	public ReadOnlyObjectProperty<Guild> guildProperty()
	{
		return guild;
	}

	public Instant getCreated()
	{
		return created.get();
	}

	public ReadOnlyObjectProperty<Instant> createdProperty()
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

	public String getFormattedAge()
	{
		return formattedAge.get();
	}

	public ReadOnlyStringProperty formattedAgeProperty()
	{
		return formattedAge;
	}
}
