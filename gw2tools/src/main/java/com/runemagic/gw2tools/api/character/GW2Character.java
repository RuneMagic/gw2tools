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
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APIFieldType;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.api.account.Guild;
import com.runemagic.gw2tools.util.StringTools;

public class GW2Character extends AuthenticatedAPIObject
{
	private final static String API_RESOURCE_CHARACTERS="characters";

	@GW2APIField(name = "name")
	private StringProperty name=new SimpleStringProperty();

	@GW2APIField(name = "race", sourceType = GW2APIFieldType.STRING, targetType = CharacterRace.class, factory = "byName")
	private ObjectProperty<CharacterRace> race=new SimpleObjectProperty<>();

	@GW2APIField(name = "gender", sourceType = GW2APIFieldType.STRING, targetType = CharacterGender.class, factory = "byName")
	private ObjectProperty<CharacterGender> gender=new SimpleObjectProperty<>();

	@GW2APIField(name = "profession", sourceType = GW2APIFieldType.STRING, targetType = CharacterProfession.class, factory = "byName")
	private ObjectProperty<CharacterProfession> profession=new SimpleObjectProperty<>();

	@GW2APIField(name = "level")
	private IntegerProperty level=new SimpleIntegerProperty();

	@GW2APIField(name = "guild", optional=true, sourceType = GW2APIFieldType.STRING, factory = "getGuild")
	private ObjectProperty<Guild> guild=new SimpleObjectProperty<>();

	@GW2APIField(name = "created", sourceType = GW2APIFieldType.DATETIME)
	private ObjectProperty<Instant> created=new SimpleObjectProperty<>();

	@GW2APIField(name = "age")
	private LongProperty age=new SimpleLongProperty();
	private StringProperty formattedAge=new SimpleStringProperty();

	@GW2APIField(name = "deaths")
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

		formattedAge.bind(Bindings.createStringBinding(() -> StringTools.formatSecondsLong(age.get()), age));
	}

	@Override
	protected void initResources()
	{
		addAPIv2Resource(() -> API_RESOURCE_CHARACTERS + "/" + getURLEncodedName(), this, this::updateCharacter);
	}

	private String getURLEncodedName()
	{
		return name.get().replaceAll(" ", "%20");//TODO proper encoding
	}

	private void updateCharacter(String data)
	{
		JSONObject json=new JSONObject(data);
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
			updateSpecializationSlot(build.getSpec1(), json.getJSONObject(0));
			updateSpecializationSlot(build.getSpec2(), json.getJSONObject(1));
			updateSpecializationSlot(build.getSpec3(), json.getJSONObject(2));
		}
		else
		{
			updateSpecializationSlot(build.getSpec1(), null);
			updateSpecializationSlot(build.getSpec2(), null);
			updateSpecializationSlot(build.getSpec3(), null);
		}
	}

	private void updateSpecializationSlot(SpecializationSlot spec, JSONObject json)
	{
		if (json!=null)
		{
			spec.setId(json.getInt("id"));
			JSONArray traitsArray = json.getJSONArray("traits");
			spec.setTrait1(GW2API.inst().getTrait(traitsArray.getInt(0)));
			spec.setTrait2(GW2API.inst().getTrait(traitsArray.getInt(1)));
			spec.setTrait3(GW2API.inst().getTrait(traitsArray.getInt(2)));
		}
		else
		{
			spec.setId(null);
			spec.setTrait1(null);
			spec.setTrait2(null);
			spec.setTrait3(null);
		}
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
