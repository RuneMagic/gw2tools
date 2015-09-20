package com.runemagic.gw2tools.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.runemagic.gw2tools.data.APIKeyHolder;
import com.runemagic.gw2tools.data.CharacterBuild;
import com.runemagic.gw2tools.data.CharacterGender;
import com.runemagic.gw2tools.data.CharacterProfession;
import com.runemagic.gw2tools.data.CharacterRace;
import com.runemagic.gw2tools.data.CharacterSpecialization;
import com.runemagic.gw2tools.data.CharacterTrait;
import com.runemagic.gw2tools.data.GW2Account;
import com.runemagic.gw2tools.data.GW2Character;

public class GW2API
{
	private final static String API_URL="https://api.guildwars2.com/v2";
	private final static String API_URL_CHARACTERS=API_URL+"/characters";

	public GW2API()
	{

	}

	public GW2Account queryAccountData(String apiKey)
	{
		GW2Account acc=new GW2Account(apiKey);
		updateAccount(acc);
		return acc;
	}

	public void updateAccount(GW2Account acc)
	{
		String data=readUrl(appendAccessToken(API_URL_CHARACTERS, acc));
		if (data==null) return;//TODO proper exception handling

		List<String> charNames=new ArrayList<String>();
		JSONArray json = new JSONArray(data);
		int len=json.length();
		for (int i=0;i<len;i++)
		{
			charNames.add(json.getString(i));
		}

		List<GW2Character> characters=acc.getCharacters();
		Iterator<GW2Character> iter=characters.iterator();
		while (iter.hasNext())
		{
			GW2Character character=iter.next();
			if (charNames.remove(character.getName()))
			{
				updateCharacter(character);
			}
			else
			{
				iter.remove();
			}
		}

		for (String charName:charNames)
		{
			GW2Character character=new GW2Character();
			characters.add(character);//TODO keep original order
			updateCharacter(character);
		}
	}

	public void updateCharacter(GW2Character character)
	{
		String data=readUrl(appendAccessToken(API_URL_CHARACTERS+"/"+character.getName(), character));
		if (data==null) return;//TODO proper exception handling
		JSONObject json=new JSONObject(data);
		character.setName(json.getString("name"));
		character.setRace(CharacterRace.byName(json.getString("race")));//TODO exception handling
		character.setGender(CharacterGender.byName(json.getString("gender")));
		character.setProfession(CharacterProfession.byName(json.getString("profession")));
		character.setLevel(json.getInt("level"));
		character.setGuild(json.optString("guild", null));//TODO guild parsing
		character.setCreated(LocalDateTime.parse(json.getString("created")));
		character.setAge(json.getLong("age"));
		character.setDeaths(json.getInt("deaths"));
		//TODO crafting
		JSONObject specs=json.optJSONObject("specializations");
		if (specs!=null)
		{
			updateBuild(character.getBuildPVE(), json.getJSONArray("pve"));
			updateBuild(character.getBuildPVP(), json.getJSONArray("pvp"));
			updateBuild(character.getBuildWVW(), json.getJSONArray("wvw"));
		}
		//TODO the rest
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

	private String appendAccessToken(String url, APIKeyHolder keyHolder)
	{
		return url+"?access_token="+keyHolder.getAPIKey();
	}

	private static String readUrl(String urlString)
	{
		try
		{
			try (BufferedReader reader= new BufferedReader(new InputStreamReader((new URL(urlString)).openStream()))) {
				StringBuffer buffer = new StringBuffer();
				int read;
				char[] chars = new char[1024];
				while ((read = reader.read(chars)) != -1)
					buffer.append(chars, 0, read);
				return buffer.toString();
			}
		}
		catch (IOException e)//TODO proper exception handling
		{
			e.printStackTrace();
			return null;
		}
	}
}
