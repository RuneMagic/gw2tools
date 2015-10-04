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

import org.json.JSONArray;
import org.json.JSONObject;

import com.runemagic.gw2tools.api.APIKey;
import com.runemagic.gw2tools.api.AuthenticatedAPIObject;
import com.runemagic.gw2tools.api.GW2APIException;
import com.runemagic.gw2tools.api.GW2APISource;

public class TokenInfo extends AuthenticatedAPIObject
{
	private final static String API_RESOURCE_TOKENINFO="tokeninfo";

	private StringProperty id=new SimpleStringProperty();
	private StringProperty name=new SimpleStringProperty();
	private SetProperty<GW2APIPermission> permissions=new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
	private Map<GW2APIPermission, BooleanProperty> permissionMap=new HashMap<>();

	public TokenInfo(GW2APISource source, APIKey apiKey)
	{
		super(source, apiKey);

		permissions.addListener((SetChangeListener<GW2APIPermission>) change -> {
			permissionMap.clear();//TODO modify only the changed permissions
			for (GW2APIPermission perm:permissions)
			{
				getPermissionProperty(perm).set(true);
			}
		});
	}

	@Override protected void updateImpl() throws GW2APIException
	{
		JSONObject json=new JSONObject(readAPIv2Resource(API_RESOURCE_TOKENINFO, this));
		id.set(json.getString("id"));
		name.set(json.getString("name"));
		JSONArray perms=json.getJSONArray("permissions");
		permissions.clear();
		int len=perms.length();
		for (int i=0;i<len;i++)
		{
			String permName=perms.getString(i);
			GW2APIPermission perm=GW2APIPermission.byName(permName);
			if (perm==null) throw new GW2APIException("Unknown permission: "+permName);
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
