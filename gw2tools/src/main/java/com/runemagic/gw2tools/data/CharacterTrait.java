package com.runemagic.gw2tools.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CharacterTrait
{

	private IntegerProperty id=new SimpleIntegerProperty();
	//TODO everything else

	private CharacterTrait(int id)
	{
		this.id.set(id);
	}

	public static CharacterTrait of(int id)
	{
		return new CharacterTrait(id);//TODO cached pool (traits are constants/immutable)
	}

	public int getId()
	{
		return id.get();
	}

	public ReadOnlyIntegerProperty idProperty()
	{
		return id;
	}
}
