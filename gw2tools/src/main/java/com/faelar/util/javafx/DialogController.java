package com.faelar.util.javafx;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogController
{
	private Dialog<ButtonType> dialog=null;

	public DialogController()
	{

	}

	public boolean closeOK()
	{
		return close(ButtonType.OK);
	}

	public boolean closeCancel()
	{
		return close(ButtonType.CANCEL);
	}

	public boolean close(ButtonType button)
	{
		if (dialog==null) return false;
		dialog.setResult(button);
		dialog.close();
		return true;
	}

	void setDialog(Dialog<ButtonType> dialog)
	{
		if (this.dialog!=null) throw new IllegalStateException("Dialog already set");
		this.dialog=dialog;
	}

}
