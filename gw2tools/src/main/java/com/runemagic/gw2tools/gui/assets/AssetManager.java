package com.runemagic.gw2tools.gui.assets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	private ExecutorService exec;

	public AssetManager()
	{
		gson = new Gson();
		jsonParser = new JsonParser();
		exec = GW2Tools.inst().getThreadManager().getExecutor("GW2APIAssets");
	}

	/**
	 * Asynchronously queries the offical Guild Wars 2 API for all common assets and verifies that a local copy has been made.
	 *
	 * @param controller The LoginController instance of the application to get access to the progress overlay
	 */
	public void updateGW2Assets(LoginController controller)
	{
		ProgressIndicatorView progress = new ProgressIndicatorView("Downloading GW2 Assets..", "Checking for missing assets..");

		controller.setSimpleOverlay(progress);
		exec.submit(() -> {
			try
			{
				initDirectories();

				Map<String, String> assets = new HashMap<>();
				GW2APISource src = GW2API.inst().getSource();

				JsonElement data = src.readAPIv2Resource("files?ids=all");
				JsonArray jsonArr = (JsonArray) data;
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
					downloadImageResource(url, Reference.DIR_ASSETS + "\\" + id + ".png");
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

	/**
	 * Load a local copy of an Image Asset from the offical Guild Wars 2 API
	 *
	 * @param name The full file name of the Asset
	 * @return A JFX Image of the specified asset
	 */
	public Image getAsset(String name)
	{
		File asset = new File(Reference.DIR_ASSETS + File.separator + name);
		if (!asset.exists()) return null;
		try
		{
			FileInputStream fis = new FileInputStream(Reference.DIR_ASSETS + File.separator + name);
			return new Image(fis);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException("Could not load asset " + name, e);
		}
	}

	/**
	 * Load an Image asset either from the local cache or from the offical Guild Wars 2 API if it's not cached yet
	 *
	 * @param sourceURL The full url of the asset
	 * @return An ObjectProperty which will get the image assigned to it once fully asynchronously loaded
	 */
	public ObjectProperty<Image> loadCachedAsset(URL sourceURL)
	{

		if (sourceURL == null) throw new RuntimeException(new GWAssetException("Asset url cannot be null!"));

		String assetPath = sourceURL.getPath();
		if (assetPath == null || !assetPath.contains("/file/"))
		{
			throw new RuntimeException(new GWAssetException(sourceURL.toString() + " is not a valid asset url!"));
		}

		String fileName = assetPath.replaceFirst("/file/", "").replaceAll("/", "_");

		ObjectProperty<Image> image = new SimpleObjectProperty<>();
		exec.submit(() -> {
			initDirectories();

			try
			{
				File cachedFile = new File(Reference.DIR_ASSETS_CACHED + File.separator + fileName);
				if (!cachedFile.exists())
				{
					downloadImageResource(sourceURL, cachedFile.getPath());
				}
				FileInputStream fis = new FileInputStream(cachedFile.getPath());
				Image img = new Image(fis);
				Platform.runLater(() -> image.setValue(img));
			}
			catch (Exception e)
			{
				e.printStackTrace();//TODO proper error handling
			}

		});
		return image;
	}

	private File downloadImageResource(URL sourceUrl, String targetPath) throws IOException
	{
		InputStream in = new BufferedInputStream(sourceUrl.openStream());
		OutputStream out = new BufferedOutputStream(new FileOutputStream(targetPath));

		for (int i; (i = in.read()) != -1; )
		{
			out.write(i);
		}
		in.close();
		out.close();
		return new File(targetPath);
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

		File assetCaheDir = new File(Reference.DIR_ASSETS_CACHED);
		if (!assetCaheDir.exists())
		{
			if (!assetCaheDir.mkdirs()) throw new RuntimeException("Failed to create directory: " + Reference.DIR_ASSETS_CACHED);
		}
	}

}
