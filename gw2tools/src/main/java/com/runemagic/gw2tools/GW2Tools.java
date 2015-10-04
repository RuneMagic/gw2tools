package com.runemagic.gw2tools;

import ch.qos.logback.classic.Level;
import com.faelar.util.io.ResourceManager;
import com.faelar.util.javafx.FontIcon;
import com.faelar.util.javafx.Icons;
import com.faelar.util.javafx.JFXTools;
import com.runemagic.gw2tools.gui.ApplicationManager;
import com.runemagic.gw2tools.util.GlobalPoolThreadManager;
import com.runemagic.gw2tools.util.ThreadManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class GW2Tools extends Application
{
	private static final Logger log = LoggerFactory.getLogger(GW2Tools.class);

	private static GW2Tools gw2tool;
	private Stage primaryStage;

	private ResourceManager res;
	private ThreadManager threads;
	private ApplicationManager app;

	public GW2Tools() throws Exception
	{
		if (gw2tool!=null) throw new Exception();//TODO better exception
		gw2tool=this;
		//conf = new INIConfigManager(new File("config"));
		res=new ResourceManager("com/runemagic/gw2tools/res/", "res");
		threads=new GlobalPoolThreadManager();
		setLogLevel(Level.INFO);//TODO configurable
	}

	private void setLogLevel(ch.qos.logback.classic.Level level)
	{
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.runemagic.gw2tools")).setLevel(level);
	}



	@Override
	public void stop() throws Exception
	{
		saveAll();
		threads.shutdown();
		threads.awaitTermination(10000);//TODO gui/feedback
	}

	public void saveAll()
	{
		saveGUI();
		//conf.save();
	}

	public static GW2Tools inst()
	{
		return gw2tool;
	}

	public static Logger log()
	{
		return log;
	}

	/*public ConfigInterface getConfig(String section)
	{
		return conf.getSection(section);
	}

	public ConfigInterface getConfig()
	{
		return conf;
	}*/

	public static Image getImageResource(String name)
	{
		return inst().getResourceManager().getImageResource(name);
	}

	public ResourceManager getResourceManager()
	{
		return res;
	}

	public <T> T loadFXML(String name) throws IOException
	{
		return FXMLLoader.load(res.getResourceURL("fxml/" + name));
	}

	public String getCSS(String name) throws IOException
	{
		URL url=res.getResourceURL("css/"+name);
		if (url==null) return null;
		return url.toExternalForm();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		app = new ApplicationManager();
		try
		{
			this.primaryStage=primaryStage;
			initPrimaryStage(primaryStage);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public ThreadManager getThreadManager()
	{
		return threads;
	}

	public static void addMainCSS(Scene scene)
	{
		try
		{
			String css = GW2Tools.inst().getCSS("default.css");
			if (css!=null) scene.getStylesheets().add(css);
			else log.error("Can't find main CSS");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Scene createScene(Parent root)
	{
		Scene ret=new Scene(root);
		addMainCSS(ret);
		return ret;
	}

	private void initPrimaryStage(Stage primaryStage) throws IOException
	{
		Font.loadFont(res.getResourceURL("fonts/fontawesome.otf").toExternalForm(), 12);

		initToolBar();
		initViews();

		Scene mainScene=new Scene(app.getRootPane());
		addMainCSS(mainScene);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("GW2Tools");
		primaryStage.show();
	}


	private void initToolBar()
	{
		HBox tool = app.getToolBar();

		tool.getChildren().add(Icons.createIconButton(FontIcon.GEAR, "Settings", 18));
	}

	private void initViews() throws IOException
	{
		app.addView("Main", loadFXML("main.fxml"), Icons.createIconLabel(FontIcon.BAR_CHART), true);
	}

	private void saveGUI()
	{
		Stage stage=getPrimaryStage();
		/*ConfigInterface conf=getConfig("GUI");
		conf.put("window_width", stage.getWidth());
		conf.put("window_height", stage.getHeight());
		conf.put("window_x", stage.getX());
		conf.put("window_y", stage.getY());
		conf.put("window_maximized", stage.isMaximized());*/
	}

	public static void main(String[] args)
	{
		launch(args);
	}

	/*public GW2ToolsGUI getPrimaryScene()
	{
		return (GW2ToolsGUI)primaryStage.getScene();
	}*/

	public Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public static void showAndWait(Parent root)
	{
		showAndWait(root, null);
	}

	public static void showAndWait(Parent root, String title)
	{
		JFXTools.showAndWait(GW2Tools.inst().getPrimaryStage(), GW2Tools.createScene(root), title);
	}

	public static void show(Parent root, String title)
	{
		// TODO use jfxtools.show
		Stage stage=new Stage();
		if (title!=null) stage.setTitle(title);
		stage.initOwner(inst().getPrimaryStage());
		stage.setScene(createScene(root));
		stage.show();
	}

}
