package com.runemagic.gw2tools.gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runemagic.gw2tools.GW2Tools;

public class GW2ToolsGUI extends Scene
{
	private static final Logger log = LoggerFactory.getLogger(GW2ToolsGUI.class);
	private GW2Tools gw2tool;

	/*private ConfigInterface guiConf;
	private ConfigInterface conf;*/

	public GW2ToolsGUI(GW2Tools gw2tool)
	{
		super(new BorderPane());
		this.gw2tool=gw2tool;
		/*guiConf=gw2tool.getConfig("GUI");
		conf=gw2tool.getConfig();*/
		loadConfig();
		initGUI();

		TabPane tabs=new TabPane();
		BorderPane root=(BorderPane) getRoot();
		root.setCenter(tabs);

		tabs.getTabs().add(new Tab("Commerce", createCommerceTab()));
	}

	private Node createCommerceTab()
	{
		TabPane tabs=new TabPane();
		tabs.getTabs().add(new Tab("Material processing", createMaterialProcessingTab()));
		return tabs;
	}

	private Node createMaterialProcessingTab()
	{

		return null;
	}

	private void initGUI()
	{
		BorderPane root=(BorderPane) this.getRoot();

	}

	public void loadConfig()
	{


	}


}
