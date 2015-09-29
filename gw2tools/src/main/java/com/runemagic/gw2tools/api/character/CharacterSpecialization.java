package com.runemagic.gw2tools.api.character;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CharacterSpecialization
{
	private IntegerProperty id=new SimpleIntegerProperty();
	private ObjectProperty<CharacterTrait> trait1=new SimpleObjectProperty();
	private ObjectProperty<CharacterTrait> trait2=new SimpleObjectProperty();
	private ObjectProperty<CharacterTrait> trait3=new SimpleObjectProperty();

	public CharacterSpecialization(int id)
	{
		this.id.set(id);
	}

	public int getId()
	{
		return id.get();
	}

	public ReadOnlyIntegerProperty idProperty()
	{
		return id;
	}

	/*public void setId(int id)
	{
		this.id.set(id);
	}*/

	public CharacterTrait getTrait1()
	{
		return trait1.get();
	}

	public ObjectProperty<CharacterTrait> trait1Property()
	{
		return trait1;
	}

	public void setTrait1(CharacterTrait trait1)
	{
		this.trait1.set(trait1);
	}

	public CharacterTrait getTrait2()
	{
		return trait2.get();
	}

	public ObjectProperty<CharacterTrait> trait2Property()
	{
		return trait2;
	}

	public void setTrait2(CharacterTrait trait2)
	{
		this.trait2.set(trait2);
	}

	public CharacterTrait getTrait3()
	{
		return trait3.get();
	}

	public ObjectProperty<CharacterTrait> trait3Property()
	{
		return trait3;
	}

	public void setTrait3(CharacterTrait trait3)
	{
		this.trait3.set(trait3);
	}
}
