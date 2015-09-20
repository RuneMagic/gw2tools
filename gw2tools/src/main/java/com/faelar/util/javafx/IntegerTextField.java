package com.faelar.util.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

public class IntegerTextField extends TextField
{

	private IntegerProperty intValue=new SimpleIntegerProperty(this, "intValue");

	public IntegerTextField()
	{
		super();
		bindValue();
	}

	public IntegerTextField(int text)
	{
		super(Integer.toString(text));
		bindValue();
	}

	private void bindValue()
	{
		textProperty().bindBidirectional(intValue, new NumberStringConverter());
	}

	public IntegerProperty integerProperty()
	{
		return intValue;
	}

	public int getInteger()
	{
		return intValue.get();
	}

	public void setInteger(int value)
	{
		intValue.set(value);
	}

	@Override
	public void replaceText(int start, int end, String text)
	{
		if (validate(text))
		{
			super.replaceText(start, end, text);
		}
	}

	@Override
	public void replaceSelection(String text)
	{
		if (validate(text))
		{
			super.replaceSelection(text);
		}
	}

	private boolean validate(String text)
	{
		return (text.matches("[0-9]*"));//TODO don't use regexp
	}
}
