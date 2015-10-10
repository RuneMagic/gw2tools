package com.runemagic.gw2tools.gui.assets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javafx.application.Platform;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.runemagic.gw2tools.GW2Tools;
import com.runemagic.gw2tools.api.GW2API;
import com.runemagic.gw2tools.api.GW2APISource;
import com.runemagic.gw2tools.gui.components.ProgressIndicatorView;
import com.runemagic.gw2tools.gui.controller.LoginController;
import com.runemagic.gw2tools.reference.Reference;

public class AssetManager
{

	private Gson gson = new Gson();
	private JsonParser jsonParser = new JsonParser();

	public void getGW2Assets(LoginController controller)
	{
		ProgressIndicatorView progress = new ProgressIndicatorView("Downloading GW2 Assets..", "Checking for missing assets..");

		controller.setSimpleOverlay(progress);
		ExecutorService exec = GW2Tools.inst().getThreadManager().getExecutor("GW2APIAssets");
		exec.submit(() -> {
			try
			{
				initDirectories();

				Map<String, String> assets = new HashMap<>();
				GW2APISource src = GW2API.inst().getSource();

				String data = src.readAPIv2Resource("files?ids=all");
				JsonArray jsonArr = (JsonArray) jsonParser.parse(data);
				ArrayList assetList = gson.fromJson(jsonArr, ArrayList.class);

				for (Object loop : assetList)
				{
					LinkedTreeMap assetData = (LinkedTreeMap) loop;
					assets.put((String) assetData.get("id"), (String) assetData.get("icon"));
				}

				//Get the total count of assets to be downloaded
				Iterator<Map.Entry<String, String>> itr = assets.entrySet().iterator();
				while (itr.hasNext())
				{
					Map.Entry<String, String> entry = itr.next();
					File assetFile = new File(Reference.DIR_ASSETS + "\\" + entry.getKey() + ".png");
					if (assetFile.exists()) itr.remove();
				}

				for (String id : assets.keySet())
				{
					Platform.runLater(() -> progress.getLblText().setText("Downloading " + id + ".png"));
					String assetUrl = assets.get(id);
					URL url = new URL(assetUrl);
					InputStream in = new BufferedInputStream(url.openStream());
					OutputStream out = new BufferedOutputStream(new FileOutputStream(Reference.DIR_ASSETS + "\\" + id + ".png"));

					for (int i; (i = in.read()) != -1; )
					{
						out.write(i);
					}
					in.close();
					out.close();
				}
				Platform.runLater(controller::hideOverlay);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException("failed to load assets!", e);
			}
		});
	}

	private void initDirectories()
	{
		File appDir = new File(Reference.DIR_APP);
		if (!appDir.exists())
		{
			if (!appDir.mkdirs()) throw new RuntimeException("Failed to create directory: " + Reference.DIR_APP);
		}

		File assetDir = new File(Reference.DIR_ASSETS);
		if (!assetDir.exists())
		{
			if (!assetDir.mkdirs()) throw new RuntimeException("Failed to create directory: " + Reference.DIR_ASSETS);
		}
	}

}
