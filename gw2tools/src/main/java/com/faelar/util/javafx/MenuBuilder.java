package com.faelar.util.javafx;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MenuBuilder
{
	private List<MenuItem> items;
	private String text;
	private EventHandler<ActionEvent> action;

	private Menu menuObj;
	private ContextMenu ctxMenuObj;

	private MenuBuilder(String text, EventHandler<ActionEvent> action)
	{
		this.items=new ArrayList<MenuItem>();
		this.text=text;
		this.action=action;
		menuObj=null;
		ctxMenuObj=null;
	}

	private MenuBuilder(Menu menu)
	{
		this.items=menu.getItems();
		this.text=menu.getText();
		this.action=menu.getOnAction();
		menuObj=menu;
		ctxMenuObj=null;
	}

	private MenuBuilder(ContextMenu menu)
	{
		this.items=menu.getItems();
		this.text=null;
		this.action=menu.getOnAction();
		menuObj=null;
		ctxMenuObj=menu;
	}

	public static MenuBuilder create()
	{
		return new MenuBuilder(null, null);
	}

	public static MenuBuilder create(String text)
	{
		return new MenuBuilder(text, null);
	}

	public static MenuBuilder create(EventHandler<ActionEvent> action)
	{
		return new MenuBuilder(null, action);
	}

	public static MenuBuilder create(String text, EventHandler<ActionEvent> action)
	{
		return new MenuBuilder(text, action);
	}

	public static MenuBuilder of(Menu menu)
	{
		return new MenuBuilder(menu);
	}

	public static MenuBuilder of(ContextMenu menu)
	{
		return new MenuBuilder(menu);
	}

	public Menu asMenu()
	{
		if (menuObj==null) menuObj=createMenu();
		return menuObj;
	}

	public ContextMenu asContextMenu()
	{
		if (ctxMenuObj==null) ctxMenuObj=createContextMenu();
		return ctxMenuObj;
	}

	private ContextMenu createContextMenu()
	{
		ContextMenu menu=new ContextMenu();
		if (action!=null) menu.setOnAction(action);
		menu.getItems().setAll(items);
		items=menu.getItems();
		return menu;
	}

	private Menu createMenu()
	{
		Menu menu=new Menu();
		if (text!=null) menu.setText(text);
		if (action!=null) menu.setOnAction(action);
		menu.getItems().setAll(items);
		items=menu.getItems();
		return menu;
	}

	public MenuBar toMenuBar()//TODO auto-wrap non-Menu items (or throw exception)
	{
		MenuBar menu=new MenuBar();
		List<Menu> menuItems=new ArrayList<Menu>();
		for (MenuItem item:items)
		{
			if (item instanceof Menu) menuItems.add((Menu) item);
		}
		menu.getMenus().setAll(menuItems);
		return menu;
	}

	public MenuItem addItem(String text, EventHandler<ActionEvent> action)
	{
		MenuItem item=new MenuItem(text);
		if (action!=null) item.setOnAction(action);
		items.add(item);
		return item;
	}

	public RadioMenuItem addRadioItem(String text, EventHandler<ActionEvent> action)
	{
		RadioMenuItem item=new RadioMenuItem(text);
		if (action!=null) item.setOnAction(action);
		items.add(item);
		return item;
	}

	public CheckMenuItem addCheckItem(String text, EventHandler<ActionEvent> action)
	{
		CheckMenuItem item=new CheckMenuItem(text);
		if (action!=null) item.setOnAction(action);
		items.add(item);
		return item;
	}

	public void addSeparator()
	{
		items.add(new SeparatorMenuItem());
	}

	public MenuBuilder addMenu(String text, EventHandler<ActionEvent> action)
	{
		Menu item=new Menu(text);
		if (action!=null) item.setOnAction(action);
		items.add(item);
		return of(item);
	}

	public MenuItem addItem(String text)
	{
		return addItem(text, null);
	}

	public RadioMenuItem addRadioItem(String text)
	{
		return addRadioItem(text, null);
	}

	public CheckMenuItem addCheckItem(String text)
	{
		return addCheckItem(text, null);
	}

	public MenuBuilder addMenu(String text)
	{
		return addMenu(text, null);
	}
}
