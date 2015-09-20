package com.faelar.util.javafx;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

public final class MenuTools
{
	private MenuTools() {}

	/**
	 * Adds a context menu with items:<br>
	 *  - Edit: Edits the selected item<br>
	 *  - Separator<br>
	 *  - Add new: Adds a new item to the end of the list<br>
	 *  - Insert new: Inserts a new item before the selected item or before the first item if there is no selection<br>
	 *  - Separator<br>
	 *  - Delete: Removes the selected item(s) from the list<br>
	 * @param list
	 * @param itemFactory The factory method for new items
	 * @param editor
	 */
	public static <T> MenuBuilder setDefaultContextMenu(ListView<T> list, Supplier<T> itemFactory, Consumer<T> editor)
	{
		return setDefaultContextMenu(list, null, itemFactory, editor);
	}

	/**
	 * Adds a context menu with items:<br>
	 *  - Add new: Adds a new item to the end of the list<br>
	 *  - Insert new: Inserts a new item before the selected item or before the first item if there is no selection<br>
	 *  - Separator<br>
	 *  - Delete: Removes the selected item(s) from the list<br>
	 * @param list
	 * @param itemFactory The factory method for new items
	 */
	public static <T> MenuBuilder setDefaultContextMenu(ListView<T> list, Supplier<T> itemFactory)
	{
		return setDefaultContextMenu(list, null, itemFactory, null);
	}

	/**
	 * Adds a context menu with items:<br>
	 *  - Edit: Edits the selected item<br>
	 *  - Separator<br>
	 *  - Add: Adds an existing item to the end of the list<br>
	 *  - Insert: Inserts an existing item before the selected item or before the first item if there is no selection<br>
	 *  - Separator<br>
	 *  - Delete: Removes the selected item(s) from the list<br>
	 * @param list
	 * @param addItemSource The list that contains the possible existing items to add
	 * @param editor
	 */
	public static <T> MenuBuilder setDefaultContextMenu(ListView<T> list, List<T> addItemSource, Consumer<T> editor)
	{
		return setDefaultContextMenu(list, addItemSource, null, editor);
	}

	/**
	 * Adds a context menu with items:<br>
	 *  - Add: Adds an existing item to the end of the list<br>
	 *  - Insert: Inserts an existing item before the selected item or before the first item if there is no selection<br>
	 *  - Separator<br>
	 *  - Delete: Removes the selected item(s) from the list<br>
	 * @param list
	 * @param addItemSource The list that contains the possible existing items to add
	 */
	public static <T> MenuBuilder setDefaultContextMenu(ListView<T> list, List<T> addItemSource)
	{
		return setDefaultContextMenu(list, addItemSource, null, null);
	}

	/**
	 * Adds a context menu with items:<br>
	 *  - Edit: Edits the selected item<br>
	 *  - Separator<br>
	 *  - Add: Adds an existing item to the end of the list<br>
	 *  - Insert: Inserts an existing item after the selected item or before the first item if there is no selection<br>
	 *  - Add new: Adds a new item to the end of the list<br>
	 *  - Insert new: Inserts a new item before the selected item or before the first item if there is no selection<br>
	 *  - Separator<br>
	 *  - Delete: Removes the selected item(s) from the list<br>
	 * @param list
	 * @param addItemSource The list that contains the possible existing items to add
	 * @param itemFactory The factory method for new items
	 * @param editor
	 */
	public static <T> MenuBuilder setDefaultContextMenu(ListView<T> list, List<T> addItemSource, Supplier<T> itemFactory, Consumer<T> editor)
	{
		MenuBuilder menu = MenuBuilder.create();

		if (editor!=null)
		{
			menu.addItem("Edit", (e)->{
				T item=list.getSelectionModel().getSelectedItem();
				if (item==null) return;
				editor.accept(item);
			}).disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

			menu.addSeparator();
		}

		if (addItemSource!=null)//TODO use multiple levels (categorize items alphabetically and whatnot, in case there is too many to show at once)
		{
			MenuBuilder mnuAdd=menu.addMenu("Add");
			MenuBuilder mnuInsert=menu.addMenu("Insert");
			for (T item:addItemSource)
			{
				mnuAdd.addItem(item.toString(), (e)->{//TODO use item name function instead of toString
					list.getItems().add(item);
				});
				mnuInsert.addItem(item.toString(), (e)->{
					int index=list.getSelectionModel().getSelectedIndex();
					if (index<0 || index>list.getItems().size()) index=0;
					list.getItems().add(index, item);
				});
			}
		}

		if (itemFactory!=null)
		{
			menu.addItem("Add new", (e)->{
				list.getItems().add(itemFactory.get());
			});

			menu.addItem("Insert new", (e)->{
				int index=list.getSelectionModel().getSelectedIndex();
				if (index<0 || index>list.getItems().size()) index=0;
				list.getItems().add(index, itemFactory.get());
			});


			menu.addSeparator();
		}

		menu.addItem("Delete", (e)->{
			//Dialogs.showConfirmationDialog(); //TODO delete confirmation dialog
			list.getItems().removeAll(list.getSelectionModel().getSelectedItems());//TODO use indices
		}).disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

		list.setContextMenu(menu.asContextMenu());
		return menu;
	}

	public static void addMenuItem(Menu menu, String text, EventHandler<ActionEvent> action)
	{
		MenuItem item=new MenuItem(text);
		if (action!=null) item.setOnAction(action);
		menu.getItems().add(item);
	}

	public static void addMenuItem(ContextMenu menu, String text, EventHandler<ActionEvent> action)
	{
		MenuItem item=new MenuItem(text);
		if (action!=null) item.setOnAction(action);
		menu.getItems().add(item);
	}

	public static void addRadioMenuItem(Menu menu, String text, EventHandler<ActionEvent> action)
	{
		RadioMenuItem item=new RadioMenuItem(text);
		if (action!=null) item.setOnAction(action);
		menu.getItems().add(item);
	}

	public static void addMenuSeparator(Menu menu)
	{
		menu.getItems().add(new SeparatorMenuItem());
	}

	public static void addMenuSeparator(ContextMenu menu)
	{
		menu.getItems().add(new SeparatorMenuItem());
	}
}
