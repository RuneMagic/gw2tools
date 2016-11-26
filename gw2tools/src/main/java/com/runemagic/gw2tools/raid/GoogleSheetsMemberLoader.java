package com.runemagic.gw2tools.raid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.settings.ApplicationSettings;

public class GoogleSheetsMemberLoader implements RaidMemberLoader
{
	private static final Logger log = LoggerFactory.getLogger(GoogleSheetsMemberLoader.class);

	private static final String APPLICATION_NAME = "GW2Tools";
	private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".credentials/gw2tools");
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);


	private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	private HttpTransport httpTransport = null;
	private FileDataStoreFactory dataStoreFactory = null;

	private String spreadsheetID;
	private String memberNamesRange;
	private String memberSkillRange;
	private String memberPreferencesRange;

	public GoogleSheetsMemberLoader()
	{

	}

	private void refreshSettings()
	{
		ApplicationSettings settings=GW2Tools.inst().getAppSettings();
		this.spreadsheetID=settings.raidSpreadsheetID.get();
		this.memberNamesRange=settings.raidNamesRange.get();
		this.memberPreferencesRange=settings.raidPreferencesRange.get();
		this.memberSkillRange=settings.raidSkillRange.get();
	}

	private FileDataStoreFactory getDataStoreFactory() throws GeneralSecurityException, IOException
	{
		if (dataStoreFactory==null) dataStoreFactory=new FileDataStoreFactory(DATA_STORE_DIR);
		return dataStoreFactory;
	}

	private HttpTransport getHTTPTransport() throws GeneralSecurityException, IOException
	{
		if (httpTransport==null) httpTransport=GoogleNetHttpTransport.newTrustedTransport();
		return httpTransport;
	}

	public Credential authorize() throws IOException, GeneralSecurityException
	{
		InputStream in=GW2Tools.inst().getResourceManager().getResource("auth/client_secret.json").openInput();
		GoogleClientSecrets clientSecrets=GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
						getHTTPTransport(), jsonFactory, clientSecrets, SCOPES)
						.setDataStoreFactory(getDataStoreFactory())
						.setAccessType("offline")
						.build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		log.debug("Credentials saved to {}", DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}


	private Sheets getSheetsService() throws IOException, GeneralSecurityException
	{
		return new Sheets.Builder(getHTTPTransport(),
				jsonFactory, authorize())
				.setApplicationName(APPLICATION_NAME)
				.build();
	}


	@Override public Set<RaidMember> getMembers() throws MemberLoaderException
	{
		refreshSettings();
		Set<RaidMember> ret=new HashSet<>();
		try
		{
			Sheets service = getSheetsService();

			BatchGetValuesResponse response = service.spreadsheets().values()
					.batchGet(spreadsheetID).setRanges(Arrays.asList(memberNamesRange, memberPreferencesRange, memberSkillRange))
					.execute();

			List<String> names=new ArrayList<>();
			List<ValueRange> ranges=response.getValueRanges();
			List<List<Object>> memberNamesData=ranges.get(0).getValues();
			if (memberNamesData == null || memberNamesData.size() == 0)
			{
				throw new MemberLoaderException("Failed to load member name data");
			}
			else
			{
				for (List<Object> row : memberNamesData)
				{
					String name=row.get(0).toString();
					if (!name.isEmpty()) names.add(name);
				}
			}
			log.debug("Loaded member names: "+names);

			List<List<Object>> memberPreferencesData=ranges.get(1).getValues();
			if (memberPreferencesData == null || memberPreferencesData.size() == 0)
			{
				throw new MemberLoaderException("Failed to load member preferences data");
			}

			List<List<Object>> memberSkillData=ranges.get(2).getValues();
			if (memberSkillData == null || memberSkillData.size() == 0)
			{
				throw new MemberLoaderException("Failed to load member skill data");
			}

			int row=0;
			for (String name:names)
			{
				List<MemberBuild> builds=new LinkedList<>();
				for (RaidBuild build:RaidBuild.values())
				{
					int col=getColumn(build);
					MemberBuild mbuild=createBuild(build, row, col, memberSkillData, memberPreferencesData);
					if (mbuild.getSkill()>0)
					{
						builds.add(mbuild);
						log.debug("{}: Loaded build {} ",name,mbuild);
					}
				}
				ret.add(new RaidMember(name, builds));
				row++;
			}

		}
		catch (IOException | GeneralSecurityException e)
		{
			throw new MemberLoaderException(e);
		}

		return ret;
	}

	private int getColumn(RaidBuild build) throws MemberLoaderException
	{
		switch (build)
		{
		case ELEMENTALIST_STAFF: return 0;
		case ELEMENTALIST_DAGGERWARHORN: return 1;
		case ENGINEER_POWER: return 3;
		case ENGINEER_CONDITION_DAMAGE: return 2;
		case GUARDIAN_HAMMER: return 4;
		case GUARDIAN_SCEPTERTORCH: return 5;
		case GUARDIAN_SWORDTORCH: return 6;
		case MESMER_TANK: return 7;
		case MESMER_CHRONOMANCER: return 8;
		case MESMER_CONDITION_DAMAGE: return 9;
		case NECROMANCER_CONDITION_DAMAGE: return 10;
		case REVENANT_POWER: return 11;
		case RANGER_POWER: return 12;
		case RANGER_CONDITION_DAMAGE: return 13;
		case RANGER_HEALER: return 14;
		case THIEF_STAFF: return 15;
		case THIEF_DAGGER: return 16;
		case WARRIOR_POWER: return 17;
		case WARRIOR_CONDITION_DAMAGE: return 18;
		default: throw new MemberLoaderException("Unknown build: "+build);
		}
	}

	private MemberBuild createBuild(RaidBuild build, int row, int col, List<List<Object>> memberSkillData, List<List<Object>> memberPreferencesData)
	{
		Integer skill=getValue(memberSkillData, row, col);
		Integer pref=getValue(memberPreferencesData, row, col);
		if (skill==null) skill=0;
		else if (skill>5) skill=5;
		else if (skill<0) skill=0;
		if (pref==null) pref=3;
		else if (pref>5) pref=5;
		else if (pref<0) pref=0;
		return new MemberBuild(build, skill, pref);
	}

	private Integer getValue(List<List<Object>> sheet, int row, int col)
	{
		if (sheet.size()<=row) return null;
		List<Object> rowData=sheet.get(row);
		if (rowData==null) return null;
		if (rowData.size()<=col) return null;
		Object data=rowData.get(col);
		if (data==null) return null;
		String str=data.toString();
		if (str.trim().isEmpty()) return null;
		try
		{
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
	}
}
