package com.runemagic.gw2tools.api.account;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.runemagic.gw2tools.api.APIKey;
import com.runemagic.gw2tools.api.AuthenticatedAPIObject;
import com.runemagic.gw2tools.api.GW2APIField;
import com.runemagic.gw2tools.api.GW2APISource;

public class TokenInfo extends AuthenticatedAPIObject
{
	private final static String API_RESOURCE_TOKENINFO="tokeninfo";

	@GW2APIField(name = "id")
	private StringProperty id=new SimpleStringProperty();

	@GW2APIField(name = "name")
	private StringProperty name=new SimpleStringProperty();
	private SetProperty<GW2APIPermission> permissions=new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
	private Map<GW2APIPermission, BooleanProperty> permissionMap=new HashMap<>();

	public TokenInfo(GW2APISource source, APIKey apiKey)
	{
		super(source, apiKey);

		permissions.addListener((SetChangeListener<GW2APIPermission>) change -> {

			for (GW2APIPermission perm:GW2APIPermission.values())
			{
				getPermissionProperty(perm).set(permissions.contains(perm));
			}
		});
	}

	@Override
	protected void initResources()
	{
		addAPIv2Resource(API_RESOURCE_TOKENINFO, this, this::updateTokenInfo);
	}

	private void updateTokenInfo(JsonElement data)
	{
		if (!data.isJsonObject()) throw new IllegalArgumentException(); //TODO proper exception
		JsonObject json=(JsonObject)data;
		JsonArray perms=json.getAsJsonArray("permissions");
		permissions.clear();
		for (JsonElement loop:perms)
		{
			String permName=loop.getAsString();
			GW2APIPermission perm=GW2APIPermission.byName(permName);
			if (perm==null) continue; //TODO //throw new GW2APIException("Unknown permission: "+permName);
			permissions.add(perm);
		}
	}

	public static String getApiResourceTokeninfo()
	{
		return API_RESOURCE_TOKENINFO;
	}

	public String getId()
	{
		return id.get();
	}

	public ReadOnlyStringProperty idProperty()
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

	public ObservableSet<GW2APIPermission> getPermissions()//TODO unmodifiable
	{
		return permissions.get();
	}

	public ReadOnlySetProperty<GW2APIPermission> permissionsProperty()
	{
		return permissions;
	}

	public boolean hasPermission(GW2APIPermission perm)
	{
		return permissions.contains(perm);
	}

	private BooleanProperty getPermissionProperty(GW2APIPermission perm)
	{
		BooleanProperty prop=permissionMap.get(perm);
		if (prop==null)
		{
			prop=new SimpleBooleanProperty();
			permissionMap.put(perm, prop);
		}
		return prop;
	}

	public ReadOnlyBooleanProperty permissionProperty(GW2APIPermission perm)
	{
		return getPermissionProperty(perm);
	}
}
