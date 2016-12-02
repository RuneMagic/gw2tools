package com.runemagic.gw2tools.raid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.runemagic.gw2tools.api.character.CharacterProfession;
import com.runemagic.gw2tools.settings.ApplicationSettings;

public class GoogleSheetsInterface implements RaidDataInterface
{
	private static final Logger log = LoggerFactory.getLogger(GoogleSheetsInterface.class);

	private static final String APPLICATION_NAME = "GW2Tools";
	private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".credentials/gw2tools");
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);


	private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	private HttpTransport httpTransport = null;
	private FileDataStoreFactory dataStoreFactory = null;
	private Sheets sheets=null;

	private String compSpreadsheetID;
	private String compBaseRange;
	private String rolesRange;
	private String buildsRange;
	private String outputSheetName;

	private String rosterSpreadsheetID;
	private String memberNamesRange;
	private String memberSkillRange;
	private String memberPreferencesRange;

	private List<RaidBuild> builds=new ArrayList<>();
	private Map<String, RaidBuild> buildMap=new HashMap<>();
	private Map<String, RaidRole> roles=new HashMap<>();
	private Set<RaidMember> members=new HashSet<>();
	private SessionBase sessionBase;

	private boolean requiresUpdate=true;

	public GoogleSheetsInterface()
	{

	}

	private void refreshSettings()
	{
		ApplicationSettings settings=GW2Tools.inst().getAppSettings();

		this.compSpreadsheetID=settings.raidCompositionSpreadsheetID.get();
		this.compBaseRange=settings.raidCompositionBaseRange.get();
		this.rolesRange=settings.raidRolesRange.get();
		this.buildsRange=settings.raidBuildsRange.get();
		this.outputSheetName=settings.raidSessionSheetName.get();

		this.rosterSpreadsheetID =settings.raidSpreadsheetID.get();
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

	private Credential authorize() throws IOException, GeneralSecurityException
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
		if (sheets==null) sheets=new Sheets.Builder(getHTTPTransport(),
				jsonFactory, authorize())
				.setApplicationName(APPLICATION_NAME)
				.build();
		return sheets;
	}

	private void loadCompositionSpreadsheet() throws RaidInterfaceException
	{
		try
		{
			Sheets service = getSheetsService();

			BatchGetValuesResponse response = service.spreadsheets().values()
					.batchGet(compSpreadsheetID).setRanges(Arrays.asList(rolesRange, buildsRange, compBaseRange))
					.execute();

			List<ValueRange> ranges=response.getValueRanges();


			List<List<Object>> rolesData=ranges.get(0).getValues();
			if (rolesData == null || rolesData.size() == 0)
			{
				throw new RaidInterfaceException("Failed to fetch roles data");
			}
			roles.clear();
			for (List<Object> row : rolesData)
			{
				String roleName=row.get(0).toString();
				if (!roleName.isEmpty())
				{
					String shortName=row.get(1).toString();
					if (roles.put(roleName, new RaidRole(roleName, shortName))!=null) throw new RaidInterfaceException("Duplicate role: '"+roleName+"'");
				}
			}

			List<List<Object>> buildsData=ranges.get(1).getValues();
			if (buildsData == null || buildsData.size() == 0)
			{
				throw new RaidInterfaceException("Failed to fetch builds data");
			}
			builds.clear();
			buildMap.clear();
			for (List<Object> row : buildsData)
			{
				String profName=row.get(0).toString();
				CharacterProfession prof=CharacterProfession.byName(profName);
				if (prof==null) throw new RaidInterfaceException("Invalid profession: '"+profName+"'");
				String buildName=row.get(1).toString();
				if (buildName.isEmpty()) throw new RaidInterfaceException("Empty build name");
				List<RaidRole> buildRoles=new LinkedList<RaidRole>();
				for (Object obj:row.subList(2,row.size()))
				{
					String roleName=obj.toString();
					if (roleName.isEmpty()) continue;
					RaidRole role=roles.get(roleName);
					if (role==null) throw new RaidInterfaceException("Invalid role '"+roleName+"' for build '"+buildName+"'");
					buildRoles.add(role);
				}
				if (buildRoles.isEmpty()) log.warn("Build without roles: '{}'", buildName);
				RaidBuild build=new RaidBuild(buildName, prof, buildRoles);
				if (buildMap.put(buildName, build)!=null) throw new RaidInterfaceException("Duplicate build: '"+buildName+"'");
				builds.add(build);
			}

			List<List<Object>> compBaseData=ranges.get(2).getValues();
			if (compBaseData == null || compBaseData.size() == 0)
			{
				throw new RaidInterfaceException("Failed to fetch composition base data");
			}
			sessionBase=null;
			Map<RaidBoss, CompositionBase> comps=new HashMap<>();
			for (RaidBoss boss:RaidBoss.values())
			{
				int raidSize=boss.getWing().getRaidSize();
				List<RaidRole> bossRoles=new LinkedList<>();
				for (int row=0;row<raidSize;row++)
				{
					int col=(boss.getWing().getNumber()+1)*boss.getNumber();
					String val=getStringValue(compBaseData, row, col);
					if (val==null) throw new RaidInterfaceException("Null raid role at ("+row+", "+col+")");
					RaidRole role=roles.get(val);
					if (role==null) throw new RaidInterfaceException("Unknown raid role: '"+val+"' at ("+row+", "+col+")");
					bossRoles.add(role);
				}
				if (bossRoles.size()!=raidSize) throw new RaidInterfaceException("Composition for boss '"+boss+"' has the wrong number of roles ("+bossRoles.size()+" instead of "+raidSize+")");
				CompositionBase base=new CompositionBase(bossRoles);
				comps.put(boss, base);
			}
			if (comps.size()!=RaidBoss.values().length) throw new RaidInterfaceException("Not enough boss compositions in session base.");
			sessionBase=new SessionBase(comps, RaidWing.SPIRIT_VALE, RaidWing.SALVATION_PASS, RaidWing.STRONGHOLD_OF_THE_FAITHFUL);
		}
		catch (IOException | GeneralSecurityException e)
		{
			throw new RaidInterfaceException(e);
		}
	}

	private void loadRosterSpreadsheet() throws RaidInterfaceException
	{
		try
		{
			Sheets service = getSheetsService();

			BatchGetValuesResponse response = service.spreadsheets().values()
					.batchGet(rosterSpreadsheetID).setRanges(Arrays.asList(memberNamesRange, memberPreferencesRange, memberSkillRange))
					.execute();

			List<String> names=new ArrayList<>();
			List<ValueRange> ranges=response.getValueRanges();
			List<List<Object>> memberNamesData=ranges.get(0).getValues();
			if (memberNamesData == null || memberNamesData.size() == 0)
			{
				throw new RaidInterfaceException("Failed to fetch member name data");
			}

			for (List<Object> row : memberNamesData)
			{
				String name=row.get(0).toString();
				if (!name.isEmpty()) names.add(name);
			}

			log.debug("Loaded member names: "+names);

			List<List<Object>> memberPreferencesData=ranges.get(1).getValues();
			if (memberPreferencesData == null || memberPreferencesData.size() == 0)
			{
				throw new RaidInterfaceException("Failed to fetch member preferences data");
			}

			List<List<Object>> memberSkillData=ranges.get(2).getValues();
			if (memberSkillData == null || memberSkillData.size() == 0)
			{
				throw new RaidInterfaceException("Failed to fetch member skill data");
			}
			members.clear();
			int row=0;
			for (String name:names)
			{
				List<MemberBuild> builds=new LinkedList<>();
				int col=0;
				for (RaidBuild build:this.builds)
				{
					MemberBuild mbuild=createBuild(build, row, col, memberSkillData, memberPreferencesData);
					if (mbuild.getSkill()>0)
					{
						builds.add(mbuild);
						log.debug("{}: Loaded build {} ",name,mbuild);
					}
					col++;
				}
				members.add(new RaidMember(name, builds));
				row++;
			}

		}
		catch (IOException | GeneralSecurityException e)
		{
			throw new RaidInterfaceException(e);
		}
	}

	private void loadData() throws RaidInterfaceException
	{
		requiresUpdate=false;
		refreshSettings();
		loadCompositionSpreadsheet();
		loadRosterSpreadsheet();
	}


	@Override public Set<RaidMember> getMembers() throws RaidInterfaceException
	{
		if (requiresUpdate) loadData();
		return new HashSet<>(members);
	}

	@Override public Set<RaidBuild> getBuilds() throws RaidInterfaceException
	{
		if (requiresUpdate) loadData();
		return new HashSet<>(builds);
	}

	@Override public Set<RaidRole> getRoles() throws RaidInterfaceException
	{
		if (requiresUpdate) loadData();
		return new HashSet<>(roles.values());
	}

	@Override public SessionBase getSessionBase() throws RaidInterfaceException
	{
		if (requiresUpdate) loadData();
		return sessionBase;
	}

	private MemberBuild createBuild(RaidBuild build, int row, int col, List<List<Object>> memberSkillData, List<List<Object>> memberPreferencesData)
	{
		Integer skill= getIntValue(memberSkillData, row, col);
		Integer pref= getIntValue(memberPreferencesData, row, col);
		if (skill==null) skill=0;
		else if (skill>5) skill=5;
		else if (skill<0) skill=0;
		if (pref==null) pref=3;
		else if (pref>5) pref=5;
		else if (pref<0) pref=0;
		return new MemberBuild(build, skill, pref);
	}

	private String getStringValue(List<List<Object>> sheet, int row, int col)
	{
		if (sheet.size()<=row) return null;
		List<Object> rowData=sheet.get(row);
		if (rowData==null) return null;
		if (rowData.size()<=col) return null;
		Object data=rowData.get(col);
		if (data==null) return null;
		return data.toString();
	}

	private Integer getIntValue(List<List<Object>> sheet, int row, int col)
	{
		String str=getStringValue(sheet, row, col);
		if (str==null) return null;
		str=str.trim();
		if (str.isEmpty()) return null;
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
