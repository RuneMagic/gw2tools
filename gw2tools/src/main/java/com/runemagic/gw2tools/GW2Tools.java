package com.runemagic.gw2tools;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.faelar.util.io.ResourceManager;
import com.faelar.util.javafx.FontIcon;
import com.faelar.util.javafx.Icons;
import com.faelar.util.javafx.JFXTools;
import com.runemagic.gw2tools.api.account.GW2Account;
import com.runemagic.gw2tools.gui.ApplicationManager;
import com.runemagic.gw2tools.gui.assets.AssetManager;
import com.runemagic.gw2tools.reference.Reference;
import com.runemagic.gw2tools.settings.ApplicationSettings;
import com.runemagic.gw2tools.util.GlobalPoolThreadManager;
import com.runemagic.gw2tools.util.ThreadManager;

import ch.qos.logback.classic.Level;
import de.pat.fxsettings.FXSettingsManager;
import de.pat.fxsettings.FXSettingsSerializerType;
import de.pat.fxsettings.FXSettingsSheetPane;
import de.pat.fxsettings.serializer.PreferencesFXSettingsSerializer;
import de.pat.util.javafx.DialogTools;
import de.pat.util.javafx.ViewBoundsSerializer;
import insidefx.undecorator.Undecorator;

public class GW2Tools extends Application
{
	private static final Logger log = LoggerFactory.getLogger(GW2Tools.class);

	private static GW2Tools gw2tool;
	private Stage primaryStage;
	private BorderPane applicationPane;

	private AssetManager assets;
	private ResourceManager res;
	private ThreadManager threads;
	private FXSettingsManager settings;
	private ApplicationManager app;

	private ApplicationSettings appSettings;
	private FXSettingsSheetPane appSettingsSheet;

	private ObjectProperty<GW2Account> account=new SimpleObjectProperty<>();

	public GW2Tools() throws Exception
	{
		if (gw2tool!=null) throw new Exception();//TODO better exception
		gw2tool=this;
		res=new ResourceManager("com/runemagic/gw2tools/res/", "res");
		threads=new GlobalPoolThreadManager();
		assets = new AssetManager();

		settings = new FXSettingsManager();
		settings.registerSerializer(FXSettingsSerializerType.FX_PREFERENCES, new PreferencesFXSettingsSerializer());

		setLogLevel(Level.INFO);//TODO configurable
	}

	private void setLogLevel(ch.qos.logback.classic.Level level)
	{
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.runemagic.gw2tools")).setLevel(level);
	}

	public GW2Account getAccount()
	{
		return account.get();
	}

	public ObjectProperty<GW2Account> accountProperty()
	{
		return account;
	}

	public void setAccount(GW2Account account)
	{
		this.account.set(account);
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
	}

	public static GW2Tools inst()
	{
		return gw2tool;
	}

	public static Logger log()
	{
		return log;
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
		DialogTools.initOwner(primaryStage);
		try
		{
			initSettings();

			this.primaryStage=primaryStage;
			initPrimaryStage(primaryStage);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			DialogTools.createExceptionDialog(e, "Unexpected Exception", "GW2Tools encountered an unexpected Exception", e.getMessage());
			System.exit(-1);
		});
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

	private void initSettings()
	{
		settings.loadFXSettingsSheet(appSettings = new ApplicationSettings());
		appSettingsSheet = settings.buildFXSettingsSheet(appSettings);
	}

	private void initPrimaryStage(Stage primaryStage) throws IOException
	{
		Font.loadFont(res.getResourceURL("fonts/fontawesome.otf").toExternalForm(), 12);

		primaryStage.setTitle("GW2Tools");
		applicationPane = new BorderPane();
		applicationPane.getStyleClass().add("root-window");

		Undecorator undecorator = new Undecorator(primaryStage, applicationPane);
		undecorator.getStylesheets().add("skin/undecorator.css");

		Scene mainScene = new Scene(undecorator, 1000, 700, Color.WHITE);
		mainScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		undecorator.installAccelerators(mainScene);

		HBox bottom = app.getBottomBar();

		if (!undecorator.maximizeProperty().get() && !undecorator.fullscreenProperty().get())
			applicationPane.setBottom(bottom);

		ChangeListener<Boolean> barToggle = (e, oldValue, newValue) -> {
			if (!undecorator.maximizeProperty().get() && !undecorator.fullscreenProperty().get())
			{
				applicationPane.setBottom(bottom);
			} else
			{
				applicationPane.setBottom(null);
			}
		};
		undecorator.maximizeProperty().addListener(barToggle);
		undecorator.fullscreenProperty().addListener(barToggle);

		HBox top = app.getTopBar();
		undecorator.setAsStageDraggable(primaryStage, top);

		initToolBar();
		initViews();

		applicationPane.setCenter(loadFXML("loginView.fxml"));

		addMainCSS(mainScene);
		new ViewBoundsSerializer().switchView(primaryStage, Reference.REGISTRY_WINDOW_BOUNDS, undecorator.maximizeProperty(), true);
		primaryStage.setScene(mainScene);
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(1000);
		primaryStage.show();
	}


	private void initToolBar()
	{
		HBox tool = app.getToolBar();

		Button btnSettings = Icons.createIconButton(FontIcon.GEAR, "Settings", 18);
		btnSettings.setOnAction((e) -> app.setOverlay("Application Settings", appSettingsSheet, Icons.createIconLabel(FontIcon.GEAR), appSettingsSheet.isShowingProperty()));
		tool.getChildren().add(btnSettings);
	}

	private void initViews() throws IOException
	{
		app.addView("Account", loadFXML("account.fxml"), Icons.createIconLabel(FontIcon.USER, 30), true);
		app.addView("Characters", loadFXML("characters.fxml"), Icons.createIconLabel(FontIcon.CHILD, 30), false);
		app.addView("Inventory", loadFXML("inventory.fxml"), Icons.createIconLabel(FontIcon.LIST, 30), false);
		app.addView("Commerce", loadFXML("commerce.fxml"), Icons.createIconLabel(FontIcon.CREDIT_CARD, 30), false);
		app.addView("About", loadFXML("about.fxml"), Icons.createIconLabel(FontIcon.INFO_CIRCLE, 30), false);
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

	public BorderPane getApplicationPane()
	{
		return applicationPane;
	}

	public ApplicationManager getApp()
	{
		return app;
	}

	public FXSettingsManager getSettingsManager()
	{
		return settings;
	}

	public ApplicationSettings getAppSettings()
	{
		return appSettings;
	}

	public AssetManager getAssets()
	{
		return assets;
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
